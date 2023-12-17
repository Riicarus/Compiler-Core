package io.github.riicarus.common.data.ast;

import io.github.riicarus.front.syntax.SyntaxSymbol;

/**
 * 终结符 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-17 3:56
 * @since 1.0.0
 */
public abstract class NonterminalASTNode extends ASTNode {

    protected SyntaxSymbol symbol;

    public SyntaxSymbol getSymbol() {
        return symbol;
    }

    public void setSymbol(SyntaxSymbol symbol) {
        this.symbol = symbol;
    }
}
