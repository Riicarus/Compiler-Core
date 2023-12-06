package io.github.riicarus.common.data.ast;

import io.github.riicarus.common.data.Token;

/**
 * 值类型 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-6 13:50
 * @since 1.0.0
 */
public class ValueNode<T> extends AstNode<T> {

    protected ValueType<T> type;
    protected Token valueToken;

    @Override
    public T compute() {
        return type.cast(valueToken.getLexeme());
    }
}
