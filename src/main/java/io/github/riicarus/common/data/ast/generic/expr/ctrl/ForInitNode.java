package io.github.riicarus.common.data.ast.generic.expr.ctrl;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.ast.generic.expr.op.compute.AssignNode;
import io.github.riicarus.common.data.table.SymbolTable;
import io.github.riicarus.common.data.table.VarKind;

import java.util.ArrayList;
import java.util.List;

/**
 * For 初始化 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-23 6:27
 * @since 1.0.0
 */
public class ForInitNode extends ExprNode {

    private final List<AssignNode> initNodeList = new ArrayList<>();

    public ForInitNode() {
        super("FOR_INIT");
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
                .append("\t\t").append(getScopeName());
        initNodeList.forEach(n -> sb.append(n.toTreeString(level + 1, prefix)));

        return sb.toString();
    }

    @Override
    public void doUpdateTable(SymbolTable table, VarKind varKind) {
        initNodeList.forEach(n -> n.updateTable(table, VarKind.VARIABLE));
    }

    public void addInitNode(AssignNode initNode) {
        initNodeList.add(0, initNode);
    }

    public List<AssignNode> getInitNodeList() {
        return initNodeList;
    }
}
