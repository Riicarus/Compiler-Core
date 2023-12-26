package io.github.riicarus.common.data.ast.generic.expr.ctrl;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.table.SymbolTable;
import io.github.riicarus.common.data.table.VarKind;

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
        super("RETURN");
        this.retValue = retValue;
    }

    public ReturnNode() {
        super("RETURN");
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
                .append("\t\t").append(getScopeName())
                .append(retValue == null ? "" : retValue.toTreeString(level + 1, prefix));

        return sb.toString();
    }

    @Override
    public void doUpdateTable(SymbolTable table, VarKind varKind) {
        if (retValue != null) {
            retValue.updateTable(table, varKind);
        }
    }
}
