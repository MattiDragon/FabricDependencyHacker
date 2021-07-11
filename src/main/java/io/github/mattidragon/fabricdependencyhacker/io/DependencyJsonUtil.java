package io.github.mattidragon.fabricdependencyhacker.io;


import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import io.github.mattidragon.fabricdependencyhacker.dependency.Dependency;
import io.github.mattidragon.fabricdependencyhacker.dependency.DependencyType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class DependencyJsonUtil {
    public static JsonObject serialize(JsonObject src, EnumMap<DependencyType, List<Dependency>> map) {
        map.forEach((type, dependencies) -> {
            JsonObject data = new JsonObject();
            for (Dependency dependency : dependencies)
                data.add(dependency.getId(), dependency.getVersion());
            src.add(type.name.toLowerCase(), data);
        });
        return src;
    }
    
    public static EnumMap<DependencyType, List<Dependency>> deserialize(JsonObject json) {
        EnumMap<DependencyType, List<Dependency>> result = new EnumMap<>(DependencyType.class);
        
        for (DependencyType type : DependencyType.values()) {
            String name = type.name.toLowerCase();
            JsonValue value = json.get(name);
            if (value == null) continue;
            if (!(value instanceof JsonObject obj)) continue;
            
            List<Dependency> dependencies = new ArrayList<>();
            for (var entry : obj) {
                dependencies.add(new Dependency(entry.getName(), entry.getValue().asString()));
            }
            result.put(type, dependencies);
        }
        return result;
    }
}
