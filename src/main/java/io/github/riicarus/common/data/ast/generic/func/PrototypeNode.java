package io.github.riicarus.common.data.ast.generic.func;

import io.github.riicarus.common.data.ast.generic.GenericASTNode;
import io.github.riicarus.common.data.ast.generic.block.CodeBlockNode;
import io.github.riicarus.common.data.ast.generic.expr.v.ValueType;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * <p>函数原型 AST 节点</p>
 * <p>函数定义会生成一个 PrototypeNode, 保存函数的类型信息, 包括:</p>
 * <li>参数列表</li>
 * <li>函数体</li>
 * <li>返回值</li>
 *
 * @author Riicarus
 * @create 2023-12-18 16:34
 * @since 1.0.0
 */
public class PrototypeNode extends GenericASTNode {

    public record ArgEntry(String name, ValueType type) {
    }

    protected final List<ArgEntry> args = new ArrayList<>();
    protected final CodeBlockNode body;
    protected final ValueType returnType;

    public PrototypeNode(List<ArgEntry> args, CodeBlockNode body, ValueType returnType) {
        super("Prototype");
        this.args.addAll(args);
        this.body = body;
        this.returnType = returnType;
    }

    @Override
    public String toTreeString(int level, String prefix) {
        StringBuilder sb = new StringBuilder();
        String t = "\t".repeat(Math.max(0, level - 1));
        String link = level == 0 ? "" : "|--- ";

        if (level != 0) {
            sb.append("\r\n");
        }

        // like: Prototype <ReturnType>(ArgType1 ArgName1, ArgType2, ArgName2, ...)
        sb.append(prefix).append(t).append(link)
                .append(name)
                .append(" <").append(returnType).append(">")
                .append("(").append(args.stream().map(e -> e.type() + " " + e.name()).collect(joining(", ")))
                .append(body.toTreeString(level + 1, prefix));

        return sb.toString();
    }

    @Override
    public ValueType getReturnType() {
        return returnType;
    }
}
