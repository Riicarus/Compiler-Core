package io.github.riicarus.common.data;

import io.github.riicarus.common.data.ast.generic.GenericASTNode;
import io.github.riicarus.common.data.table.SymbolTable;
import io.github.riicarus.common.data.table.VarKind;

import java.util.Objects;

/**
 * 语法分析结果
 *
 * @author Riicarus
 * @create 2023-12-7 22:26
 * @since 1.0.0
 */
public final class SyntaxParseResult {
    private final GenericASTNode ast;
    private final SymbolTable table = new SymbolTable();

    public SyntaxParseResult(GenericASTNode ast) {
        this.ast = ast;
        ast.updateTable(table, VarKind.VARIABLE);
        table.finishScan();
    }

    public String print(String prefix) {
        return prefix + "AST: \r\n"
                + ast.print(prefix + "\t")
                + "\r\n\r\n" + prefix + "SymbolTable: \r\n" +
                table.print(prefix + "\t");
    }

    public GenericASTNode getAST() {
        return ast;
    }

    public SymbolTable getTable() {
        return table;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SyntaxParseResult) obj;
        return Objects.equals(this.ast, that.ast) &&
                Objects.equals(this.table, that.table);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ast, table);
    }

    @Override
    public String toString() {
        return print("");
    }

}
