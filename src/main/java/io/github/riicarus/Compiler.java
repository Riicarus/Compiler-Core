package io.github.riicarus;

import io.github.riicarus.common.data.Token;
import io.github.riicarus.front.lex.Lexer;

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

    private final Lexer lexer;

    public Compiler(Lexer lexer) {
        this.lexer = lexer;
    }

    public void compile(String dir, String name, String srcSuffix, String lexSuffix) {
        System.out.println("Compiling...");
        long timestamp = System.currentTimeMillis();

        lex(dir, name, srcSuffix, lexSuffix);

        System.out.println("Compile finished, time used: " + (System.currentTimeMillis() - timestamp) + "ms");
    }

    private void lex(String dir, String name, String srcSuffix, String lexSuffix) {
        System.out.println("Lexer parsing...");
        long timestamp = System.currentTimeMillis();
        String srcFilePath = dir + "/" + name + "." + srcSuffix;
        String lexFilePath = dir + "/" + name + "." + lexSuffix;
        try (
                BufferedReader reader = new BufferedReader(new FileReader(srcFilePath));
                BufferedWriter writer = new BufferedWriter(new FileWriter(lexFilePath, false))
        ) {

            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) lines.add(line);

            List<Token> tokenList = new ArrayList<>();
            lines.forEach(l -> tokenList.addAll(lexer.parse(l.toCharArray())));

            StringBuilder dydSB = new StringBuilder();
            tokenList.forEach(t -> dydSB.append(t.toFileString()).append('\n'));
            writer.write(dydSB.toString());
            writer.flush();
            System.out.println("Lexer parse finished, time used: " + (System.currentTimeMillis() - timestamp) + "ms");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
