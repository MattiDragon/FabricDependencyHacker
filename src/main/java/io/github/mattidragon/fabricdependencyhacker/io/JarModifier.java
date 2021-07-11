package io.github.mattidragon.fabricdependencyhacker.io;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class JarModifier {
    private ZipFile jar;
    private final File file;
    private boolean closed = false;
    
    public JarModifier(File file) {
        this.file = file;
    }
    
    public JsonObject readSettings() throws IOException {
        if (closed) throw new IllegalStateException("JarModifier is closed!");
        if (jar == null) jar = new ZipFile(file);
        
        ZipEntry entry = jar.getEntry("fabric.mod.json");
        try (InputStream in = jar.getInputStream(entry)) {
            try {
                return Json.parse(new InputStreamReader(in)).asObject();
            } catch (Exception e) {
                throw new IOException("fabric.mod.json is invalid!", e);
            }
        }
    }
    
    public void writeSettings(JsonObject json) throws IOException {
        if (closed) throw new IllegalStateException("JarModifier is closed!");
        if (jar == null) jar = new ZipFile(file);
        
        String data = json.toString();
        
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        String fileExtension = dotIndex != -1 ? fileName.substring(dotIndex) : "";
        String realName = dotIndex != -1 ? fileName.substring(0, dotIndex) : fileName;
        String path = file.getPath();
        int slashIndex = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
        String realPath = slashIndex == -1 ? "" : path.substring(0, slashIndex + 1);
        
        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(realPath + realName + "-hacked" + fileExtension))) {
            var entries = jar.entries();
            ZipEntry oldData = null;
            
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().equals("fabric.mod.json")) {
                    oldData = entry;
                    continue;
                }
                out.putNextEntry(new ZipEntry(entry));
                byte[] bytes = jar.getInputStream(entry).readAllBytes();
                out.write(bytes, 0, bytes.length);
                out.closeEntry();
            }
            
            out.putNextEntry(oldData == null ? new ZipEntry("fabric.mod.json") : new ZipEntry(oldData));
            byte[] bytes = data.getBytes();
            out.write(bytes, 0, bytes.length);
            out.closeEntry();
        }
    }
    
    public void close() throws IOException {
        closed = true;
        if (jar != null) jar.close();
    }
}
