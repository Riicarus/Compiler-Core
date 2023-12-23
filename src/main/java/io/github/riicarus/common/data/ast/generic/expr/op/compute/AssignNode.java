package io.github.riicarus.common.data.ast.generic.expr.op.compute;

import io.github.riicarus.common.data.ast.generic.expr.op.abstruct.BinaryOpNode;
import io.github.riicarus.common.data.ast.generic.expr.v.VariableNode;

/**
 * <p>赋值 AST 节点</p>
 * <p>将一个 表达式 的值赋值给一个 变量</p>
 * <p>VariableNode := ExprNode</p>
 * <p></p>
 *
 * @author Riicarus
 * @create 2023-12-18 1:06
 * @since 1.0.0
 */
public class AssignNode extends BinaryOpNode {

    public AssignNode() {
        super(":=");
    }

    @Override
    public VariableNode getLeftOperand() {
        try {
            return (VariableNode) super.getLeftOperand();
        } catch (ClassCastException e) {
            throw new IllegalStateException("LL1Syntax error, can not cast node into correct typeNode, need typeNode: VariableNode, but get node: " + getLeftOperand());
        }
    }

}
