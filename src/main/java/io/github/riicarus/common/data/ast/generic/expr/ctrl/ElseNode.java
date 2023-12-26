package io.github.riicarus.common.data.ast.generic.expr.ctrl;

import io.github.riicarus.common.data.ast.generic.code.CodeBlockNode;
import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.table.SymbolTable;
import io.github.riicarus.common.data.table.VarKind;

import java.util.ArrayList;
import java.util.List;

/**
 * else 跟随的执行语句 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-18 0:55
 * @since 1.0.0
 */
public class ElseNode extends ExprNode {

    private final List<ElseIfNode> elseIfList = new ArrayList<>();
    private CodeBlockNode codeBlockNode;

    public ElseNode() {
        super("ELSE");
    }

    @Override
    public final String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) {
            sb.append("\r\n");
        }

        // like: else
        sb.append(prefix).append(t).append(link)
                .append(name).append("\t\t").append(getScopeName());
        elseIfList.forEach(n -> sb.append(n.toTreeString(level + 1, prefix)));
        sb.append(codeBlockNode == null ? "" : codeBlockNode.toTreeString(level + 1, prefix));

        return sb.toString();
    }

    @Override
    public void doUpdateTable(SymbolTable table, VarKind varKind) {
        elseIfList.forEach(n -> n.updateTable(table, VarKind.VARIABLE));

        table.enterNewScope(name);
        if (codeBlockNode != null) {
            codeBlockNode.updateTable(table, VarKind.VARIABLE);
        }
        table.exitScope();
    }

    public void addElseIfNode(ElseIfNode elseIfNode) {
        elseIfList.add(0, elseIfNode);
    }

    public List<ElseIfNode> getElseIfList() {
        return elseIfList;
    }

    public CodeBlockNode getCodeBlockNode() {
        return codeBlockNode;
    }

    public void setCodeBlockNode(CodeBlockNode codeBlockNode) {
        this.codeBlockNode = codeBlockNode;
    }
}
