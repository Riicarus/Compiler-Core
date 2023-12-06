package io.github.riicarus.common.data.ast;

/**
 * AST 节点
 *
 * @author Riicarus
 * @create 2023-12-6 13:44
 * @since 1.0.0
 */
public abstract class AstNode<T> {

    protected AstNode<?> left;
    protected AstNode<?> right;

    public abstract T compute();

}
