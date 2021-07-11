package io.github.mattidragon.fabricdependencyhacker.gui;

import io.github.mattidragon.fabricdependencyhacker.dependency.Dependency;
import io.github.mattidragon.fabricdependencyhacker.util.Pair;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Optional;

public class DependencyListTableModel extends AbstractTableModel {
    private final String[] columnNames = {"Mod", "Version", ""};
    private List<Pair<Dependency, JButton>> rows;
    
    public DependencyListTableModel(List<Pair<Dependency, JButton>> rowdata) {
        rows = rowdata;
    }
    
    public JButton createButton() {
        JButton button = new JButton();
        
        button.setAction(new AbstractAction("Remove") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Optional<Pair<Dependency, JButton>> currentRow = rows.stream()
                        .filter(row -> row.second() == button).findFirst();
                if (currentRow.isEmpty()) return;
                
                int index = rows.indexOf(currentRow.get());
                
                rows.remove(index);
                fireTableRowsDeleted(index, index);
            }
        });
        
        return button;
    }
    
    public void setRows(List<Pair<Dependency, JButton>> rows) {
        fireTableRowsDeleted(0, this.rows.size());
        this.rows = rows;
        fireTableRowsInserted(0, rows.size());
    }
    
    public void addRow() {
        rows.add(new Pair<>(new Dependency("", ""), createButton()));
        fireTableRowsInserted(rows.size()-1, rows.size()-1);
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 2;
    }
    @Override
    public int getRowCount() {
        return rows.size();
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        var row = rows.get(rowIndex);
        switch (columnIndex) {
            case 0 -> row.first().setId(aValue.toString());
            case 1 -> row.first().setVersion(aValue.toString());
        }
    }
    
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var row = rows.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> row.first().getId();
            case 1 -> row.first().getVersion();
            case 2 -> row.second();
            default -> null;
        };
    }
    
    public Class<?> getColumnClass(int column) {
        if (rows.isEmpty()) return Object.class;
        var row = rows.get(0);
        Object value = switch (column) {
            case 0 -> row.first().getId();
            case 1 -> row.first().getVersion();
            case 2 -> row.second();
            default -> null;
        };
        return value != null ? value.getClass() : Object.class;
    }
}
