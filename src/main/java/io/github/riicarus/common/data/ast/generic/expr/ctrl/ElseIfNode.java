package io.github.riicarus.common.data.ast.generic.expr.ctrl;

import io.github.riicarus.common.data.ast.generic.code.CodeBlockNode;
import io.github.riicarus.common.data.ast.generic.expr.ExprNode;

/**
 * else if AST 节点
 *
 * @author Riicarus
 * @create 2023-12-23 6:11
 * @since 1.0.0
 */
public class ElseIfNode extends ExprNode {

    private ExprNode conditionNode;
    private CodeBlockNode codeBlockNode;

    public ElseIfNode() {
        super("ElseIf");
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
                .append(conditionNode == null ? "" : conditionNode.toTreeString(level + 1, prefix))
                .append(codeBlockNode == null ? "" : codeBlockNode.toTreeString(level + 1, prefix));

        return sb.toString();
    }

    public ExprNode getConditionNode() {
        return conditionNode;
    }

    public void setConditionNode(ExprNode conditionNode) {
        this.conditionNode = conditionNode;
    }

    public CodeBlockNode getCodeBlockNode() {
        return codeBlockNode;
    }

    public void setCodeBlockNode(CodeBlockNode codeBlockNode) {
        this.codeBlockNode = codeBlockNode;
    }
}
