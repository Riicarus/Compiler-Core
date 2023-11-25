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

    private SyntaxSymbol epsSymbol;
    private final Map<String, SyntaxSymbol> syntaxSymbolMap = new HashMap<>();
    private final List<SyntaxProduction> syntaxProductionList = new ArrayList<>();

    private final Set<SyntaxSymbol> nullableSymbolSet = new HashSet<>();
    private final Map<SyntaxSymbol, Set<SyntaxSymbol>> firstSet = new HashMap<>();
    private final Map<SyntaxSymbol, Set<SyntaxSymbol>> followSet = new HashMap<>();

    @Override
    public void loadFrom(String path) {
        Set<SyntaxSymbol> terminalSymbolSet = new HashSet<>();
        Set<SyntaxSymbol> nonterminalSymbolSet = new HashSet<>();

        // 读取文法定义文件
        try (
                BufferedReader reader = new BufferedReader(new FileReader(path))
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

            while ((line = reader.readLine()) != null && !line.isEmpty() && !line.equals(CharUtil.SYNTAX_SEPARATOR)) {
                final LL1SyntaxSymbol symbol = new LL1SyntaxSymbol(handleEscapeBack(handleEscape(line)), false);
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

        // 校验格式
        if (terminalSymbolSet.isEmpty() || nonterminalSymbolSet.isEmpty() || syntaxStringList.isEmpty()) {
            throw new IllegalStateException("Load LL1Syntax definition failed: Syntax definition is not complete.");
        }

        terminalSymbolSet.forEach(s -> syntaxSymbolMap.put(s.getName(), s));
        nonterminalSymbolSet.forEach(s -> syntaxSymbolMap.put(s.getName(), s));

        parseProduction();

        // 计算属性
        computeNullableSet();
        System.out.println("NULLABLE: " + nullableSymbolSet);

        computeFirstSet();
        System.out.println("FIRST:");
        firstSet.forEach((k, v) -> System.out.println(k + ": " + v));

        computeFollowSet();
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

    private void parseProduction() {
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

            Set<List<SyntaxSymbol>> body = getBodyParts(headBodyParts[1]);
            SyntaxProduction production = new LL1SyntaxProduction(head, body);
            syntaxProductionList.add(production);
        }
        syntaxProductionList.forEach(System.out::println);
    }

    @Override
    public void load() {
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

    private void computeNullableSet() {
        boolean hasNew = true;
        while (hasNew) {
            hasNew = false;
            for (SyntaxProduction production : syntaxProductionList) {
                if (nullableSymbolSet.contains(production.getHead())) continue;

                for (List<SyntaxSymbol> betaList : production.getBody()) {
                    if (betaList.size() == 1 && betaList.get(0).isTerminal() && betaList.get(0).equals(epsSymbol)) {
                        // 如果是空终结符
                        hasNew = true;
                        nullableSymbolSet.add(production.getHead());
                        continue;
                    }

                    boolean hasTerminal = false;
                    for (SyntaxSymbol symbol : betaList) {
                        hasTerminal = symbol.isTerminal();
                        if (hasTerminal) break;
                    }

                    if (!hasTerminal) {
                        if (nullableSymbolSet.containsAll(betaList)) {
                            // 如果是非终结符, 并且所有的终结符都是可能为空的.
                            hasNew = true;
                            nullableSymbolSet.add(production.getHead());
                        }
                    }
                }
            }
        }
    }

    private void computeFirstSet() {
        for (SyntaxProduction production : syntaxProductionList) {
            firstSet.put(production.getHead(), new HashSet<>());
        }

        boolean hasNew = true;
        while (hasNew) {
            hasNew = false;

            for (SyntaxProduction production : syntaxProductionList) {
                SyntaxSymbol head = production.getHead();
                for (List<SyntaxSymbol> betaList : production.getBody()) {
                    // 停止的条件是遇到终结符或者不可能为空的非终结符.
                    for (SyntaxSymbol symbol : betaList) {
                        if (symbol.isTerminal()) {
                            if (!firstSet.get(head).contains(symbol)) {
                                hasNew = true;
                                firstSet.get(head).add(symbol);
                            }
                            break;
                        }

                        if (!firstSet.get(head).containsAll(firstSet.get(symbol))) {
                            hasNew = true;
                            firstSet.get(head).addAll(firstSet.get(symbol));
                        }
                        if (!nullableSymbolSet.contains(symbol)) break;
                    }
                }
            }
        }
    }

    private void computeFollowSet() {

    }

    private static <T, E> boolean isSetMapAllEmpty(Map<T, Set<E>> map) {
        for (Set<E> set : map.values()) {
            if (!set.isEmpty()) return false;
        }

        return true;
    }

    @Override
    public List<SyntaxProduction> getSyntaxProductionList() {
        return Collections.unmodifiableList(syntaxProductionList);
    }

    @Override
    public Set<SyntaxSymbol> getSyntaxSymbolSet() {
        return new HashSet<>(syntaxSymbolMap.values());
    }
}
