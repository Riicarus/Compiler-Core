package io.github.riicarus.common.data;

import io.github.riicarus.common.data.ast.detailed.DetailedASTNode;

/**
 * 语法分析结果
 *
 * @author Riicarus
 * @create 2023-12-7 22:26
 * @since 1.0.0
 */
public class SyntaxParseResult {

    private final DetailedASTNode ast;

    public SyntaxParseResult(DetailedASTNode ast) {
        this.ast = ast;
    }

    public DetailedASTNode getAst() {
        return ast;
    }

    public String print(String prefix) {
        return prefix + "AST: \r\n" + ast.print(prefix + prefix);
    }
}
