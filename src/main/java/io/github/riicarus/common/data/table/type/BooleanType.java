package io.github.riicarus.common.data.table.type;

/**
 * boolean 类型
 *
 * @author Riicarus
 * @create 2023-12-24 10:42
 * @since 1.0.0
 */
public class BooleanType extends VarType {

    private static final BooleanType INSTANCE = new BooleanType();

    private BooleanType() {
        super("BOOL");
    }

    public static BooleanType getInstance() {
        return INSTANCE;
    }
}
