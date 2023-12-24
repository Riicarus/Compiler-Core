package io.github.riicarus.common.data.table;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;

/**
 * 变量名表
 *
 * @author Riicarus
 * @create 2023-12-24 9:52
 * @since 1.0.0
 */
public class VariableTable {

    private final Map<String, Map<Integer, VariableInfo>> variableMap = new HashMap<>();

    public void addVariable(VariableInfo variableInfo) {
        variableMap.putIfAbsent(variableInfo.name(), new HashMap<>());
        variableMap.get(variableInfo.name()).put(variableInfo.level(), variableInfo);
    }

    public VariableInfo getVariable(String name, int level) {
        Map<Integer, VariableInfo> map = variableMap.get(name);

        return map == null ? null : map.get(level);
    }

    public String print(String prefix) {
        return prefix + variableMap.values().stream()
                .flatMap(m -> m.values().stream().map(VariableInfo::toString)).collect(joining("\r\n" + prefix));
    }
}
