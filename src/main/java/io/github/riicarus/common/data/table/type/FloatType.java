package io.github.riicarus.common.data.table.type;

/**
 * float 类型
 *
 * @author Riicarus
 * @create 2023-12-24 10:43
 * @since 1.0.0
 */
public class FloatType extends VarType {

    private static final FloatType INSTANCE = new FloatType();

    private FloatType() {
        super("FLOAT");
    }

    public static FloatType getInstance() {
        return INSTANCE;
    }
}
