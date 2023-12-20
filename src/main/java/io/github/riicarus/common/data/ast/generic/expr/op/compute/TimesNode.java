package io.github.riicarus.common.data.ast.generic.expr.op.compute;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.ast.generic.expr.op.abstruct.BinaryOpNode;
import io.github.riicarus.common.data.ast.generic.expr.v.ValueType;

import static io.github.riicarus.common.data.ast.generic.expr.v.ValueType.INTEGER;

/**
 * 乘法 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-18 0:14
 * @since 1.0.0
 */
public class TimesNode extends BinaryOpNode {

    protected TimesNode(ValueType leftOperandType, ValueType rightOperandType, ValueType returnType,
                     ExprNode leftOperand, ExprNode rightOperand) {
        super(
                "*",
                leftOperandType, rightOperandType, returnType,
                leftOperand, rightOperand
        );
    }

    public TimesNode newIntegerTypeInstance(ExprNode leftOperand, ExprNode rightOperand) {
        return new TimesNode(INTEGER, INTEGER, INTEGER, leftOperand, rightOperand);
    }

}
