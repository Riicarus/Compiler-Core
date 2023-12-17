package io.github.riicarus.front.syntax;

import io.github.riicarus.common.data.ast.ASTCreator;
import io.github.riicarus.common.data.ast.ASTNode;

import java.util.List;

/**
 * 文法产生式
 *
 * @author Riicarus
 * @create 2023-11-23 16:05
 * @since 1.0.0
 */
public interface SyntaxProduction<T extends ASTNode> {

    /**
     * 获取产生式左部.
     *
     * @return 产生式左部符号
     */
    SyntaxSymbol getLHS();

    /**
     * 获取产生式右部.
     *
     * @return 产生式右部的符号集合
     */
    List<SyntaxSymbol> getRHS();

    /**
     * 获取产生式对应的 AST 构造器.
     *
     * @return AST 构造器
     */
    ASTCreator<T> getASTCreator();

    /**
     * 创建 AST 节点
     *
     * @param children 子节点列表
     * @return AST 节点
     */
    T createNode(List<ASTNode> children);

}
