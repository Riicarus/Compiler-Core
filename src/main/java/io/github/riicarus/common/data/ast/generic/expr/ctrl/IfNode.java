package io.github.riicarus.common.data.ast.generic.expr.ctrl;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.table.ProcedureTable;
import io.github.riicarus.common.data.table.VarKind;
import io.github.riicarus.common.data.table.VariableTable;

/**
 * if AST 节点
 *
 * @author Riicarus
 * @create 2023-12-18 0:46
 * @since 1.0.0
 */
public class IfNode extends ExprNode {

    private final IfConditionNode condition;
    private final ThenNode then;
    private final ElseNode _else;

    public IfNode(IfConditionNode condition, ThenNode then, ElseNode _else) {
        super("If");
        this.condition = condition;
        this.then = then;
        this._else = _else;
    }

    @Override
    public final String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) {
            sb.append("\r\n");
        }

        // like: if
        sb.append(prefix).append(t).append(link).append(name)
                .append(condition == null ? "" : condition.toTreeString(level + 1, prefix))
                .append(then == null ? "" : then.toTreeString(level + 1, prefix))
                .append(_else == null ? "" : _else.toTreeString(level + 1, prefix));

        return sb.toString();
    }

    @Override
    public void updateTable(VariableTable vt, ProcedureTable pt, String scopeName, VarKind kind, int level) {
        // if 主干语句本身不增加层数, 只有其中的 codeBlock 增加层数.
        if (condition != null) {
            condition.updateTable(vt, pt, scopeName, kind, level);
        }
        if (then != null) {
            then.updateTable(vt, pt, scopeName, kind, level + 1);
        }
        if (_else != null) {
            _else.updateTable(vt, pt, scopeName, kind, level + 1);
        }
    }
}
