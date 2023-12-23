package io.github.riicarus.common.data.ast.generic.expr.v;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.ast.generic.type.TypeNode;

/**
 * <p>变量 AST 节点</p>
 * <p>包含各种变量, 如: int, bool, function .etc</p>
 *
 * @author Riicarus
 * @create 2023-12-17 23:31
 * @since 1.0.0
 */
public class VariableNode extends ExprNode {

    protected final String varName;
    protected TypeNode typeNode;

    public VariableNode(String varName) {
        super("Var");
        this.varName = varName;
    }

    @Override
    public final String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) {
            sb.append("\r\n");
        }

        // like: Var (VarName)
        sb.append(prefix).append(t).append(link)
                .append(name)
                .append("(").append(varName).append(")")
                .append(typeNode == null ? "" : typeNode.toTreeString(level + 1, prefix));

        return sb.toString();
    }

    public String getVarName() {
        return varName;
    }

    public TypeNode getTypeNode() {
        return typeNode;
    }

    public void setTypeNode(TypeNode typeNode) {
        this.typeNode = typeNode;
    }
}
