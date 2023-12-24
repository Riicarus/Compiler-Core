package io.github.riicarus.common.data.table;

import io.github.riicarus.common.data.table.type.VarType;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 变量信息, 用于变量名表
 *
 * @author Riicarus
 * @create 2023-12-24 9:52
 * @since 1.0.0
 */
public final class VariableInfo {
    private static final AtomicInteger ADDR_GEN = new AtomicInteger(0);

    private final String name;
    private final String scopeName;
    private final VarKind kind;
    private final VarType type;
    private final int level;
    private final int addr;

    public VariableInfo(String name, String scopeName, VarKind kind, VarType type, int level) {
        this.name = name;
        this.scopeName = scopeName;
        this.kind = kind;
        this.type = type;
        this.level = level;
        this.addr = ADDR_GEN.getAndIncrement();
    }

    public String name() {
        return name;
    }

    public String procedure() {
        return scopeName;
    }

    public VarKind kind() {
        return kind;
    }

    public VarType type() {
        return type;
    }

    public int level() {
        return level;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (VariableInfo) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.scopeName, that.scopeName) &&
                Objects.equals(this.kind, that.kind) &&
                Objects.equals(this.type, that.type) &&
                this.level == that.level;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, scopeName, kind, type, level);
    }

    @Override
    public String toString() {
        return "[" +
                name + ", " +
                scopeName + ", " +
                kind + ", " +
                type.getName() + ", " +
                level +
                "]";
    }


}
