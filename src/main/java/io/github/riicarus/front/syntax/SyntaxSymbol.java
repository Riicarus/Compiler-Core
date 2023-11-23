package io.github.riicarus.front.syntax;

/**
 * 文法符号.
 *
 * @author Riicarus
 * @create 2023-11-22 22:20
 * @since 1.0.0
 */
public interface SyntaxSymbol {

    String getName();

    SyntaxSymbolType getType();

}
