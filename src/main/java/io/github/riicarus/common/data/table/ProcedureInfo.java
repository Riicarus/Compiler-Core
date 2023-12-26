package io.github.riicarus.common.data.table;

import io.github.riicarus.common.data.table.type.VarType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.joining;

/**
 * 过程信息
 *
 * @author Riicarus
 * @create 2023-12-24 10:10
 * @since 1.0.0
 */
public final class ProcedureInfo {
    private final String name;
    private final String scopeName;
    private final VarType returnType;
    private final int level;
    private final List<ArgEntry> argList = new ArrayList<>();

    public ProcedureInfo(String name, String scopeName, VarType returnType, List<ArgEntry> argList, int level) {
        this.name = name;
        this.scopeName = scopeName;
        this.returnType = returnType;
        this.level = level;
        this.argList.addAll(argList);
    }

    public record ArgEntry(String name, VarType type) {
    }

    public String getName() {
        return name;
    }

    public VarType getReturnType() {
        return returnType;
    }

    public int getLevel() {
        return level;
    }

    public List<ArgEntry> getArgList() {
        return argList;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ProcedureInfo) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.returnType, that.returnType) &&
                this.level == that.level &&
                Objects.equals(this.argList, that.argList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, returnType, level, argList);
    }

    @Override
    public String toString() {
        return "[" +
                name + ", " +
                scopeName + ", " +
                returnType + ", " +
                "args: [" + argList.stream().map(e -> e.type.toString() + " " + e.name).collect(joining(", ")) + "]," +
                level + ", " +
                "]";
    }

}
