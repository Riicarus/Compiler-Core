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

根据语法定义的顺序可以自动确定运算符的优先级.
