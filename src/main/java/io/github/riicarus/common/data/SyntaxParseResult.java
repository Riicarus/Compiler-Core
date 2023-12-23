package io.github.riicarus.common.data;

import io.github.riicarus.common.data.ast.generic.GenericASTNode;

/**
 * 语法分析结果
 *
 * @author Riicarus
 * @create 2023-12-7 22:26
 * @since 1.0.0
 */
public record SyntaxParseResult(GenericASTNode ast) {

    public String print(String prefix) {
        return prefix + "AST: \r\n" + ast.print(prefix + prefix);
    }
}
