package io.github.riicarus.front.syntax.ll1;

import io.github.riicarus.front.syntax.SyntaxDefiner;
import io.github.riicarus.front.syntax.SyntaxProduction;
import io.github.riicarus.front.syntax.SyntaxSymbol;

import java.util.*;

/**
 * LL1 文法定义抽象类.
 *
 * @author Riicarus
 * @create 2023-11-27 21:16
 * @since 1.0.0
 */
public abstract class LL1SyntaxDefiner implements SyntaxDefiner {

    private List<SyntaxProduction> syntaxProductionList;

    private final Set<SyntaxSymbol<?>> nullableSymbolSet = new HashSet<>();
    private final Map<SyntaxSymbol<?>, Set<SyntaxSymbol<?>>> firstSetMap = new HashMap<>();
    private final Map<SyntaxSymbol<?>, Set<SyntaxSymbol<?>>> followSetMap = new HashMap<>();
    private final Map<SyntaxProduction, Set<SyntaxSymbol<?>>> selectSetMap = new HashMap<>();
    private final Map<SyntaxSymbol<?>, Map<String, Set<SyntaxProduction>>> LL1AnalyzeMap = new HashMap<>();

    @Override
    public final void load() {
        syntaxProductionList = new ArrayList<>(loadSyntaxProductions());

        computeNullableSet();
        computeFirstSetMap();
        computeFollowSetMap();
        computeSelectSetMap();
        computeLL1AnalyzeMap();
    }

    protected abstract List<SyntaxProduction> loadSyntaxProductions();

    private void computeNullableSet() {
        boolean hasNew = true;
        while (hasNew) {
            hasNew = false;
            for (SyntaxProduction production : syntaxProductionList) {
                if (nullableSymbolSet.contains(production.getHead())) continue;

                List<SyntaxSymbol<?>> betaList = production.getBody();

                // 如果只有空终结符
                if (betaList.size() == 1 && betaList.get(0).equals(getEpsilonSymbol())) {
                    nullableSymbolSet.add(production.getHead());
                    hasNew = true;
                    continue;
                }

                // 先判断是否都为非终结符
                boolean hasTerminal = false;
                for (SyntaxSymbol<?> beta : betaList) {
                    hasTerminal = beta.isTerminal();
                    if (hasTerminal) break;
                }
                if (!hasTerminal) {
                    // 如果所有的终结符都是可能为空的.
                    if (nullableSymbolSet.containsAll(production.getBody())) {
                        hasNew = true;
                        nullableSymbolSet.add(production.getHead());
                    }
                }
            }
        }

        System.out.println("NULLABLE:");
        nullableSymbolSet.forEach(System.out::println);
    }

    private void computeFirstSetMap() {
        for (SyntaxProduction production : syntaxProductionList) {
            firstSetMap.put(production.getHead(), new HashSet<>());
        }

        boolean hasNew = true;
        while (hasNew) {
            hasNew = false;

            for (SyntaxProduction production : syntaxProductionList) {
                SyntaxSymbol<?> head = production.getHead();

                for (SyntaxSymbol<?> beta : production.getBody()) {
                    // 如果终结符, 添加并停止遍历
                    if (beta.isTerminal()) {
                        hasNew |= firstSetMap.get(head).add(beta);
                        break;
                    }

                    // 如果是非终结符, 将 FIRST(beta) 添加到 FIRST(head) 中
                    hasNew |= firstSetMap.get(head).addAll(firstSetMap.get(beta));

                    // 如果是不可能为空的非终结符, 停止遍历
                    if (!nullableSymbolSet.contains(beta)) break;
                }

                // 如果右部所有的符号都是可能推出空串的非终结符
                if (nullableSymbolSet.containsAll(production.getBody()))
                    hasNew |= firstSetMap.get(head).add(getEpsilonSymbol());
            }
        }

        System.out.println("FIRST:");
        firstSetMap.forEach((k, v) -> System.out.println(k + ": " + v));
    }

    private void computeFollowSetMap() {
        for (SyntaxProduction production : syntaxProductionList) {
            followSetMap.put(production.getHead(), new HashSet<>());
        }

        followSetMap.get(getStartSymbol()).add(getEndSymbol());

        boolean hasNew = true;
        while (hasNew) {
            hasNew = false;

            for (SyntaxProduction production : syntaxProductionList) {
                Set<SyntaxSymbol<?>> tmp = new HashSet<>(followSetMap.get(production.getHead()));

                List<SyntaxSymbol<?>> betaList = production.getBody();
                // 逆序遍历
                for (int i = betaList.size() - 1; i >= 0; i--) {
                    SyntaxSymbol<?> beta = betaList.get(i);

                    // 终结符
                    if (beta.isTerminal()) {
                        tmp.clear();
                        tmp.add(beta);
                        continue;
                    }

                    // 非终结符
                    hasNew = followSetMap.get(beta).addAll(tmp);

                    if (nullableSymbolSet.contains(beta)) {
                        tmp.addAll(firstSetMap.get(beta));
                        tmp.remove(getEpsilonSymbol());
                    } else {
                        tmp.clear();
                        tmp.addAll(firstSetMap.get(beta));
                    }
                }
            }
        }

        System.out.println("FOLLOW:");
        followSetMap.forEach((k, v) -> System.out.println(k + ": " + v));
    }

    private void computeSelectSetMap() {
        for (SyntaxProduction production : syntaxProductionList) {
            selectSetMap.put(production, new HashSet<>());

            computeSelectSet(production);
        }

        System.out.println("SELECT:");
        selectSetMap.forEach((k, v) -> System.out.println(k + ": " + v));
    }

    private void computeSelectSet(SyntaxProduction production) {
        if (production.getBody().isEmpty()) return;

        SyntaxSymbol<?> head = production.getHead();
        for (SyntaxSymbol<?> beta : production.getBody()) {
            if (beta.equals(getEpsilonSymbol())) {   // 如果是空终结符
                break;
            } else if (beta.isTerminal()) { // 如果是非空终结符
                selectSetMap.get(production).add(beta);
                return;
            }

            // 如果是非终结符
            selectSetMap.get(production).addAll(firstSetMap.get(beta));
            if (!nullableSymbolSet.contains(beta)) return;
        }

        selectSetMap.get(production).addAll(followSetMap.get(head));
    }

    private void computeLL1AnalyzeMap() {
        for (Map.Entry<SyntaxProduction, Set<SyntaxSymbol<?>>> entry : selectSetMap.entrySet()) {
            SyntaxProduction production = entry.getKey();

            SyntaxSymbol<?> head = production.getHead();
            for (SyntaxSymbol<?> symbol : entry.getValue()) {
                LL1AnalyzeMap.putIfAbsent(head, new HashMap<>());
                LL1AnalyzeMap.get(head).putIfAbsent(symbol.getName(), new HashSet<>());

                LL1AnalyzeMap.get(head).get(symbol.getName()).add(production);
            }
        }

        System.out.println("LL1AnalyzeMap:");
        LL1AnalyzeMap.forEach((t, setMap) ->
                setMap.forEach((nt, pSet) -> {
                            System.out.println(t.getName() + "--[" + nt + "]-->");
                            pSet.forEach(p -> System.out.println("\t" + p));
                        }
                )
        );

        for (Map.Entry<SyntaxSymbol<?>, Map<String, Set<SyntaxProduction>>> entry : LL1AnalyzeMap.entrySet()) {
            for (Map.Entry<String, Set<SyntaxProduction>> innerEntry : entry.getValue().entrySet()) {
                if (innerEntry.getValue().size() > 1) {
                    throw new IllegalStateException("LL1Syntax not support: " +
                            "there are more than 1 elements of transition map, " +
                            "maybe you should eliminate left recursions or extract left common factors.");
                }
            }
        }
    }

    @Override
    public List<SyntaxProduction> getSyntaxProductionList() {
        return Collections.unmodifiableList(syntaxProductionList);
    }

    @Override
    public Map<SyntaxSymbol<?>, Map<String, Set<SyntaxProduction>>> getAnalyzeMap() {
        return new HashMap<>(LL1AnalyzeMap);
    }
}
