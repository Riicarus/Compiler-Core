package io.github.riicarus.common.data.table;

import io.github.riicarus.common.data.table.type.VarType;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.joining;

/**
 * 作用域, 保存作用域中的变量和过程
 *
 * @author Riicarus
 * @create 2023-12-25 21:12
 * @since 1.0.0
 */
public class Scope {

    private static final AtomicInteger ANONYMOUS_SCOPE_ID_GEN = new AtomicInteger(0);

    private final String name;
    private final String fullName;
    private final int level;
    private final Map<String, VariableInfo> variableMap = new HashMap<>();
    private final Map<String, ProcedureInfo> procedureMap = new HashMap<>();

    private final Scope parentScope;
    private final Map<String, Scope> children = new HashMap<>();

    public Scope(String name, int index, String upperScopeFullName, int level, Scope parentScope) {
        if (name == null) {
            name = "ANONYMOUS_SCOPE_" + ANONYMOUS_SCOPE_ID_GEN.getAndIncrement();
        }

        this.name = name + "#" + index;
        this.fullName = upperScopeFullName == null ? this.name : upperScopeFullName + "." + this.name;
        this.level = level;
        this.parentScope = parentScope;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public int getLevel() {
        return level;
    }

    public void addVariable(String varName, VarKind varKind, VarType varType) {
        VariableInfo variableInfo = new VariableInfo(varName, fullName, varKind, varType, level);

        variableMap.put(variableInfo.getName(), variableInfo);
    }

    public VariableInfo getVariable(String name) {
        return variableMap.get(name);
    }

    public boolean containsVariable(String name) {
        return variableMap.containsKey(name);
    }

    public void addProcedure(String proName, VarType retType, List<ProcedureInfo.ArgEntry> argList) {
        ProcedureInfo procedureInfo = new ProcedureInfo(proName, fullName, retType, argList, level);
        procedureMap.put(procedureInfo.getName(), procedureInfo);
    }

    public ProcedureInfo getProcedure(String name) {
        return procedureMap.get(name);
    }

    public boolean containsProcedure(String name) {
        return procedureMap.containsKey(name);
    }

    public Scope addScope(String scopeName) {
        Scope scope = new Scope(scopeName, children.size(), fullName, level + 1, this);

        children.put(scope.getFullName(), scope);
        return scope;
    }

    public Scope getScope(String name) {
        return children.get(name);
    }

    public Scope getParentScope() {
        return parentScope;
    }

    public Collection<Scope> getChildrenScopes() {
        return children.values();
    }

    public String print(String prefix) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix).append(fullName).append("(").append(level).append("):");
        if (isEmpty()) {
            return sb.append(" EMPTY").toString();
        }

        if (!variableMap.isEmpty()) {
            sb.append("\r\n").append(prefix).append("\t").append("Variables:").append("\r\n");
            sb.append(prefix).append("\t\t").append(
                    variableMap.values().stream()
                            .map(VariableInfo::toString)
                            .collect(joining("\r\n" + prefix + "\t\t"))
            );
        }
        if (!procedureMap.isEmpty()) {
            sb.append("\r\n").append(prefix).append("\t").append("Procedures:").append("\r\n");
            sb.append(prefix).append("\t\t").append(
                    procedureMap.values().stream()
                            .map(ProcedureInfo::toString)
                            .collect(joining("\r\n" + prefix + "\t\t"))
            );
        }

        return sb.toString();
    }

    public boolean isEmpty() {
        return variableMap.isEmpty() && procedureMap.isEmpty();
    }
}
