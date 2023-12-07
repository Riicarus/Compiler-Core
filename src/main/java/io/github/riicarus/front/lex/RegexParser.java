package io.github.riicarus.front.lex;

import io.github.riicarus.common.util.CharUtil;

import java.util.Set;
import java.util.Stack;

/**
 * 正则表达式转换器, 支持将正则表达式转为中缀表达式和后缀表达式, 以及转换为 NFA.
 *
 * <p>仅支持 ASCII 字符集.</p>
 * <p>支持转义字符, 注意转义之后的字符会使用 ASCII 之外的一些字符表示.</p>
 * <p>字符转义发生在正则转换为中缀表达式的过程中.</p>
 * <p>转义后的非 ASCII 字符仅在正则中缀表达式和后缀表达式中生效, 在 NFA 中会被转换为原字符.</p>
 * <p>可转义字符包括: '(', ')', '*', '.', '|'</p>
 *
 * @author Riicarus
 * @create 2023-11-21 19:41
 * @since 1.0.0
 */
public class RegexParser {

    /**
     * 将正则表达式转换为 NFA.
     *
     * @param expr         正则表达式.
     * @param inputCharSet 输入中可使用的字符集合.
     * @return NFA
     */
    public static NFA reToNFA(String expr, Set<Character> inputCharSet) {
        if (!isRegexLegal(expr, inputCharSet)) throw new IllegalArgumentException("RegExp is not legal.");
        return reSuffixToNFA(infixToSuffix(reToInfix(expr)), inputCharSet);
    }

    /**
     * 将正则表达式转换为 NFA.
     *
     * @param expr 正则表达式.
     * @return NFA
     */
    public static NFA reToNFA(String expr) {
        return reSuffixToNFA(infixToSuffix(reToInfix(expr)), null);
    }

    /**
     * 将正则表达式的后缀表达式转换为 NFA.
     *
     * @param reSuffix     正则表达式的后缀表达式.
     * @param inputCharSet 输入中可使用的字符集合.
     * @return NFA
     */
    private static NFA reSuffixToNFA(String reSuffix, Set<Character> inputCharSet) {
        Stack<NFA> nfaStack = new Stack<>();

        if (inputCharSet == null || inputCharSet.isEmpty()) {
            inputCharSet = CharUtil.getDefaultASCIICharSet();
        }

        int ID = -1;

        for (int i = 0; i < reSuffix.length(); i++) {
            char tmp = reSuffix.charAt(i);

            if (tmp == CharUtil.UNION) {
                NFA b = nfaStack.pop();
                NFA a = nfaStack.pop();
                nfaStack.push(NFA.union(a, b));
            } else if (tmp == CharUtil.CONCAT) {
                NFA b = nfaStack.pop();
                NFA a = nfaStack.pop();
                nfaStack.push(NFA.concat(a, b));
            } else if (tmp == CharUtil.CLOSURE) {
                nfaStack.push(NFA.closure(nfaStack.pop()));
            } else {    // 如果 tmp 不是功能符号, 就生成一条边.
                // 必须先判断是否合法.
                if (inputCharSet.contains(tmp)) {
                    // 可能会转义回原字符
                    if (CharUtil.shouldRegexEscapeBack(tmp)) tmp = CharUtil.escapeRegexBack(tmp);

                    nfaStack.push(new NFA(tmp));
                } else {
                    throw new IllegalStateException("reSuffixToNFA(): Forbidden character.");
                }
            }
        }

        return nfaStack.pop();
    }

    /**
     * 将正则表达式转换为其中缀表达式.
     *
     * @param expr 正则表达式.
     * @return 正则表达式的中缀表达式
     */
    private static String reToInfix(String expr) {
        final char END = '\0';

        StringBuilder infixBuilder = new StringBuilder();
        boolean needEscape = false;
        for (int i = 0; i < expr.length(); i++) {
            char tmp = expr.charAt(i);
            char next;

            if (i == expr.length() - 1)
                next = END;
            else
                next = expr.charAt(i + 1);

            // 不需要转义的时候, 遇到转义字符, 设置为需要转义, 直接解析下一个字符.
            if (!needEscape && tmp == CharUtil.BACKSLASH) {
                needEscape = true;
                continue;
            }

            // 如果有转义字符, 将其转义
            if (needEscape) {
                if (CharUtil.canRegexEscape(tmp)) {
                    tmp = CharUtil.escapeRegex(tmp);
                } else {
                    throw new IllegalArgumentException("Regex parse error: Can not escape letter: " + tmp);
                }
                needEscape = false;
            }

            if (tmp != CharUtil.L_BRACKET && tmp != CharUtil.CONCAT && tmp != CharUtil.UNION && next != CharUtil.R_BRACKET &&
                    next != CharUtil.CLOSURE && next != CharUtil.UNION && next != CharUtil.CONCAT && next != END) {
                // 如果表示的是连接操作(tmp 和 next 都不是可连接的功能性字符), 如: "ab", 就转换成 "a.b"; 否则直接拼接字符.
                infixBuilder.append(tmp).append(CharUtil.CONCAT);
            } else {
                infixBuilder.append(tmp);
            }
        }

        // System.out.println(infixBuilder);

        return infixBuilder.toString();
    }

    /**
     * 将正则表达式的中缀表达式转换为后缀表达式.
     *
     * @param infix 正则表达式的中缀表达式.
     * @return 正则表达式的后缀表达式.
     */
    private static String infixToSuffix(String infix) {
        StringBuilder suffixBuilder = new StringBuilder();
        Stack<Character> op = new Stack<>();

        for (int i = 0; i < infix.length(); i++) {
            char tmp = infix.charAt(i);

            if (tmp == CharUtil.L_BRACKET) {
                op.push(tmp);
            } else if (tmp == CharUtil.R_BRACKET) {
                while (op.peek() != CharUtil.L_BRACKET) {
                    suffixBuilder.append(op.pop());
                }
                op.pop();
            } else if (tmp == CharUtil.CLOSURE || tmp == CharUtil.CONCAT || tmp == CharUtil.UNION) {
                while (!op.isEmpty() && op.peek() != CharUtil.L_BRACKET && CharUtil.regexPrecedence(tmp) <= CharUtil.regexPrecedence(op.peek())) {
                    suffixBuilder.append(op.pop());
                }
                op.push(tmp);
            } else {
                suffixBuilder.append(tmp);
            }
        }

        while (!op.isEmpty()) suffixBuilder.append(op.pop());

        return suffixBuilder.toString();
    }

    /**
     * 检查正则表达式是否合法.
     *
     * @param expr         正则表达式
     * @param inputCharSet 允许输入的字符集合
     * @return 正则表达式是否合法
     */
    private static boolean isRegexLegal(String expr, Set<Character> inputCharSet) {
        Stack<Character> bracktStack = new Stack<>();

        if (inputCharSet == null || inputCharSet.isEmpty())
            inputCharSet = CharUtil.getDefaultASCIICharSet();

        boolean needEscape = false;

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);

            // 转义的判断是最优先的.
            if (!needEscape && c == CharUtil.BACKSLASH) {
                needEscape = true;
                continue;
            }
            if (needEscape) {
                if (CharUtil.canRegexEscape(c)) {
                    needEscape = false;
                    continue;
                } else {
                    return false;
                }
            }

            // 判断非转义字符
            if (inputCharSet.contains(c)) continue;
            if (c == CharUtil.UNION || c == CharUtil.CLOSURE || c == CharUtil.CONCAT) continue;

            if (c == CharUtil.L_BRACKET) bracktStack.push(c);
            else if (c == CharUtil.R_BRACKET)
                if (bracktStack.isEmpty()) return false;
                else bracktStack.pop();
            else return false;
        }

        if (!bracktStack.isEmpty()) return false;

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            char next;
            if (c == CharUtil.UNION) {
                if (i == 0 || i == expr.length() - 1) {
                    return false;
                } else {
                    next = expr.charAt(i + 1);
                    if (next == CharUtil.UNION || next == CharUtil.CLOSURE || next == CharUtil.CONCAT || next == CharUtil.R_BRACKET)
                        return false;
                }
            } else if (c == CharUtil.CONCAT) {
                if (i == 0 || i == expr.length() - 1) return false;
                else {
                    next = expr.charAt(i + 1);
                    if (next == CharUtil.UNION || next == CharUtil.CLOSURE || next == CharUtil.CONCAT || next == CharUtil.R_BRACKET)
                        return false;
                }
            } else if (c == CharUtil.CLOSURE) {
                if (i == 0) return false;
                else if (i != expr.length() - 1 && expr.charAt(i + 1) == CharUtil.CLOSURE) return false;
            }
        }

        return true;
    }

}
