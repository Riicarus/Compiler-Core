package io.github.riicarus.common.data.ast.generic.expr.ctrl;

import io.github.riicarus.common.data.ast.generic.code.CodeBlockNode;
import io.github.riicarus.common.data.ast.generic.expr.ExprNode;

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

        sb.append(prefix).append(t).append(link).append(name)
                .append(forInitNode == null ? "" : forInitNode.toTreeString(level + 1, prefix))
                .append(forConditionNode == null ? "" : forConditionNode.toTreeString(level + 1, prefix))
                .append(forUpdateNode == null ? "" : forUpdateNode.toTreeString(level + 1, prefix))
                .append(codeBlockNode == null ? "" : codeBlockNode.toTreeString(level + 1, prefix));

        return sb.toString();
    }
}
