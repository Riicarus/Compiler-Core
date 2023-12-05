package io.github.riicarus.front.syntax.ll1;

import io.github.riicarus.common.util.CharUtil;
import io.github.riicarus.front.syntax.SyntaxProduction;
import io.github.riicarus.front.syntax.SyntaxSymbol;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * LL1 文法定义.
 *
 * @author Riicarus
 * @create 2023-11-23 15:14
 * @since 1.0.0
 */
public class LL1SyntaxFileDefiner extends LL1SyntaxDefiner {

    private final String syntaxFilePath;
    private SyntaxSymbol epsSymbol;
    private SyntaxSymbol startSymbol;
    private static final SyntaxSymbol END_SYMBOL = new LL1SyntaxSymbol(String.valueOf(CharUtil.LEX_SYNTAX_END), true);

    private final List<String> syntaxStringList = new ArrayList<>();
    private final Map<String, SyntaxSymbol> syntaxSymbolMap = new HashMap<>();

    private final Set<SyntaxSymbol> terminalSymbolSet = new HashSet<>();
    private final Set<SyntaxSymbol> nonterminalSymbolSet = new HashSet<>();

    public LL1SyntaxFileDefiner(String syntaxFilePath) {
        this.syntaxFilePath = syntaxFilePath;
    }

    @Override
    protected List<SyntaxProduction> loadSyntaxProductions() {
        loadFromFile();

        return constructProductionList();
    }

    private void loadFromFile() {
        // 读取文法定义文件
        try (
                BufferedReader reader = new BufferedReader(new FileReader(syntaxFilePath))
        ) {
            String eps = reader.readLine();
            if (eps == null || eps.isEmpty()) {
                throw new IllegalStateException("Load LL1Syntax definition failed: Syntax definition error: You must define a \"epsilon\" symbol in the first line.");
            }
            epsSymbol = new LL1SyntaxSymbol(handleEscapeBack(handleEscape(eps)), true);
            terminalSymbolSet.add(epsSymbol);

            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty() && !line.equals(CharUtil.SYNTAX_SEPARATOR)) {
                terminalSymbolSet.add(new LL1SyntaxSymbol(handleEscapeBack(handleEscape(line)), true));
            }

            int ithNonterminal = 0;
            while ((line = reader.readLine()) != null && !line.isEmpty() && !line.equals(CharUtil.SYNTAX_SEPARATOR)) {
                final LL1SyntaxSymbol symbol = new LL1SyntaxSymbol(handleEscapeBack(handleEscape(line)), false);

                // 文法符号定义区域中的第一个符号需要为开始符号.
                if (ithNonterminal == 0) {
                    startSymbol = symbol;
                }
                ithNonterminal++;

                if (terminalSymbolSet.contains(symbol))
                    throw new IllegalStateException("Load LL1Syntax definition failed: Syntax definition error: symbol \"" + symbol + "\" has already been defined as terminal symbol.");

                nonterminalSymbolSet.add(symbol);
            }

            while ((line = reader.readLine()) != null && !line.isEmpty() && !line.equals(CharUtil.SYNTAX_SEPARATOR)) {
                syntaxStringList.add(handleEscape(line));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Load LL1Syntax definition failed.", e);
        }

        checkSyntax();

        constructSyntaxSymbolMap();
    }

    private void checkSyntax() {
        if (terminalSymbolSet.isEmpty() || nonterminalSymbolSet.isEmpty() || syntaxStringList.isEmpty()) {
            throw new IllegalStateException("Load LL1Syntax definition failed: Syntax definition is not complete.");
        }
    }

    private void constructSyntaxSymbolMap() {
        terminalSymbolSet.forEach(s -> syntaxSymbolMap.put(s.getName(), s));
        nonterminalSymbolSet.forEach(s -> syntaxSymbolMap.put(s.getName(), s));
    }

    private List<SyntaxProduction> constructProductionList() {
        List<SyntaxProduction> syntaxProductionList = new ArrayList<>();

        for (String expr : syntaxStringList) {
            String[] headBodyParts = expr.split(CharUtil.SYNTAX_DEFINE);
            if (headBodyParts.length != 2) {
                throw new IllegalStateException("Parse LL1Syntax definition failed: Syntax definition error, need: \"head -> body\", get: " + expr);
            }

            SyntaxSymbol head = syntaxSymbolMap.get(handleEscapeBack(headBodyParts[0]));
            if (head == null) {
                throw new IllegalStateException("Parse LL1Syntax definition failed: Syntax definition error, can not find symbol: " + headBodyParts[0]);
            }

            for (SyntaxProduction production : syntaxProductionList) {
                if (production.getHead().equals(head)) {
                    throw new IllegalStateException("Parse LL1Syntax definition failed: Syntax definition error: symbol: " + head + " has already been defined.");
                }
            }

            Set<List<SyntaxSymbol>> bodySet = getBodyParts(headBodyParts[1]);
            for (List<SyntaxSymbol> body : bodySet) {
                SyntaxProduction production = new LL1SyntaxProduction(head, body);
                syntaxProductionList.add(production);
            }
        }
        syntaxProductionList.forEach(System.out::println);

        return syntaxProductionList;
    }

    private Set<List<SyntaxSymbol>> getBodyParts(String body) {
        Set<List<SyntaxSymbol>> bodySymbolList = new HashSet<>();

        String[] bodyUnionParts = body.split(CharUtil.SYNTAX_UNION);
        for (String bodyUnionPart : bodyUnionParts) {
            List<SyntaxSymbol> symbols = new ArrayList<>();
            String[] concatParts = bodyUnionPart.split(CharUtil.SYNTAX_CONCAT);
            for (String concatPart : concatParts) {
                SyntaxSymbol symbol = syntaxSymbolMap.get(handleEscapeBack(concatPart));
                if (symbol == null) {
                    throw new IllegalStateException("Parse LL1Syntax definition failed: Syntax definition error, can not find symbol: " + concatPart);
                }

                symbols.add(symbol);
            }
            bodySymbolList.add(symbols);
        }

        return bodySymbolList;
    }

    private String handleEscape(String s) {
        StringBuilder sb = new StringBuilder();

        boolean needEscape = false;
        for (int i = 0; i < s.length(); i++) {
            char tmp = s.charAt(i);

            if (!needEscape && tmp == CharUtil.BACKSLASH) {
                needEscape = true;
                continue;
            }

            if (needEscape) {
                if (CharUtil.canSyntaxEscape(tmp)) {
                    tmp = CharUtil.escapeSyntax(tmp);
                } else {
                    throw new IllegalArgumentException("Parsing escape letter error, can not escape letter: " + tmp);
                }
                needEscape = false;
            }

            sb.append(tmp);
        }

        return sb.toString();
    }

    private String handleEscapeBack(String s) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            char tmp = s.charAt(i);

            if (CharUtil.shouldSyntaxEscapeBack(tmp))
                tmp = CharUtil.escapeSyntaxBack(tmp);

            sb.append(tmp);
        }

        return sb.toString();
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
        return END_SYMBOL;
    }

    @Override
    public SyntaxSymbol getEpsilonSymbol() {
        return epsSymbol;
    }
}
