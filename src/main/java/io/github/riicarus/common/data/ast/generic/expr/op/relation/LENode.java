package io.github.riicarus.common.data.ast.generic.expr.op.relation;

import io.github.riicarus.common.data.ast.generic.expr.op.abstruct.BinaryOpNode;

/**
 * 小于等于 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-18 17:44
 * @since 1.0.0
 */
public class LENode extends BinaryOpNode {

    public LENode() {
        super("<=");
    }

}
