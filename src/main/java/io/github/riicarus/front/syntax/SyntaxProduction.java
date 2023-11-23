package io.github.riicarus.front.syntax;

import java.util.List;
import java.util.Set;

/**
 * 文法产生式
 *
 * @author Riicarus
 * @create 2023-11-23 16:05
 * @since 1.0.0
 */
public interface SyntaxProduction {

    /**
     * 获取产生式头.
     *
     * @return 产生式头符号
     */
    SyntaxSymbol getHead();

    /**
     * 获取产生式体.
     *
     * @return 产生式体中的符号集合
     */
    Set<List<SyntaxSymbol>> getBody();

    /**
     * 获取 FIRST 集
     *
     * @return FIRST 集
     */
    Set<Character> getFirstSet();

    /**
     * 获取 FOLLOW 集.
     *
     * @return FOLLOW 集
     */
    Set<Character> getFollowerSet();

    /**
     * 添加符号到 FIRST 集合.
     *
     * @param c 开始符号
     */
    void addFirst(char c);

    /**
     * 添加符号到 FOLLOW 集合.
     *
     * @param c 跟随符号
     */
    void addFollow(char c);

}
