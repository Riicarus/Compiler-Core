package io.github.riicarus.common.data.ast.generic.type;

import io.github.riicarus.common.data.ast.generic.GenericASTNode;
import io.github.riicarus.common.data.table.ProcedureTable;
import io.github.riicarus.common.data.table.VarKind;
import io.github.riicarus.common.data.table.VariableTable;
import io.github.riicarus.common.data.table.type.VarType;

/**
 * 类型定义节点
 *
 * @author Riicarus
 * @create 2023-12-22 22:11
 * @since 1.0.0
 */
public abstract class TypeNode extends GenericASTNode {
    public TypeNode(String name) {
        super(name);
    }

    public abstract VarType getVarType();

    @Override
    public void updateTable(VariableTable vt, ProcedureTable pt, String scopeName, VarKind kind, int level) {
    }
}
