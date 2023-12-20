package io.github.riicarus.common.data.ast.generic.expr.v;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;

/**
 * 值 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-17 23:33
 * @since 1.0.0
 */
public class ValueNode extends ExprNode {

    protected final String value;
    protected final ValueType type;

    public ValueNode(String value, ValueType type) {
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

        // like: Val <INT>(1)
        sb.append(prefix).append(t).append(link)
                .append(name)
                .append("<").append(type).append(">")
                .append("(").append(value).append(")");

        return sb.toString();
    }

    @Override
    public final ValueType getReturnType() {
        return type;
    }
}
