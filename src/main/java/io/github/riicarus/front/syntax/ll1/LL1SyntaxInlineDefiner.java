package io.github.riicarus.front.syntax.ll1;

import io.github.riicarus.common.data.AstConstructStrategy;
import io.github.riicarus.common.util.CharUtil;
import io.github.riicarus.front.syntax.SyntaxProduction;
import io.github.riicarus.front.syntax.SyntaxSymbol;
import io.github.riicarus.front.syntax.SyntaxSymbolType;

import java.util.*;

/**
 * 内联定义 LL1 文法
 *
 * @author Riicarus
 * @create 2023-12-4 10:41
 * @since 1.0.0
 */
public class LL1SyntaxInlineDefiner extends LL1SyntaxDefiner {

    private static final SyntaxSymbol<?> END_SYMBOL = new LL1SyntaxSymbol<>(String.valueOf(CharUtil.LEX_SYNTAX_END), true, SyntaxSymbolType.ASST);
    private final SyntaxSymbol<?> epsSymbol;
    private final SyntaxSymbol<?> endSymbol;
    private final SyntaxSymbol<?> startSymbol;

    private final Map<SyntaxSymbol<?>, Set<SyntaxProduction>> productionMap = new HashMap<>();
    private final Map<String, SyntaxSymbol<?>> syntaxSymbolMap = new HashMap<>();

    private final Map<SyntaxSymbol<?>, Integer> opPrecedenceMap = new HashMap<>();
    private final boolean useDefaultOpPrecedence;
    private final AstConstructStrategy strategy;

    public LL1SyntaxInlineDefiner(SyntaxSymbol<?> epsSymbol,
                                  SyntaxSymbol<?> startSymbol,
                                  SyntaxSymbol<?> endSymbol,
                                  Map<SyntaxSymbol<?>, Integer> opPrecedenceMap,
                                  AstConstructStrategy strategy) {
        this.epsSymbol = epsSymbol;
        this.startSymbol = startSymbol;
        this.endSymbol = endSymbol;

        syntaxSymbolMap.put(epsSymbol.getName(), epsSymbol);
        syntaxSymbolMap.put(startSymbol.getName(), startSymbol);

        this.opPrecedenceMap.putAll(opPrecedenceMap);
        useDefaultOpPrecedence = false;

        this.strategy = strategy;
    }

    public LL1SyntaxInlineDefiner(SyntaxSymbol<?> epsSymbol,
                                  SyntaxSymbol<?> startSymbol,
                                  SyntaxSymbol<?> endSymbol,
                                  AstConstructStrategy strategy) {
        this.epsSymbol = epsSymbol;
        this.startSymbol = startSymbol;
        this.endSymbol = endSymbol;

        syntaxSymbolMap.put(epsSymbol.getName(), epsSymbol);
        syntaxSymbolMap.put(startSymbol.getName(), startSymbol);

        useDefaultOpPrecedence = true;

        this.strategy = strategy;
    }

    public LL1SyntaxInlineDefiner(SyntaxSymbol<?> epsSymbol,
                                  SyntaxSymbol<?> startSymbol,
                                  Map<SyntaxSymbol<?>, Integer> opPrecedenceMap,
                                  AstConstructStrategy strategy) {
        this.epsSymbol = epsSymbol;
        this.startSymbol = startSymbol;
        this.endSymbol = END_SYMBOL;

        syntaxSymbolMap.put(epsSymbol.getName(), epsSymbol);
        syntaxSymbolMap.put(startSymbol.getName(), startSymbol);

        this.opPrecedenceMap.putAll(opPrecedenceMap);
        useDefaultOpPrecedence = false;

        this.strategy = strategy;
    }

    public LL1SyntaxInlineDefiner(SyntaxSymbol<?> epsSymbol,
                                  SyntaxSymbol<?> startSymbol,
                                  AstConstructStrategy strategy) {
        this.epsSymbol = epsSymbol;
        this.startSymbol = startSymbol;
        this.endSymbol = END_SYMBOL;

        syntaxSymbolMap.put(epsSymbol.getName(), epsSymbol);
        syntaxSymbolMap.put(startSymbol.getName(), startSymbol);

        useDefaultOpPrecedence = true;

        this.strategy = strategy;
    }

    public void addSymbols(Set<SyntaxSymbol<?>> symbolList) {
        symbolList.forEach(s -> syntaxSymbolMap.put(s.getName(), s));
    }

    public void addProduction(String head, List<String> body) {
        final SyntaxSymbol<?> headSymbol = syntaxSymbolMap.get(head);
        if (headSymbol == null) {
            throw new IllegalStateException("Parse LL1Syntax definition failed: Syntax definition error, can not find symbol: " + head);
        }
        if (headSymbol.isTerminal()) {
            throw new IllegalStateException("Parse LL1Syntax definition failed: Syntax definition error, head symbol can not be terminal: " + head);
        }

        List<SyntaxSymbol<?>> bodySymbolList = new ArrayList<>();
        for (String s : body) {
            final SyntaxSymbol<?> bodySymbol = syntaxSymbolMap.get(s);
            if (bodySymbol == null) {
                throw new IllegalStateException("Parse LL1Syntax definition failed: Syntax definition error, can not find symbol: " + s);
            }
            bodySymbolList.add(bodySymbol);
        }

        productionMap.putIfAbsent(headSymbol, new HashSet<>());
        productionMap.get(headSymbol).add(new LL1SyntaxProduction(headSymbol, bodySymbolList));
    }

    @Override
    protected List<SyntaxProduction> loadSyntaxProductions() {
        if (useDefaultOpPrecedence) {
            opPrecedenceMap.clear();
            opPrecedenceMap.putAll(loadDefaultOpPrecedenceMap());
        }

        List<SyntaxProduction> productionList = new ArrayList<>();
        productionMap.values().forEach(productionList::addAll);
        return productionList;
    }

    protected Map<SyntaxSymbol<?>, Integer> loadDefaultOpPrecedenceMap() {
        Map<SyntaxSymbol<?>, Integer> opPreMap = new HashMap<>();

        Set<SyntaxProduction> startProductionSet = productionMap.get(startSymbol);
        computeOpPrecedence(startProductionSet, 0, opPreMap);

        System.out.println("OpPrecedence:");
        opPreMap.entrySet().forEach(System.out::println);

        return opPreMap;
    }

    private void computeOpPrecedence(Set<SyntaxProduction> productionSet, int level, Map<SyntaxSymbol<?>, Integer> opPreMap) {
        if (productionSet.isEmpty()) return;

        Set<SyntaxProduction> nextProductionSet = new HashSet<>();
        for (SyntaxProduction p : productionSet) {
            p.getBody().stream().filter(s -> s.getType().equals(SyntaxSymbolType.OP)).forEach(s -> opPreMap.put(s, level));
            p.getBody().stream().filter(s -> !s.isTerminal() && !s.equals(p.getHead())).forEach(s -> nextProductionSet.addAll(productionMap.get(s)));
        }

        computeOpPrecedence(nextProductionSet, level + 1, opPreMap);
    }

    @Override
    public Set<SyntaxSymbol<?>> getSyntaxSymbolSet() {
        return new HashSet<>(syntaxSymbolMap.values());
    }

    @Override
    public SyntaxSymbol<?> getStartSymbol() {
        return startSymbol;
    }

    @Override
    public SyntaxSymbol<?> getEndSymbol() {
        return endSymbol;
    }

    @Override
    public SyntaxSymbol<?> getEpsilonSymbol() {
        return epsSymbol;
    }

    @Override
    public Map<SyntaxSymbol<?>, Integer> getOpPrecedenceMap() {
        return opPrecedenceMap;
    }

    @Override
    public AstConstructStrategy getAstConstructStrategy() {
        return strategy;
    }

    public void setOpPrecedenceMap(Map<SyntaxSymbol<?>, Integer> opPrecedenceMap) {
        this.opPrecedenceMap.clear();
        this.opPrecedenceMap.putAll(opPrecedenceMap);
    }
}
