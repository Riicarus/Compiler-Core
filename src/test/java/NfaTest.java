import io.github.riicarus.common.util.CharUtil;
import io.github.riicarus.front.lex.RegexParser;
import io.github.riicarus.front.lex.NfaEdge;
import io.github.riicarus.front.lex.NFA;
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
        NFA nfa = RegexParser.reToNFA("a(b|c)*");
        System.out.println(nfa);
        System.out.println(nfa.epsClosureMove(nfa.epsClosureOfState(1), CharUtil.EPS_TRANS_VALUE));
        System.out.println(nfa.epsClosureMove(nfa.epsClosureOfState(1), 'b'));
        System.out.println(nfa.epsClosureMove(nfa.epsClosureOfState(1), 'c'));
    }

    @Test
    public void testReToNFA() {
        String expr = "a(b|c)*";
//        String expr = "cd";
        System.out.println(RegexParser.reToNFA(expr, CharUtil.PASCAL_CHAR_SET));
    }

    @Test
    public void testReToNfaWithEscapeLetter() {
        String expr = "a\\(\\*\\)(b|c)*";
        System.out.println(RegexParser.reToNFA(expr, CharUtil.PASCAL_CHAR_SET));
    }

    @Test
    public void testMergeNFA() {
        System.out.println(NFA.merge(List.of(RegexParser.reToNFA("a(b|c)*"), RegexParser.reToNFA("abc"))));
        System.out.println(NFA.merge(List.of(RegexParser.reToNFA("a"), RegexParser.reToNFA("b"), RegexParser.reToNFA("cd"))));
    }

}
