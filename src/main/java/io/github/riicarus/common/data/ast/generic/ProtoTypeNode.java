package io.github.riicarus.common.data.ast.generic;

import io.github.riicarus.common.data.ast.generic.code.CodeBlockNode;
import io.github.riicarus.common.data.ast.generic.expr.v.VariableNode;
import io.github.riicarus.common.data.ast.generic.type.TypeNode;
import io.github.riicarus.common.data.table.ProcedureInfo;
import io.github.riicarus.common.data.table.SymbolTable;
import io.github.riicarus.common.data.table.VarKind;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>函数原型 AST 节点</p>
 * <p>函数定义会生成一个 PrototypeNode, 保存函数的类型信息, 包括:</p>
 * <li>参数列表</li>
 * <li>函数体</li>
 * <li>返回值</li>
 *
 * @author Riicarus
 * @create 2023-12-18 16:34
 * @since 1.0.0
 */
public class ProtoTypeNode extends GenericASTNode {

    protected final List<VariableNode> argNodeList = new ArrayList<>();
    protected CodeBlockNode codeBlockNode;
    protected TypeNode returnTypeNode;

    public ProtoTypeNode() {
        super("PROTOTYPE");
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) {
            sb.append("\r\n");
        }

        // like: Prototype
        sb.append(prefix).append(t).append(link).append(name)
                .append(getScopeName());
        argNodeList.forEach(n -> sb.append(n.toTreeString(level + 1, prefix)));
        sb.append(returnTypeNode == null ? "" : returnTypeNode.toTreeString(level + 1, prefix))
                .append(codeBlockNode == null ? "" : codeBlockNode.toTreeString(level + 1, prefix));

        return sb.toString();
    }

    public List<VariableNode> getArgNodeList() {
        return argNodeList;
    }

    public List<ProcedureInfo.ArgEntry> getArgEntryList() {
        return argNodeList.stream()
                .map(n -> new ProcedureInfo.ArgEntry(n.getVarName(), n.getTypeNode().getVarType()))
                .collect(Collectors.toList());
    }

    public void addArgNode(VariableNode argNode) {
        this.argNodeList.add(argNode);
    }

    public CodeBlockNode getCodeBlockNode() {
        return codeBlockNode;
    }

    public void setCodeBlockNode(CodeBlockNode codeBlockNode) {
        this.codeBlockNode = codeBlockNode;
    }

    public TypeNode getReturnTypeNode() {
        return returnTypeNode;
    }

    public void setReturnTypeNode(TypeNode returnTypeNode) {
        this.returnTypeNode = returnTypeNode;
    }

    @Override
    public void doUpdateTable(SymbolTable table, VarKind varKind) {
        argNodeList.forEach(n -> n.updateTable(table, VarKind.PARAMETER));
        codeBlockNode.updateTable(table, VarKind.VARIABLE);
        returnTypeNode.updateTable(table, VarKind.PARAMETER);
    }
}
