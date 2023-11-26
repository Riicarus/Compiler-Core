package io.github.riicarus.front.syntax;

import java.util.List;

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
    List<SyntaxSymbol> getBody();

}
