package io.github.riicarus.common.data;

/**
 * 词法分析后生成的词法符号.
 *
 * @author Riicarus
 * @create 2023-11-7 23:49
 * @since 1.0.0
 */
public class Token {

    private final LexicalSymbol symbol;

    private final String lexeme;

    private int line;


    public Token(LexicalSymbol symbol, String lexeme) {
        this.symbol = symbol;
        this.lexeme = lexeme;
        this.line = 0;
    }

    public Token(LexicalSymbol symbol, String lexeme, int line) {
        this.symbol = symbol;
        this.lexeme = lexeme;
        this.line = line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getLen() {
        return lexeme.length();
    }

    public LexicalSymbol getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return "<" + symbol.getCode() + ", " + (symbol.isNeedPrintVal() ? lexeme : "_") + ", " + line + ">";
//        return "<" + symbol.getCode() + ", " + lexeme + ">";
    }
}