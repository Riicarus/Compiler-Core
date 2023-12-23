package io.github.riicarus.common.data.ast.generic.type;

/**
 * void 类型 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-23 21:47
 * @since 1.0.0
 */
public class VoidTypeNode extends TypeNode {

    private static final VoidTypeNode INSTANCE = new VoidTypeNode();

    protected VoidTypeNode() {
        super("Void");
    }

    public static VoidTypeNode getInstance() {
        return INSTANCE;
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) {
            sb.append("\r\n");
        }

        // like: Void
        sb.append(prefix).append(t).append(link).append(name);

        return sb.toString();
    }
}
