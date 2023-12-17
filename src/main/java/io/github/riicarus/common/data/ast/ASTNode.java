package io.github.riicarus.common.data.ast;

import io.github.riicarus.front.syntax.SyntaxSymbol;

/**
 * 抽象 AST
 *
 * @author Riicarus
 * @create 2023-12-6 13:44
 * @since 1.0.0
 */
public abstract class ASTNode {

    protected SyntaxSymbol symbol;

    public ASTNode() {
    }

    public ASTNode(SyntaxSymbol symbol) {
        this.symbol = symbol;
    }

    public SyntaxSymbol getSymbol() {
        return symbol;
    }

    public void setSymbol(SyntaxSymbol symbol) {
        this.symbol = symbol;
    }

    public abstract String toTreeString(int level, String prefix);

    public String print(String prefix) {
        return toTreeString(0, prefix);
    }
}
