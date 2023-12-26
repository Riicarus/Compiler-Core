package io.github.riicarus.common.data.table.type;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * func 类型
 *
 * @author Riicarus
 * @create 2023-12-24 10:50
 * @since 1.0.0
 */
public class FuncType extends VarType {

    private VarType returnType;
    private final List<VarType> argTypeList = new ArrayList<>();

    public FuncType() {
        super("FUNCTION");
    }

    public void addArgType(VarType type) {
        argTypeList.add(type);
    }

    public void setReturnType(VarType type) {
        returnType = type;
    }

    public VarType getReturnType() {
        return returnType;
    }

    public List<VarType> getArgTypeList() {
        return argTypeList;
    }

    @Override
    public String toString() {
        return returnType.toString() + " " + name + "(" + argTypeList.stream().map(VarType::toString).collect(joining(", ")) + ")";
    }
}
