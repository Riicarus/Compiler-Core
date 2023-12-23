package io.github.riicarus.common.data.ast.generic.expr.op.relation;

import io.github.riicarus.common.data.ast.generic.expr.op.abstruct.BinaryOpNode;

/**
 * 小于 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-18 1:16
 * @since 1.0.0
 */
public class LTNode extends BinaryOpNode {

    public LTNode() {
        super("<");
    }

}
