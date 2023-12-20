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
& return \to return \\
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
& + \to + \\
& - \to - \\
& * \to * \\
& / \to / \\
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
& P \to CB\\
& CB \to begin \; DST \; EST \; end \\
& DST \to DS ; \; DST \mid \epsilon \\
& DS \to VD \mid FD \\
& VD \to T \; Id \\
& T \to integer \mid boolean \mid float \mid string \\
& Id \to identifier \\
& FD \to T \; function \; Id (ADT); \; CB \\
& ADT \to T \; Id \; ADTS \mid \epsilon \\
& ADTS \to , \; T \; Id \; ADTS \mid \epsilon \\
& EST \to ES ; \; EST \mid \epsilon \\
& ES \to RS \mid WS \mid AS \mid CS \mid RT \\
& RS \to read(Id) \\
& WS \to write(Id) \\
& RT \to return \; AE \\
& AS \to Id := AE \\
& AE \to AE - I \mid AE + I \mid I \\
& I \to I * F \mid I / F \mid F \\
& F \to Id \mid C \mid FC \\
& FC \to Id (AL) \\
& AL \to AE \; ALS \mid \epsilon \\
& ALS \to , \; AE \; ALS \mid \epsilon \\
& C \to constant \\
& CS \to if \; CE \; then \; CB \; else \; CB \\
& CE \to AE \; RO \; AE \\
& RO \to < \mid <= \mid > \mid >= \mid = \mid <> \\
\end{align}
$$

#### 消除左递归

语法中存在左递归, 如: $(47), (48)$, 将他们消除左递归后, 文法如下:

$(48)$ 对应文法:

$$
\begin{align}
& AE \to I \; AES \\
& AES \to - I \; AES \mid + I \; AES \mid \epsilon \\
\end{align}
$$

$(49)$ 对应文法:

$$
\begin{align}
& I \to F \; IS \\
& IS \to * F \; IS \mid / F \; IS \mid \epsilon \\
\end{align}
$$

#### 消除公共左因子

语法中有两处公共左因子 $(34), (35), (38)$ 和 $(50), (51)$:

$(34), (35), (38)$ 中:

$$
\begin{align}
& DS \to VD \mid FD \\
& VD \to T \; Id \\
& FD \to T \; function \; Id (ADT); \; CB \\
\end{align}
$$

都以 $T$ 开始.

$(50), (51)$ 中:

$$
\begin{align}
& F \to Id \mid FC \\
& FC \to Id(AL) \\
\end{align}
$$

都以 $Id$ 开始.

**更改后**:

$(34), (35), (38)$ 对应文法:

$$
\begin{align}
& DS \to T \; VDorFDS \\
& VDorFDS \to Id \mid function \; Id (ADT); \; CB \\
\end{align}
$$

$(50), (51)$ 对应文法:

$$
\begin{align}
& F \to VorFC \mid C \\
& VorFC \to Id \;\; VorFCS \\
& VorFCS \to (AL) \mid \epsilon \\
\end{align}
$$

#### LL(1) 语法

$$
\begin{align}
& P \to CB\\
& CB \to begin \; DST \; EST \; end \\
& DST \to DS ; \; DST \mid \epsilon \\
& DS \to T \; VDorFDS \\
& VDorFDS \to Id \mid function \; Id (ADT); \; CB \\
& T \to integer \mid boolean \mid float \mid string \\
& Id \to identifier \\
& ADT \to T \; Id \; ADTS \mid \epsilon \\
& ADTS \to , \; T \; Id \; ADTS \mid \epsilon \\
& EST \to ES ; \; EST \mid \epsilon \\
& ES \to RS \mid WS \mid AS \mid CS \mid RT \\
& RS \to read(Id) \\
& WS \to write(Id) \\
& RT \to return \; AE \\
& AS \to Id := AE \\
& AE \to I \; AES \\
& AES \to - I \; AES \mid + I \; AES \mid \epsilon \\
& I \to F \; IS \\
& IS \to * F \; IS \mid / F \; IS \mid \epsilon \\
& F \to VorFC \mid C \\
& VorFC \to Id \; VorFCS \\
& VorFCS \to (AL) \mid \epsilon \\
& AL \to AE \; ALS \mid \epsilon \\
& ALS \to , \; AE \; ALS \mid \epsilon \\
& C \to constant \\
& CS \to if \; CE \; then \; CB \; else \; CB \\
& WS \to while(CE) \; CB \\
& CE \to ES \; BARO \; ES \mid ULRO \; CE |  \\
& BARO \to < \mid <= \mid > \mid >= \mid = \mid <> \\
& ULRO \to ! \\
& BLRO \to \And \mid | \\
\end{align}
$$

#### 涉及符号及含义

##### 终结符

- `begin`: 程序/分程序开始符号
- `end`: 程序/分程序结束符号
- `;`: 一个变量/函数定义的结束
- `function`: 定义函数
- `(`: 参数(定义/传递)的开始
- `)`: 参数(定义/传递)的结束
- `integer`: 定义 integer 类型(变量/函数返回值)
- `boolean`: 定义 boolean 类型(变量/函数返回值)
- `float`: 定义 float 类型(变量/函数返回值)
- `string`: 定义 string 类型(变量/函数返回值)
- `identifier`: 标识符
- `colon`: 逗号
- `read`: 读取
- `write`: 写入
- `return`: 返回
- `:=`: 赋值
- `-`: 减法
- `+`: 加法
- `*`: 乘法
- `/`: 除法
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
- `CB`: 代码块
- `DST`: 说明语句表
- `DS`: 说明语句
- `VDorFDS`: 变量或函数说明后缀(消除公共左因子)
- `T`: 类型
- `Id`: 标识符, 变量名称
- `ADT`: 参数定义表
- `ADTS`: 参数定义表(消除左递归)
- `CB`: 函数体
- `EST`: 执行语句表
- `ES`: 执行语句
- `RS`: 读语句
- `WS`: 写语句
- `RT`: 返回语句
- `AS`: 赋值语句
- `AE`: 算数表达式
- `AES`: 算数表达式(消除左递归)
- `I`: 项(乘法)
- `IS`: 项(乘法)(消除左递归)
- `F`: 因子
- `VorFC`: 变量或者函数调用
- `VorFCS`: 变量或者函数调用的后缀(消除公共左因子)
- `AL`: 传递参数列表
- `ALS`: 传递参数列表(消除左递归)
- `C`: 常数
- `CS`: 条件语句
- `CE`: 条件表达式
- `RO`: 关系表达式
