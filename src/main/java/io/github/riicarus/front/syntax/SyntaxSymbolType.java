package io.github.riicarus.front.syntax;

/**
 * 文法符号类型.
 *
 * @author Riicarus
 * @create 2023-11-22 22:22
 * @since 1.0.0
 */
public enum SyntaxSymbolType {

    // 终结符对应的类型
    OP, VALUE, CTRL, ACT,
    // 辅助类型
    ASST,
    // 非终结符的类型
    EXPR

}
