package io.github.mattidragon.fabricdependencyhacker.util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class LambdaAction extends AbstractAction {
    private final Consumer<ActionEvent> consumer;
    
    public LambdaAction(String name, Consumer<ActionEvent> consumer) {
        super(name);
        this.consumer = consumer;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        consumer.accept(e);
    }
}
