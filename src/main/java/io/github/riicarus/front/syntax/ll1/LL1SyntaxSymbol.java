package io.github.riicarus.front.syntax.ll1;

import io.github.riicarus.front.syntax.SyntaxSymbol;
import io.github.riicarus.front.syntax.SyntaxSymbolType;

import java.util.Objects;

/**
 * LL1 文法符号.
 *
 * @author Riicarus
 * @create 2023-11-22 22:24
 * @since 1.0.0
 */
public class LL1SyntaxSymbol implements SyntaxSymbol {

    private final String name;
    private final SyntaxSymbolType type;
    private final boolean isTerminal;

    public LL1SyntaxSymbol(String name, SyntaxSymbolType type, boolean isTerminal) {
        this.name = name;
        this.type = type;
        this.isTerminal = isTerminal;
    }

    public LL1SyntaxSymbol(String name, SyntaxSymbolType type) {
        this.name = name;
        this.type = type;
        this.isTerminal = false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SyntaxSymbolType getType() {
        return type;
    }

    @Override
    public boolean isTerminal() {
        return isTerminal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LL1SyntaxSymbol that = (LL1SyntaxSymbol) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name + "(" + type + ", " + isTerminal + ")";
    }
}
