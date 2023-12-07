package io.github.riicarus.common.data;

/**
 * 语法分析结果
 *
 * @author Riicarus
 * @create 2023-12-7 22:26
 * @since 1.0.0
 */
public class SyntaxParseResult {

    private final AbstractAST<?> ast;

    public SyntaxParseResult(AbstractAST<?> ast) {
        this.ast = ast;
    }

    public AbstractAST<?> getAst() {
        return ast;
    }

    @Override
    public String toString() {
        return "AST: \r\n" + ast;
    }
}
