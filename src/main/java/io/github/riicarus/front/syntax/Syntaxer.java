package io.github.riicarus.front.syntax;

import io.github.riicarus.common.data.Token;

import java.util.List;

/**
 * 文法分析器.
 *
 * @author Riicarus
 * @create 2023-11-22 22:09
 * @since 1.0.0
 */
public interface Syntaxer {

    /**
     * 对 tokenList 进行文法解析, 返回解析的结果.
     *
     * @param tokenList 需要解析的词法符号列表
     * @param dir 定义词法的文件夹路径
     * @param name 词法文件的名称
     * @param srcSuffix 词法文件的后缀
     * @param dstSuffix 解析后生成文件的后缀
     */
    void parse(List<Token> tokenList, String dir, String name, String srcSuffix, String dstSuffix);

}
