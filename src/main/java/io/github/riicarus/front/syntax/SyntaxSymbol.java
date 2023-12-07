package io.github.riicarus.front.syntax;

import io.github.riicarus.common.data.AbstractAST;
import io.github.riicarus.common.data.AstConstructStrategy;
import io.github.riicarus.common.data.AstConstructor;

import java.util.Stack;

/**
 * 文法符号.
 *
 * @author Riicarus
 * @create 2023-11-22 22:20
 * @since 1.0.0
 */
public interface SyntaxSymbol<T> {

    String getName();

    SyntaxSymbolType getType();

    boolean isTerminal();

    default AbstractAST<T> constructAST(AstConstructStrategy strategy, String lexeme, Stack<AbstractAST<?>> astStack, Stack<SyntaxSymbol<?>> opSymbolStack) {
        final AstConstructor<T> constructor = strategy.get(this);
        if (constructor == null)
            throw new IllegalStateException("LL1Syntax wrong: can not construct AST, because can not get the related constructor of symbol:\"" + this + "\" from strategy.");

        return constructor.construct(lexeme, astStack, opSymbolStack);
    }

}
