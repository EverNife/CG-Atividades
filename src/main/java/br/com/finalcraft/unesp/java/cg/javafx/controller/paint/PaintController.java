package br.com.finalcraft.unesp.java.cg.javafx.controller.paint;

import br.com.finalcraft.unesp.java.cg.JavaFXMain;
import br.com.finalcraft.unesp.java.cg.data.MathUtil;
import br.com.finalcraft.unesp.java.cg.data.colorutil.ColorUtil;
import br.com.finalcraft.unesp.java.cg.data.image.ImageHelper;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static br.com.finalcraft.unesp.java.cg.javafx.controller.MainController.rightImage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class PaintController {

    public static PaintController instance;

    public PaintTool toolBeingUsed = new PaintTool() {};

    public Color theColor1 = new Color(0,0,0);
    public Color theColor2 = new Color(200,255,255);
    public int currentColorNumber = 1;

    public void updateImageOnly(BufferedImage bufferedImage){
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        imageView.setImage(image);

        imageView.setFitWidth(image.getWidth());
        imageView.setFitHeight(image.getHeight());
    }

    public void updatePaintImage(){
        BufferedImage bufferedImage = ImageHelper.convertToBufferedImage(rightImage);
        updateImageOnly(bufferedImage);
    }

    @FXML
    void initialize() {
        instance = this;
        imageView.setPreserveRatio(true);

        color1.setStyle("-fx-background-color: " + String.format("#%06x", theColor1.getRGB() & 0xFFFFFF) + "; ");
        color2.setStyle("-fx-background-color: " + String.format("#%06x", theColor2.getRGB() & 0xFFFFFF) + "; ");
    }

    @FXML
    public BorderPane borderPane;

    @FXML
    private ImageView imageView;

    @FXML
    private Button color1;

    @FXML
    private Button color2;

    @FXML
    void onLine(ActionEvent event) {
        System.out.println("Selecting Tool: Line");
        toolBeingUsed = new PaintTool() {
            int downX = 0;
            int downY = 0;
            boolean dragStart;
            @Override
            public void onMouseDown(MouseEvent event) {
                int xCoord = (int) event.getX();
                int yCoord = (int) event.getY();
                if (!rightImage.hasPixel(xCoord, yCoord)) return;

                dragStart = true;
                downX = xCoord;
                downY = yCoord;
                System.out.println(String.format("Selection Position at [%d, %d] as line Start.", xCoord, yCoord) );
            }

            @Override
            public void onMouseDragged(MouseEvent event) {
            }

            @Override
            public void onMouseUP(MouseEvent event) {
                if (dragStart){
                    dragStart = false;

                    int xCoord = (int) event.getX();
                    int yCoord = (int) event.getY();
                    if (!rightImage.hasPixel(xCoord, yCoord)) return;

                    List<Integer[]> pixelsToPaint = MathUtil.getAllPixelsBetween(downX, downY, xCoord, yCoord);

                    for (Integer[] cords : pixelsToPaint) {
                        rightImage.setPixel(cords[0], cords[1], getTheCurrentColor());
                    }
                    System.out.println(String.format("Selection Position at [%d, %d] as line End.", xCoord, yCoord) );
                    System.out.println(String.format("Total of %d pixels colored!",  pixelsToPaint.size()));
                    updatePaintImage();
                }
            }
        };
    }

    @FXML
    void onPencil(ActionEvent event) {
        System.out.println("Selecting Tool: Pencil");
        toolBeingUsed = new PaintTool() {
            @Override
            public void onMouseDown(MouseEvent event) {
                int xCoord = (int) event.getX();
                int yCoord = (int) event.getY();
                if (!rightImage.hasPixel(xCoord, yCoord)) return;

                rightImage.setPixel(xCoord, yCoord, getTheCurrentColor());
                System.out.println(String.format("Seting color at pixel [%d, %d] to (%d, %d, %d)", xCoord, yCoord, getTheCurrentColor().getRed(), getTheCurrentColor().getGreen(), getTheCurrentColor().getBlue()) );
                updatePaintImage();

                dragStart = true;
                downX = xCoord;
                downY = yCoord;
            }

            int downX = 0;
            int downY = 0;
            boolean dragStart;

            long pixelsThisSecond = 0;
            long lastPixelsWarn = 0;
            boolean haltWarn = false;
            @Override
            public void onMouseDragged(MouseEvent event) {
                if (dragStart){
                    int xCoord = (int) event.getX();
                    int yCoord = (int) event.getY();
                    if (!rightImage.hasPixel(xCoord, yCoord)) return;

                    List<Integer[]> pixelsToPaint = MathUtil.getAllPixelsBetween(downX, downY, xCoord, yCoord);
                    for (Integer[] cords : pixelsToPaint) {
                        rightImage.setPixel(cords[0], cords[1], getTheCurrentColor());
                        if (pixelsThisSecond < 5){
                            System.out.println(String.format("Seting color at pixel [%d, %d] to (%d, %d, %d)", cords[0], cords[1], getTheCurrentColor().getRed(), getTheCurrentColor().getGreen(), getTheCurrentColor().getBlue()) );
                        }else{
                            if (System.currentTimeMillis() - lastPixelsWarn > 1000){
                                lastPixelsWarn = System.currentTimeMillis();
                                pixelsThisSecond = 0;
                                haltWarn = false;
                            }
                            else if (!haltWarn){
                                haltWarn = true;
                                System.out.println("Halting pixel painting log to prevent interface lag!");
                            }
                        }
                        pixelsThisSecond++;
                    }

                    downX = xCoord;
                    downY = yCoord;
                    updatePaintImage();
                }
            }

            @Override
            public void onMouseUP(MouseEvent event) {
                if (dragStart){
                    dragStart = false;
                    onMouseDragged(event);
                }
            }
        };
    }

    @FXML
    void onRubber(ActionEvent event) {

    }

    @FXML
    void onColorPicker(ActionEvent event) {
        System.out.println("Selecting Tool: Color Picker");
        toolBeingUsed = new PaintTool() {
            @Override
            public void onMouseDragged(MouseEvent event) {
                int xCoord = (int) event.getX();
                int yCoord = (int) event.getY();
                if (!rightImage.hasPixel(xCoord, yCoord)) return;

                int R = rightImage.getRed().getPixel(xCoord, yCoord);
                int G = rightImage.getGreen().getPixel(xCoord, yCoord);
                int B = rightImage.getBlue().getPixel(xCoord, yCoord);

                Color color = new Color(R, G, B);

                if (currentColorNumber == 1){
                    color1.setStyle("-fx-background-color: " + String.format("#%06x", color.getRGB() & 0xFFFFFF) + "; ");
                    theColor1 = color;
                }else {
                    color2.setStyle("-fx-background-color: " + String.format("#%06x", color.getRGB() & 0xFFFFFF) + "; ");
                    theColor2 = color;
                }
            }
        };
    }

    private Stage colorPickerStage = null;
    private ColorPicker colorPicker;
    private javafx.scene.text.Text text;
    public void showColorPicker(){
        if (colorPickerStage == null){
            colorPickerStage = new Stage();
            colorPickerStage.initModality(Modality.WINDOW_MODAL);
            // Defines a modal window that blocks events from being
            // delivered to any other application window.
            colorPickerStage.initOwner(JavaFXMain.primaryStage);

            colorPickerStage.setTitle("ColorPicker");
            Scene scene = new Scene(new HBox(20), 400, 100);
            HBox box = (HBox) scene.getRoot();
            box.setPadding(new Insets(5, 5, 5, 5));
            colorPicker = new ColorPicker();
            colorPicker.setValue(ColorUtil.toJavaFXColor(getTheCurrentColor()));

            text = new javafx.scene.text.Text("Escolha uma cor!");
            text.setFont(Font.font("Verdana", 20));
            text.setFill(colorPicker.getValue());
            box.getChildren().addAll(colorPicker, text);
            colorPickerStage.setScene(scene);
        }

        colorPicker.setOnAction(event -> {
            text.setFill(colorPicker.getValue());
            if (currentColorNumber == 1){
                theColor1 = ColorUtil.toAwtColor(colorPicker.getValue());
                String hexColor = String.format("#%06x", theColor1.getRGB() & 0xFFFFFF);
                color1.setStyle("-fx-background-color: " + hexColor + "; ");
            }else{
                theColor2 = ColorUtil.toAwtColor(colorPicker.getValue());
                String hexColor = String.format("#%06x", theColor2.getRGB() & 0xFFFFFF);
                color2.setStyle("-fx-background-color: " + hexColor + "; ");
            }
        });

        colorPickerStage.show();
    }

    @FXML
    void onMouseClickColor1(MouseEvent event) {
        if (currentColorNumber == 1){
            if (event.getClickCount() > 1){
                showColorPicker();
            }
        }else {
            currentColorNumber = 1;
            color2.setOpacity(0.5);
            color1.setOpacity(1);
        }
    }

    @FXML
    void onMouseClickColor2(MouseEvent event) {
        if (currentColorNumber == 2){
            if (event.getClickCount() > 1){
                showColorPicker();
            }
        }else {
            currentColorNumber = 2;
            color1.setOpacity(0.5);
            color2.setOpacity(1);
        }
    }

    public Color getTheCurrentColor(){
        if (currentColorNumber == 1){
            return theColor1;
        }else {
            return theColor2;
        }
    }

    //============================================================================
    //Image View Manipulation
    //============================================================================

    @FXML
    void onMouseMoved(MouseEvent event) {
        if (true)return;
        try {

        }catch (Exception ignored){

        }
    }

    @FXML
    void onMouseDown(MouseEvent event) {
        toolBeingUsed.onMouseDown(event);
    }

    @FXML
    void onMouseDragged(MouseEvent event) {
        toolBeingUsed.onMouseDragged(event);
    }

    @FXML
    void onMouseUP(MouseEvent event) {
        toolBeingUsed.onMouseUP(event);
    }

}
