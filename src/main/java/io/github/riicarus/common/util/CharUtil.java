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

        PASCAL_CHAR_SET.addAll(List.of('*', '-', '(', ')', '\n', '\r'));
    }

    public static Set<Character> getDefaultASCIICharSet() {
        return Collections.unmodifiableSet(DEFAULT_CHAR_SET);
    }

    public static final char L_BRACKET = '[';
    public static final char R_BRACKET = ']';
    public static final char CLOSURE = '^';
    public static final char CONCAT = '.';
    public static final char UNION = '|';


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
