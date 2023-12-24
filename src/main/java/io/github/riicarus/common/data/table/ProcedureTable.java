package io.github.riicarus.common.data.table;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;

/**
 * 过程名表
 *
 * @author Riicarus
 * @create 2023-12-24 10:55
 * @since 1.0.0
 */
public class ProcedureTable {

    private final Map<String, Map<Integer, ProcedureInfo>> procedureMap = new HashMap<>();

    public void addProcedure(ProcedureInfo procedureInfo) {
        procedureMap.putIfAbsent(procedureInfo.name(), new HashMap<>());
        procedureMap.get(procedureInfo.name()).put(procedureInfo.level(), procedureInfo);
    }

    public ProcedureInfo getProcedure(String name, int level) {
        Map<Integer, ProcedureInfo> map = procedureMap.get(name);

        return map == null ? null : map.get(level);
    }

    public String print(String prefix) {
        return prefix + procedureMap.values().stream()
                .flatMap(m -> m.values().stream().map(ProcedureInfo::toString)).collect(joining("\r\n" + prefix));
    }

}
