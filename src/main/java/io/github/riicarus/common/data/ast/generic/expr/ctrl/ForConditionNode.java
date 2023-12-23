package io.github.riicarus.common.data.ast.generic.expr.ctrl;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;

/**
 * For Condition AST 节点
 *
 * @author Riicarus
 * @create 2023-12-23 6:35
 * @since 1.0.0
 */
public class ForConditionNode extends ExprNode {

    private final ExprNode exprNode;

    public ForConditionNode(ExprNode exprNode) {
        super("ForCondition");
        this.exprNode = exprNode;
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) {
            sb.append("\r\n");
        }

        sb.append(prefix).append(t).append(link).append(name)
                .append(exprNode == null ? "" : exprNode.toTreeString(level + 1, prefix));

        return sb.toString();
    }
}
