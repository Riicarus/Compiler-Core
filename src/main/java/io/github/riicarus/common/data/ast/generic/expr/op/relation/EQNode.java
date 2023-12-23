package io.github.riicarus.common.data.ast.generic.expr.op.relation;

import io.github.riicarus.common.data.ast.generic.expr.op.abstruct.BinaryOpNode;

/**
 * 等于 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-18 17:48
 * @since 1.0.0
 */
public class EQNode extends BinaryOpNode {

    public EQNode() {
        super("==");
    }

}