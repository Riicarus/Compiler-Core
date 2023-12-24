package io.github.riicarus.common.data.ast.generic.expr.ctrl;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.table.ProcedureTable;
import io.github.riicarus.common.data.table.VarKind;
import io.github.riicarus.common.data.table.VariableTable;

/**
 * 返回语句 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-18 20:30
 * @since 1.0.0
 */
public class ReturnNode extends ExprNode {
    protected final ExprNode retValue;

    public ReturnNode(ExprNode retValue) {
        super("Return");
        this.retValue = retValue;
    }

    public ReturnNode() {
        super("Return");
        this.retValue = null;
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) {
            sb.append("\r\n");
        }

        // like: Return
        sb.append(prefix).append(t).append(link).append(name)
                .append(retValue == null ? "" : retValue.toTreeString(level + 1, prefix));

        return sb.toString();
    }

    @Override
    public void updateTable(VariableTable vt, ProcedureTable pt, String scopeName, VarKind kind, int level) {
        if (retValue != null) {
            retValue.updateTable(vt, pt, scopeName, kind, level);
        }
    }
}
