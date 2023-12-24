package io.github.riicarus.common.data.table.type;

/**
 * 变量类型
 *
 * @author Riicarus
 * @create 2023-12-24 10:04
 * @since 1.0.0
 */
public abstract class VarType {

    protected final String name;

    public VarType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
