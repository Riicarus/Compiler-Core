package io.github.riicarus.common.data;

import io.github.riicarus.front.lex.LexicalSymbol;

import java.util.Objects;

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

    public String getLexeme() {
        return lexeme;
    }

    public int getLine() {
        return line;
    }

    public int getLen() {
        return lexeme.length();
    }

    public LexicalSymbol getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return "<" + symbol.getCode() + ", " + (symbol.needPrintVal() ? lexeme : "_") + ">";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return symbol == token.symbol && Objects.equals(lexeme, token.lexeme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, lexeme);
    }
}