package io.github.riicarus.common.data.ast.generic.code;

import io.github.riicarus.common.data.ast.generic.GenericASTNode;

/**
 * 语句 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-23 5:52
 * @since 1.0.0
 */
public class StatementNode extends GenericASTNode {

    private GenericASTNode curNode;

    // 只用来在构建过程中保存语句先后关系, 不参加打印
    private StatementNode nextStatementNode;

    public StatementNode() {
        super("Statement");
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
                .append(curNode == null ? "" : curNode.toTreeString(level + 1, prefix));

        return sb.toString();
    }

    public GenericASTNode getCurNode() {
        return curNode;
    }

    public void setCurNode(GenericASTNode curNode) {
        this.curNode = curNode;
    }

    public StatementNode getNextStatementNode() {
        return nextStatementNode;
    }

    public void setNextStatementNode(StatementNode nextStatementNode) {
        this.nextStatementNode = nextStatementNode;
    }
}
