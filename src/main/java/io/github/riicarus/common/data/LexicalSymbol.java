package io.github.riicarus.common.data;

/**
 * 单词符号.
 *
 * @author Riicarus
 * @create 2023-11-6 17:43
 * @since 1.0.0
 */
public enum LexicalSymbol {

    BEGIN("begin", 1), END("end", 2),
    INTEGER("integer", 3),
    IF("if", 4), THEN("then", 5), ELSE("else", 6),
    FUNC("function", 7),
    READ("read", 8), WRITE("write", 9),
    IDENTIFIER("identifier", 10), CONST("constant", 11),
    EQ("=", 12), NE("<>", 13),
    LE("<=", 14), LT("<", 15),
    GE(">=", 16), GT(">", 17),
    MINUS("-", 18), TIMES("*", 19),
    ASSIGN(":=", 20),
    L_PAREN("(", 21), R_PAREN(")", 22),
    SEMICOLON(";", 23),
    EOL("EOL", 24), EOF("EOF", 25);

    public final String name;

    public final int code;

    LexicalSymbol(String name, int code) {
        this.name = name;
        this.code = code;
    }
}
