package io.github.riicarus.front.syntax.ll1;

import io.github.riicarus.common.data.ast.DetailedASTCreator;
import io.github.riicarus.common.data.ast.detailed.NonterminalASTNode;
import io.github.riicarus.common.util.CharUtil;
import io.github.riicarus.front.syntax.SyntaxProduction;
import io.github.riicarus.front.syntax.SyntaxSymbol;

import java.util.*;

import static java.util.stream.Collectors.joining;

/**
 * 内联定义 LL1 文法
 *
 * @author Riicarus
 * @create 2023-12-4 10:41
 * @since 1.0.0
 */
public class LL1SyntaxInlineDefiner extends LL1SyntaxDefiner {

    private static final SyntaxSymbol END_SYMBOL = new LL1SyntaxSymbol(String.valueOf(CharUtil.LEX_SYNTAX_END), true);
    private final SyntaxSymbol epsSymbol;
    private final SyntaxSymbol endSymbol;
    private final SyntaxSymbol startSymbol;

    private final Map<SyntaxSymbol, Set<SyntaxProduction<?>>> productionMap = new HashMap<>();
    private final Map<String, SyntaxSymbol> syntaxSymbolMap = new HashMap<>();

    public LL1SyntaxInlineDefiner(SyntaxSymbol epsSymbol,
                                  SyntaxSymbol startSymbol,
                                  SyntaxSymbol endSymbol,
                                  Map<SyntaxSymbol, Integer> opPrecedenceMap) {
        this.epsSymbol = epsSymbol;
        this.startSymbol = startSymbol;
        this.endSymbol = endSymbol;

        syntaxSymbolMap.put(epsSymbol.getName(), epsSymbol);
        syntaxSymbolMap.put(startSymbol.getName(), startSymbol);
    }

    public LL1SyntaxInlineDefiner(SyntaxSymbol epsSymbol,
                                  SyntaxSymbol startSymbol,
                                  SyntaxSymbol endSymbol) {
        this.epsSymbol = epsSymbol;
        this.startSymbol = startSymbol;
        this.endSymbol = endSymbol;

        syntaxSymbolMap.put(epsSymbol.getName(), epsSymbol);
        syntaxSymbolMap.put(startSymbol.getName(), startSymbol);
    }

    public LL1SyntaxInlineDefiner(SyntaxSymbol epsSymbol,
                                  SyntaxSymbol startSymbol,
                                  Map<SyntaxSymbol, Integer> opPrecedenceMap) {
        this.epsSymbol = epsSymbol;
        this.startSymbol = startSymbol;
        this.endSymbol = END_SYMBOL;

        syntaxSymbolMap.put(epsSymbol.getName(), epsSymbol);
        syntaxSymbolMap.put(startSymbol.getName(), startSymbol);
    }

    public LL1SyntaxInlineDefiner(SyntaxSymbol epsSymbol,
                                  SyntaxSymbol startSymbol) {
        this.epsSymbol = epsSymbol;
        this.startSymbol = startSymbol;
        this.endSymbol = END_SYMBOL;

        syntaxSymbolMap.put(epsSymbol.getName(), epsSymbol);
        syntaxSymbolMap.put(startSymbol.getName(), startSymbol);
    }

    public <T extends NonterminalASTNode> void addProduction(SyntaxSymbol lhs, List<SyntaxSymbol> rhs, DetailedASTCreator<T> constructor) {
        if (lhs == null) {
            throw new IllegalStateException("Parse LL1Syntax definition failed: Syntax definition error, production's left hand size symbol can not be null.");
        }
        if (lhs.isTerminal()) {
            throw new IllegalStateException("Parse LL1Syntax definition failed: Syntax definition error, production's left hand size symbol can not be terminal: " + lhs);
        }

        if (rhs == null || rhs.isEmpty()) {
            throw new IllegalStateException("Parse LL1Syntax definition failed: Syntax definition error, production's right hand size symbol list can not be null or empty.");
        }

        productionMap.putIfAbsent(lhs, new HashSet<>());
        productionMap.get(lhs).add(new LL1SyntaxProduction<>(lhs, rhs, constructor));

        syntaxSymbolMap.putIfAbsent(lhs.getName(), lhs);
        rhs.forEach(s -> syntaxSymbolMap.putIfAbsent(s.getName(), s));
    }

    @Override
    protected List<SyntaxProduction<?>> loadSyntaxProductions() {
        List<SyntaxProduction<?>> productionList = new ArrayList<>();
        productionMap.values().forEach(productionList::addAll);

        System.out.println();
        System.out.println("Production Load:");
        productionMap.forEach((k, v) -> {
            String sb = k.getName() + " --> " +
                    v.stream().map(p -> p.getRHS().stream().map(SyntaxSymbol::getName).collect(joining(" "))).collect(joining(" | "));
            System.out.println("\t" + sb);
        });

        return productionList;
    }

    @Override
    public Set<SyntaxSymbol> getSyntaxSymbolSet() {
        return new HashSet<>(syntaxSymbolMap.values());
    }

    @Override
    public SyntaxSymbol getStartSymbol() {
        return startSymbol;
    }

    @Override
    public SyntaxSymbol getEndSymbol() {
        return endSymbol;
    }

    @Override
    public SyntaxSymbol getEpsilonSymbol() {
        return epsSymbol;
    }
}
