package io.github.riicarus.common.data.ast.generic.expr;

import io.github.riicarus.common.data.ast.generic.GenericASTNode;

/**
 * 表达式 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-17 23:30
 * @since 1.0.0
 */
public abstract class ExprNode extends GenericASTNode {
    public ExprNode(String name) {
        super(name);
    }
}
