import io.github.riicarus.Compiler;
import io.github.riicarus.front.lex.PascalLexer;
import org.junit.Test;

/**
 * @author Riicarus
 * @create 2023-11-18 1:10
 * @since 1.0.0
 */
public class CompilerTest {

    @Test
    public void testCompiler() {
        Compiler compiler = new Compiler(new PascalLexer());

        int loopCnt = 100;
        long t0 = System.currentTimeMillis();
        for (int i = 1; i < loopCnt; i++) {
            compiler.compile("D:/tmp/compiler", "program", "pas", "dys");
        }

        long t1 = System.currentTimeMillis();
        System.out.println("Test time used: " + (t1 - t0) + "ms , loops: " + loopCnt + ", avgTime: " + (t1 - t0) / loopCnt + " ms.");
    }

}
