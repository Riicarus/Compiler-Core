package io.github.riicarus.common.data.ast.generic.expr.ctrl;

import io.github.riicarus.common.data.ast.generic.code.CodeBlockNode;
import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.table.ProcedureTable;
import io.github.riicarus.common.data.table.VarKind;
import io.github.riicarus.common.data.table.VariableTable;

/**
 * For AST 节点
 *
 * @author Riicarus
 * @create 2023-12-23 6:27
 * @since 1.0.0
 */
public class ForNode extends ExprNode {

    private final ForInitNode forInitNode;
    private final ForConditionNode forConditionNode;
    private final ForUpdateNode forUpdateNode;
    private final CodeBlockNode codeBlockNode;

    public ForNode(ForInitNode forInitNode, ForConditionNode forConditionNode, ForUpdateNode forUpdateNode, CodeBlockNode codeBlockNode) {
        super("For");
        this.forInitNode = forInitNode;
        this.forConditionNode = forConditionNode;
        this.forUpdateNode = forUpdateNode;
        this.codeBlockNode = codeBlockNode;
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) {
            sb.append("\r\n");
        }

        sb.append(prefix).append(t).append(link).append(name).append(forInitNode == null ? "" : forInitNode.toTreeString(level + 1, prefix)).append(forConditionNode == null ? "" : forConditionNode.toTreeString(level + 1, prefix)).append(forUpdateNode == null ? "" : forUpdateNode.toTreeString(level + 1, prefix)).append(codeBlockNode == null ? "" : codeBlockNode.toTreeString(level + 1, prefix));

        return sb.toString();
    }

    @Override
    public void updateTable(VariableTable vt, ProcedureTable pt, String scopeName, VarKind kind, int level) {
        if (forInitNode != null) {
            forInitNode.updateTable(vt, pt, scopeName, kind, level + 1);
        }
        if (forConditionNode != null) {
            forConditionNode.updateTable(vt, pt, scopeName, kind, level + 1);
        }
        if (forUpdateNode != null) {
            forUpdateNode.updateTable(vt, pt, scopeName, kind, level + 1);
        }

        // codeBlockNode 会将 level 自增 1, 需要保证 for 中所有变量的 level 一致.
        if (codeBlockNode != null) {
            codeBlockNode.updateTable(vt, pt, scopeName + "#" + CodeBlockNode.genCodeBlockName(name), kind, level + 1);
        }
    }
}
