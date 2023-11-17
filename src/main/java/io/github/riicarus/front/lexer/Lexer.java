package io.github.riicarus.front.lexer;

import io.github.riicarus.common.data.LexicalSymbol;
import io.github.riicarus.common.data.Token;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 词法解析器.
 * <p>根据词法状态转换图解析出对应的词法符号, 放入符号表中.</p>
 *
 * @author Riicarus
 * @create 2023-11-7 23:28
 * @since 1.0.0
 */
public class Lexer {

    // 输入缓冲区
    private char[] buffer;

    // 当前分析到的位置
    private int cur;

    // 当前分析位置对应的行数
    private int line;

    // 当前分析位置在当前行中的位置
    private int lineIdx;

    /**
     * 分析输入的串, 返回对应的词法符号列表.
     *
     * @param input 输入串
     * @return 词法符号列表
     */
    public List<Token> analyze(char[] input) {
        List<Token> tokenList = new LinkedList<>();

        buffer = input;
        cur = 0;
        line = 1;
        lineIdx = 0;

        while (cur < buffer.length) {
            final Token token = analyzeOne();

            token.setLine(line);

            if (token.getSymbol().equals(LexicalSymbol.EOL)) {
                line++;
                lineIdx = 0;
            }

            tokenList.add(token);
        }

        tokenList.add(new Token(LexicalSymbol.EOF, "EOF", line));

        return tokenList;
    }

    /**
     * 向前分析一个串, 使用最长匹配原则.
     *
     * @return Token
     */
    private Token analyzeOne() {
        char[] s = Arrays.copyOfRange(buffer, cur, buffer.length);

        final Token token = LexicalSymbol.validateLongest(String.valueOf(s));

        if (token == null) throw new IllegalStateException("Lexer: Wrong syntax \"" + buffer[cur] + "\", line: " + line + "idx: " + lineIdx);

        cur += token.getLen();
        lineIdx += token.getLen();

        return token;
    }

}
