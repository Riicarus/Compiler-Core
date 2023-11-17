package io.github.riicarus.front.analyzer.lexical;

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
public class LexicalAnalyzer {

    private char[] buffer;

    private int cur;

    private int line;

    private int lineIdx;

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

    public Token analyzeOne() {
        // if (cur == forward) moveForward();
        char[] s = Arrays.copyOfRange(buffer, cur, buffer.length);

        final Token token = LexicalSymbol.validateLongest(String.valueOf(s));

        if (token == null) throw new IllegalStateException("Lexer: Wrong syntax \"" + buffer[cur] + "\", line: " + line + "idx: " + lineIdx);

        cur += token.getLen();
        lineIdx += token.getLen();

        return token;
    }

}
