package io.github.riicarus.common.data.ast.generic.expr.op.abstruct;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.ast.generic.expr.v.ValueType;

/**
 * 一元运算符 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-17 23:55
 * @since 1.0.0
 */
public abstract class UnaryOpNode extends OpNode {

    protected final ValueType operandType;
    protected final ValueType returnType;
    protected final ExprNode operand;

    public UnaryOpNode(String name, ValueType operandType, ValueType returnType, ExprNode operand) {
        super(name);
        this.operandType = operandType;
        this.returnType = returnType;
        this.operand = operand;
    }

    @Override
    public final String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) {
            sb.append("\r\n");
        }

        // like: ! <OperandType -> ReturnType>
        sb.append(prefix).append(t).append(link)
                .append(name)
                .append(" <").append(operandType).append(" -> ").append(returnType).append(">")
                .append(operand.toTreeString(level + 1, prefix));

        return sb.toString();
    }

    @Override
    public final ValueType getReturnType() {
        return returnType;
    }
}
