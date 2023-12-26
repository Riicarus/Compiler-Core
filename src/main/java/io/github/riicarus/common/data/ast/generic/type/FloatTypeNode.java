package io.github.riicarus.common.data.ast.generic.type;

import io.github.riicarus.common.data.table.type.FloatType;

/**
 * Float 类型 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-22 22:22
 * @since 1.0.0
 */
public class FloatTypeNode extends TypeNode {

    public FloatTypeNode() {
        super("FLOAT");
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) {
            sb.append("\r\n");
        }

        // like: Float
        sb.append(prefix).append(t).append(link).append(name).append("\t\t").append(getScopeName());

        return sb.toString();
    }

    @Override
    public FloatType getVarType() {
        return new FloatType();
    }
}
