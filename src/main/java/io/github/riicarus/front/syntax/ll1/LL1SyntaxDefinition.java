package io.github.riicarus.front.syntax.ll1;

import io.github.riicarus.common.util.CharUtil;
import io.github.riicarus.front.syntax.SyntaxDefinition;
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
public class LL1SyntaxDefinition implements SyntaxDefinition {

    private final List<String> syntaxStringList = new ArrayList<>();
    private final List<SyntaxProduction> syntaxList = new ArrayList<>();
    private final Map<String, SyntaxSymbol> syntaxSymbolMap = new HashMap<>();

    @Override
    public void loadFrom(String path) {
        try (
                BufferedReader reader = new BufferedReader(new FileReader(path))
        ) {
            Set<SyntaxSymbol> terminalSymbolSet = new HashSet<>();
            Set<SyntaxSymbol> nonterminalSymbolSet = new HashSet<>();

            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty() && !line.equals(CharUtil.SYNTAX_SEPARATOR)) {
                terminalSymbolSet.add(new LL1SyntaxSymbol(line, true));
            }

            while ((line = reader.readLine()) != null && !line.isEmpty() && !line.equals(CharUtil.SYNTAX_SEPARATOR)) {
                nonterminalSymbolSet.add(new LL1SyntaxSymbol(line, false));
            }

            while ((line = reader.readLine()) != null && !line.isEmpty() && !line.equals(CharUtil.SYNTAX_SEPARATOR)) {
                syntaxStringList.add(line);
            }

            if (terminalSymbolSet.isEmpty() || nonterminalSymbolSet.isEmpty() || syntaxStringList.isEmpty()) {
                throw new IllegalStateException("Load LL1Syntax definition failed: Syntax definition is not complete.");
            }

            terminalSymbolSet.forEach(s -> syntaxSymbolMap.put(s.getName(), s));
            nonterminalSymbolSet.forEach(s -> syntaxSymbolMap.put(s.getName(), s));

            for (String expr : syntaxStringList) {
                String[] headBodyParts = expr.split(CharUtil.SYNTAX_ARROW);
                if (headBodyParts.length != 2) {
                    throw new IllegalStateException("Parse LL1Syntax definition failed: Syntax definition is wrong, need: \"head -> body\", get: " + expr);
                }

                SyntaxSymbol head = syntaxSymbolMap.get(headBodyParts[0]);
                if (head == null) {
                    throw new IllegalStateException("Parse LL1Syntax definition failed: Syntax definition is wrong, can not find symbol: " + headBodyParts[0]);
                }

                Set<List<SyntaxSymbol>> body = getBodyParts(headBodyParts[1]);
                SyntaxProduction production = new LL1SyntaxProduction(head, body);
                syntaxList.add(production);
            }

            syntaxList.forEach(System.out::println);
        } catch (IOException e) {
            throw new IllegalStateException("Load LL1Syntax definition failed.", e);
        }
    }

    private Set<List<SyntaxSymbol>> getBodyParts(String body) {
        Set<List<SyntaxSymbol>> bodySymbolList = new HashSet<>();

        String[] bodyUnionParts = body.split(CharUtil.SYNTAX_UNION);
        for (String bodyUnionPart : bodyUnionParts) {
            List<SyntaxSymbol> symbols = new ArrayList<>();
            String[] concatParts = bodyUnionPart.split(CharUtil.SYNTAX_CONCAT);
            for (String concatPart : concatParts) {
                SyntaxSymbol symbol = syntaxSymbolMap.get(concatPart);
                if (symbol == null) {
                    throw new IllegalStateException("Parse LL1Syntax definition failed: Syntax definition is wrong, can not find symbol: " + concatPart);
                }

                symbols.add(symbol);
            }
            bodySymbolList.add(symbols);
        }

        return bodySymbolList;
    }

    @Override
    public void load() {
    }

    @Override
    public List<SyntaxProduction> getSyntaxList() {
        return Collections.unmodifiableList(syntaxList);
    }

    @Override
    public Set<SyntaxSymbol> getSyntaxSymbolSet() {
        return new HashSet<>(syntaxSymbolMap.values());
    }
}
