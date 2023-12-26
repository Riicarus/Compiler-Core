package io.github.riicarus.common.data.ast.generic.expr.v;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.ast.generic.type.TypeNode;
import io.github.riicarus.common.data.table.SymbolTable;
import io.github.riicarus.common.data.table.VarKind;

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
        super("VAR");
        this.varName = varName;
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) {
            sb.append("\r\n");
        }

        // like: Var (VarName)
        sb.append(prefix).append(t).append(link).append(name)
                .append("(").append(varName).append(")")
                .append("\t\t").append(getScopeName())
                .append(typeNode == null ? "" : typeNode.toTreeString(level + 1, prefix));

        return sb.toString();
    }

    @Override
    public void doUpdateTable(SymbolTable table, VarKind varKind) {
        if (typeNode != null) {
            table.addVariable(varName, varKind, typeNode.getVarType());

            typeNode.updateTable(table, varKind);
        }
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
