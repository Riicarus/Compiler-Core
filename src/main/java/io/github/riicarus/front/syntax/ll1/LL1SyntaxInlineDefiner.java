package io.github.riicarus.front.syntax.ll1;

import io.github.riicarus.common.data.ast.ASTCreator;
import io.github.riicarus.common.data.ast.NonterminalASTNode;
import io.github.riicarus.common.util.CharUtil;
import io.github.riicarus.front.syntax.SyntaxProduction;
import io.github.riicarus.front.syntax.SyntaxSymbol;
import io.github.riicarus.front.syntax.SyntaxSymbolType;

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

    private static final SyntaxSymbol END_SYMBOL = new LL1SyntaxSymbol(String.valueOf(CharUtil.LEX_SYNTAX_END), SyntaxSymbolType.ASST, true);
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

    public void addSymbols(Set<SyntaxSymbol> symbolList) {
        symbolList.forEach(s -> syntaxSymbolMap.put(s.getName(), s));
    }

    public <T extends NonterminalASTNode> void addProduction(String head, List<String> body, ASTCreator<T> constructor) {
        final SyntaxSymbol headSymbol = syntaxSymbolMap.get(head);
        if (headSymbol == null) {
            throw new IllegalStateException("Parse LL1Syntax definition failed: Syntax definition error, can not find symbol: " + head);
        }
        if (headSymbol.isTerminal()) {
            throw new IllegalStateException("Parse LL1Syntax definition failed: Syntax definition error, head symbol can not be terminal: " + head);
        }

        List<SyntaxSymbol> bodySymbolList = new ArrayList<>();
        for (String s : body) {
            final SyntaxSymbol bodySymbol = syntaxSymbolMap.get(s);
            if (bodySymbol == null) {
                throw new IllegalStateException("Parse LL1Syntax definition failed: Syntax definition error, can not find symbol: " + s);
            }
            bodySymbolList.add(bodySymbol);
        }

        productionMap.putIfAbsent(headSymbol, new HashSet<>());
        productionMap.get(headSymbol).add(new LL1SyntaxProduction<>(headSymbol, bodySymbolList, constructor));
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
