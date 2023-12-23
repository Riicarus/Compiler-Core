package io.github.riicarus.common.data.ast.generic.expr.op.compute;

import io.github.riicarus.common.data.ast.generic.expr.op.abstruct.BinaryOpNode;

/**
 * 减法 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-18 0:13
 * @since 1.0.0
 */
public class MinusNode extends BinaryOpNode {

    public MinusNode() {
        super("-");
    }

}
