package io.github.riicarus.common.data.ast.generic.expr.op.relation;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.ast.generic.expr.op.abstruct.BinaryOpNode;
import io.github.riicarus.common.data.ast.generic.expr.v.ValueType;

import static io.github.riicarus.common.data.ast.generic.expr.v.ValueType.BOOLEAN;
import static io.github.riicarus.common.data.ast.generic.expr.v.ValueType.INTEGER;

/**
 * 大于 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-18 17:41
 * @since 1.0.0
 */
public class GTNode extends BinaryOpNode {

    protected GTNode(ValueType leftOperandType, ValueType rightOperandType,
                     ExprNode leftOperand, ExprNode rightOperand) {
        super(
                ">",
                leftOperandType, rightOperandType,
                BOOLEAN,
                leftOperand, rightOperand
        );
    }

    public GTNode newIntegerTypeInstance(ExprNode leftOperand, ExprNode rightOperand) {
        return new GTNode(INTEGER, INTEGER, leftOperand, rightOperand);
    }
}