package io.github.riicarus.common.data.ast.generic.expr.op.relation;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.ast.generic.expr.op.abstruct.BinaryOpNode;
import io.github.riicarus.common.data.ast.generic.expr.v.ValueType;

import static io.github.riicarus.common.data.ast.generic.expr.v.ValueType.BOOLEAN;
import static io.github.riicarus.common.data.ast.generic.expr.v.ValueType.INTEGER;

/**
 * 大于等于 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-18 17:46
 * @since 1.0.0
 */
public class GENode extends BinaryOpNode {

    protected GENode(ValueType leftOperandType, ValueType rightOperandType,
                     ExprNode leftOperand, ExprNode rightOperand) {
        super(
                ">=",
                leftOperandType, rightOperandType,
                BOOLEAN,
                leftOperand, rightOperand
        );
    }

    public GENode newIntegerTypeInstance(ExprNode leftOperand, ExprNode rightOperand) {
        return new GENode(INTEGER, INTEGER, leftOperand, rightOperand);
    }
}