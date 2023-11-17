package io.github.riicarus.front.lexer;

/**
 * NFA 的一条边.
 *
 * @author Riicarus
 * @create 2023-11-16 19:49
 * @since 1.0.0
 */
public class NfaEdge {

    // 边的起点
    private final int fromState;

    // 边的终点
    private final int toState;

    // 边的转换值
    private final char transValue;

    // 使用一个不是 ASCII 的 char 进行标志
    public static final char EPS_TRANS_VALUE = (char) 128;

    /**
     * 创建一个边.
     *
     * @param fromState 起始状态.
     * @param toState 终止状态.
     * @param transValue 转换值.
     */
    public NfaEdge(int fromState, char transValue, int toState) {
        this.fromState = fromState;
        this.transValue = transValue;
        this.toState = toState;
    }

    /**
     * 创建一个 eps 边.
     *
     * @param fromState 起始状态
     * @param toState 终止状态
     */
    public NfaEdge(int fromState, int toState) {
        this.fromState = fromState;
        this.toState = toState;
        this.transValue = EPS_TRANS_VALUE;
    }

    public int getFromState() {
        return fromState;
    }

    public int getToState() {
        return toState;
    }

    public char getTransValue() {
        return transValue;
    }

    @Override
    public String toString() {
        return fromState + "--" + transValue + "-->" + toState;
    }
}
