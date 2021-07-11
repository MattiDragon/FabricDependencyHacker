package io.github.mattidragon.fabricdependencyhacker.dependency;

public class Dependency {
    private String id;
    private String version;
    
    public Dependency(String id, String version) {
        this.id = id;
        this.version = version;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
}
