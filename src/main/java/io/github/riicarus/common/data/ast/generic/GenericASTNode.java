package io.github.riicarus.common.data.ast.generic;

import io.github.riicarus.common.data.ast.ASTNode;
import io.github.riicarus.common.data.ast.generic.expr.v.ValueType;

/**
 * 通用型 AST 节点
 *
 * @author Riicarus
 * @create 2023-12-17 23:12
 * @since 1.0.0
 */
public abstract class GenericASTNode extends ASTNode {

    protected final String name;

    public GenericASTNode(String name) {
        this.name = name;
    }

    public abstract ValueType getReturnType();
}
