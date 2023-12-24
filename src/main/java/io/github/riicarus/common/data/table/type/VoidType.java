package io.github.riicarus.common.data.table.type;

/**
 * void 类型
 *
 * @author Riicarus
 * @create 2023-12-24 10:44
 * @since 1.0.0
 */
public class VoidType extends VarType {

    private static final VoidType INSTANCE = new VoidType();

    private VoidType() {
        super("VOID");
    }

    public static VoidType getInstance() {
        return INSTANCE;
    }
}
