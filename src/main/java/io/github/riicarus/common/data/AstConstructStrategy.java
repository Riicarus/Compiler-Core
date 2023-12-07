package io.github.riicarus.common.data;

import io.github.riicarus.front.syntax.SyntaxSymbol;

import java.util.HashMap;
import java.util.Map;

/**
 * AST 构建策略
 *
 * @author Riicarus
 * @create 2023-12-7 17:23
 * @since 1.0.0
 */
public class AstConstructStrategy {

    private final Map<SyntaxSymbol<?>, AstConstructor<?>> constructorStrategyMap = new HashMap<>();

    public <T> void add(SyntaxSymbol<T> symbol, AstConstructor<T> constructor) {
        constructorStrategyMap.put(symbol, constructor);
    }

    @SuppressWarnings("unchecked")  // 容器不对泛型进行任何操作, get 和 add 方法都使用相同的泛型类型.
    public <T> AstConstructor<T> get(SyntaxSymbol<T> symbol) {
        return (AstConstructor<T>) constructorStrategyMap.get(symbol);
    }

}
