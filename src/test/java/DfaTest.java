import io.github.riicarus.front.lex.RegexParser;
import io.github.riicarus.front.lex.DFA;
import io.github.riicarus.front.lex.NFA;
import org.junit.Test;

import java.util.List;

/**
 * @author Riicarus
 * @create 2023-11-17 15:28
 * @since 1.0.0
 */
public class DfaTest {

    @Test
    public void testNfaToDfa() {
        NFA nfa = NFA.merge(List.of(RegexParser.reToNFA("a(b|c)*"), RegexParser.reToNFA("ab")));
        System.out.println(nfa);
        DFA dfa = DFA.nfaToDfa(nfa, null);
        System.out.println(dfa);
    }

    @Test
    public void testNumberDfa() {
        String regex = "(0|1|2|3|4|5|6|7|8|9)(0|1|2|3|4|5|6|7|8|9)*";
        NFA nfa = RegexParser.reToNFA(regex);
        System.out.println(DFA.nfaToDfa(nfa, null));
    }

    @Test
    public void testMinimizeDfa() {
        DFA dfa = DFA.nfaToDfa(NFA.merge(List.of(RegexParser.reToNFA("a(b|c)*"), RegexParser.reToNFA("ab"))), null);
        System.out.println(dfa);
        DFA miniminzedDfa = dfa.minimize();
        System.out.println(miniminzedDfa);
    }

    @Test
    public void testMinimizeComplexDfa() {
        String regex = "(0|1|2|3|4|5|6|7|8|9)(0|1|2|3|4|5|6|7|8|9)*";
        NFA nfa = RegexParser.reToNFA(regex);
        System.out.println(DFA.nfaToDfa(nfa, null).minimize());
    }

}
