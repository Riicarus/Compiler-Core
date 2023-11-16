package io.github.riicarus.front.analyzer.lexical;

import java.util.LinkedList;
import java.util.List;

/**
 * DFA 的状态.
 *
 * @author Riicarus
 * @create 2023-11-16 22:50
 * @since 1.0.0
 */
public class DfaState {

    private int lastId;

    private char trans;

    private int nowId;

    private final List<Integer> epsClosure = new LinkedList<>();

    boolean terminal = false;
}
