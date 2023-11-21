package io.github.riicarus.front.lexer;

import io.github.riicarus.common.data.Token;
import io.github.riicarus.common.util.CharUtil;
import io.github.riicarus.common.util.RegexParser;

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
    IDENTIFIER("identifier", 10, true, CharUtil.IDENTIFIER_REGEX),
    CONST("constant", 11, true, CharUtil.NUMBER_REGEX),
    EQ("=", 12, "="), NE("<>", 13, "<>"),
    LE("<=", 14, "<="), LT("<", 15, "<"),
    GE(">=", 16, ">="), GT(">", 17, ">"),
    MINUS("-", 18, "-"), TIMES("*", 19, "*"),
    ASSIGN(":=", 20, ":="),
    L_PAREN("(", 21, "("), R_PAREN(")", 22, ")"),
    SEMICOLON(";", 23, ";"),
    EMPTY_SPACE(" ", 24, "[ ][ ]^"),
    EOL("EOL", 25, "[\r]^\n"), EOF("EOF", 26, String.valueOf((char) 26));

    private final String name;

    private final int code;

    private final boolean needPrintVal;

    private final DFA dfa;

    private Token genToken(String value) {
        return new Token(this, value);
    }

    public Token validate(String s) {
        String value = dfa.validateString(s);

        if (value == null || value.length() == 0) return null;

        return genToken(value);
    }

    LexicalSymbol(String name, int code, boolean needPrintVal, String regex) {
        this.name = name;
        this.code = code;
        this.needPrintVal = needPrintVal;
        this.dfa = DFA.nfaToDfa(RegexParser.reToNFA(regex, CharUtil.PASCAL_CHAR_SET), CharUtil.PASCAL_CHAR_SET);
    }

    LexicalSymbol(String name, int code, String regex) {
        this.name = name;
        this.code = code;
        this.needPrintVal = false;
        this.dfa = DFA.nfaToDfa(RegexParser.reToNFA(regex, CharUtil.PASCAL_CHAR_SET), CharUtil.PASCAL_CHAR_SET);
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public boolean isNeedPrintVal() {
        return needPrintVal;
    }

    public DFA getDfa() {
        return dfa;
    }

    public static Token validateLongest(String s) {
        Token target = null;
        for (LexicalSymbol symbol : LexicalSymbol.values()) {
            final Token token = symbol.validate(s);

            if (token == null) continue;

            if (target == null) target = token;
            else if (target.getLen() < token.getLen()) target = token;
        }

        return target;
    }

    public static void avoidLazyLoad() {}
}
