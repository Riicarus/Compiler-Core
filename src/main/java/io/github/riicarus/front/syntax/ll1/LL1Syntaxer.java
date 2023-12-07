package io.github.riicarus.front.syntax.ll1;

import io.github.riicarus.common.data.Token;
import io.github.riicarus.front.lex.LexicalSymbol;
import io.github.riicarus.front.syntax.SyntaxDefiner;
import io.github.riicarus.front.syntax.SyntaxProduction;
import io.github.riicarus.front.syntax.SyntaxSymbol;
import io.github.riicarus.front.syntax.Syntaxer;

import java.util.*;

/**
 * LL1 文法分析器.
 *
 * @author Riicarus
 * @create 2023-11-22 21:27
 * @since 1.0.0
 */
public class LL1Syntaxer implements Syntaxer {

    private final SyntaxDefiner definition;
    private final Map<SyntaxSymbol, Map<String, Set<SyntaxProduction>>> analyzeMap;

    // 分析栈
    private final Stack<SyntaxSymbol> stack = new Stack<>();
    private SyntaxSymbol topSymbol;

    private int tokenIdx;
    private Token curToken;
    private final List<Token> tokens = new ArrayList<>();

    private final Set<LexicalSymbol> assistantLexSymbolSet = new HashSet<>();

    public LL1Syntaxer(SyntaxDefiner definition) {
        this.definition = definition;
        this.analyzeMap = definition.getAnalyzeMap();

        definition.load();
    }

    @Override
    public void parse(List<Token> tokenList, Set<LexicalSymbol> assistSet) {
        if (!reset(tokenList, assistSet))
            throw new IllegalStateException("LL1Syntax wrong: token list is null.");

        while (!stack.isEmpty() && !checkEnds()) {  // 如果分析栈不空, 并且没有遇到结束符
            System.out.println(stack);
            System.out.println(curToken);

            if (topSymbol.isTerminal())
                handleTerminal();
            else
                handleNonterminal();
        }

        System.out.println(stack);
    }

    @SuppressWarnings("all") // 抑制空 while 循环
    private void nextTokenWithAssitant() {
        while (nextToken() && assistantLexSymbolSet.contains(tokens.get(tokenIdx).getSymbol())) ;
    }

    private boolean nextToken() {
        if (tokenIdx < tokens.size()) {
            tokenIdx++;
            curToken = tokens.get(tokenIdx);
            return true;
        }

        return false;
    }

    private boolean reset(List<Token> tokenList, Set<LexicalSymbol> assistSet) {
        tokenIdx = -1;
        curToken = null;
        topSymbol = null;

        tokens.clear();
        tokens.addAll(tokenList);

        if (!nextToken()) return false;

        assistantLexSymbolSet.clear();
        assistantLexSymbolSet.addAll(assistSet);

        stack.clear();
        stack.push(definition.getEndSymbol());
        stack.push(definition.getStartSymbol());

        return true;
    }

    private boolean checkEnds() {
        topSymbol = stack.peek();

        if (topSymbol.equals(definition.getEndSymbol())) {
            if (tokenIdx >= tokens.size())  // 如果 tokens 已经遍历完, 报错
                throw new IllegalStateException("LL1Syntax wrong: syntax not complete, want: \"" + topSymbol + "\" but get null.");
            else {
                if (curToken.getSymbol().getName().equals(definition.getEndSymbol().getName())) {  // 如果 tokens 也是结束符号, 就直接消去
                    stack.pop();
                    return true;
                } else    // 否则报错
                    throw new IllegalStateException("LL1Syntax wrong: syntax complete, but get \"" + topSymbol + "\".");
            }
        }

        return false;
    }

    private void handleTerminal() {
        if (topSymbol.equals(definition.getEpsilonSymbol())) {    // 空串直接弹出
            stack.pop();
        } else if (topSymbol.getName().equals(curToken.getSymbol().getName())) { // 非空终结符弹出, 匹配下一个 token
            stack.pop();
            nextTokenWithAssitant();
        } else {    // 找不到对应的非终结符
            throw new IllegalStateException("LL1Syntax wrong: want : \"" + topSymbol.getName() + "\", get: \"" + curToken.getSymbol().getName() + "\"");
        }
    }

    private void handleNonterminal() {
        stack.pop();

        Map<String, Set<SyntaxProduction>> setMap = analyzeMap.get(topSymbol);
        if (setMap == null || setMap.isEmpty())
            throw new IllegalStateException("LL1Syntax wrong: analyze map can not find symbol: \"" + topSymbol);

        Set<SyntaxProduction> productionSet = setMap.get(curToken.getSymbol().getName());
        if (productionSet == null || productionSet.isEmpty())
            throw new IllegalStateException("LL1Syntax wrong: illegal token: " + curToken + " for current symbol: " + topSymbol);

        productionSet.forEach(
                // 注意, LL1 文法中的 Set 实际上只会有一个产生式, 这里只循环一次.
                p -> {
                    for (int j = p.getBody().size() - 1; j >= 0; j--) {
                        stack.push(p.getBody().get(j));
                    }
                });
    }
}
