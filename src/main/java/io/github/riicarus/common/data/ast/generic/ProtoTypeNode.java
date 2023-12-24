package io.github.riicarus.common.data.ast.generic;

import io.github.riicarus.common.data.ast.generic.code.CodeBlockNode;
import io.github.riicarus.common.data.ast.generic.expr.v.VariableNode;
import io.github.riicarus.common.data.ast.generic.type.TypeNode;
import io.github.riicarus.common.data.table.ProcedureTable;
import io.github.riicarus.common.data.table.VarKind;
import io.github.riicarus.common.data.table.VariableTable;

import java.util.ArrayList;
import java.util.List;

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
    protected CodeBlockNode body;
    protected TypeNode returnType;

    public ProtoTypeNode() {
        super("Prototype");
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
        sb.append(prefix).append(t).append(link).append(name);
        argNodeList.forEach(n -> sb.append(n.toTreeString(level + 1, prefix)));
        sb.append(returnType == null ? "" : returnType.toTreeString(level + 1, prefix))
                .append(body == null ? "" : body.toTreeString(level + 1, prefix));

        return sb.toString();
    }

    public List<VariableNode> getArgNodeList() {
        return argNodeList;
    }

    public void addArgNode(VariableNode argNode) {
        this.argNodeList.add(argNode);
    }

    public CodeBlockNode getBody() {
        return body;
    }

    public void setBody(CodeBlockNode body) {
        this.body = body;
    }

    public TypeNode getReturnType() {
        return returnType;
    }

    public void setReturnType(TypeNode returnType) {
        this.returnType = returnType;
    }

    @Override
    public void updateTable(VariableTable vt, ProcedureTable pt, String scopeName, VarKind kind, int level) {
        argNodeList.forEach(n -> n.updateTable(vt, pt, scopeName, VarKind.PARAMETER, level));
        body.updateTable(vt, pt, scopeName + "#" + CodeBlockNode.genCodeBlockName(name), VarKind.VARIABLE, level);
        returnType.updateTable(vt, pt, scopeName, kind, level);
    }
}
