package io.github.riicarus.common.data.ast.generic.expr.func;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.ast.generic.expr.v.ValueType;

import java.util.List;

/**
 * <p>函数调用 AST 节点</p>
 * <p>根据函数名称和参数列表调用函数, 包括以下内容:</p>
 * <li>函数名称--String</li>
 * <li>参数列表--List&lt;ExprNode&gt;</li>
 * </br>
 * <p>函数调用的返回值由函数名称和参数列表共同决定, 在符号表中找到对应的函数定义, 然后返回返回值</p>
 *
 * @author Riicarus
 * @create 2023-12-18 0:38
 * @since 1.0.0
 */
public class FunctionCallNode extends ExprNode {

    protected final String funcName;
    protected final List<ExprNode> args;

    public FunctionCallNode(String funcName, List<ExprNode> args) {
        super("FuncCall");
        this.funcName = funcName;
        this.args = args;
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) {
            sb.append("\r\n");
        }

        // like: FuncCall <ReturnType>(FuncName)
        sb.append(prefix).append(t).append(link)
                .append(name)
                .append(" <").append(getReturnType()).append(">")
                .append("(").append(funcName).append(")");
        args.forEach(n -> sb.append(n.toTreeString(level + 1, prefix)));

        return sb.toString();
    }

    @Override
    public ValueType getReturnType() {
        // TODO: 从符号表中找到函数原型, 获取返回类型
        return null;
    }
}
