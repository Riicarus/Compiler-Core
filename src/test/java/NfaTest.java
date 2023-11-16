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
    public void testEpsClosureNFA() {
        testUnionNFA();
        System.out.println(NFA.closure(NFA.union(nfaA, nfaB)));
    }

    @Test
    public void testReToNFA() {
        String expr = "a*(bc|d)";
        String infix = NFA.toInfix(expr);
        System.out.println(infix);
        String suffix = NFA.infixToSuffix(infix);
        System.out.println(suffix);

        System.out.println(NFA.reToNFA(expr));
    }

}
