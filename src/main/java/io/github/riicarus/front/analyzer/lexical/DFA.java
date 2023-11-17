package io.github.riicarus.front.analyzer.lexical;

import io.github.riicarus.common.util.CharUtil;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 确定有限状态自动机.
 *
 * @author Riicarus
 * @create 2023-11-16 22:53
 * @since 1.0.0
 */
public class DFA {

    private final AtomicInteger ID_GENERATOR = new AtomicInteger(0);

    // 通过状态存储 DFA
    private final List<DfaState> dfaStates = new LinkedList<>();

    private final Set<Character> inputCharSet = new HashSet<>();

    private DfaState startState;

    public String validateString(String s) {
        DfaState cur = startState;
        char[] charArray = s.toCharArray();
        int spot = -1;
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            cur = cur.getTransMap().get(c);
            if (cur == null) break;
            else if (cur.isTerminate()) spot = i;
        }

        return s.substring(0, spot + 1);
    }

    public void addState(DfaState state) {
        dfaStates.add(state);
    }

    /**
     * 根据 nfaStates 获取对应的 DfaState, 如果没有, 就新建一个实例, 并将实例添加到 DFA 中.
     *
     * @param nfaStates nfa 状态集合
     * @return nfa 状态集合对应的 DfaState
     */
    public DfaState getOrAddDefaultState(Set<Integer> nfaStates, NFA nfa) {
        for (DfaState dfaState : dfaStates) {
            if (dfaState.getEpsNfaStates().size() == nfaStates.size() && dfaState.getEpsNfaStates().containsAll(nfaStates))
                return dfaState;
        }

        DfaState dfaState = new DfaState(ID_GENERATOR.getAndIncrement(), nfaStates);
        addState(dfaState);
        if (containsTerminateState(nfaStates, nfa.getTerminateStateSet())) {
            dfaState.setTerminate(true);
        }

        return dfaState;
    }

    private Set<Set<DfaState>> hopcroft() {
        Set<DfaState> terminateStateSet = new HashSet<>();
        Set<DfaState> notTerminateStateSet = new HashSet<>();

        for (DfaState dfaState : dfaStates) {
            if (dfaState.isTerminate()) terminateStateSet.add(dfaState);
            else notTerminateStateSet.add(dfaState);
        }

        // 状态的划分集合
        Set<Set<DfaState>> P = new HashSet<>();
        // 待处理的集合
        Set<Set<DfaState>> W = new HashSet<>();

        P.add(terminateStateSet);
        P.add(notTerminateStateSet);
        W.add(terminateStateSet);
        W.add(notTerminateStateSet);

        while (!W.isEmpty()) {
            Set<DfaState> A = W.iterator().next();
            W.remove(A);

            for (char c : inputCharSet) {
                // 通过字符 c 到达的 A 的所有状态的集合
                Set<DfaState> X = new HashSet<>();

                for (DfaState dfaState : dfaStates) {
                    if (dfaState.getTransMap().containsKey(c) && A.contains(dfaState.getTransMap().get(c)))
                        X.add(dfaState);
                }

                Set<Set<DfaState>> PNew = new HashSet<>();
                for (Set<DfaState> Y : P) {
                    // 交集
                    Set<DfaState> intersect = new HashSet<>(X);
                    intersect.retainAll(Y);

                    // 差集
                    Set<DfaState> difference = new HashSet<>(Y);
                    difference.removeAll(X);

                    if (!intersect.isEmpty() && !difference.isEmpty()) {
                        PNew.add(intersect);
                        PNew.add(difference);
                        W.add(intersect);
                        W.add(difference);
                    } else {
                        PNew.add(Y);
                    }
                }
                P = PNew;
            }
        }

        return P;
    }

    public DFA minimize() {
        Set<Set<DfaState>> P = hopcroft();

        Map<DfaState, DfaState> stateMap = new HashMap<>();
        DFA minimizedDFA = new DFA();

        // 为每个等价类创建新的状态
        for (Set<DfaState> stateSet : P) {
            Set<Integer> nfaStateSet = new HashSet<>();

            boolean terminate = false;
            for (DfaState oldState : stateSet) {
                nfaStateSet.addAll(oldState.getEpsNfaStates());
                terminate = terminate || oldState.isTerminate();
            }
            DfaState newState = new DfaState(minimizedDFA.ID_GENERATOR.getAndIncrement(), nfaStateSet, terminate);

            for (DfaState oldState : stateSet) {
                stateMap.put(oldState, newState);

                // 如果新的等价集合包含开始状态, 那么将新的状态设置为开始状态.
                if (oldState.getState() == startState.getState()) minimizedDFA.startState = newState;
            }

            minimizedDFA.addState(newState);
        }

        for (DfaState oldState : dfaStates) {
            DfaState newState = stateMap.get(oldState);

            for (Map.Entry<Character, DfaState> entry : oldState.getTransMap().entrySet()) {
                newState.addTransition(entry.getKey(), stateMap.get(entry.getValue()));
            }
        }

        minimizedDFA.setInputCharSet(new HashSet<>(inputCharSet));

        return minimizedDFA;
    }

    public void setInputCharSet(Set<Character> inputCharSet) {
        this.inputCharSet.addAll(inputCharSet);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        dfaStates.forEach(sb::append);
        sb.append("start: ").append(startState.getState());

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
            dfa.setInputCharSet(inputCharSet);

            for (Character c : inputCharSet) {
                Set<Integer> t = nfa.epsClosureMove(q, c);

                if (t.isEmpty()) continue;

                // System.out.println(q + "--" + c + "-->: " + t);

                // q --c--> t
                DfaState fromState = dfa.getOrAddDefaultState(q, nfa);
                DfaState toState = dfa.getOrAddDefaultState(t, nfa);
                fromState.addTransition(c, toState);

                // 遇到新的 DFA 状态, 更新 Q 和 workList
                if (!containsSet(Q, t)) {
                    Q.add(t);
                    workList.add(t);
                }
            }
        }

        // System.out.println(Q);

        for (DfaState dfaState : dfa.dfaStates) {
            if (dfaState.getState() == 0) dfa.startState = dfaState;
        }

        return dfa;
    }

    /**
     * 根据集合中的元素判断 s1 是否包含 s2.
     *
     * <p>集合中的元素完全相同时, 才称为包含.</p>
     *
     * @param s1 集合容器
     * @param s2 集合
     * @return s1 中是否包含 s2
     */
    private static boolean containsSet(Set<Set<Integer>> s1, Set<Integer> s2) {
        for (Set<Integer> set : s1) {
            if (set.size() == s2.size() && set.containsAll(s2)) return true;
        }

        return false;
    }

    public static boolean containsTerminateState(Set<Integer> nfaStateSet, Set<Integer> terminateStateSet) {
        for (Integer s : nfaStateSet) {
            if (terminateStateSet.contains(s)) return true;
        }

        return false;
    }

}
