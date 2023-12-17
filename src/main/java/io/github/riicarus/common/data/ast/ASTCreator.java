package io.github.riicarus.common.data.ast;

import java.util.List;
import java.util.function.Function;

/**
 * AST 构造器
 *
 * @author Riicarus
 * @create 2023-12-16 23:07
 * @since 1.0.0
 */
public interface ASTCreator<T extends ASTNode> extends Function<List<ASTNode>, T> {
}
