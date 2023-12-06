package io.github.riicarus.common.data.ast;

import java.util.function.Function;

/**
 * AST 值节点的值类型
 *
 * @author Riicarus
 * @create 2023-12-6 14:53
 * @since 1.0.0
 */
public class ValueType<T> {

    public static final ValueType<Integer> INT_VALUE_TYPE = new ValueType<>(Integer::parseInt);
    public static final ValueType<String> STRING_VALUE_TYPE = new ValueType<>(val -> val);
    public static final ValueType<Double> DOUBLE_VALUE_TYPE = new ValueType<>(Double::parseDouble);
    public static final ValueType<Float> FLOAT_VALUE_TYPE = new ValueType<>(Float::parseFloat);

    private final Function<String, T> castFunc;

    public ValueType(Function<String, T> castFunc) {
        this.castFunc = castFunc;
    }

    public final T cast(String val) {
        return castFunc.apply(val);
    }

}
