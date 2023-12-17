package io.github.riicarus.front.lex;

import io.github.riicarus.common.data.Token;

import java.util.List;

/**
 * 词法符号接口.
 *
 * @author Riicarus
 * @create 2023-11-22 21:32
 * @since 1.0.0
 */
public interface LexicalSymbol {

    String getName();

    int getCode();

    boolean needPrintVal();

    DFA getDfa();

    Token validate(String s);

    static Token validateLongest(String s, List<LexicalSymbol> symbolSet) {
        {
            Token target = null;
            for (LexicalSymbol symbol : symbolSet) {
                final Token token = symbol.validate(s);

                if (token == null) continue;

                if (target == null) target = token;
                else if (target.getLen() < token.getLen()) target = token;
            }

            return target;
        }
    }
}
