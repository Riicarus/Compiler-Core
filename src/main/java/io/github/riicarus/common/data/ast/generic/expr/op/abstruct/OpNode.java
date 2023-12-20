package io.github.riicarus.common.data.ast.generic.expr.op.abstruct;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;

/**
 * 运算符 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-17 23:44
 * @since 1.0.0
 */
public abstract class OpNode extends ExprNode {
    public OpNode(String name) {
        super(name);
    }
}
