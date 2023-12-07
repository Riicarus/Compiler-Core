package io.github.riicarus.common.data;

import io.github.riicarus.front.syntax.SyntaxSymbol;

import java.util.Stack;

/**
 * AST 构造方法
 *
 * @author Riicarus
 * @create 2023-12-7 17:24
 * @since 1.0.0
 */
@FunctionalInterface
public interface AstConstructor<T> {

    AbstractAST<T> construct(String lexeme, Stack<AbstractAST<?>> astStack, Stack<SyntaxSymbol<?>> opSymbolStack);

}
