package io.github.riicarus.common.data.ast.generic.expr.v;

import io.github.riicarus.common.data.ast.generic.ProtoTypeNode;
import io.github.riicarus.common.data.ast.generic.type.TypeNode;
import io.github.riicarus.common.data.table.SymbolTable;
import io.github.riicarus.common.data.table.VarKind;
import io.github.riicarus.common.data.table.type.FuncType;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 函数 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-23 5:17
 * @since 1.0.0
 */
public class FunctionNode extends VariableNode {

    private static final AtomicInteger ANONYMOUS_FUNC_ID_GEN = new AtomicInteger(0);

    private ProtoTypeNode protoTypeNode;

    public FunctionNode(String varName) {
        super(varName);
    }

    public FunctionNode(VariableNode variableNode) {
        super(variableNode.getVarName());
    }

    public FunctionNode() {
        super("ANONYMOUS_FUNC_" + ANONYMOUS_FUNC_ID_GEN.getAndIncrement());
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
                .append(protoTypeNode == null ? "" : protoTypeNode.toTreeString(level + 1, prefix));

        return sb.toString();
    }

    @Override
    public void doUpdateTable(SymbolTable table, VarKind varKind) {
        if (protoTypeNode != null) {
            FuncType funcType = new FuncType();
            funcType.setReturnType(protoTypeNode.getReturnTypeNode().getVarType());
            protoTypeNode.getArgNodeList().forEach(n -> funcType.addArgType(n.getTypeNode().getVarType()));

            table.addVariable(varName, varKind, funcType);
            table.addProcedure(
                    varName,
                    protoTypeNode.getReturnTypeNode().getVarType(),
                    protoTypeNode.getArgEntryList()
            );

            table.enterNewScope(varName);
            protoTypeNode.updateTable(table, VarKind.VARIABLE);
            table.exitScope();
        }
    }

    public ProtoTypeNode getProtoTypeNode() {
        return protoTypeNode;
    }

    public void setProtoTypeNode(ProtoTypeNode protoTypeNode) {
        this.protoTypeNode = protoTypeNode;
    }

    public void setReturnType(TypeNode typeNode) {
        if (protoTypeNode != null) {
            protoTypeNode.setReturnTypeNode(typeNode);
        }
    }
}
