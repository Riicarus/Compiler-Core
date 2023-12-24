package io.github.riicarus.common.data.ast.generic.code;

import io.github.riicarus.common.data.ast.generic.GenericASTNode;
import io.github.riicarus.common.data.table.ProcedureTable;
import io.github.riicarus.common.data.table.VarKind;
import io.github.riicarus.common.data.table.VariableTable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 代码块 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-18 20:39
 * @since 1.0.0
 */
public class CodeBlockNode extends GenericASTNode {

    private static final AtomicInteger CODE_BLOCK_ID_GEN = new AtomicInteger(0);

    public static String genCodeBlockName(String scopeName) {
        return scopeName + "#CODE_BLOCK_" + CODE_BLOCK_ID_GEN.getAndIncrement();
    }

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

    @Override
    public void updateTable(VariableTable vt, ProcedureTable pt, String scopeName, VarKind kind, int level) {
        statementList.forEach(n -> n.updateTable(vt, pt, scopeName, kind, level + 1));
    }

    public void addStatement(StatementNode statementNode) {
        statementList.add(statementNode);
    }

    public List<StatementNode> getStatementList() {
        return statementList;
    }
}
