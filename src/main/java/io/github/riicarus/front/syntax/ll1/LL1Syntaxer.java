package io.github.riicarus.front.syntax.ll1;

import io.github.riicarus.common.data.AbstractAST;
import io.github.riicarus.common.data.AstConstructStrategy;
import io.github.riicarus.common.data.SyntaxParseResult;
import io.github.riicarus.common.data.Token;
import io.github.riicarus.front.lex.LexicalSymbol;
import io.github.riicarus.front.syntax.*;

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
    private final Map<SyntaxSymbol<?>, Map<String, Set<SyntaxProduction>>> analyzeMap = new HashMap<>();

    // 分析栈
    private final Stack<SyntaxSymbol<?>> syntaxStack = new Stack<>();
    private SyntaxSymbol<?> topSymbol;

    private int tokenIdx;
    private Token curToken;
    private final List<Token> tokens = new ArrayList<>();

    private final Set<LexicalSymbol> assistantLexSymbolSet = new HashSet<>();

    private AstConstructStrategy astConstructStrategy;
    private final Stack<AbstractAST<?>> astStack = new Stack<>();
    private final Stack<SyntaxSymbol<?>> opSymbolStack = new Stack<>();

    public LL1Syntaxer(SyntaxDefiner definition) {
        this.definition = definition;

        definition.load();
        this.analyzeMap.clear();
        this.analyzeMap.putAll(definition.getAnalyzeMap());
    }

    @Override
    public SyntaxParseResult parse(List<Token> tokenList, Set<LexicalSymbol> assistSet, AstConstructStrategy strategy) {
        if (!reset(tokenList, assistSet, strategy))
            throw new IllegalStateException("LL1Syntax wrong: token list is null.");

        while (!syntaxStack.isEmpty() && !checkEnds()) {  // 如果分析栈不空, 并且没有遇到结束符
            if (topSymbol.isTerminal())
                handleTerminal();
            else
                handleNonterminal();
        }

        handlePostProcessAstConstruction();

        if (astStack.size() != 1)
            throw new IllegalStateException("LL1Syntax wrong: not expected: ast stack's remaining size is not 1.");

        return new SyntaxParseResult(astStack.pop());
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

    private boolean reset(List<Token> tokenList, Set<LexicalSymbol> assistSet, AstConstructStrategy strategy) {
        tokenIdx = -1;
        curToken = null;
        topSymbol = null;

        tokens.clear();
        tokens.addAll(tokenList);

        if (!nextToken()) return false;

        assistantLexSymbolSet.clear();
        assistantLexSymbolSet.addAll(assistSet);

        astConstructStrategy = strategy;

        syntaxStack.clear();
        syntaxStack.push(definition.getEndSymbol());
        syntaxStack.push(definition.getStartSymbol());

        astStack.clear();
        opSymbolStack.clear();

        return true;
    }

    private boolean checkEnds() {
        topSymbol = syntaxStack.peek();

        if (topSymbol.equals(definition.getEndSymbol())) {
            if (tokenIdx >= tokens.size())  // 如果 tokens 已经遍历完, 报错
                throw new IllegalStateException("LL1Syntax wrong: syntax not complete, want: \"" + topSymbol + "\" but get null.");
            else {
                if (curToken.getSymbol().getName().equals(definition.getEndSymbol().getName())) {  // 如果 tokens 也是结束符号, 就直接消去
                    syntaxStack.pop();
                    return true;
                } else    // 否则报错
                    throw new IllegalStateException("LL1Syntax wrong: syntax complete, but get \"" + topSymbol + "\".");
            }
        }

        return false;
    }

    private void handleTerminal() {
        if (topSymbol.equals(definition.getEpsilonSymbol())) {    // 空串直接弹出
            syntaxStack.pop();
        } else if (topSymbol.getName().equals(curToken.getSymbol().getName())) { // 非空终结符弹出, 匹配下一个 token
            syntaxStack.pop();
            handleInProcessAstConstruction(curToken);
            nextTokenWithAssitant();
        } else {    // 找不到对应的非终结符
            throw new IllegalStateException("LL1Syntax wrong: want : \"" + topSymbol.getName() + "\", get: \"" + curToken.getSymbol().getName() + "\"");
        }
    }

    private void handleNonterminal() {
        syntaxStack.pop();
        // handleAstGeneration(curToken);

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
                        syntaxStack.push(p.getBody().get(j));
                    }
                });
    }

    private void handleInProcessAstConstruction(Token token) {
        if (topSymbol.equals(definition.getEndSymbol())) return;

        if (topSymbol.getType().equals(SyntaxSymbolType.OP)) {
            if (!opSymbolStack.isEmpty()) {
                SyntaxSymbol<?> topOp;
                int pTop = definition.getOpPrecedenceMap().get(topSymbol);
                int pTopOp;

                // 如果符号栈栈顶的符号的优先级比当前分析符号的优先级高, 先处理符号栈栈顶的符号
                for (
                        topOp = opSymbolStack.peek(), pTopOp = definition.getOpPrecedenceMap().get(topOp);
                        pTopOp >= pTop;
                        topOp = opSymbolStack.peek(), pTopOp = definition.getOpPrecedenceMap().get(topOp)
                ) {
                    opSymbolStack.pop();
                    AbstractAST<?> ast = topOp.constructAST(astConstructStrategy, topOp.getName(), astStack, opSymbolStack);
                    if (ast != null) astStack.push(ast);

                    if (opSymbolStack.isEmpty()) break;
                }
            }

            opSymbolStack.push(topSymbol);
        } else {
            AbstractAST<?> abstractAST = topSymbol.constructAST(astConstructStrategy, token.getLexeme(), astStack, opSymbolStack);
            if (abstractAST == null) return;
            astStack.push(abstractAST);
        }
    }

    /**
     * 此时所有的 SyntaxSymbol 都被检查了一一遍, 要么被压入符号栈中, 要么被构建成为了 AST.
     */
    private void handlePostProcessAstConstruction() {
        while (!opSymbolStack.isEmpty()) {
            SyntaxSymbol<?> op = opSymbolStack.pop();

            AbstractAST<?> ast = op.constructAST(astConstructStrategy, op.getName(), astStack, opSymbolStack);
            if (ast == null) continue;
            astStack.push(ast);
        }
    }
}
