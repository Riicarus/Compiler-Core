package io.github.riicarus.common.data;

/**
 * 抽象 AST
 *
 * @author Riicarus
 * @create 2023-12-6 13:44
 * @since 1.0.0
 */
public abstract class AbstractAST<T> {

    protected final AstType type;

    public AbstractAST(AstType type) {
        this.type = type;
    }

    public AstType getType() {
        return type;
    }

    public abstract String toTreeString(int level);
}
