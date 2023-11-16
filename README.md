# 类 PASCAL 文法编译器设计

## 词法分析

词法分析使用自动机的形式进行处理. 词法分析的代码生成流程为:

RE(正则表达式)->NFA(非确定有限状态自动机)->DFA(确定有限状态自动机)-> Minimize DFA->Lexical Analyzer(词法分析器)

> RE: Regex
>
> NFA: Nondeterministic Finite Automaton
>
> DFA: Deterministic Finite Automaton

## RE

使用正则表达式(RE)表示词法支持的类型. 如: $a(b \mid c)^*$ 表示支持以 $a$ 开头的, 后接任意多个 $b$ 或者 $c$ 的串.

### 词法对应的 RE

$$
letter \to [a-zA-Z] \\
id \to letter(letter | digit)* \\
digit \to [0-9] \\
number \to digit+ \\
constant \to number \\

begin \to begin \\
end \to end \\
integer \to integer \\
if \to if \\
then \to then \\
else \to else \\
function \to function \\
read \to read \\
write \to write \\
= \to = \\
<> \to <> \\
<= \to <= \\
< \to < \\
>= \to >= \\
> \to > \\
- \to - \\
* \to * \\
:= \to := \\
( \to ( \\
) \to ) \\
; \to ; \\
whitespace \to \s \\
$$

### 运算符的优先级

$-$, $*$ 左结合. $:=$ 右结合.

## RE -> NFA

使用 Thompson 算法将正则表达式转换为非确定有限状态自动机.



## NFA -> DFA

使用有限子集算法将非确定状态转换为确定状态.

## Minimize DFA

最小化 DFA.

## DFA -> LexicalAnalyzer

根据确定有限状态自动机生成对应的词法分析器的代码.
