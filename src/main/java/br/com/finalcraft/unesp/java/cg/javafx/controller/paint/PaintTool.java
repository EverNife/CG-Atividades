package br.com.finalcraft.unesp.java.cg.javafx.controller.paint;

import javafx.scene.input.MouseEvent;

public interface PaintTool {

    default void onMouseDown(MouseEvent event){

    }

    default void onMouseDragged(MouseEvent event){

    }

    default void onMouseUP(MouseEvent event){

    }
}
