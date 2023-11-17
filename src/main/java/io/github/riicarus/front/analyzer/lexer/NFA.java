package io.github.riicarus.front.analyzer.lexer;

import io.github.riicarus.common.util.CharUtil;

import java.util.*;

/**
 * 非确定有限状态自动机.
 *
 * <p>基于 Thompson 算法, 根据正则表达式生成 NFA, 过程为:</p>
 * <li>根绝正则表达式生成其中缀表达式</li>
 * <li>根绝中缀表达式生成后缀表达式</li>
 * <li>根绝后缀表达式生成 NFA</li>
 *
 * <p>仅支持连接, 合并和闭包操作.</p>
 *
 * @author Riicarus
 * @create 2023-11-16 18:09
 * @since 1.0.0
 */
public class NFA {

    private final List<NfaEdge> nfaEdges;

    private final int stateCount;

    private final Set<Integer> terminateStateSet = new HashSet<>();

    public NFA() {
        stateCount = 2;
        nfaEdges = new LinkedList<>();
        NfaEdge e = new NfaEdge(0, 1);
        nfaEdges.add(e);
        terminateStateSet.add(1);
    }

    public NFA(char c) {
        stateCount = 2;
        nfaEdges = new LinkedList<>();
        NfaEdge e = new NfaEdge(0, c, 1);
        nfaEdges.add(e);
        terminateStateSet.add(1);
    }

    public NFA(List<NfaEdge> nfaEdges, int stateCount) {
        this.nfaEdges = nfaEdges;
        this.stateCount = stateCount;
    }

    /**
     * 获取从 state 开始的 eps-闭包.
     *
     * @param state 开始状态
     * @return 从 state 开始的 esp-闭包集合(set)
     */
    public Set<Integer> epsClosureOfState(int state) {
        Set<Integer> epsClosure = new HashSet<>();

        Queue<Integer> queue = new LinkedList<>();
        queue.add(state);
        while (!queue.isEmpty()) {
            int s = queue.poll();
            epsClosure.add(s);

            for (NfaEdge e : nfaEdges) {
                // 从 State s 开始, 通过 eps 转换的边
                if (e.getFromState() == s && e.getTransValue() == NfaEdge.EPS_TRANS_VALUE) {
                    int u = e.getToState();
                    if (!epsClosure.contains(u)) {
                        epsClosure.add(u);
                        queue.add(u);
                    }
                }
            }
        }

        return epsClosure;
    }

    /**
     * 获取从 beginSet 中任意一个状态, 能够通过 trans 转换到的目标状态集合.
     *
     * @param beginSet 开始状态集合
     * @param trans    转换字符
     * @return 目标状态集合
     */
    public Set<Integer> move(Set<Integer> beginSet, char trans) {
        Set<Integer> end = new HashSet<>();

        for (Integer begin : beginSet) {
            for (NfaEdge edge : nfaEdges) {
                if (begin == edge.getFromState() && trans == edge.getTransValue()) {
                    end.add(edge.getToState());
                }
            }
        }

        return end;
    }

    /**
     * 获取从 beginSet 中任意状态, 能够通过 trans 转换到的目标状态集合的 eps-闭包集合.
     *
     * @param beginSet 开始状态集合
     * @param trans    转换字符
     * @return 目标状态集合的 eps-闭包集合
     */
    public Set<Integer> epsClosureMove(Set<Integer> beginSet, char trans) {
        Set<Integer> epsClosureEndSet = new HashSet<>();

        for (Integer state : move(beginSet, trans)) {
            epsClosureEndSet.addAll(epsClosureOfState(state));
        }

        return epsClosureEndSet;
    }

    public List<NfaEdge> getEdges() {
        return nfaEdges;
    }

    public int getStateCount() {
        return stateCount;
    }

    public Set<Integer> getTerminateStateSet() {
        return Collections.unmodifiableSet(terminateStateSet);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Start state: ").append(0);
        sb.append("\n").append("End state: ").append(stateCount - 1);
        nfaEdges.forEach(x -> sb.append("\n").append(x));
        sb.append("\n").append("terminate: ").append(terminateStateSet);
        return sb.toString();
    }

    /**
     * 将多个 NFA 合并为一个.
     *
     * @param collection NFA 集合
     * @return 合并后的 NFA
     */
    public static NFA merge(Collection<NFA> collection) {
        List<NfaEdge> edges = new LinkedList<>();
        Set<Integer> terminatedStateSet = new HashSet<>();

        int offset = 1;
        for (NFA nfa : collection) {
            edges.add(new NfaEdge(0, offset));
            for (NfaEdge nfaEdge : nfa.nfaEdges) {
                NfaEdge e = new NfaEdge(nfaEdge.getFromState() + offset, nfaEdge.getTransValue(), nfaEdge.getToState() + offset);
                edges.add(e);
            }
            for (Integer t : nfa.terminateStateSet) {
                terminatedStateSet.add(t + offset);
            }

            offset += nfa.getStateCount();
        }

        NFA merged = new NFA(edges, offset);
        merged.terminateStateSet.addAll(terminatedStateSet);

        return merged;
    }

    /**
     * 取两个 NFA 的使用 eps 连接的并集, 即 "a|b", 注意顺序.
     *
     * @param a NFA
     * @param b NFA
     * @return 两个 NFA 的并集
     */
    public static NFA union(NFA a, NFA b) {
        if (a == null && b == null) return new NFA();
        else if (a != null && b == null) return a;
        else if (a == null) return b;

        List<NfaEdge> edgesA = new LinkedList<>(a.getEdges());
        List<NfaEdge> edgesB = new LinkedList<>(b.getEdges());
        List<NfaEdge> edgesC = new LinkedList<>();

        int countA = a.getStateCount();
        int countB = b.getStateCount();

        NfaEdge startToA = new NfaEdge(0, 1);
        edgesC.add(startToA);

        for (NfaEdge nfaEdge : edgesA) {
            NfaEdge e = new NfaEdge(nfaEdge.getFromState() + 1, nfaEdge.getTransValue(), nfaEdge.getToState() + 1);
            edgesC.add(e);
        }

        NfaEdge startToB = new NfaEdge(0, countA + 1);
        edgesC.add(startToB);

        for (NfaEdge nfaEdge : edgesB) {
            NfaEdge e = new NfaEdge(nfaEdge.getFromState() + countA + 1, nfaEdge.getTransValue(), nfaEdge.getToState() + countA + 1);
            edgesC.add(e);
        }

        NfaEdge AToEnd = new NfaEdge(countA, countA + countB + 1);
        edgesC.add(AToEnd);

        NfaEdge BToEnd = new NfaEdge(countA + countB, countA + countB + 1);
        edgesC.add(BToEnd);

        NFA nfa = new NFA(edgesC, countA + countB + 2);
        nfa.terminateStateSet.add(nfa.stateCount - 1);

        return nfa;
    }

    /**
     * 取两个 NFA 的使用 eps 连接的连接, 即: "ab", 注意顺序.
     *
     * @param a NFA
     * @param b NFA
     * @return 两个 NFA 的连接
     */
    public static NFA concat(NFA a, NFA b) {
        if (a == null && b == null) return new NFA();
        else if (a != null && b == null) return a;
        else if (a == null) return b;

        List<NfaEdge> edgesA = new LinkedList<>(a.getEdges());
        List<NfaEdge> edgesB = new LinkedList<>(b.getEdges());
        List<NfaEdge> edgesC = new LinkedList<>();

        int countA = a.getStateCount();
        int countB = b.getStateCount();

        for (NfaEdge nfaEdge : edgesA) {
            NfaEdge e = new NfaEdge(nfaEdge.getFromState(), nfaEdge.getTransValue(), nfaEdge.getToState());
            edgesC.add(e);
        }

        NfaEdge aToB = new NfaEdge(countA - 1, countA);
        edgesC.add(aToB);

        for (NfaEdge nfaEdge : edgesB) {
            NfaEdge e = new NfaEdge(nfaEdge.getFromState() + countA, nfaEdge.getTransValue(), nfaEdge.getToState() + countA);
            edgesC.add(e);
        }

        NFA nfa = new NFA(edgesC, countA + countB);
        nfa.terminateStateSet.add(nfa.stateCount - 1);

        return nfa;
    }

    /**
     * 取 NFA 的闭包.
     *
     * @param a NFA
     * @return NFA 的闭包
     */
    public static NFA closure(NFA a) {
        if (a == null) return new NFA();

        List<NfaEdge> edgesA = new LinkedList<>(a.getEdges());
        List<NfaEdge> edgesB = new LinkedList<>();

        int countA = a.getStateCount();

        NfaEdge start = new NfaEdge(0, 1);
        edgesB.add(start);

        for (NfaEdge nfaEdge : edgesA) {
            NfaEdge e = new NfaEdge(nfaEdge.getFromState() + 1, nfaEdge.getTransValue(), nfaEdge.getToState() + 1);
            edgesB.add(e);
        }

        NfaEdge end = new NfaEdge(countA, countA + 1);
        edgesB.add(end);

        NfaEdge startToEnd = new NfaEdge(0, countA + 1);
        edgesB.add(startToEnd);

        NfaEdge circle = new NfaEdge(countA, 1);
        edgesB.add(circle);

        NFA nfa = new NFA(edgesB, countA + 2);
        nfa.terminateStateSet.add(nfa.stateCount - 1);

        return nfa;
    }

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
                // 如果所有字符都可以使用
                if (inputCharSet == null || inputCharSet.isEmpty()) {
                    nfaStack.push(new NFA(tmp));
                } else {
                    // 如果只有一些字符能使用, 必须先判断是否合法.
                    if (inputCharSet.contains(tmp)) {
                        nfaStack.push(new NFA(tmp));
                    } else {
                        throw new IllegalStateException("reSuffixToNFA(): Forbidden character.");
                    }
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
        StringBuilder infixBuilder = new StringBuilder();
        for (int i = 0; i < expr.length(); i++) {
            char tmp = expr.charAt(i);
            char next;

            if (i == expr.length() - 1)
                next = '\0';
            else
                next = expr.charAt(i + 1);

            // 如果表示的是连接操作, 如: "ab", 就转换成 "a.b"; 否则直接拼接字符.
            if (tmp != CharUtil.L_BRACKET && tmp != CharUtil.CONCAT && tmp != CharUtil.UNION && next != CharUtil.R_BRACKET &&
                    next != CharUtil.CLOSURE && next != CharUtil.UNION && next != CharUtil.CONCAT && next != '\0') {
                infixBuilder.append(tmp).append(CharUtil.CONCAT);
            } else {
                infixBuilder.append(tmp);
            }
        }

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
                while (!op.isEmpty() && op.peek() != CharUtil.L_BRACKET && CharUtil.precedence(tmp) <= CharUtil.precedence(op.peek())) {
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

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
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
                    if (next == CharUtil.UNION || next == CharUtil.CLOSURE || next == CharUtil.CONCAT || next == CharUtil.R_BRACKET) return false;
                }
            } else if (c == CharUtil.CONCAT) {
                if (i == 0 || i == expr.length() - 1) return false;
                else {
                    next = expr.charAt(i + 1);
                    if (next == CharUtil.UNION || next == CharUtil.CLOSURE || next == CharUtil.CONCAT || next == CharUtil.R_BRACKET) return false;
                }
            } else if (c == CharUtil.CLOSURE) {
                if (i == 0) return false;
                else if (i != expr.length() - 1 && expr.charAt(i + 1) == CharUtil.CLOSURE) return false;
            }
        }

        return true;
    }
}
