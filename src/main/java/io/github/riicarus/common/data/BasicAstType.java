package io.github.riicarus.common.data;

/**
 * 基础 AST 类型
 *
 * @author Riicarus
 * @create 2023-12-7 18:08
 * @since 1.0.0
 */
public enum BasicAstType implements AstType {

    OP, VALUE, VAR;

    @Override
    public AstType getType() {
        return this;
    }
}
