package io.github.riicarus.front.syntax.ll1;

import io.github.riicarus.front.syntax.SyntaxProduction;
import io.github.riicarus.front.syntax.SyntaxSymbol;

import java.util.*;

/**
 * LL1 文法产生式.
 *
 * @author Riicarus
 * @create 2023-11-22 22:17
 * @since 1.0.0
 */
public class LL1SyntaxProduction implements SyntaxProduction {

    private final SyntaxSymbol head;
    private final Set<List<SyntaxSymbol>> body = new HashSet<>();

    public LL1SyntaxProduction(SyntaxSymbol head, Set<List<SyntaxSymbol>> body) {
        this.head = head;
        this.body.addAll(body);
    }

    @Override
    public SyntaxSymbol getHead() {
        return head;
    }

    @Override
    public Set<List<SyntaxSymbol>> getBody() {
        return Collections.unmodifiableSet(body);
    }

    @Override
    public String toString() {
        return head + " -> " + body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LL1SyntaxProduction that = (LL1SyntaxProduction) o;
        return Objects.equals(head, that.head);
    }

    @Override
    public int hashCode() {
        return Objects.hash(head);
    }
}
