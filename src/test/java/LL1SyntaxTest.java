import io.github.riicarus.front.syntax.SyntaxDefinition;
import io.github.riicarus.front.syntax.ll1.LL1SyntaxDefinition;
import org.junit.Test;

/**
 * @author Riicarus
 * @create 2023-11-23 15:25
 * @since 1.0.0
 */
public class LL1SyntaxTest {

    @Test
    public void testLL1SyntaxDefineLoad() {
        SyntaxDefinition definition = new LL1SyntaxDefinition();
        definition.loadFrom("D:/tmp/compiler/program.syn");
    }

}
