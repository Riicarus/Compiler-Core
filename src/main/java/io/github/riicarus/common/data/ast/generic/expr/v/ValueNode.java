package io.github.riicarus.common.data.ast.generic.expr.v;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.ast.generic.type.TypeNode;

/**
 * 值 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-17 23:33
 * @since 1.0.0
 */
public class ValueNode extends ExprNode {

    protected final String value;
    protected TypeNode type;

    public ValueNode(String value) {
        super("Val");
        this.value = value;
    }

    public ValueNode(String value, TypeNode type) {
        super("Val");
        this.value = value;
        this.type = type;
    }

    @Override
    public final String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) {
            sb.append("\r\n");
        }

        // like: Val (1)
        sb.append(prefix).append(t).append(link).append(name)
                .append("(").append(value).append(")")
                .append(type == null ? "" : type.toTreeString(level + 1, prefix));

        return sb.toString();
    }
}
