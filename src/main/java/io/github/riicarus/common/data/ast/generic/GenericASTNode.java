package io.github.riicarus.common.data.ast.generic;

import io.github.riicarus.common.data.ast.ASTNode;
import io.github.riicarus.common.data.table.SymbolTable;
import io.github.riicarus.common.data.table.VarKind;

/**
 * 通用型 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-17 23:12
 * @since 1.0.0
 */
public abstract class GenericASTNode extends ASTNode {

    protected final String name;
    protected String scopeName;

    public GenericASTNode(String name) {
        this.name = name;
    }

    public final void updateTable(SymbolTable table, VarKind varKind) {
        setScopeName(table.getCurScopeName());
        doUpdateTable(table, varKind);
    }

    protected abstract void doUpdateTable(SymbolTable table, VarKind varKind);

    public String getScopeName() {
        return scopeName;
    }

    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }
}
