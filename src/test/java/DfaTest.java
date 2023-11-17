import io.github.riicarus.front.lexer.LexicalSymbol;
import io.github.riicarus.common.data.Token;
import io.github.riicarus.front.lexer.DFA;
import io.github.riicarus.front.lexer.NFA;
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
        NFA nfa = NFA.merge(List.of(NFA.reToNFA("a[b|c]^"), NFA.reToNFA("ab")));
        System.out.println(nfa);
        DFA dfa = DFA.nfaToDfa(nfa, null);
        System.out.println(dfa);
    }

    @Test
    public void testNumberDfa() {
        String regex = "[0|1|2|3|4|5|6|7|8|9)(0|1|2|3|4|5|6|7|8|9]^";
        NFA nfa = NFA.reToNFA(regex);
        System.out.println(DFA.nfaToDfa(nfa, null));
    }

    @Test
    public void testMinimizeDfa() {
        DFA dfa = DFA.nfaToDfa(NFA.merge(List.of(NFA.reToNFA("a[b|c]^"), NFA.reToNFA("ab"))), null);
        System.out.println(dfa);
        DFA miniminzedDfa = dfa.minimize();
        System.out.println(miniminzedDfa);
    }

    @Test
    public void testMinimizeComplexDfa() {
        String regex = "[0|1|2|3|4|5|6|7|8|9)(0|1|2|3|4|5|6|7|8|9]^";
        NFA nfa = NFA.reToNFA(regex);
        System.out.println(DFA.nfaToDfa(nfa, null).minimize());
    }

    @Test
    public void testValidateString() {
        String regex = "[0|1|2|3|4|5|6|7|8|9)(0|1|2|3|4|5|6|7|8|9]^";
        NFA nfa = NFA.reToNFA(regex);
        DFA numberDfa = DFA.nfaToDfa(nfa, null).minimize();
        System.out.println(numberDfa.validateString("123c4"));

        Token target = null;
        for (LexicalSymbol symbol : LexicalSymbol.values()) {
            final Token token = symbol.validate("\r\n");

            if (token == null) continue;

            if (target == null) target = token;
            else if (target.getLen() < token.getLen()) target = token;
        }

        System.out.println(target);
    }

}
