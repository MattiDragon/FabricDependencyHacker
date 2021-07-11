package io.github.mattidragon.fabricdependencyhacker.gui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class JTableButtonRenderer implements TableCellRenderer {
    final TableCellRenderer internal;
    
    public JTableButtonRenderer(TableCellRenderer internal) {
        this.internal = internal;
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if(value instanceof Component)
            return (Component)value;
        return internal.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
