package io.github.riicarus.common.data.ast.generic.type;

/**
 * String 类型 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-22 22:23
 * @since 1.0.0
 */
public class StringTypeNode extends TypeNode {

    private static final StringTypeNode INSTANCE = new StringTypeNode();

    protected StringTypeNode() {
        super("String");
    }

    public static StringTypeNode getInstance() {
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

        // like: String
        sb.append(prefix).append(t).append(link).append(name);

        return sb.toString();
    }
}
