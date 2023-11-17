package io.github.riicarus.front.analyzer.lexical;

import io.github.riicarus.common.util.CharUtil;

import java.util.*;

/**
 * 确定有限状态自动机.
 *
 * @author Riicarus
 * @create 2023-11-16 22:53
 * @since 1.0.0
 */
public class DFA {

    // 通过状态存储 DFA
    private final List<DfaState> dfaStates = new LinkedList<>();

    public void addState(DfaState state) {
        dfaStates.add(state);
    }

    /**
     * 根据 nfaStates 获取对应的 DfaState, 如果没有, 就新建一个实例, 并将实例添加到 DFA 中.
     *
     * @param nfaStates nfa 状态集合
     * @return nfa 状态集合对应的 DfaState
     */
    public DfaState getOrAddDefaultState(Set<Integer> nfaStates) {
        for (DfaState dfaState : dfaStates) {
            if (dfaState.getEpsNfaStates().size() == nfaStates.size() && dfaState.getEpsNfaStates().containsAll(nfaStates)) return dfaState;
        }

        DfaState dfaState = new DfaState(nfaStates);
        addState(dfaState);

        return dfaState;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        dfaStates.forEach(sb::append);

        return sb.toString();
    }

    /**
     * 将 NFA 转换为 DFA.
     *
     * @param nfa NFA
     * @return DFA
     */
    public static DFA nfaToDfa(NFA nfa, Set<Character> inputCharSet) {
        DFA dfa = new DFA();
        Set<Set<Integer>> Q = new HashSet<>();
        Queue<Set<Integer>> workList = new LinkedList<>();

        // 根据 n0 求 q0
        Set<Integer> startStates = nfa.epsClosureOfState(0);
        Q.add(startStates);
        workList.add(startStates);

        while (!workList.isEmpty()) {
            Set<Integer> q = workList.poll();

            if (inputCharSet == null || inputCharSet.isEmpty()) {
                inputCharSet = CharUtil.getDefaultASCIICharSet();
            }
            for (Character c : inputCharSet) {
                Set<Integer> t = nfa.epsClosureMove(q, c);

                if (t.isEmpty()) continue;

                // System.out.println(q + "--" + c + "-->: " + t);

                // q --c--> t
                DfaState fromState = dfa.getOrAddDefaultState(q);
                DfaState toState = dfa.getOrAddDefaultState(t);
                fromState.addTransition(c, toState);

                // 遇到新的 DFA 状态, 更新 Q 和 workList
                if (!containsSet(Q, t)) {
                    Q.add(t);
                    workList.add(t);
                }
            }
        }

        // System.out.println(Q);

        return dfa;
    }

    private static boolean containsSet(Set<Set<Integer>> s1, Set<Integer> s2) {
        for (Set<Integer> set : s1) {
            if (set.size() == s2.size() && set.containsAll(s2)) return true;
        }

        return false;
    }

}
