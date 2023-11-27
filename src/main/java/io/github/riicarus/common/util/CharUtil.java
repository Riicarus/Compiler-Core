package io.github.riicarus.common.util;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 字符工具类, 判断输入字符的类型.
 *
 * @author Riicarus
 * @create 2023-11-7 22:52
 * @since 1.0.0
 */
public class CharUtil {

    public static final char L_BRACKET = '(';
    public static final char R_BRACKET = ')';
    public static final char CLOSURE = '*';
    public static final char CONCAT = '.';
    public static final char UNION = '|';
    public static final char BACKSLASH = '\\';
    public static final char L_BRACKET_ESCAPED = (char) 128;
    public static final char R_BRACKET_ESCAPED = (char) 129;
    public static final char CLOSURE_ESCAPED = (char) 130;
    public static final char CONCAT_ESCAPED = (char) 131;
    public static final char UNION_ESCAPED = (char) 132;
    public static final char BACKSLASH_ESCAPED = (char) 133;
    public static final char EPS_TRANS_VALUE = (char) 134;

    private static final Set<Character> FUNCTIONAL_CHAR_SET = Set.of(
            L_BRACKET,
            R_BRACKET,
            CLOSURE,
            CONCAT,
            UNION,
            BACKSLASH
    );

    public static boolean isFunctionalChar(char c) {
        return FUNCTIONAL_CHAR_SET.contains(c);
    }

    private static final Map<Character, Character> REGEX_ESCAPE_CHAR_MAP = Map.of(
            L_BRACKET, L_BRACKET_ESCAPED,
            R_BRACKET, R_BRACKET_ESCAPED,
            CLOSURE, CLOSURE_ESCAPED,
            CONCAT, CONCAT_ESCAPED,
            UNION, UNION_ESCAPED,
            BACKSLASH, BACKSLASH_ESCAPED
    );

    private static final Map<Character, Character> REGEX_ESCAPE_BACK_CHAR_MAP = REGEX_ESCAPE_CHAR_MAP.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

    public static boolean canRegexEscape(char c) {
        return REGEX_ESCAPE_CHAR_MAP.containsKey(c);
    }

    public static char escapeRegex(char c) {
        return REGEX_ESCAPE_CHAR_MAP.get(c);
    }

    public static boolean shouldRegexEscapeBack(char c) {
        return REGEX_ESCAPE_BACK_CHAR_MAP.containsKey(c);
    }

    public static char escapeRegexBack(char c) {
        return REGEX_ESCAPE_BACK_CHAR_MAP.get(c);
    }

    public static int regexPrecedence(char c) {
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

    private static final Set<Character> DEFAULT_CHAR_SET = new HashSet<>();

    static {
        for (int i = 0; i <= 127; i++) {
            DEFAULT_CHAR_SET.add((char) i);
        }
    }

    public static Set<Character> PASCAL_CHAR_SET = new HashSet<>();

    public static Set<Character> getDefaultASCIICharSet() {
        return Collections.unmodifiableSet(DEFAULT_CHAR_SET);
    }

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

        PASCAL_CHAR_SET.addAll(List.of('*', '-', '(', ')', '\n', '\r', ' ', (char) 26, CharUtil.LEX_SYNTAX_END));
        PASCAL_CHAR_SET.addAll(REGEX_ESCAPE_CHAR_MAP.values());
    }

    public static final String LETTER_REGEX = "a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z|A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z";
    public static final String DIGIT_REGEX = "0|1|2|3|4|5|6|7|8|9";
    public static final String IDENTIFIER_REGEX = L_BRACKET + LETTER_REGEX + R_BRACKET + L_BRACKET + LETTER_REGEX + UNION + DIGIT_REGEX + R_BRACKET + CLOSURE;
    public static final String NUMBER_REGEX = L_BRACKET + DIGIT_REGEX + R_BRACKET + L_BRACKET + DIGIT_REGEX + R_BRACKET + CLOSURE;

    /*
     * 对文法中需要用到的符号进行定义和处理.
     * */
    public static final String SYNTAX_CONCAT = " ";
    public static final String SYNTAX_UNION = " \\| ";
    public static final String SYNTAX_DEFINE = " : ";
    public static final String SYNTAX_SEPARATOR = "%";

    public static final char COLON = ':';
    public static final char VERTICAL_LINE = '|';
    public static final char PERCENTAGE = '%';
    public static final char COLON_ESCAPED = (char) 135;
    public static final char VERTICAL_LINE_ESCAPED = (char) 136;
    public static final char PERCENTAGE_ESCAPED = (char) 137;
    public static final char LEX_SYNTAX_END = (char) 138;

    private static final Map<Character, Character> SYNTAX_ESCAPE_CHAR_MAP = Map.of(
            COLON, COLON_ESCAPED,
            VERTICAL_LINE, VERTICAL_LINE_ESCAPED,
            BACKSLASH, BACKSLASH_ESCAPED,
            PERCENTAGE, PERCENTAGE_ESCAPED
    );

    private static final Map<Character, Character> SYNTAX_ESCAPE_BACK_CHAR_MAP = SYNTAX_ESCAPE_CHAR_MAP.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

    public static boolean canSyntaxEscape(char c) {
        return SYNTAX_ESCAPE_CHAR_MAP.containsKey(c);
    }

    public static char escapeSyntax(char c) {
        return SYNTAX_ESCAPE_CHAR_MAP.get(c);
    }

    public static boolean shouldSyntaxEscapeBack(char c) {
        return SYNTAX_ESCAPE_BACK_CHAR_MAP.containsKey(c);
    }

    public static char escapeSyntaxBack(char c) {
        return SYNTAX_ESCAPE_BACK_CHAR_MAP.get(c);
    }

}
