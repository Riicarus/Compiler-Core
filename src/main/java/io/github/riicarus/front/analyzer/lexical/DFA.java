package io.github.riicarus.front.analyzer.lexical;

import java.util.LinkedList;
import java.util.List;

/**
 * 确定有限状态自动机.
 *
 * @author Riicarus
 * @create 2023-11-16 22:53
 * @since 1.0.0
 */
public class DFA {

    // 通过边存储 NFA
    private final List<NfaEdge> nfa = new LinkedList<>();

    // 通过状态存储 DFA
    private final List<DfaState> dfa = new LinkedList<>();



}
