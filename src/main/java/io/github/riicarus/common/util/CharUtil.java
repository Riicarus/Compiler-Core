package io.github.riicarus.common.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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

    public static Set<Character> PASCAL_CHAR_SET = new HashSet<>();

    static {
        for (int i = 48; i <= 62; i++) {
            PASCAL_CHAR_SET.add((char) i);
        }

        for (int i = 65; i <= 90; i++) {
            PASCAL_CHAR_SET.add((char) i);
        }

        for (int i = 97; i <= 122; i++) {
            PASCAL_CHAR_SET.add((char) i);
        }

        PASCAL_CHAR_SET.addAll(List.of('*', '-', '(', ')', '\n', '\r', ' ', (char) 26));
    }

    public static Set<Character> getDefaultASCIICharSet() {
        return Collections.unmodifiableSet(DEFAULT_CHAR_SET);
    }

    public static final char L_BRACKET = '[';
    public static final char R_BRACKET = ']';
    public static final char CLOSURE = '^';
    public static final char CONCAT = '.';
    public static final char UNION = '|';
    public static final char L_BRACKET_ESCAPED = (char) 128;
    public static final char R_BRACKET_ESCAPED = (char) 129;
    public static final char CLOSURE_ESCAPED = (char) 130;
    public static final char CONCAT_ESCAPED = (char) 131;
    public static final char UNION_ESCAPED = (char) 132;
    public static final char ESCAPE = '\\';

    public static final String LETTER_REGEX = "a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z|A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z";
    public static final String DIGIT_REGEX = "0|1|2|3|4|5|6|7|8|9";
    public static final String IDENTIFIER_REGEX = "[" + LETTER_REGEX + "][" + LETTER_REGEX + "|" + DIGIT_REGEX + "]^";
    public static final String NUMBER_REGEX = "[" + DIGIT_REGEX + "][" + DIGIT_REGEX + "]^";

    public static int precedence(char c) {
        switch (c) {
            case CLOSURE -> {
                return 3;
            }
            case CONCAT -> {
                return 2;
            }
            case UNION -> {
                return 1;
            }
            default -> {
                return -1;
            }
        }
    }

}
