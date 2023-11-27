package io.github.riicarus.front.lex;

import io.github.riicarus.common.data.Token;
import io.github.riicarus.common.util.CharUtil;
import io.github.riicarus.common.util.RegexParser;

import java.util.Set;

/**
 * 类 Pascal 语言单词符号.
 *
 * <p>定义时需要将关键字放在标识符之前.</p>
 *
 * @author Riicarus
 * @create 2023-11-6 17:43
 * @since 1.0.0
 */
public enum PascalLexicalSymbol implements LexicalSymbol {

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
    MINUS("-", 18, "-"), TIMES("*", 19, "\\*"),
    ASSIGN(":=", 20, ":="),
    L_PAREN("(", 21, "\\("), R_PAREN(")", 22, "\\)"),
    SEMICOLON(";", 23, ";"),
    EMPTY_SPACE(" ", 24, "( )( )*"),
    EOL("EOL", 25, "(\r)*\n"), EOF("EOF", 26, String.valueOf((char) 26)),
    LEX_END(String.valueOf(CharUtil.LEX_SYNTAX_END), 27, String.valueOf(CharUtil.LEX_SYNTAX_END));

    private final String name;

    private final int code;

    private final boolean needPrintVal;

    private final DFA dfa;

    PascalLexicalSymbol(String name, int code, boolean needPrintVal, String regex) {
        this.name = name;
        this.code = code;
        this.needPrintVal = needPrintVal;
        this.dfa = DFA.nfaToDfa(RegexParser.reToNFA(regex, CharUtil.PASCAL_CHAR_SET), CharUtil.PASCAL_CHAR_SET);
    }

    PascalLexicalSymbol(String name, int code, String regex) {
        this.name = name;
        this.code = code;
        this.needPrintVal = false;
        this.dfa = DFA.nfaToDfa(RegexParser.reToNFA(regex, CharUtil.PASCAL_CHAR_SET), CharUtil.PASCAL_CHAR_SET);
    }

    @Override
    public Token validate(String s) {
        String value = dfa.validateString(s);

        if (value == null || value.length() == 0) return null;

        return new Token(this, value);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public boolean needPrintVal() {
        return needPrintVal;
    }

    @Override
    public DFA getDfa() {
        return dfa;
    }

    public static void avoidLazyLoad() {
    }

    public static Set<LexicalSymbol> assistantLexSymbols() {
        return Set.of(EMPTY_SPACE, EOL, EOF);
    }
}
