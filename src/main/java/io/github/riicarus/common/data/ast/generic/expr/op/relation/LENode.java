package io.github.riicarus.common.data.ast.generic.expr.op.relation;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.ast.generic.expr.op.abstruct.BinaryOpNode;
import io.github.riicarus.common.data.ast.generic.expr.v.ValueType;

import static io.github.riicarus.common.data.ast.generic.expr.v.ValueType.BOOLEAN;
import static io.github.riicarus.common.data.ast.generic.expr.v.ValueType.INTEGER;

/**
 * 小于等于 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-18 17:44
 * @since 1.0.0
 */
public class LENode extends BinaryOpNode {

    protected LENode(ValueType leftOperandType, ValueType rightOperandType,
                     ExprNode leftOperand, ExprNode rightOperand) {
        super(
                "<=",
                leftOperandType, rightOperandType,
                BOOLEAN,
                leftOperand, rightOperand
        );
    }

    public LENode newIntegerTypeInstance(ExprNode leftOperand, ExprNode rightOperand) {
        return new LENode(INTEGER, INTEGER, leftOperand, rightOperand);
    }
}
