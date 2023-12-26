package io.github.riicarus.common.data.ast.generic.expr.ctrl;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.table.SymbolTable;
import io.github.riicarus.common.data.table.VarKind;

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
        super("FOR_CONDITION");
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
                .append("\t\t").append(getScopeName())
                .append(exprNode == null ? "" : exprNode.toTreeString(level + 1, prefix));

        return sb.toString();
    }

    @Override
    public void doUpdateTable(SymbolTable table, VarKind varKind) {
        if (exprNode != null) {
            exprNode.updateTable(table, VarKind.VARIABLE);
        }
    }
}
