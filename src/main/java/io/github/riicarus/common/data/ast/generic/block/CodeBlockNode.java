package io.github.riicarus.common.data.ast.generic.block;

import io.github.riicarus.common.data.ast.generic.GenericASTNode;
import io.github.riicarus.common.data.ast.generic.expr.v.ValueType;

import java.util.ArrayList;
import java.util.List;

import static io.github.riicarus.common.data.ast.generic.expr.v.ValueType.VOID;

/**
 * 代码块 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-18 20:39
 * @since 1.0.0
 */
public class CodeBlockNode extends GenericASTNode {

    protected static final ValueType TYPE = VOID;
    protected final List<GenericASTNode> codes = new ArrayList<>();

    public CodeBlockNode(List<GenericASTNode> codes) {
        super("CodeBlock");
        this.codes.addAll(codes);
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) {
            sb.append("\r\n");
        }

        // like: CodeBlock
        sb.append(prefix).append(t).append(link).append(name);
        codes.forEach(n -> sb.append(n.toTreeString(level + 1, prefix)));

        return sb.toString();
    }

    @Override
    public ValueType getReturnType() {
        return TYPE;
    }
}
