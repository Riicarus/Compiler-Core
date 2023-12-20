package io.github.riicarus.front.syntax.ll1;

import io.github.riicarus.common.data.ast.DetailedASTCreator;
import io.github.riicarus.common.data.ast.detailed.DetailedASTNode;
import io.github.riicarus.common.data.ast.detailed.NonterminalASTNode;
import io.github.riicarus.front.syntax.SyntaxProduction;
import io.github.riicarus.front.syntax.SyntaxSymbol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * LL1 文法产生式.
 *
 * @author Riicarus
 * @create 2023-11-22 22:17
 * @since 1.0.0
 */
public class LL1SyntaxProduction<T extends NonterminalASTNode> implements SyntaxProduction<T> {

    private final SyntaxSymbol lhs;
    private final List<SyntaxSymbol> rhs = new ArrayList<>();
    private final DetailedASTCreator<T> creator;

    public LL1SyntaxProduction(SyntaxSymbol lhs, List<SyntaxSymbol> rhs, DetailedASTCreator<T> creator) {
        if (lhs == null)
            throw new IllegalArgumentException("LL1 syntax production build failed: production's left hand side can not be null.");

        if (rhs == null || rhs.isEmpty())
            throw new IllegalArgumentException("LL1 syntax production build failed: production's right hand side can not be null or empty.");

        this.lhs = lhs;
        this.rhs.addAll(rhs);
        this.creator = creator;
    }

    @Override
    public SyntaxSymbol getLHS() {
        return lhs;
    }

    @Override
    public List<SyntaxSymbol> getRHS() {
        return Collections.unmodifiableList(rhs);
    }

    @Override
    public DetailedASTCreator<T> getASTCreator() {
        return creator;
    }

    @Override
    public T createNode(List<DetailedASTNode> children) {
        final T node = creator.apply(children);
        node.setSymbol(lhs);
        return node;
    }

    @Override
    public String toString() {
        return lhs + " -> " + rhs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LL1SyntaxProduction<?> that = (LL1SyntaxProduction<?>) o;
        return Objects.equals(lhs, that.lhs) && Objects.deepEquals(rhs, that.rhs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lhs, rhs);
    }
}
