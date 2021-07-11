package io.github.mattidragon.fabricdependencyhacker.dependency;

public enum DependencyType {
    DEPENDS("Depends", "If no match is found the fabric loader will fail to load"),
    RECOMMENDS("Recommends", "If no match is found the fabric loader will log a warning"),
    SUGGESTS("Suggests", null),
    CONFLICTS("Conflicts", "If a match is found the fabric loader will log a warning"),
    BREAKS("Breaks", "If a match is found the fabric loader will fail to load");
    
    public final String name;
    public final String description;
    
    DependencyType(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    private DependencyType get(String name) {
        return valueOf(name.toUpperCase());
    }
}