package io.github.riicarus.front.lex;

import io.github.riicarus.common.data.Token;

import java.util.List;
import java.util.Set;

/**
 * 词法解析器接口.
 * <p>根据词法状态转换图解析出对应的词法符号, 放入符号表中.</p>
 *
 * @author Riicarus
 * @create 2023-11-22 21:54
 * @since 1.0.0
 */
public interface Lexer {

    /**
     * 分析输入的串, 返回对应的词法符号列表.
     *
     * @param input 输入串
     * @return 词法符号列表
     */
    List<Token> parse(char[] input);

    /**
     * 获取辅助词法符号集合
     *
     * @return 辅助词法符号集合
     */
    Set<LexicalSymbol> getAssistantLexSymbolSet();

}
