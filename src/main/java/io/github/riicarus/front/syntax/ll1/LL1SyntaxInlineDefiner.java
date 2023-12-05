package io.github.riicarus.front.syntax.ll1;

import io.github.riicarus.common.util.CharUtil;
import io.github.riicarus.front.syntax.SyntaxProduction;
import io.github.riicarus.front.syntax.SyntaxSymbol;

import java.util.*;

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

    private final List<SyntaxProduction> productionList = new ArrayList<>();
    private final Map<String, SyntaxSymbol> syntaxSymbolMap = new HashMap<>();

    public LL1SyntaxInlineDefiner(SyntaxSymbol epsSymbol, SyntaxSymbol endSymbol, SyntaxSymbol startSymbol) {
        this.epsSymbol = epsSymbol;
        this.endSymbol = endSymbol;
        this.startSymbol = startSymbol;

        syntaxSymbolMap.put(epsSymbol.getName(), epsSymbol);
        syntaxSymbolMap.put(startSymbol.getName(), startSymbol);
    }

    public LL1SyntaxInlineDefiner(SyntaxSymbol epsSymbol, SyntaxSymbol startSymbol) {
        this.epsSymbol = epsSymbol;
        this.startSymbol = startSymbol;
        this.endSymbol = END_SYMBOL;

        syntaxSymbolMap.put(epsSymbol.getName(), epsSymbol);
        syntaxSymbolMap.put(startSymbol.getName(), startSymbol);
    }

    public void addTerminalSymbols(Set<String> terminalSymbolList) {
        terminalSymbolList.forEach(s -> syntaxSymbolMap.put(s, new LL1SyntaxSymbol(s, true)));
    }

    public void addNonterminalSymbols(Set<String> nonterminalSymbolList) {
        nonterminalSymbolList.forEach(s -> syntaxSymbolMap.put(s, new LL1SyntaxSymbol(s, false)));
    }

    public void addProduction(String head, List<String> body) {
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

        productionList.add(new LL1SyntaxProduction(headSymbol, bodySymbolList));
    }

    @Override
    protected List<SyntaxProduction> loadSyntaxProductions() {
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
