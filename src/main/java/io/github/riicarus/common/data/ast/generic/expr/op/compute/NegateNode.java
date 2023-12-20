package io.github.riicarus.common.data.ast.generic.expr.op.compute;

import io.github.riicarus.common.data.ast.generic.expr.ExprNode;
import io.github.riicarus.common.data.ast.generic.expr.op.abstruct.UnaryOpNode;
import io.github.riicarus.common.data.ast.generic.expr.v.ValueType;

import static io.github.riicarus.common.data.ast.generic.expr.v.ValueType.BOOLEAN;

/**
 * 取反 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-18 0:17
 * @since 1.0.0
 */
public class NegateNode extends UnaryOpNode {

    protected NegateNode(ValueType operandType, ValueType returnType, ExprNode operand) {
        super("!", operandType, returnType, operand);
    }

    public NegateNode newBooleanTypeInstance(ExprNode operand) {
        return new NegateNode(BOOLEAN, BOOLEAN, operand);
    }

}
