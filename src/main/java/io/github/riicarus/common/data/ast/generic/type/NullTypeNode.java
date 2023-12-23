package io.github.riicarus.common.data.ast.generic.type;

/**
 * Null 类型 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-22 22:35
 * @since 1.0.0
 */
public class NullTypeNode extends TypeNode {

    private static final NullTypeNode INSTANCE = new NullTypeNode();

    protected NullTypeNode() {
        super("Null");
    }

    public static NullTypeNode getInstance() {
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

        // like: Null
        sb.append(prefix).append(t).append(link).append(name);

        return sb.toString();
    }
}
