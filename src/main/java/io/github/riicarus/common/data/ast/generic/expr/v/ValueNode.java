package io.github.riicarus.common.data.ast.generic.expr.v;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.ast.generic.type.TypeNode;
import io.github.riicarus.common.data.table.ProcedureTable;
import io.github.riicarus.common.data.table.VarKind;
import io.github.riicarus.common.data.table.VariableTable;

/**
 * 值 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-17 23:33
 * @since 1.0.0
 */
public class ValueNode extends ExprNode {

    protected final String value;
    protected TypeNode typeNode;

    public ValueNode(String value) {
        super("Val");
        this.value = value;
    }

    public ValueNode(String value, TypeNode typeNode) {
        super("Val");
        this.value = value;
        this.typeNode = typeNode;
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
                .append(typeNode == null ? "" : typeNode.toTreeString(level + 1, prefix));

        return sb.toString();
    }

    @Override
    public void updateTable(VariableTable vt, ProcedureTable pt, String scopeName, VarKind kind, int level) {
        if (typeNode != null) {
            typeNode.updateTable(vt, pt, scopeName, kind, level);
        }
    }
}
