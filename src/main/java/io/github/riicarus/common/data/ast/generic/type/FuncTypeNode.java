package io.github.riicarus.common.data.ast.generic.type;

import io.github.riicarus.common.data.table.SymbolTable;
import io.github.riicarus.common.data.table.VarKind;
import io.github.riicarus.common.data.table.type.FuncType;

import java.util.ArrayList;
import java.util.List;

/**
 * Function 类型 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-22 22:50
 * @since 1.0.0
 */
public class FuncTypeNode extends TypeNode {

    private TypeNode returnType;
    private final List<TypeNode> argTypeList = new ArrayList<>();

    public FuncTypeNode() {
        super("FUNCTION");
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) {
            sb.append("\r\n");
        }

        // like: Func
        sb.append(prefix).append(t).append(link).append(name)
                .append("\t\t").append(getScopeName())
                .append(returnType.toTreeString(level + 1, prefix));
        argTypeList.forEach(n -> sb.append(n.toTreeString(level + 1, prefix)));

        return sb.toString();
    }

    public TypeNode getReturnType() {
        return returnType;
    }

    public List<TypeNode> getArgTypeList() {
        return argTypeList;
    }

    public void setReturnType(TypeNode returnType) {
        this.returnType = returnType;
    }

    /**
     * <p>如果函数的返回类型还是函数, 那么应该设置返回类型的 返回类型.</p>
     * <p>如果函数的返回类型只是普通类型, 那么将其设置为函数的返回类型.</p>
     *
     * @param retType 返回类型节点
     */
    public void deepSetReturnType(TypeNode retType) {
        if (returnType instanceof FuncTypeNode) {
            ((FuncTypeNode) returnType).deepSetReturnType(retType);
            return;
        }

        returnType = retType;
    }

    public void addArgType(TypeNode typeNode) {
        argTypeList.add(0, typeNode);
    }

    @Override
    public FuncType getVarType() {
        FuncType funcType = new FuncType();

        funcType.setReturnType(returnType.getVarType());
        argTypeList.forEach(n -> funcType.addArgType(n.getVarType()));
        return funcType;
    }

    @Override
    public void doUpdateTable(SymbolTable table, VarKind varKind) {
        returnType.updateTable(table, varKind);
        argTypeList.forEach(n -> n.updateTable(table, varKind));
    }
}
