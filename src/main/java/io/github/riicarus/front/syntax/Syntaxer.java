package io.github.riicarus.front.syntax;

import io.github.riicarus.common.data.AstConstructStrategy;
import io.github.riicarus.common.data.SyntaxParseResult;
import io.github.riicarus.common.data.Token;
import io.github.riicarus.front.lex.LexicalSymbol;

import java.util.List;
import java.util.Set;

/**
 * 文法分析器.
 *
 * @author Riicarus
 * @create 2023-11-22 22:09
 * @since 1.0.0
 */
public interface Syntaxer {


    /**
     * 根据文法分析输入的 token 串, 返回分析后生成的结果, 包括:<br/>
     * <li>AST 树</li>
     * <li>符号表</li>
     * <li>过程名表</li>
     *
     * @param tokenList 需要解析的词法符号列表
     * @param assistantLexSymbolSet 辅助词法符号集合
     * @param strategy  AST 构建策略工厂
     * @return 分析结果
     */
    SyntaxParseResult parse(List<Token> tokenList, Set<LexicalSymbol> assistantLexSymbolSet, AstConstructStrategy strategy);
}
