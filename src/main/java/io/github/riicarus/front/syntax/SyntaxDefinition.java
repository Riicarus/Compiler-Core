package io.github.riicarus.front.syntax;

import java.util.List;
import java.util.Set;

/**
 * 文法定义.
 *
 * @author Riicarus
 * @create 2023-11-23 15:09
 * @since 1.0.0
 */
public interface SyntaxDefinition {

    /**
     * 加载定义的文法.
     *
     * @param path 文法定义文件的路径
     */
    void loadFrom(String path);

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
    Set<SyntaxSymbol> getSyntaxSymbolSet();

}
