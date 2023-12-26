package io.github.riicarus.common.data.table;

import io.github.riicarus.common.data.table.type.VarType;

import java.util.Objects;

/**
 * 变量信息, 用于变量名表
 *
 * @author Riicarus
 * @create 2023-12-24 9:52
 * @since 1.0.0
 */
public final class VariableInfo {

    private final String name;
    private final String scopeName;
    private final VarKind varKind;
    private final VarType varType;
    private final int level;

    public VariableInfo(String name, String scopeName, VarKind varKind, VarType varType, int level) {
        this.name = name;
        this.scopeName = scopeName;
        this.varKind = varKind;
        this.varType = varType;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public String getProcedure() {
        return scopeName;
    }

    public VarKind getVarKind() {
        return varKind;
    }

    public VarType getVarType() {
        return varType;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (VariableInfo) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.scopeName, that.scopeName) &&
                Objects.equals(this.varKind, that.varKind) &&
                Objects.equals(this.varType, that.varType) &&
                this.level == that.level;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, scopeName, varKind, varType, level);
    }

    @Override
    public String toString() {
        return "[" +
                name + ", " +
                scopeName + ", " +
                varKind + ", " +
                varType + ", " +
                level +
                "]";
    }


}
