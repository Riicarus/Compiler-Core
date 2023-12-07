package io.github.riicarus.front.syntax.ll1;

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
public class LL1SyntaxProduction implements SyntaxProduction {

    private final SyntaxSymbol<?> head;
    private final List<SyntaxSymbol<?>> body = new ArrayList<>();

    public LL1SyntaxProduction(SyntaxSymbol<?> head, List<SyntaxSymbol<?>> body) {
        if (head == null)
            throw new IllegalArgumentException("LL1 syntax production build failed: production's head can not be null.");

        if (body == null || body.isEmpty())
            throw new IllegalArgumentException("LL1 syntax production build failed: production's body can not be null or empty.");

        this.head = head;
        this.body.addAll(body);
    }

    @Override
    public SyntaxSymbol<?> getHead() {
        return head;
    }

    @Override
    public List<SyntaxSymbol<?>> getBody() {
        return Collections.unmodifiableList(body);
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
        return Objects.equals(head, that.head) && Objects.deepEquals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, body);
    }
}
