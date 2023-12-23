package io.github.riicarus.common.data.ast.generic.expr.op.compute;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.ast.generic.expr.op.abstruct.UnaryOpNode;

/**
 * 取反 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-18 0:17
 * @since 1.0.0
 */
public class NegateNode extends UnaryOpNode {

    public NegateNode() {
        super("!");
    }

    public NegateNode(ExprNode operand) {
        super("!", operand);
    }
}
