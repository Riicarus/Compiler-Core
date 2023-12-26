package io.github.riicarus.common.data;

import io.github.riicarus.common.data.ast.generic.GenericASTNode;
import io.github.riicarus.common.data.table.SymbolTable;

/**
 * 语法分析结果
 *
 * @author Riicarus
 * @create 2023-12-7 22:26
 * @since 1.0.0
 */
public record SyntaxParseResult(GenericASTNode ast, SymbolTable table) {

    public String print(String prefix) {
        return prefix + "AST: \r\n"
                + ast.print(prefix + "\t")
                + "\r\n\r\n" + prefix + "SymbolTable: \r\n" +
                table.print(prefix + "\t");
    }
}
