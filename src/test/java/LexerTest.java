import io.github.riicarus.front.lex.Lexer;
import org.junit.Test;

/**
 * @author Riicarus
 * @create 2023-11-8 0:15
 * @since 1.0.0
 */
public class LexerTest {

    @Test
    public void testAnalyze() {
        Lexer lexer = new Lexer();
        long time = System.currentTimeMillis();
        System.out.println(lexer.analyze("begin := F - * read(123) \r\n abc \n 123 \n end".toCharArray()));
        System.out.println(System.currentTimeMillis() - time);
    }

}
