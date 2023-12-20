package io.github.riicarus.common.data.ast.generic.expr.ctrl;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.ast.generic.expr.v.ValueType;

/**
 * 返回语句 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-18 20:30
 * @since 1.0.0
 */
public class ReturnNode extends ExprNode {
    protected final ValueType returnType;
    protected final ExprNode operand;

    public ReturnNode(ValueType returnType, ExprNode operand) {
        super("Return");
        this.returnType = returnType;
        this.operand = operand;
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) {
            sb.append("\r\n");
        }

        // like: Return <ReturnType>
        sb.append(prefix).append(t).append(link)
                .append(name)
                .append(" <").append(returnType).append(">")
                .append(operand.toTreeString(level + 1, prefix));

        return sb.toString();
    }

    @Override
    public ValueType getReturnType() {
        return returnType;
    }
}
