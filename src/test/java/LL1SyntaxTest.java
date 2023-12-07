import io.github.riicarus.front.syntax.ll1.LL1SyntaxFileDefiner;
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
}
