package io.github.riicarus.common.data.ast.generic.expr.op.abstruct;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.table.ProcedureTable;
import io.github.riicarus.common.data.table.VarKind;
import io.github.riicarus.common.data.table.VariableTable;

/**
 * 一元运算符 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-17 23:55
 * @since 1.0.0
 */
public abstract class UnaryOpNode extends OpNode {

    protected ExprNode operand;

    public UnaryOpNode(String name, ExprNode operand) {
        super(name);
        this.operand = operand;
    }

    public UnaryOpNode(String name) {
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

        // like: !
        sb.append(prefix).append(t).append(link).append(name)
                .append(operand == null ? "" : operand.toTreeString(level + 1, prefix));

        return sb.toString();
    }

    @Override
    public void updateTable(VariableTable vt, ProcedureTable pt, String scopeName, VarKind kind, int level) {
        if (operand != null) {
            operand.updateTable(vt, pt, scopeName, kind, level);
        }
    }
}
