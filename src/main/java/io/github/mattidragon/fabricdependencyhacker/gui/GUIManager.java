package io.github.mattidragon.fabricdependencyhacker.gui;

import com.eclipsesource.json.JsonObject;
import io.github.mattidragon.fabricdependencyhacker.dependency.Dependency;
import io.github.mattidragon.fabricdependencyhacker.dependency.DependencyType;
import io.github.mattidragon.fabricdependencyhacker.io.DependencyJsonUtil;
import io.github.mattidragon.fabricdependencyhacker.io.JarModifier;
import io.github.mattidragon.fabricdependencyhacker.util.LambdaAction;
import io.github.mattidragon.fabricdependencyhacker.util.Pair;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class GUIManager {
    private final EnumMap<DependencyType, List<Pair<Dependency, JButton>>> data = new EnumMap<>(DependencyType.class);
    private final JTable[] tables = new JTable[DependencyType.values().length];
    private JarModifier modifier;
    private JsonObject meta;
    
    public static void main(String[] args) {
        new GUIManager();
    }
    
    public GUIManager() {
        //Setup
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Failed to set look and feel!");
        }
        
        //Main Frame
        JFrame frame = new JFrame("FDH");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setResizable(false);
        try {
            frame.setIconImage(ImageIO.read(getClass().getResource("/icon.png")));
        } catch (Exception e) {
            System.err.println("Failed to set icon!");
            e.printStackTrace(System.err);
        }
        
        //Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new CardLayout());
        
        //The tabbed dependency list
        JTabbedPane tabs = new JTabbedPane();
        for (DependencyType type : DependencyType.values()) data.putIfAbsent(type, new ArrayList<>());
        addTabs(tabs, data);
        tabs.setFocusable(false);
        tabs.setVisible(false);
        
        //The text when no file is loaded
        JPanel noFilePanel = new JPanel(new BorderLayout());
        JLabel noFileLabel = new JLabel("No file opened...");
        noFileLabel.setVerticalAlignment(JLabel.CENTER);
        noFileLabel.setHorizontalAlignment(JLabel.CENTER);
        noFilePanel.add(noFileLabel, BorderLayout.CENTER);
        
        mainPanel.add(noFilePanel, "Disabled");
        mainPanel.add(tabs, "Enabled");
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
    
        
        //Toolbar
        JPanel toolbar = new JPanel();
        toolbar.setBackground(toolbar.getBackground().brighter());
    
        JButton saveButton = new JButton("Save");
        saveButton.setAction(new LambdaAction("Save" ,event -> {
            try {
                modifier.writeSettings(DependencyJsonUtil.serialize(meta, getDependencies()));
                modifier.close();
                saveButton.setEnabled(false);
                tabs.setVisible(false);
                ((CardLayout) mainPanel.getLayout()).show(mainPanel, "Disabled");
                EnumMap<DependencyType, List<Dependency>> map = new EnumMap<>(DependencyType.class);
                for (DependencyType type : DependencyType.values()) map.put(type, new ArrayList<>());
                setDependencies(map);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, e.toString(), "Error while saving!", JOptionPane.ERROR_MESSAGE);
            }
        }));
        saveButton.setEnabled(false);
        saveButton.setFocusPainted(false);
    
        JButton loadButton = new JButton("Load");
        loadButton.setAction(new LambdaAction("Load", event -> {
            try {
                if (modifier != null) modifier.close();
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File("").getAbsoluteFile());
                int result = fileChooser.showOpenDialog(frame);
    
                if (result == JFileChooser.APPROVE_OPTION) {
                    if (modifier != null) modifier.close();
                    modifier = new JarModifier(fileChooser.getSelectedFile());
                    meta = modifier.readSettings();
                    setDependencies(DependencyJsonUtil.deserialize(meta));
                }
                
                saveButton.setEnabled(true);
                tabs.setVisible(true);
                ((CardLayout) mainPanel.getLayout()).show(mainPanel, "Enabled");
                for (JTable table : tables) table.setEnabled(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, e.toString(), "Error while saving!", JOptionPane.ERROR_MESSAGE);
            }
        }));
        loadButton.setEnabled(true);
        loadButton.setFocusPainted(false);
    
        toolbar.add(saveButton);
        toolbar.add(loadButton);
        frame.getContentPane().add(BorderLayout.SOUTH, toolbar);
    
        frame.setVisible(true);
    }
    
    /**
     * Gets all the dependencies of the currently loaded mod
     * @return the dependencies of currently loaded mod
     */
    public EnumMap<DependencyType, List<Dependency>> getDependencies() {
        EnumMap<DependencyType, List<Dependency>> map = new EnumMap<>(DependencyType.class);
        for (var entry : data.entrySet()) {
            List<Dependency> dependencies = new ArrayList<>();
            for (var pair : entry.getValue()) {
                dependencies.add(pair.first());
            }
            map.put(entry.getKey(), dependencies);
        }
        return map;
    }
    
    public void setDependencies(EnumMap<DependencyType, List<Dependency>> map) {
        map.forEach((type, dependencies) -> {
            DependencyListTableModel model = (DependencyListTableModel) tables[type.ordinal()].getModel();
    
            List<Pair<Dependency, JButton>> rows = new ArrayList<>();
            
            for (Dependency dependency : dependencies) {
                rows.add(new Pair<>(dependency, model.createButton()));
            }
            
            model.setRows(rows);
        });
    }
    
    /**
     * Used to add the tabs for dependency types.
     * @param tabs The {@link JTabbedPane} that the tabs should be added to.
     * @param tabTypes The initial data for the tabs.
     */
    private void addTabs(JTabbedPane tabs, EnumMap<DependencyType, List<Pair<Dependency, JButton>>> tabTypes) {
        int i = 0;
        for (var entry : tabTypes.entrySet()) {
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            JTable table = new JTable(new DependencyListTableModel(entry.getValue()));
            
            table.setColumnSelectionAllowed(false);
            table.setRowSelectionAllowed(false);
            table.getTableHeader().setReorderingAllowed(false);
            table.getTableHeader().setResizingAllowed(false);
            table.setCellSelectionEnabled(false);
            
            table.setDefaultRenderer(JButton.class, new JTableButtonRenderer(table.getDefaultRenderer(JButton.class)));
            table.addMouseListener(new JTableButtonMouseListener(table));
    
            DefaultCellEditor editor = (DefaultCellEditor) table.getDefaultEditor(Object.class);
            editor.setClickCountToStart(1);
    
            JButton addButton = new JButton();
            addButton.setAction(new AbstractAction("New Dependency") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ((DependencyListTableModel)table.getModel()).addRow();
                }
            });
            addButton.setFocusPainted(false);
            
            tables[i++] = table;
            table.setEnabled(false);
            
            panel.add(BorderLayout.CENTER, new JScrollPane(table));
            panel.add(BorderLayout.SOUTH, addButton);
            
            tabs.addTab(entry.getKey().name, null, panel, entry.getKey().description);
        }
    }
}
