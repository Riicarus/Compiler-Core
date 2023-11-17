import io.github.riicarus.Compiler;
import org.junit.Test;

/**
 * @author Riicarus
 * @create 2023-11-18 1:10
 * @since 1.0.0
 */
public class CompilerTest {

    @Test
    public void testCompiler() {
        Compiler compiler = new Compiler();
        compiler.compile("D:/tmp/compiler", "program");
    }

}
