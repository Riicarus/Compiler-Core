package io.github.riicarus.front.syntax.ll1;

import io.github.riicarus.common.data.Token;
import io.github.riicarus.front.lex.LexicalSymbol;
import io.github.riicarus.front.syntax.SyntaxDefiner;
import io.github.riicarus.front.syntax.SyntaxProduction;
import io.github.riicarus.front.syntax.SyntaxSymbol;
import io.github.riicarus.front.syntax.Syntaxer;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * LL1 文法分析器.
 *
 * @author Riicarus
 * @create 2023-11-22 21:27
 * @since 1.0.0
 */
public class LL1Syntaxer implements Syntaxer {

    private final SyntaxDefiner definition;

    // 分析栈
    private final Stack<SyntaxSymbol> stack = new Stack<>();

    private int tokenIdx;
    private List<Token> tokens;

    public LL1Syntaxer(SyntaxDefiner definition) {
        this.definition = definition;

        definition.load();
    }

    @Override
    public void parse(List<Token> tokenList, Set<LexicalSymbol> assistantLexSymbolSet) {
        tokens = tokenList;

        final Map<SyntaxSymbol, Map<String, Set<SyntaxProduction>>> analyzeMap = definition.getAnalyzeMap();

        stack.clear();
        stack.push(definition.getEndSymbol());
        stack.push(definition.getStartSymbol());

        tokenIdx = 0;
        while (!stack.isEmpty()) {
            System.out.println(stack);

            SyntaxSymbol top = stack.peek();

            if (top.equals(definition.getEndSymbol()))  break;
            else if (tokenIdx == tokens.size()) {
                throw new IllegalStateException("LL1Syntax wrong: syntax not complete, want: \"" + top + "\" but get null.");
            }

            Token token = tokens.get(tokenIdx);

            System.out.println(token.getSymbol().getName());

            if (top.isTerminal()) {
                if (top.equals(definition.getEpsilonSymbol())) {    // 空串直接弹出
                    stack.pop();
                } else if (top.getName().equals(token.getSymbol().getName())) { // 非空终结符弹出, 匹配下一个 token
                    stack.pop();
                    tokenIdx++;
                    handleAssistantLexSymbols(assistantLexSymbolSet);
                } else {    // 找不到对应的非终结符
                    throw new IllegalStateException("LL1Syntax wrong: want : \"" + top.getName() + "\", get: \"" + token.getSymbol().getName() + "\"");
                }
            } else {
                stack.pop();

                final Map<String, Set<SyntaxProduction>> setMap = analyzeMap.get(top);
                if (setMap == null || setMap.isEmpty())
                    throw new IllegalStateException("LL1Syntax wrong: analyze map can not find symbol: \"" + top);

                final Set<SyntaxProduction> productionSet = setMap.get(token.getSymbol().getName());
                if (productionSet == null || productionSet.isEmpty())
                    throw new IllegalStateException("LL1Syntax wrong: illegal token: " + token + " for current symbol: " + top);

                productionSet.forEach(
                        // 注意, LL1 文法中的 Set 实际上只会有一个产生式, 这里只循环一次.
                        p -> {
                            for (int j = p.getBody().size() - 1; j >= 0; j--) {
                                stack.push(p.getBody().get(j));
                            }
                        });
            }
        }
    }

    private void handleAssistantLexSymbols(Set<LexicalSymbol> assistantLexSymbolSet) {
        while (tokenIdx < tokens.size() && assistantLexSymbolSet.contains(tokens.get(tokenIdx).getSymbol())) tokenIdx++;
    }
}
