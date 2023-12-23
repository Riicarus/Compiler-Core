package io.github.riicarus.common.data.ast.generic.expr.func;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.ast.generic.expr.v.VariableNode;
import io.github.riicarus.common.data.ast.generic.type.ProtoTypeNode;
import io.github.riicarus.common.data.ast.generic.type.TypeNode;

/**
 * 函数 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-23 5:17
 * @since 1.0.0
 */
public class FunctionNode extends ExprNode {

    private VariableNode varNode;
    private ProtoTypeNode protoTypeNode;

    public FunctionNode() {
        super("Function");
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
                .append(varNode == null ? "" : varNode.toTreeString(level + 1, prefix))
                .append(protoTypeNode == null ? "" : protoTypeNode.toTreeString(level + 1, prefix));

        return sb.toString();
    }

    public VariableNode getVarNode() {
        return varNode;
    }

    public void setVarNode(VariableNode varNode) {
        this.varNode = varNode;
    }

    public ProtoTypeNode getProtoTypeNode() {
        return protoTypeNode;
    }

    public void setProtoTypeNode(ProtoTypeNode protoTypeNode) {
        this.protoTypeNode = protoTypeNode;
    }

    public void setReturnType(TypeNode typeNode) {
        if (protoTypeNode != null) {
            protoTypeNode.setReturnType(typeNode);
        }
    }
}
