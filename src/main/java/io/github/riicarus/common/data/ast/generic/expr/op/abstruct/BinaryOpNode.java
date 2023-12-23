package io.github.riicarus.common.data.ast.generic.expr.op.abstruct;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;

/**
 * 二元运算符 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-17 23:48
 * @since 1.0.0
 */
public abstract class BinaryOpNode extends OpNode {

    protected ExprNode leftOperand;
    protected ExprNode rightOperand;
    protected BinaryOpNode topOpNode = this;

    public BinaryOpNode(String name,
                        ExprNode leftOperand, ExprNode rightOperand) {
        super(name);

        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    public BinaryOpNode(String name) {
        super(name);
    }

    @Override
    public final String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) {
            sb.append("\r\n");
        }

        // like: :=
        sb.append(prefix).append(t).append(link).append(name)
                .append(leftOperand == null ? "" : leftOperand.toTreeString(level + 1, prefix))
                .append(rightOperand == null ? "" : rightOperand.toTreeString(level + 1, prefix));

        return sb.toString();
    }

    public ExprNode getLeftOperand() {
        return leftOperand;
    }

    public void setLeftOperand(ExprNode leftOperand) {
        this.leftOperand = leftOperand;
    }

    public ExprNode getRightOperand() {
        return rightOperand;
    }

    public void setRightOperand(ExprNode rightOperand) {
        this.rightOperand = rightOperand;
    }

    public BinaryOpNode getTopOpNode() {
        return topOpNode;
    }

    public void setTopOpNode(BinaryOpNode topOpNode) {
        this.topOpNode = topOpNode;
    }
}
