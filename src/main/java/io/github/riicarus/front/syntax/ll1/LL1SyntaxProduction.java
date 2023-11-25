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

    private final Set<Character> firstSet = new HashSet<>();
    private final Set<Character> followSet = new HashSet<>();

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
    public Set<Character> getFirstSet() {
        return Collections.unmodifiableSet(firstSet);
    }

    @Override
    public Set<Character> getFollowerSet() {
        return Collections.unmodifiableSet(followSet);
    }

    @Override
    public void addFirst(char c) {
        firstSet.add(c);
    }

    @Override
    public void addFollow(char c) {
        followSet.add(c);
    }

    @Override
    public String toString() {
        return head + " -> " + body + "; first: " + firstSet + " , follow: " + followSet;
    }
}
