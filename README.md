# 类 PASCAL 文法编译器设计

## 词法分析

词法分析使用自动机的形式进行处理. 词法分析的代码生成流程为:

RE(正则表达式)->NFA(非确定有限状态自动机)->DFA(确定有限状态自动机)-> Minimize DFA->Lexical Analyzer(词法分析器)

> RE: Regex
>
> NFA: Nondeterministic Finite Automaton
>
> DFA: Deterministic Finite Automaton

### RE

使用正则表达式(RE)表示词法支持的类型. 如: $a(b \mid c)^*$ 表示支持以 $a$ 开头的, 后接任意多个 $b$ 或者 $c$ 的串.

#### 词法对应的 RE

$$
\begin{align}
& letter \to [a-zA-Z] \\
& id \to letter(letter | digit)* \\
& digit \to [0-9] \\
& number \to digit+ \\
& constant \to number \\
& begin \to begin \\
& end \to end \\
& integer \to integer \\
& if \to if \\
& then \to then \\
& else \to else \\
& function \to function \\
& read \to read \\
& write \to write \\
& = \to = \\
& <> \to <> \\
& <= \to <= \\
& < \to < \\
& >= \to >= \\
& > \to > \\
& - \to - \\
& * \to * \\
& := \to := \\
& ( \to ( \\
& ) \to ) \\
& ; \to ; \\
& \end{align}
$$

### RE -> NFA

使用 Thompson 算法将正则表达式转换为非确定有限状态自动机.

### NFA -> DFA

使用有限子集算法将非确定状态转换为确定状态.

### Minimize DFA

最小化 DFA.

### DFA -> LexicalAnalyzer

根据确定有限状态自动机生成对应的词法分析器的代码.

## 语法分析

### 任务

根据词法分析的输出的 Token 列表, 返回语法分析后的结果.

```java
public interface Syntaxer {
    SyntaxParseResult parse(List<Token> tokenList, Set<LexicalSymbol> assistantLexSymbolSet, AstConstructStrategy strategy);
}
```

#### 输入

- 词法分析后的 token 串.
- 辅助符号列表.
- AST 构建策略工厂
- 语法定义

### 运算符的优先级

根据语法定义的顺序可以自动确定运算符的优先级, 也可在语法分析的开始传入运算符的总体优先级.

在语法分析的过程中, 根据算符优先级表构建 AST.

### 语法

$$
\begin{align}
& P \to SP\\
& SP \to begin \; DST \; EST \; end \\
& DST \to DS ; \; DST \mid \epsilon \\
& DS \to VD \mid FD \\
& VD \to integer \; V \\
& V \to Id \\
& Id \to identifier \\
& FD \to integer \; function \; Id (A); \; FB \\
& A \to V \\
& FB \to begin \; DST \; EST \; end \\
& EST \to ES ; \; EST \mid \epsilon \\
& ES \to RS \mid WS \mid AS \mid CS \\
& RS \to read(V) \\
& WS \to write(V) \\
& AS \to V := AE \\
& AE \to AE - I \mid I \\
& I \to I * F \mid F \\
& F \to V \mid C \mid FC \\
& FC \to Id (AE) \\
& C \to constant \\
& CS \to if \; CE \; then \; ES \; else \; ES \\
& CE \to AE \; RO \; AE \\
& RO \to < \mid <= \mid > \mid >= \mid = \mid <> \\
\end{align}
$$

#### 消除左递归

语法中存在左递归, 如: $(42), (43)$, 将他们消除左递归后, 文法如下:

$(42)$ 对应文法:
$$
\begin{align}
& AE \to I \; AE' \\
& AE' \to - \; I \; AE' \mid \epsilon \\
\end{align}
$$

$(43)$ 对应文法:
$$
\begin{align}
& I \to F \; I' \\
& I' \to * \; F \; I' \mid \epsilon \\
\end{align}
$$

#### 消除公共左因子

语法中有两处公共左因子 $(31)$ 和 $(45)$:

$(31)$: $VD \to integer \; V$ 和 $FD \to integer \; function \; Id (A); \; FB$ 都以 $integer$ 开始.

$(45)$: $F \to V$ 和 $F \to FC$ 都以 $identifier$ 开始.

更改后:

$(31)$ 对应文法:
$$
\begin{align}
& DS \to integer \; VDorFDS \\
& VDorFDS \to V \mid function \; Id (A); \; FB \\
\end{align}
$$

$(45)$ 对应文法:
$$
\begin{align}
& F \to VorFC \mid C \\
& VorFC \to Id \; VorFCS \\
& VorFCS \to (A) \mid \epsilon \\
\end{align}
$$

#### LL(1) 语法

$$
\begin{align}
& P \to SP\\
& SP \to begin \; DST \; EST \; end \\
& DST \to DS ; \; DST \mid \epsilon \\
& DS \to integer \; VDorFDS \\
& VDorFDS \to V \mid function \; Id (A); \; FB \\
& V \to Id \\
& Id \to identifier \\
& A \to V \\
& FB \to begin \; DST \; EST \; end \\
& EST \to ES ; \; EST \mid \epsilon \\
& ES \to RS \mid WS \mid AS \mid CS \\
& RS \to read(V) \\
& WS \to write(V) \\
& AS \to V := AE \\
& AE \to I \; AE' \\
& AE' \to - \; I \; AE' \mid \epsilon \\
& I \to F \; I' \\
& I' \to * \; F \; I' \mid \epsilon \\
& F \to VorFC \mid C \\
& VorFC \to Id \;\; VorFCS \\
& VorFCS \to (AE) \mid \epsilon \\
& C \to constant \\
& CS \to if \; CE \; then \; ES \; else \; ES \\
& CE \to AE \; RO \; AE \\
& RO \to < \mid <= \mid > \mid >= \mid = \mid <> \\
\end{align}
$$

#### 涉及符号及含义

##### 终结符

- `begin`: 程序/分程序开始符号
- `end`: 程序/分程序结束符号
- `integer`: 定义 integer 类型(变量/函数返回值)
- `identifier`: 标识符
- `function`: 定义函数
- `(`: 参数(定义/传递)的开始
- `)`: 参数(定义/传递)的结束
- `;`: 一个变量/函数定义的结束
- `read`: 读取
- `write`: 写入
- `:=`: 赋值
- `*`: 乘法
- `-`: 减法
- `constant`: 常数
- `if`: 条件语句中, 条件表达式开始
- `then`: 条件语句中, 结果为真的执行语句开始
- `else`: 条件语句中, 结果为假的执行语句开始
- `<`: 关系运算符, 小于
- `<=`: 关系运算符, 小于或等于
- `>`: 关系运算符, 大于
- `>=`: 关系运算符, 大于或等于
- `=`: 关系运算符, 等于
- `<>`: 关系运算符, 不等于

##### 非终结符

- `P`: 程序
- `SP`: 分程序
- `DST`: 说明语句表
- `DS`: 说明语句
- `VDorFDS`: 变量或函数说明后缀
- `V`: 变量
- `Id`: 标识符
- `A`: 参数
- `FB`: 函数体
- `EST`: 执行语句表
- `ES`: 执行语句
- `RS`: 读语句
- `WS`: 写语句
- `AS`: 赋值语句
- `AE`: 算数表达式
- `AE'`: 算数表达式(消除左递归)
- `I`: 项(乘法)
- `I'`: 项(乘法)(消除左递归)
- `F`: 因子
- `VorFC`: 变量或者函数调用
- `VorFCS`: 变量或者函数调用的后缀
- `C`: 常数
- `CS`: 条件语句
- `CE`: 条件表达式
- `RO`: 关系表达式
