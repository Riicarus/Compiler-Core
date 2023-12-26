package io.github.riicarus.common.data.ast.generic.expr.ctrl;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.table.SymbolTable;
import io.github.riicarus.common.data.table.VarKind;

/**
 * continue 语句 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-22 15:50
 * @since 1.0.0
 */
public class ContinueNode extends ExprNode {

    public ContinueNode() {
        super("CONTINUE");
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) {
            sb.append("\r\n");
        }

        // like: continue
        sb.append(prefix).append(t).append(link).append(name)
                .append("\t\t").append(getScopeName());

        return sb.toString();
    }

    @Override
    public void doUpdateTable(SymbolTable table, VarKind varKind) {
    }
}
