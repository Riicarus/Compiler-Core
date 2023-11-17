import io.github.riicarus.front.analyzer.lexical.DFA;
import io.github.riicarus.front.analyzer.lexical.NFA;
import org.junit.Test;

/**
 * @author Riicarus
 * @create 2023-11-17 15:28
 * @since 1.0.0
 */
public class DfaTest {

    @Test
    public void testNfaToDfa() {
        NFA nfa = NFA.reToNFA("a(b|c)*");
        DFA dfa = DFA.nfaToDfa(nfa, null);
        System.out.println(dfa);
    }

}
