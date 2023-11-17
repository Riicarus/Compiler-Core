package io.github.riicarus.common.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 字符工具类, 判断输入字符的类型.
 *
 * @author Riicarus
 * @create 2023-11-7 22:52
 * @since 1.0.0
 */
public class CharUtil {

    private static final Set<Character> DEFAULT_CHAR_SET = new HashSet<>();

    static {
        for (int i = 0; i <= 127; i++) {
            DEFAULT_CHAR_SET.add((char) i);
        }
    }

    public static Set<Character> getDefaultASCIICharSet() {
        return Collections.unmodifiableSet(DEFAULT_CHAR_SET);
    }

    public static boolean isBlank(char c) {
        return c == ' ';
    }

    public static boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    public static boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }
    
    public static boolean isEquals(char c) {
        return c == '=';
    }

    public static boolean isTimes(char c) {
        return c == '*';
    }

    public static boolean isMinus(char c) {
        return c == '-';
    }

    public static boolean isLeftBracket(char c) {
        return c == '(';
    }

    public static boolean isRightBracket(char c) {
        return c == ')';
    }

    public static boolean isLeftAngleBracket(char c) {
        return c == '<';
    }

    public static boolean isRightAngleBracket(char c) {
        return c == '>';
    }

    public static boolean isColon(char c) {
        return c == ':';
    }

    public static boolean isSemicolon(char c) {
        return c == ';';
    }

    public static boolean isSingleSymbol(char c) {
        return c == '=' || c == '-' || c == '*' || c == '(' || c == ')' || c == ';';
    }

    public static boolean isMultiSymbolPrefix(char c) {
        return c == '<' || c == '>' || c == ':';
    }

    public static boolean isMultiSymbolSuffix(char c) {
        return c == '=' || c == '>';
    }

    public static boolean isEOLFPrefix(char c) {
        return c == '\r';
    }

    public static boolean isEOL(char c) {
        return c == '\n';
    }

    public static int precedence(char c) {
        switch (c) {
            case '*' -> {
                return 3;
            }
            case '.' -> {
                return 2;
            }
            case '|' -> {
                return 1;
            }
            default -> {
                return -1;
            }
        }
    }

}
