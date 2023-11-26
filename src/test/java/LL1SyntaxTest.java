import io.github.riicarus.front.lex.PascalLexer;
import io.github.riicarus.front.syntax.Syntaxer;
import io.github.riicarus.front.syntax.ll1.LL1SyntaxDefinition;
import io.github.riicarus.front.syntax.ll1.LL1Syntaxer;
import org.junit.Test;

/**
 * @author Riicarus
 * @create 2023-11-23 15:25
 * @since 1.0.0
 */
public class LL1SyntaxTest {

    @Test
    public void testLL1SyntaxDefineLoad() {
        try {
            new LL1SyntaxDefinition().loadFrom("D:/tmp/compiler/program.syn");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLL1SyntaxWithLeftRecursion() {
        try {
            new LL1SyntaxDefinition().loadFrom("D:/tmp/compiler/program_lr.syn");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            new LL1SyntaxDefinition().loadFrom("D:/tmp/compiler/program_lr_s.syn");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLL1SyntaxWithLeftCommonFactor() {
        try {
            new LL1SyntaxDefinition().loadFrom("D:/tmp/compiler/program_lcf.syn");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            new LL1SyntaxDefinition().loadFrom("D:/tmp/compiler/program_lcf_s.syn");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLL1SyntaxParse() {
        Syntaxer syntaxer = new LL1Syntaxer();
        syntaxer.parse(new PascalLexer().parse("3 - 4 * 5".toCharArray()), "D:/tmp/compiler", "program", "syn", "dys");
    }
}
