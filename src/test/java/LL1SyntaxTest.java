import io.github.riicarus.front.lex.Lexer;
import io.github.riicarus.front.lex.PascalLexer;
import io.github.riicarus.front.syntax.SyntaxSymbol;
import io.github.riicarus.front.syntax.Syntaxer;
import io.github.riicarus.front.syntax.ll1.*;
import org.junit.Test;

import java.util.List;
import java.util.Set;

/**
 * @author Riicarus
 * @create 2023-11-23 15:25
 * @since 1.0.0
 */
public class LL1SyntaxTest {

    @Test
    public void testLL1SyntaxDefineLoad() {
        try {
            new LL1SyntaxFileDefiner("D:/tmp/compiler/program_lcf.syn");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLL1SyntaxWithLeftRecursion() {
        try {
            new LL1SyntaxFileDefiner("D:/tmp/compiler/program_lcf.syn");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            new LL1SyntaxFileDefiner("D:/tmp/compiler/program_lcf.syn");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLL1SyntaxWithLeftCommonFactor() {
        try {
            new LL1SyntaxFileDefiner("D:/tmp/compiler/program_lcf.syn");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            new LL1SyntaxFileDefiner("D:/tmp/compiler/program_lcf_s.syn");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLL1SyntaxParse() {
        Syntaxer syntaxer = new LL1Syntaxer(new LL1SyntaxFileDefiner("D:/tmp/compiler/program.syn"));
        Lexer lexer = new PascalLexer();
        syntaxer.parse(lexer.parse("3 - 4 * 5 - 7 * 8 - 0 ".toCharArray()), lexer.getAssistantLexSymbolSet());
    }

    @Test
    public void testLL1SyntaxInlineDefinerParse() {
        SyntaxSymbol startSymbol = new LL1SyntaxSymbol("E", false);

        final LL1SyntaxInlineDefiner definer = new LL1SyntaxInlineDefiner(
                new LL1SyntaxSymbol("eps", true),
                startSymbol
        );
        definer.addTerminalSymbols(Set.of("eps", "*", "-", "constant"));
        definer.addNonterminalSymbols(Set.of("E", "E'", "T", "T'", "F"));

        definer.addProduction("E", List.of("T", "E'"));
        definer.addProduction("E'", List.of("-", "T", "E'"));
        definer.addProduction("E'", List.of("eps"));
        definer.addProduction("T", List.of("F", "T'"));
        definer.addProduction("T'", List.of("*", "F", "T'"));
        definer.addProduction("T'", List.of("eps"));
        definer.addProduction("F", List.of("constant"));

        Syntaxer pascalSyntaxer = new LL1Syntaxer(definer);

        Lexer lexer = new PascalLexer();
        pascalSyntaxer.parse(lexer.parse("3 - 4 * 5 - 7 * 8 - 0 ".toCharArray()), lexer.getAssistantLexSymbolSet());

    }
}
