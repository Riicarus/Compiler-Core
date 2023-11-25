package io.github.riicarus.front.syntax.ll1;

import io.github.riicarus.common.util.CharUtil;
import io.github.riicarus.front.syntax.SyntaxDefinition;
import io.github.riicarus.front.syntax.SyntaxProduction;
import io.github.riicarus.front.syntax.SyntaxSymbol;
import io.github.riicarus.front.syntax.SyntaxSymbolType;

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
    private final List<SyntaxProduction> syntaxProductionList = new ArrayList<>();
    private final Map<String, SyntaxSymbol> syntaxSymbolMap = new HashMap<>();
    private final Set<SyntaxSymbol> nullableSymbolSet = new HashSet<>();
    private SyntaxSymbol epsSymbol;

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

        // 转换为文法产生式
        for (String expr : syntaxStringList) {
            String[] headBodyParts = expr.split(CharUtil.SYNTAX_DEFINE);
            if (headBodyParts.length != 2) {
                throw new IllegalStateException("Parse LL1Syntax definition failed: Syntax definition is wrong, need: \"head -> body\", get: " + expr);
            }

            SyntaxSymbol head = syntaxSymbolMap.get(handleEscapeBack(headBodyParts[0]));
            if (head == null) {
                throw new IllegalStateException("Parse LL1Syntax definition failed: Syntax definition is wrong, can not find symbol: " + headBodyParts[0]);
            }

            Set<List<SyntaxSymbol>> body = getBodyParts(headBodyParts[1]);
            SyntaxProduction production = new LL1SyntaxProduction(head, body);
            syntaxProductionList.add(production);
        }
        syntaxProductionList.forEach(System.out::println);

        // 计算属性
        computeNullableSet();
        System.out.println(nullableSymbolSet);
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
        Set<SyntaxSymbol> newNullable = new HashSet<>();

        do {
            for (SyntaxProduction production : syntaxProductionList) {
                // 移出上一轮新找到的 nullable 文法符号.
                if (nullableSymbolSet.contains(production.getHead())) {
                    newNullable.remove(production.getHead());
                    continue;
                }

                for (List<SyntaxSymbol> beta : production.getBody()) {
                    if (beta.size() == 1 && SyntaxSymbolType.TERMINAL.equals(beta.get(0).getType()) && beta.get(0).equals(epsSymbol)) {
                        // 如果是空终结符
                        newNullable.add(production.getHead());
                        nullableSymbolSet.add(production.getHead());
                        continue;
                    }

                    boolean hasTerminal = false;
                    for (SyntaxSymbol symbol : beta) {
                        hasTerminal = SyntaxSymbolType.TERMINAL.equals(symbol.getType());
                        if (hasTerminal) break;
                    }

                    if (!hasTerminal) {
                        if (nullableSymbolSet.containsAll(beta)) {
                            // 如果是非终结符, 并且所有的终结符都是可能为空的.
                            newNullable.add(production.getHead());
                            nullableSymbolSet.add(production.getHead());
                        }
                    }
                }
            }
        } while (!newNullable.isEmpty());
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
