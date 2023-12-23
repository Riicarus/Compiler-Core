package io.github.riicarus.common.data.ast.detailed;

import io.github.riicarus.common.data.ast.generic.GenericASTNode;
import io.github.riicarus.front.syntax.SyntaxSymbol;

/**
 * 终结符 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-17 3:56
 * @since 1.0.0
 */
public abstract class NonterminalASTNode extends DetailedASTNode {

    protected SyntaxSymbol symbol;

    public SyntaxSymbol getSymbol() {
        return symbol;
    }

    public void setSymbol(SyntaxSymbol symbol) {
        this.symbol = symbol;
    }

    /**
     * 简化 AST 节点为通用型节点.
     *
     * @return 简化后的通用型 AST 节点
     */
    public abstract GenericASTNode toGeneric();
}
