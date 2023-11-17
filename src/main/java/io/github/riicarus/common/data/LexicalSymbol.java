package io.github.riicarus.common.data;

import io.github.riicarus.common.util.CharUtil;
import io.github.riicarus.front.analyzer.lexical.DFA;
import io.github.riicarus.front.analyzer.lexical.NFA;

/**
 * 单词符号.
 *
 * <p>定义时需要将关键字放在标识符之前.</p>
 *
 * @author Riicarus
 * @create 2023-11-6 17:43
 * @since 1.0.0
 */
public enum LexicalSymbol {

    BEGIN("begin", 1, "begin"), END("end", 2, "end"),
    INTEGER("integer", 3, "integer"),
    IF("if", 4, "if"), THEN("then", 5, "then"), ELSE("else", 6, "else"),
    FUNC("function", 7, "function"),
    READ("read", 8, "read"), WRITE("write", 9, "write"),
    IDENTIFIER("identifier", 10, "[a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z][a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z|0|1|2|3|4|5|6|7|8|9]^"),
    CONST("constant", 11, "[0|1|2|3|4|5|6|7|8|9)(0|1|2|3|4|5|6|7|8|9]^"),
    EQ("=", 12, "="), NE("<>", 13, "<>"),
    LE("<=", 14, "<="), LT("<", 15, "<"),
    GE(">=", 16, ">="), GT(">", 17, ">"),
    MINUS("-", 18, "-"), TIMES("*", 19, "*"),
    ASSIGN(":=", 20, ":="),
    L_PAREN("(", 21, "("), R_PAREN(")", 22, ")"),
    SEMICOLON(";", 23, ";"),
    EOL("EOL", 24, "\r\n"), EOF("EOF", 25, "\n");

    private final String name;

    private final int code;

    private final DFA dfa;

    private Token genToken(String value) {
        return new Token(this, value);
    }

    public Token validate(String s) {
        return genToken(dfa.validateString(s));
    }

    LexicalSymbol(String name, int code, String regex) {
        this.name = name;
        this.code = code;
        this.dfa = DFA.nfaToDfa(NFA.reToNFA(regex, CharUtil.PASCAL_CHAR_SET), CharUtil.PASCAL_CHAR_SET);
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public DFA getDfa() {
        return dfa;
    }
}
