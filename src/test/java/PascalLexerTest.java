import io.github.riicarus.front.lex.PascalLexer;
import org.junit.Test;

/**
 * @author Riicarus
 * @create 2023-11-8 0:15
 * @since 1.0.0
 */
public class PascalLexerTest {

    @Test
    public void testAnalyze() {
        PascalLexer pascalLexer = new PascalLexer();
        long time = System.currentTimeMillis();
        System.out.println(pascalLexer.parse("begin := F - * read(123) \r\n abc \n 123 \n end".toCharArray()));
        System.out.println(System.currentTimeMillis() - time);
    }

}
