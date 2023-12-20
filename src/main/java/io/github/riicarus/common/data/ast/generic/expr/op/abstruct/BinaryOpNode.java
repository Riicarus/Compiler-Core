package io.github.riicarus.common.data.ast.generic.expr.op.abstruct;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.ast.generic.expr.v.ValueType;

/**
 * 二元运算符 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-17 23:48
 * @since 1.0.0
 */
public abstract class BinaryOpNode extends OpNode {

    protected final ValueType leftOperandType;
    protected final ValueType rightOperandType;
    protected final ValueType returnType;

    protected final ExprNode leftOperand;
    protected final ExprNode rightOperand;

    public BinaryOpNode(String name,
                        ValueType leftOperandType, ValueType rightOperandType,
                        ValueType returnType,
                        ExprNode leftOperand, ExprNode rightOperand) {
        super(name);
        this.leftOperandType = leftOperandType;
        this.rightOperandType = rightOperandType;
        this.returnType = returnType;

        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public final String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) {
            sb.append("\r\n");
        }

        // like: := <LeftOperandType, RightOperandType -> ReturnType>
        sb.append(prefix).append(t).append(link)
                .append(name)
                .append(" <")
                .append(leftOperandType).append(", ").append(rightOperandType)
                .append(" -> ").append(returnType)
                .append(">")
                .append(leftOperand.toTreeString(level + 1, prefix))
                .append(rightOperand.toTreeString(level + 1, prefix));

        return sb.toString();
    }

    @Override
    public final ValueType getReturnType() {
        return returnType;
    }
}
