package io.github.riicarus;

import io.github.riicarus.common.data.Token;
import io.github.riicarus.front.lex.Lexer;
import io.github.riicarus.front.lex.LexicalSymbol;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 编译器
 *
 * @author Riicarus
 * @create 2023-11-18 0:58
 * @since 1.0.0
 */
public class Compiler {

    static {
        System.out.println("Lexer DFA building...");
        long time = System.currentTimeMillis();
        LexicalSymbol.avoidLazyLoad();
        System.out.println("Lexer DFA build finished, time used: " + (System.currentTimeMillis() - time) + "ms");
    }

    private final Lexer lexer = new Lexer();

    public void compile(String dir, String name) {
        System.out.println("Compiling...");
        long timestamp = System.currentTimeMillis();

        lex(dir, name);

        System.out.println("Compile finished, time used: " + (System.currentTimeMillis() - timestamp) + "ms");
    }

    private void lex(String dir, String name) {
        System.out.println("Lexer parsing...");
        long timestamp = System.currentTimeMillis();
        String pasFilePath = dir + "/" + name + ".pas";
        String dydFilePath = dir + "/" + name + ".dys";
        try (
                BufferedReader reader = new BufferedReader(new FileReader(pasFilePath));
                BufferedWriter writer = new BufferedWriter(new FileWriter(dydFilePath, false))
        ) {

            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) lines.add(line);

            List<Token> tokenList = new ArrayList<>();
            lines.forEach(l -> tokenList.addAll(lexer.analyze(l.toCharArray())));

            StringBuilder dydSB = new StringBuilder();
            tokenList.forEach(t -> dydSB.append(t).append('\n'));
            writer.write(dydSB.toString());
            writer.flush();
            System.out.println("Lexer parse finished, time used: " + (System.currentTimeMillis() - timestamp) + "ms");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
