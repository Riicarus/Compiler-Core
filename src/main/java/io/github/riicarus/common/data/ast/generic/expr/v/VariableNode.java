package io.github.riicarus.common.data.ast.generic.expr.v;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;

/**
 * <p>变量 AST 节点</p>
 * <p>包含各种变量, 如: int, bool, function .etc</p>
 *
 * @author Riicarus
 * @create 2023-12-17 23:31
 * @since 1.0.0
 */
public class VariableNode extends ExprNode {

    protected final String varName;
    protected final ValueType type;

    public VariableNode(String varName, ValueType type) {
        super("Var");
        this.varName = varName;
        this.type = type;
    }

    @Override
    public final String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) {
            sb.append("\r\n");
        }

        // like: Var <Type>(VarName), eg: Var <Func>(FuncName)
        sb.append(prefix).append(t).append(link)
                .append(name)
                .append(" <").append(type).append(">")
                .append("(").append(varName).append(")");

        return sb.toString();
    }

    @Override
    public final ValueType getReturnType() {
        return type;
    }

}
