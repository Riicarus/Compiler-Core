package io.github.riicarus.front.syntax;

import io.github.riicarus.common.data.AstConstructStrategy;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 文法定义.
 *
 * @author Riicarus
 * @create 2023-11-23 15:09
 * @since 1.0.0
 */
public interface SyntaxDefiner {

    /**
     * 加载定义的文法.
     */
    void load();

    /**
     * 获取文法定义列表.
     *
     * @return 文法定义列表
     */
    List<SyntaxProduction> getSyntaxProductionList();

    /**
     * 获取文法符号集合.
     *
     * @return 文法符号集合
     */
    Set<SyntaxSymbol<?>> getSyntaxSymbolSet();

    /**
     * 获取分析表.
     *
     * @return 文法分析表
     */
    Map<SyntaxSymbol<?>, Map<String, Set<SyntaxProduction>>> getAnalyzeMap();

    /**
     * 获取文法的开始符号.
     *
     * @return 文法的开始符号.
     */
    SyntaxSymbol<?> getStartSymbol();

    /**
     * 获取文法的结束符号.
     *
     * @return 文法的结束符号
     */
    SyntaxSymbol<?> getEndSymbol();

    /**
     * 获取文法的空串符号.
     *
     * @return 文法的空串符号
     */
    SyntaxSymbol<?> getEpsilonSymbol();

    /**
     * 获取文法符号中的优先级顺序, 仅针对 OP 类型的符号.
     *
     * @return 文法符号的优先级顺序表
     */
    Map<SyntaxSymbol<?>, Integer> getOpPrecedenceMap();

    /**
     * 获取文法符号对应的 AST 构建策略工厂.
     *
     * @return AST 构建策略工厂
     */
    AstConstructStrategy getAstConstructStrategy();
}
