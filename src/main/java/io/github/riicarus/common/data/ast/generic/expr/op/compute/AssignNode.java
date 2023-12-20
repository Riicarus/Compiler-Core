package io.github.riicarus.common.data.ast.generic.expr.op.compute;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.ast.generic.expr.op.abstruct.BinaryOpNode;
import io.github.riicarus.common.data.ast.generic.expr.v.ValueType;
import io.github.riicarus.common.data.ast.generic.expr.v.VariableNode;

import static io.github.riicarus.common.data.ast.generic.expr.v.ValueType.INTEGER;
import static io.github.riicarus.common.data.ast.generic.expr.v.ValueType.VOID;

/**
 * <p>赋值 AST 节点</p>
 * <p>将一个 表达式 的值赋值给一个 变量</p>
 * <p>VariableNode := ExprNode</p>
 * <p></p>
 *
 * @author Riicarus
 * @create 2023-12-18 1:06
 * @since 1.0.0
 */
public class AssignNode extends BinaryOpNode {
    protected AssignNode(ValueType leftOperandType, ValueType rightOperandType,
                         VariableNode leftOperand, ExprNode rightOperand) {
        super(
                ":=",
                leftOperandType, rightOperandType,
                VOID,
                leftOperand, rightOperand
        );
    }

    public AssignNode newIntegerTypeInstance(VariableNode leftOperand, ExprNode rightOperand) {
        return new AssignNode(INTEGER, INTEGER, leftOperand, rightOperand);
    }
}
