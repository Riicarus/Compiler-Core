package io.github.riicarus.common.data.ast;

/**
 * AST 节点
 *
 * @author Riicarus
 * @create 2023-12-17 23:15
 * @since 1.0.0
 */
public abstract class ASTNode {

    /**
     * 层级打印 AST 树
     *
     * @param level 层数
     * @param prefix 每一行的前缀
     * @return AST 树打印结果
     */
    public abstract String toTreeString(int level, String prefix);

    /**
     * 以当前节点为根, 层级打印 AST 树
     *
     * @param prefix 每一行的前缀
     * @return AST 树打印结果
     */
    public final String print(String prefix) {
        return toTreeString(0, prefix);
    }

}
