import io.github.riicarus.front.analyzer.lexical.NfaEdge;
import io.github.riicarus.front.analyzer.lexical.NFA;
import org.junit.Test;

import java.util.List;

/**
 * @author Riicarus
 * @create 2023-11-16 20:18
 * @since 1.0.0
 */
public class NfaTest {

    NfaEdge nfaEdgeA = new NfaEdge(0, 'a', 1);
    NfaEdge nfaEdgeB = new NfaEdge(0, 'b', 1);
    NFA nfaA = new NFA(List.of(nfaEdgeA), 2);
    NFA nfaB = new NFA(List.of(nfaEdgeB), 2);

    @Test
    public void testUnionNFA() {
        System.out.println(NFA.union(nfaA, nfaB));
    }

    @Test
    public void testConcatNFA() {
        System.out.println(NFA.concat(nfaA, nfaB));
    }

    @Test
    public void testClosureNFA() {
        testUnionNFA();
        System.out.println(NFA.closure(NFA.union(nfaA, nfaB)));
    }

    @Test
    public void testEpsClosureNFA() {
        NFA nfa = NFA.reToNFA("a(b|c)*");
        System.out.println(nfa);
        System.out.println(nfa.epsClosureMove(nfa.epsClosureOfState(1), NfaEdge.EPS_TRANS_VALUE));
        System.out.println(nfa.epsClosureMove(nfa.epsClosureOfState(1), 'b'));
        System.out.println(nfa.epsClosureMove(nfa.epsClosureOfState(1), 'c'));
    }

    @Test
    public void testReToNFA() {
        String expr = "a(b|c)*";
//        String expr = "cd";
        System.out.println(NFA.reToNFA(expr));
    }

    @Test
    public void testMergeNFA() {
        System.out.println(NFA.merge(List.of(NFA.reToNFA("a(b|c)*"), NFA.reToNFA("abc"))));
        System.out.println(NFA.merge(List.of(NFA.reToNFA("a"), NFA.reToNFA("b"), NFA.reToNFA("cd"))));
    }

}
