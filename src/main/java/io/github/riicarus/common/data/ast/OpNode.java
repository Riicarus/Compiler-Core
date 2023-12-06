package io.github.riicarus.common.data.ast;

import io.github.riicarus.common.data.Token;

/**
 * 操作符 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-6 13:46
 * @since 1.0.0
 */
public class OpNode<T> extends AstNode<T> {

    protected Token opToken;

    @Override
    public T compute() {
        return null;
    }
}
