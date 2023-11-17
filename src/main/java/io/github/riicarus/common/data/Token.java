package io.github.riicarus.common.data;

import java.util.Optional;

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

    protected Token(LexicalSymbol symbol, String lexeme) {
        this.symbol = symbol;
        this.lexeme = lexeme;
    }

    public int getLen() {
        return lexeme.length();
    }

    @Override
    public String toString() {
        return "<" + symbol.getCode() + ", " + Optional.ofNullable(lexeme).orElse("_") + ">";
    }
}