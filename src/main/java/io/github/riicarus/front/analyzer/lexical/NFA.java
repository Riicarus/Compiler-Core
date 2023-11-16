package io.github.riicarus.front.analyzer.lexical;

import io.github.riicarus.common.util.CharUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * 非确定有限状态自动机.
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

    public static NFA reToNFA(String expr) {
        return reSuffixToNFA(infixToSuffix(toInfix(expr)));
    }

    public static NFA reSuffixToNFA(String reSuffix) {
        Stack<NFA> nfaStack = new Stack<>();

        int ID = -1;

        for (int i = 0; i < reSuffix.length(); i++) {
            char tmp = reSuffix.charAt(i);

            // if current char is letter, create a new edge
            if (tmp >= 'A' && tmp <= 'z') {
                nfaStack.push(new NFA(tmp));
            } else if (tmp == '|') {
                NFA b = nfaStack.pop();
                NFA a = nfaStack.pop();
                nfaStack.push(NFA.union(a, b));
            } else if (tmp == '.') {
                NFA b = nfaStack.pop();
                NFA a = nfaStack.pop();
                nfaStack.push(NFA.concat(a, b));
            } else if (tmp == '*')
                nfaStack.push(NFA.closure(nfaStack.pop()));
            else throw new IllegalStateException("reSuffixToNFA(): Wrong op.");
        }

        return nfaStack.pop();
    }

    public static String toInfix(String expr) {
        StringBuilder infixBuilder = new StringBuilder();
        for (int i = 0; i < expr.length(); i++) {
            char tmp = expr.charAt(i);
            char next;

            if (i == expr.length() - 1) next = '\0';
            else next = expr.charAt(i + 1);

            // 如果表示的是连接操作, 如: "ab", 就转换成 "a.b"; 否则直接拼接字符.
            if (tmp != '(' && tmp != '.' && tmp != '|' && next != ')' && next != '*' && next != '|' && next != '.' && next != '\0') {
                infixBuilder.append(tmp).append(".");
            } else infixBuilder.append(tmp);
        }

        return infixBuilder.toString();
    }

    public static String infixToSuffix(String infix) {
        StringBuilder suffixBuilder = new StringBuilder();
        Stack<Character> op = new Stack<>();

        for (int i = 0; i < infix.length(); i++) {
            char tmp = infix.charAt(i);

            if (tmp == '(') op.push(tmp);
            else if (tmp == ')') {
                while (op.peek() != '(') {
                    suffixBuilder.append(op.pop());
                }
                op.pop();
            } else if (tmp == '*' || tmp == '.' || tmp == '|') {
                while (!op.isEmpty() && op.peek() != '(' && CharUtil.precedence(tmp) <= CharUtil.precedence(op.peek())) {
                    suffixBuilder.append(op.pop());
                }
                op.push(tmp);
            } else suffixBuilder.append(tmp);
        }

        while (!op.isEmpty()) suffixBuilder.append(op.pop());

        return suffixBuilder.toString();
    }
}
