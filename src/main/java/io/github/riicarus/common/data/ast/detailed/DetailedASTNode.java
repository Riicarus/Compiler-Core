package io.github.riicarus.common.data.ast.detailed;

import io.github.riicarus.common.data.ast.ASTNode;
import io.github.riicarus.front.syntax.SyntaxSymbol;

/**
 * 抽象 AST
 *
 * @author Riicarus
 * @create 2023-12-6 13:44
 * @since 1.0.0
 */
public abstract class DetailedASTNode extends ASTNode {

    protected SyntaxSymbol symbol;

    public DetailedASTNode() {
    }

    public DetailedASTNode(SyntaxSymbol symbol) {
        this.symbol = symbol;
    }

    public SyntaxSymbol getSymbol() {
        return symbol;
    }

    public void setSymbol(SyntaxSymbol symbol) {
        this.symbol = symbol;
    }

}
