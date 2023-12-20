package io.github.riicarus.common.data.ast.generic.expr.ctrl;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.ast.generic.expr.v.ValueType;

import static io.github.riicarus.common.data.ast.generic.expr.v.ValueType.BOOLEAN;

/**
 * if 条件语句 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-18 0:46
 * @since 1.0.0
 */
public class IfConditionNode extends ExprNode {

    private static final ValueType TYPE = BOOLEAN;
    private final ExprNode condition;

    public IfConditionNode(ExprNode condition) {
        super("condition");
        this.condition = condition;
    }

    @Override
    public final String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) {
            sb.append("\r\n");
        }

        // like: condition <BOOLEAN>
        sb.append(prefix).append(t).append(link)
                .append(name)
                .append(" <").append(TYPE).append(">")
                .append(condition.toTreeString(level + 1, prefix));

        return sb.toString();
    }

    @Override
    public final ValueType getReturnType() {
        return TYPE;
    }
}
