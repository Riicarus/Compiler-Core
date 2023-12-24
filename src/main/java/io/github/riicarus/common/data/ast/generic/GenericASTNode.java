package io.github.riicarus.common.data.ast.generic;

import io.github.riicarus.common.data.ast.ASTNode;
import io.github.riicarus.common.data.table.ProcedureTable;
import io.github.riicarus.common.data.table.VarKind;
import io.github.riicarus.common.data.table.VariableTable;

/**
 * 通用型 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-17 23:12
 * @since 1.0.0
 */
public abstract class GenericASTNode extends ASTNode {

    protected final String name;

    public GenericASTNode(String name) {
        this.name = name;
    }

    public abstract void updateTable(VariableTable vt, ProcedureTable pt, String scopeName, VarKind kind, int level);

}
