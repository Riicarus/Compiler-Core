package io.github.riicarus.common.data.ast.generic.expr.op.compute;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.ast.generic.expr.op.abstruct.BinaryOpNode;
import io.github.riicarus.common.data.ast.generic.expr.v.ValueType;

import static io.github.riicarus.common.data.ast.generic.expr.v.ValueType.INTEGER;

/**
 * 减法 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-18 0:13
 * @since 1.0.0
 */
public class MinusNode extends BinaryOpNode {

    protected MinusNode(ValueType leftOperandType, ValueType rightOperandType, ValueType returnType,
                       ExprNode leftOperand, ExprNode rightOperand) {
        super(
                "-",
                leftOperandType, rightOperandType, returnType,
                leftOperand, rightOperand
        );
    }

    public MinusNode newIntegerTypeInstance(ExprNode leftOperand, ExprNode rightOperand) {
        return new MinusNode(INTEGER, INTEGER, INTEGER, leftOperand, rightOperand);
    }

}
