package br.com.finalcraft.unesp.java.cg.javafx.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;

public class MyFXMLs {

    public static Parent main_screen;
    public static Parent paint_center;
    public static void loadUpFiles() throws IOException {
        main_screen = FXMLLoader.load(MyFXMLs.class.getResource("/assets/main_screen.fxml"));
        paint_center = FXMLLoader.load(MyFXMLs.class.getResource("/assets/paint/paint_center.fxml"));
    }

    public static URL getConsoleCSS(){
        return MyFXMLs.class.getResource("/assets/console/console-style.css");
    }
}
