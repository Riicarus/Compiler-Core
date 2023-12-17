package io.github.riicarus.common.data.ast;

import io.github.riicarus.front.syntax.SyntaxSymbol;

/**
 * 终结符 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-17 3:56
 * @since 1.0.0
 */
public class TerminalASTNode extends ASTNode {

    private TerminalASTNode() {
    }

    public TerminalASTNode(SyntaxSymbol symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) {
            sb.append("\r\n");
        }

        sb.append(prefix).append(t).append(link).append(symbol);

        return sb.toString();
    }
}
