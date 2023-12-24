package io.github.riicarus.common.data.table;

import io.github.riicarus.common.data.table.type.VarType;

import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * 过程信息
 *
 * @author Riicarus
 * @create 2023-12-24 10:10
 * @since 1.0.0
 */
public record ProcedureInfo(String name, VarType returnType, int level, List<ArgEntry> argList) {

    public record ArgEntry(String name, VarType type) {}

    @Override
    public String toString() {
        return "[" +
                name + ", " +
                returnType + ", " +
                level + ", " +
                "args: [" + argList.stream().map(e -> e.type.toString() + " " + e.name).collect(joining(", ")) + "]" +
                "]";
    }
}
