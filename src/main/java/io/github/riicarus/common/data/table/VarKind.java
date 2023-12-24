package io.github.riicarus.common.data.table;

/**
 * 变量分类
 *
 * @author Riicarus
 * @create 2023-12-24 10:03
 * @since 1.0.0
 */
public enum VarKind {

    // 变量(实参)
    VARIABLE(0),
    // 形参
    PARAMETER(1);

    private final int code;

    VarKind(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
