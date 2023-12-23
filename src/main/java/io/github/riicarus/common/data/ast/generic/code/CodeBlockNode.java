package io.github.riicarus.common.data.ast.generic.code;

import io.github.riicarus.common.data.ast.generic.GenericASTNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 代码块 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-18 20:39
 * @since 1.0.0
 */
public class CodeBlockNode extends GenericASTNode {

    protected final List<StatementNode> statementList = new ArrayList<>();

    public CodeBlockNode(List<StatementNode> statementList) {
        super("CodeBlock");
        this.statementList.addAll(statementList);
    }

    public CodeBlockNode() {
        super("CodeBlock");
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) {
            sb.append("\r\n");
        }

        // like: CodeBlock
        sb.append(prefix).append(t).append(link).append(name);
        statementList.forEach(n -> sb.append(n.toTreeString(level + 1, prefix)));

        return sb.toString();
    }

    public void addStatement(StatementNode statementNode) {
        statementList.add(statementNode);
    }

    public List<StatementNode> getStatementList() {
        return statementList;
    }
}
