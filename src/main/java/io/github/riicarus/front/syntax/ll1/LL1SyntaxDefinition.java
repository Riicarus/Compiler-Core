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
    private final Map<SyntaxSymbol, Set<SyntaxSymbol>> firstSetMap = new HashMap<>();
    private final Map<SyntaxSymbol, Set<SyntaxSymbol>> followSetMap = new HashMap<>();
    private final Map<SyntaxProduction, Set<SyntaxSymbol>> firstSSetMap = new HashMap<>();

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
        firstSetMap.forEach((k, v) -> System.out.println(k + ": " + v));

        computeFollowSet();
        System.out.println("FOLLOW:");
        followSetMap.forEach((k, v) -> System.out.println(k + ": " + v));

        computeFirstSSet();
        System.out.println("FIRST_S:");
        firstSSetMap.forEach((k, v) -> System.out.println(k + ": " + v));
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

            Set<List<SyntaxSymbol>> bodySet = getBodyParts(headBodyParts[1]);
            for (List<SyntaxSymbol> body : bodySet) {
                // 注意, 需要移除掉空终结符.
                body.remove(epsSymbol);
                SyntaxProduction production = new LL1SyntaxProduction(head, body);
                syntaxProductionList.add(production);
            }
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

                List<SyntaxSymbol> betaList = production.getBody();

                // 如果只有空终结符
                if (betaList.size() == 0) {
                    nullableSymbolSet.add(production.getHead());
                    hasNew = true;
                    continue;
                }

                // 先判断是否都为非终结符
                boolean hasTerminal = false;
                for (SyntaxSymbol beta : betaList) {
                    hasTerminal = beta.isTerminal();
                    if (hasTerminal) break;
                }
                if (!hasTerminal) {
                    if (nullableSymbolSet.containsAll(production.getBody())) {
                        // 如果所有的终结符都是可能为空的.
                        hasNew = true;
                        nullableSymbolSet.add(production.getHead());
                    }
                }
            }
        }
    }

    private void computeFirstSet() {
        for (SyntaxProduction production : syntaxProductionList) {
            firstSetMap.put(production.getHead(), new HashSet<>());
        }

        boolean hasNew = true;
        while (hasNew) {
            hasNew = false;

            for (SyntaxProduction production : syntaxProductionList) {
                SyntaxSymbol head = production.getHead();
                for (SyntaxSymbol beta : production.getBody()) {
                    // 停止的条件是遇到终结符或者不可能为空的非终结符.
                    if (beta.isTerminal()) {
                        if (!firstSetMap.get(head).contains(beta)) {
                            hasNew = true;
                            firstSetMap.get(head).add(beta);
                        }
                        break;
                    }

                    // 如果是非终结符
                    if (!firstSetMap.get(head).containsAll(firstSetMap.get(beta))) {
                        hasNew = true;
                        firstSetMap.get(head).addAll(firstSetMap.get(beta));
                    }
                    if (!nullableSymbolSet.contains(beta)) break;
                }
            }
        }
    }

    private void computeFollowSet() {
        for (SyntaxProduction production : syntaxProductionList) {
            followSetMap.put(production.getHead(), new HashSet<>());
        }

        boolean hasNew = true;
        while (hasNew) {
            hasNew = false;

            for (SyntaxProduction production : syntaxProductionList) {
                Set<SyntaxSymbol> tmp = followSetMap.get(production.getHead());

                List<SyntaxSymbol> betaList = production.getBody();
                for (int i = betaList.size() - 1; i >= 0; i--) {
                    // 逆序遍历
                    SyntaxSymbol beta = betaList.get(i);

                    // 终结符
                    if (beta.isTerminal()) {
                        tmp = new HashSet<>();
                        tmp.add(beta);
                        continue;
                    }

                    // 非终结符
                    if (!followSetMap.get(beta).containsAll(tmp)) {
                        followSetMap.get(beta).addAll(tmp);
                        hasNew = true;
                    }
                    if (!nullableSymbolSet.contains(beta)) {
                        tmp = firstSetMap.get(beta);
                    } else {
                        tmp.addAll(firstSetMap.get(beta));
                    }
                }
            }
        }
    }

    private void computeFirstSSet() {
        for (SyntaxProduction production : syntaxProductionList) {
            firstSSetMap.put(production, new HashSet<>());

            computeOneFirstSSet(production);
        }
    }

    private void computeOneFirstSSet(SyntaxProduction production) {
        SyntaxSymbol head = production.getHead();
        for (SyntaxSymbol beta : production.getBody()) {
            // 如果是终结符
            if (beta.isTerminal()) {
                firstSSetMap.get(production).add(beta);
                return;
            }

            // 如果是非终结符
            firstSSetMap.get(production).addAll(firstSetMap.get(beta));
            if (!nullableSymbolSet.contains(beta)) return;
        }

        firstSSetMap.get(production).addAll(followSetMap.get(head));
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
