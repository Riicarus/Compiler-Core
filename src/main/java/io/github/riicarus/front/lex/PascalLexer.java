package io.github.riicarus.front.lex;

import io.github.riicarus.common.data.Token;
import io.github.riicarus.common.util.CharUtil;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 词法解析器.
 * <p>根据词法状态转换图解析出对应的词法符号, 放入符号表中.</p>
 *
 * @author Riicarus
 * @create 2023-11-7 23:28
 * @since 1.0.0
 */
public class PascalLexer implements Lexer {

    static {
        System.out.println("Lexer DFA building...");
        long time = System.currentTimeMillis();
        PascalLexicalSymbol.avoidLazyLoad();
        System.out.println("Lexer DFA build finished, time used: " + (System.currentTimeMillis() - time) + "ms");
    }

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
    @Override
    public List<Token> parse(char[] input) {
        List<Token> tokenList = new LinkedList<>();

        buffer = input;
        cur = 0;
        line = 1;
        lineIdx = 0;

        while (cur < buffer.length) {
            final Token token = parseOne();

            token.setLine(line);

            if (token.getSymbol().equals(PascalLexicalSymbol.EOL)) {
                line++;
                lineIdx = 0;
            }

            tokenList.add(token);
        }

        tokenList.add(new Token(PascalLexicalSymbol.EOF, "EOF", line));
        tokenList.add(new Token(PascalLexicalSymbol.LEX_END, String.valueOf(CharUtil.LEX_SYNTAX_END),line));

        return tokenList;
    }

    /**
     * 向前分析一个串, 使用最长匹配原则.
     *
     * @return Token
     */
    private Token parseOne() {
        char[] s = Arrays.copyOfRange(buffer, cur, buffer.length);

        final Token token = LexicalSymbol.validateLongest(String.valueOf(s), Set.of(PascalLexicalSymbol.values()));

        if (token == null) throw new IllegalStateException("PascalLexer: Wrong syntax \"" + buffer[cur] + "\", line: " + line + ", idx: " + lineIdx);

        cur += token.getLen();
        lineIdx += token.getLen();

        return token;
    }

    @Override
    public Set<LexicalSymbol> getAssistantLexSymbolSet() {
        return PascalLexicalSymbol.assistantLexSymbols();
    }
}
