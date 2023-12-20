package io.github.riicarus.common.data.ast;

import io.github.riicarus.common.data.ast.detailed.DetailedASTNode;

import java.util.List;
import java.util.function.Function;

/**
 * AST 构造器
 *
 * @author Riicarus
 * @create 2023-12-16 23:07
 * @since 1.0.0
 */
public interface DetailedASTCreator<T extends DetailedASTNode> extends Function<List<DetailedASTNode>, T> {
}
