package io.github.riicarus.common.data.table;

import io.github.riicarus.common.data.table.type.VarType;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 符号表
 *
 * @author Riicarus
 * @create 2023-12-25 21:51
 * @since 1.0.0
 */
public class SymbolTable {
    private final Scope rootScope = new Scope("$MAIN", 0, null, 0, null);
    private Scope curScope = rootScope;
    private boolean readOnly = false;

    public void enterNewScope(String scopeName) {
        if (readOnly) {
            throw new IllegalStateException("Syntax error: ast scope scan has already finished, scope is read only, can not enter new.");
        }

        curScope = curScope.addScope(scopeName);
    }

    public void enterScope(String scopeName) {
        if (!readOnly) {
            throw new IllegalStateException("Syntax error: can not enter scope before ast scope scan finished.");
        }

        Scope nextScope = curScope.getScope(scopeName);
        if (nextScope == null) {
            throw new IllegalStateException("Syntax error: can not enter scope: " + scopeName);
        }
        curScope = curScope.getScope(scopeName);
    }

    public void exitScope() {
        Scope parentScope = curScope.getParentScope();
        if (parentScope != null) {
            curScope = parentScope;
        }
    }

    public void finishScan() {
        readOnly = true;
        curScope = rootScope;
    }

    public void addVariable(String varName, VarKind varKind, VarType varType) {
        curScope.addVariable(varName, varKind, varType);
    }

    public void addProcedure(String proName, VarType retType, List<ProcedureInfo.ArgEntry> argList) {
        curScope.addProcedure(proName, retType, argList);
    }

    public VariableInfo getVariable(String name) {
        Scope scope = curScope;
        while (scope != null) {
            if (scope.containsVariable(name)) {
                return scope.getVariable(name);
            }

            scope = scope.getParentScope();
        }

        return null;
    }

    public ProcedureInfo getProcedure(String name) {
        Scope scope = curScope;
        while (scope != null) {
            if (scope.containsProcedure(name)) {
                return scope.getProcedure(name);
            }

            scope = scope.getParentScope();
        }

        return null;
    }

    public String getCurScopeName() {
        return curScope.getFullName();
    }

    public String print(String prefix) {
        List<String> stringList = new LinkedList<>();
        Queue<Scope> q = new LinkedList<>();
        q.add(rootScope);

        while (!q.isEmpty()) {
            Scope scope = q.poll();
            stringList.add(scope.print(prefix));
            q.addAll(scope.getChildrenScopes());
        }

        return String.join("\r\n", stringList);
    }
}
