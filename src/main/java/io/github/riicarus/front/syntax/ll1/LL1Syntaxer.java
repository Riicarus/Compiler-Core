package io.github.riicarus.front.syntax.ll1;

import io.github.riicarus.common.data.Token;
import io.github.riicarus.front.syntax.SyntaxDefinition;
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

    private final SyntaxDefinition definition = new LL1SyntaxDefinition();
    private List<Token> tokens;

    @Override
    public void parse(List<Token> tokenList, String dir, String name, String srcSuffix, String syntaxSuffix) {
        tokens = tokenList;
        definition.loadFrom(dir + "/" + name + "." + srcSuffix);
        final Map<SyntaxSymbol, Map<String, Set<SyntaxProduction>>> analyzeMap = definition.getAnalyzeMap();

        int i = 0;
        Stack<SyntaxSymbol> stack = new Stack<>();
        stack.push(definition.getStartSymbol());

        while (!stack.isEmpty()) {
            SyntaxSymbol top = stack.peek();
            if (top.isTerminal()) {
                if (top.getName().equals(tokenList.get(i).getSymbol().getName())) {
                    stack.pop();
                    i++;
                } else {
                    throw new IllegalStateException("LL1Syntax parse failed: want : \"" + top.getName() + "\", get: \"" + tokenList.get(i).getSymbol().getName() + "\"");
                }
            } else {
                stack.pop();
                analyzeMap.get(top).get(tokenList.get(i).getSymbol().getName()).forEach(
                        // 注意, LL1 文法中的 Set 实际上只会有一个产生式, 这里只循环一次.
                        p -> {
                            for (int j = p.getBody().size() - 1; j >= 0; j--) {
                                stack.push(p.getBody().get(j));
                            }
                        });
            }
        }
    }
}
