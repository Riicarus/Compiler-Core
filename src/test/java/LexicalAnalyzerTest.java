import io.github.riicarus.front.analyzer.lexical.LexicalAnalyzer;
import org.junit.Test;

/**
 * @author Riicarus
 * @create 2023-11-8 0:15
 * @since 1.0.0
 */
public class LexicalAnalyzerTest {

    @Test
    public void testAnalyze() {
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
        System.out.println(lexicalAnalyzer.analyze("begin read(123) \r\n abc \n 123 \n end".toCharArray()));
    }

}
