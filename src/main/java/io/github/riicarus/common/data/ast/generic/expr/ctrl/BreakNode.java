package io.github.riicarus.common.data.ast.generic.expr.ctrl;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;

/**
 * break 语句 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-22 15:52
 * @since 1.0.0
 */
public class BreakNode extends ExprNode {

    public BreakNode() {
        super("Break");
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) {
            sb.append("\r\n");
        }

        // like: break
        sb.append(prefix).append(t).append(link).append(name);

        return sb.toString();
    }
}
