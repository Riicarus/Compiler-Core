package io.github.riicarus.common.data.ast.generic.expr.ctrl;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.ast.generic.expr.op.compute.AssignNode;
import io.github.riicarus.common.data.table.SymbolTable;
import io.github.riicarus.common.data.table.VarKind;

import java.util.ArrayList;
import java.util.List;

/**
 * For 循环更新 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-23 4:57
 * @since 1.0.0
 */
public class ForUpdateNode extends ExprNode {

    private final List<AssignNode> updateList = new ArrayList<>();

    public ForUpdateNode() {
        super("FOR_UPDATE");
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) {
            sb.append("\r\n");
        }

        // like: ForUpdate
        sb.append(prefix).append(t).append(link).append(name)
                .append("\t\t").append(getScopeName());
        updateList.forEach(n -> sb.append(n.toTreeString(level + 1, prefix)));

        return sb.toString();
    }

    @Override
    public void doUpdateTable(SymbolTable table, VarKind varKind) {
        updateList.forEach(n -> n.updateTable(table, VarKind.VARIABLE));
    }

    public void addUpdate(AssignNode updateNode) {
        updateList.add(0, updateNode);
    }

    public List<AssignNode> getUpdateList() {
        return updateList;
    }
}
