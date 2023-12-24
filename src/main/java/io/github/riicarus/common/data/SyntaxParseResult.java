package io.github.riicarus.common.data;

import io.github.riicarus.common.data.ast.generic.GenericASTNode;
import io.github.riicarus.common.data.table.ProcedureTable;
import io.github.riicarus.common.data.table.VariableTable;

/**
 * 语法分析结果
 *
 * @author Riicarus
 * @create 2023-12-7 22:26
 * @since 1.0.0
 */
public record SyntaxParseResult(GenericASTNode ast, VariableTable vt, ProcedureTable pt) {

    public String print(String prefix) {
        return prefix + "AST: \r\n"
                +  ast.print(prefix + prefix)
                + "\r\n\r\n" + prefix + "VariableTable: \r\n" + vt.print(prefix + prefix)
                + "\r\n\r\n" + prefix + "ProcedureTable: \r\n" + pt.print(prefix + prefix);
    }
}
