package io.github.riicarus.common.data.ast.generic.expr.ctrl;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.table.SymbolTable;
import io.github.riicarus.common.data.table.VarKind;

/**
 * if 条件语句 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-18 0:46
 * @since 1.0.0
 */
public class IfConditionNode extends ExprNode {

    private final ExprNode condition;

    public IfConditionNode(ExprNode condition) {
        super("IF_CONDITION");
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

        // like: condition
        sb.append(prefix).append(t).append(link)
                .append(name).append("\t\t").append(getScopeName())
                .append(condition == null ? "" : condition.toTreeString(level + 1, prefix));

        return sb.toString();
    }

    @Override
    public void doUpdateTable(SymbolTable table, VarKind varKind) {
        if (condition != null) {
            condition.updateTable(table, VarKind.VARIABLE);
        }
    }
}
