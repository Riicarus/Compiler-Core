package io.github.riicarus.front.analyzer.lexical;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DFA 的状态.
 *
 * @author Riicarus
 * @create 2023-11-16 22:50
 * @since 1.0.0
 */
public class DfaState {

    public static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);

    private final int state;

    private final Map<Character, DfaState> transMap = new HashMap<>();

    private final Set<Integer> epsNfaStates = new HashSet<>();

    private boolean terminal = false;

    public DfaState(Set<Integer> epsNfaStates, boolean terminal) {
        this.state = ID_GENERATOR.getAndIncrement();
        this.epsNfaStates.addAll(epsNfaStates);
        this.terminal = terminal;
    }

    public DfaState(Set<Integer> epsNfaStates) {
        this.state = ID_GENERATOR.getAndIncrement();
        this.epsNfaStates.addAll(epsNfaStates);
    }

    public void addTransition(char transVal, DfaState dst) {
        transMap.put(transVal, dst);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DfaState dfaState = (DfaState) o;
        return dfaState.epsNfaStates.size() == epsNfaStates.size() && dfaState.epsNfaStates.containsAll(epsNfaStates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(epsNfaStates.toArray());
    }

    public int getState() {
        return state;
    }

    public Map<Character, DfaState> getTransMap() {
        return transMap;
    }

    public Set<Integer> getEpsNfaStates() {
        return epsNfaStates;
    }

    public boolean isTerminal() {
        return terminal;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(state).append(": ").append(epsNfaStates).append("\n");

        transMap.forEach((t, s) -> sb.append("\t").append(t).append("-->").append(s.state).append(": ").append(s.epsNfaStates).append("\n"));

        return sb.toString();
    }
}
