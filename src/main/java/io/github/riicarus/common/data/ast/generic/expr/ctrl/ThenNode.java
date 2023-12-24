package io.github.riicarus.common.data.ast.generic.expr.ctrl;

import io.github.riicarus.common.data.ast.generic.code.CodeBlockNode;
import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.table.ProcedureTable;
import io.github.riicarus.common.data.table.VarKind;
import io.github.riicarus.common.data.table.VariableTable;

/**
 * then 跟随的执行语句 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-18 0:52
 * @since 1.0.0
 */
public class ThenNode extends ExprNode {

    private final CodeBlockNode codeBlockNode;

    public ThenNode(CodeBlockNode codeBlockNode) {
        super("Then");
        this.codeBlockNode = codeBlockNode;
    }

    @Override
    public final String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) {
            sb.append("\r\n");
        }

        // like: then
        sb.append(prefix).append(t).append(link).append(name)
                .append(codeBlockNode == null ? "" : codeBlockNode.toTreeString(level + 1, prefix));

        return sb.toString();
    }

    @Override
    public void updateTable(VariableTable vt, ProcedureTable pt, String scopeName, VarKind kind, int level) {
        if (codeBlockNode != null) {
            codeBlockNode.updateTable(vt, pt, scopeName + "#" + CodeBlockNode.genCodeBlockName(name), kind, level);
        }
    }
}
