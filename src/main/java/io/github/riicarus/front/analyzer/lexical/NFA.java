package io.github.riicarus.front.analyzer.lexical;

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

    public NFA() {
        stateCount = 2;
        nfaEdges = new LinkedList<>();
        NfaEdge e = new NfaEdge(0, 1);
        nfaEdges.add(e);
    }

    public NFA(char c) {
        stateCount = 2;
        nfaEdges = new LinkedList<>();
        NfaEdge e = new NfaEdge(0, c, 1);
        nfaEdges.add(e);
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
     * @param trans 转换字符
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Start state: ").append(0);
        sb.append("\n").append("End state: ").append(stateCount);
        nfaEdges.forEach(x -> sb.append("\n").append(x));
        return sb.toString();
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

        return new NFA(edgesC, countA + countB + 2);
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

        return new NFA(edgesC, countA + countB);
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

        return new NFA(edgesB, countA + 2);
    }

    /**
     * 将正则表达式转换为 NFA.
     *
     * @param expr 正则表达式.
     * @param inputCharSet 输入中可使用的字符集合.
     * @return NFA
     */
    public static NFA reToNFA(String expr, Set<Character> inputCharSet) {
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
     * @param reSuffix 正则表达式的后缀表达式.
     * @param inputCharSet 输入中可使用的字符集合.
     * @return NFA
     */
    private static NFA reSuffixToNFA(String reSuffix, Set<Character> inputCharSet) {
        Stack<NFA> nfaStack = new Stack<>();

        int ID = -1;

        for (int i = 0; i < reSuffix.length(); i++) {
            char tmp = reSuffix.charAt(i);

            if (tmp == '|') {
                NFA b = nfaStack.pop();
                NFA a = nfaStack.pop();
                nfaStack.push(NFA.union(a, b));
            } else if (tmp == '.') {
                NFA b = nfaStack.pop();
                NFA a = nfaStack.pop();
                nfaStack.push(NFA.concat(a, b));
            } else if (tmp == '*') {
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
            if (tmp != '(' && tmp != '.' && tmp != '|' && next != ')' &&
                    next != '*' && next != '|' && next != '.' && next != '\0') {
                infixBuilder.append(tmp).append(".");
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

            if (tmp == '(') {
                op.push(tmp);
            } else if (tmp == ')') {
                while (op.peek() != '(') {
                    suffixBuilder.append(op.pop());
                }
                op.pop();
            } else if (tmp == '*' || tmp == '.' || tmp == '|') {
                while (!op.isEmpty() && op.peek() != '(' && CharUtil.precedence(tmp) <= CharUtil.precedence(op.peek())) {
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
}
