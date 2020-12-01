package br.com.finalcraft.unesp.java.cg.javafx.controller;

import br.com.finalcraft.unesp.java.cg.data.image.FileHelper;
import br.com.finalcraft.unesp.java.cg.data.image.ImageHelper;
import br.com.finalcraft.unesp.java.cg.data.wrapper.ImgWrapper;
import br.com.finalcraft.unesp.java.cg.javafx.controller.consoleview.ConsoleView;
import br.com.finalcraft.unesp.java.cg.javafx.controller.filemanager.FileLoaderHandler;
import br.com.finalcraft.unesp.java.cg.javafx.controller.filemanager.FileSaverHandler;
import br.com.finalcraft.unesp.java.cg.javafx.controller.paint.PaintController;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;
import java.io.File;

public class MainController implements FileLoaderHandler, FileSaverHandler {

    public static MainController instance;

    public static ImgWrapper leftImage;
    public static ImgWrapper rightImage;
    public static ImgWrapper rightImageBackUp;

    public void updateImagesBeingDisplayed(){
        if (leftImage != null){
            BufferedImage bufferedImage = ImageHelper.convertToBufferedImage(leftImage);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            leftImageViwer.setImage(image);
        }
        if (rightImage != null){
            BufferedImage bufferedImage = ImageHelper.convertToBufferedImage(rightImage);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            rightImageViwer.setImage(image);

            if (paintIsOpen){
                PaintController.instance.updateImageOnly(bufferedImage);
            }
        }
    }

    private void enableAllButtons(){
        brightSlider.setDisable(false);
        brightTextField.setDisable(false);
        buttonInvertColors.setDisable(false);
        buttomRotateLeft.setDisable(false);
        buttomRotateRight.setDisable(false);
        buttomFlipHorizonttal.setDisable(false);
        buttomFlipVertical.setDisable(false);
        buttomTempSave.setDisable(false);
        buttomRestaurar.setDisable(false);
        buttomOpenPaint.setDisable(false);
        menuAvancado.setDisable(false);
        menuCanais.setDisable(false);
    }

    @Override
    public void onFileLoaded(File file) {

        ImgWrapper imgWrapper = FileHelper.readAndCreateImageWrapper(file);
        if (imgWrapper != null){
            System.out.println("Loading image: " + file.getAbsolutePath());

            leftImage = imgWrapper;
            rightImage = imgWrapper.clone();
            rightImageBackUp = imgWrapper.clone();

            enableAllButtons();
            zoomProperty.set(leftImage.getRed().getWidth() / 4D);

            if (paintIsOpen){
                PaintController.instance.openPaint(rightImage);
            }
        }
        setLight(0);
    }

    @Override
    public void onFileSaved(File file) {
        FileHelper.export(file, rightImage);
        System.out.println("RightImage exporatada pada: \n" + file);
    }

    @FXML
    void onConsoleOpen() {
        ConsoleView.initialize();
    }

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button buttomOpenPaint;

    @FXML
    private MenuButton menuAvancado;

    @FXML
    private MenuButton menuCanais;

    @FXML
    private HBox centerHBox;

    @FXML
    private Slider brightSlider;

    @FXML
    private TextField brightTextField;

    @FXML
    private ImageView leftImageViwer;

    @FXML
    private ImageView rightImageViwer;

    @FXML
    private Button buttonInvertColors;

    @FXML
    private Button buttomRotateLeft;

    @FXML
    private Button buttomRotateRight;

    @FXML
    private Button buttomFlipHorizonttal;

    @FXML
    private Button buttomFlipVertical;

    @FXML
    private Button restaurarButtom;

    @FXML
    private ScrollPane scrollPaneLeft;

    @FXML
    private ScrollPane scrollPaneRight;

    @FXML
    private Button buttomTempSave;

    @FXML
    private Button buttomRestaurar;

    private final DoubleProperty zoomProperty = new SimpleDoubleProperty(100);

    @FXML
    void initialize() {
        instance = this;

        leftImageViwer.setPreserveRatio(true);
        rightImageViwer.setPreserveRatio(true);

        zoomProperty.addListener(new InvalidationListener() {
            public void invalidated(Observable observable) {
                leftImageViwer.setFitWidth(zoomProperty.get() * 4);
                leftImageViwer.setFitHeight(zoomProperty.get() * 3);
                rightImageViwer.setFitWidth(zoomProperty.get() * 4);
                rightImageViwer.setFitHeight(zoomProperty.get() * 3);
            }
        });

        EventHandler<ScrollEvent> scrollEventEventHandler = new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaY() > 0) {
                    zoomProperty.set(zoomProperty.get() * 1.1);
                    System.out.println("zoomProperty: " + zoomProperty);
                } else if (event.getDeltaY() < 0) {
                    zoomProperty.set(zoomProperty.get() / 1.1);
                    System.out.println("zoomProperty: " + zoomProperty);
                }
            }
        };

        scrollPaneLeft.addEventFilter(ScrollEvent.ANY, scrollEventEventHandler);
        scrollPaneRight.addEventFilter(ScrollEvent.ANY, scrollEventEventHandler);
    }

    int currentLowerBound = 0;
    int currentHigherBound = 255;

    public void updateLight(){
        rightImage = rightImageBackUp.setBright(currentbright, currentLowerBound, currentHigherBound);
        updateImagesBeingDisplayed();
    }

    public void setLight(int value){
        currentbright = value;
        brightTextField.setText(String.valueOf(currentbright));
        brightSlider.setValue(value);
        updateLight();
    }
    int currentbright = 0;

    @FXML
    void onBrightManual(KeyEvent event) {
        try {
            int integer = (int) Double.parseDouble(brightTextField.getText());
            if (integer > brightSlider.getMax()){
                integer = (int) brightSlider.getMax();
                brightSlider.setValue(integer);
            }
            if (integer < brightSlider.getMin()){
                integer = (int) brightSlider.getMin();
                brightSlider.setValue(integer);
            }
            currentbright = integer;
            updateLight();
            System.out.println("Bright set to: " + currentbright);
        }catch (Exception e){
            brightTextField.setText("" + currentbright);
            System.out.println("Failed to set bright to non Int");
        }
    }

    @FXML
    void onDragSlider() {
        setLight((int) brightSlider.getValue());
        System.out.println("Bright set to: " + currentbright);
    }

        @FXML
    void onInvertColors() {
        rightImage = rightImage.inverse();
        rightImageBackUp = rightImage.clone();
        setLight(0);
        System.out.println("Colors Inverted");
    }

    @FXML
    void onRotateLeft() {
        this.rightImage = this.rightImage.rotateLeft();
        rightImageBackUp = rightImage.clone();
        setLight(0);
        System.out.println("Rotated to Left");
    }

    @FXML
    void onRotateRight() {
        this.rightImage = this.rightImage.rotateRight();
        rightImageBackUp = rightImage.clone();
        setLight(0);
        System.out.println("Rotated to Right");
    }

    @FXML
    void onFlipHorizontal() {
        this.rightImage = this.rightImage.flipHorizontal();
        rightImageBackUp = rightImage.clone();
        setLight(0);
        System.out.println("Flip Horizontal");
    }

    @FXML
    void onFlipVertical() {
        this.rightImage = this.rightImage.flipVertical();
        rightImageBackUp = rightImage.clone();
        setLight(0);
        System.out.println("Flip Vertical");
    }

    @FXML
    void onOpenFile(){
        startHearingForFileLoads();
        FileLoaderHandler.openFilerLoader();
        stopHearingForFileLoads();
    }

    @FXML
    void onExport(){
        startHearingForFileSaves();
        FileSaverHandler.openFilerLoader(this);
        stopHearingForFileSaves();
    }

    @FXML
    void onRestaurar(){
        this.rightImage = this.leftImage.clone();
        this.rightImageBackUp = this.leftImage.clone();
        setLight(0);
        if (paintIsOpen){
            PaintController.instance.openPaint(rightImage);
        }
    }

    @FXML
    void onTempSave(){
        this.leftImage = this.rightImage.clone();
        onRestaurar();
    }

    boolean paintIsOpen = false;
    @FXML
    void onPaintOpen(){
        if (!paintIsOpen){
            //Open then
            buttomOpenPaint.setText("Fechar Modo Paint");
            buttomOpenPaint.setTextFill(Color.valueOf("#d62e99"));
            this.borderPane.setCenter(PaintController.instance.borderPane);
            PaintController.instance.openPaint(rightImage);
        }else {
            buttomOpenPaint.setText("Abrir Modo Paint");
            buttomOpenPaint.setTextFill(Color.valueOf("#16900d"));
            this.rightImage = PaintController.instance.imgWrapper;
            this.rightImageBackUp = rightImage.clone();
            this.borderPane.setCenter(this.centerHBox);
            setLight(0);
        }
        paintIsOpen = !paintIsOpen;
    }


    @FXML
    void onBrightEqualization(){
        this.rightImage = this.rightImage.equalizarBrilho();
        rightImageBackUp = rightImage.clone();
        setLight(0);
        System.out.println("Brilho da imagem equalizado!");
    }

    @FXML
    void onBinarizacao(){
        this.rightImage = this.rightImage.setBright(-255,0, 127);
        this.rightImage = this.rightImage.setBright(255,128, 255);
        this.rightImageBackUp = this.rightImage.clone();
        setLight(0);
    }

    @FXML
    void onConvertFromRGBToHSI(){
        this.rightImage = this.rightImage.onConvertFromRGBToHSI();
        this.rightImageBackUp = this.rightImage.clone();
        setLight(0);
    }


    @FXML
    void onColorir(){
        this.rightImage = this.rightImage.colorify();
        this.rightImageBackUp = this.rightImage.clone();
        setLight(0);
    }


    @FXML
    void rgb_to_r() {
        this.rightImage = this.rightImage.extractRed();
        this.rightImageBackUp = this.rightImage.clone();
        setLight(0);
    }

    @FXML
    void rgb_to_g() {
        this.rightImage = this.rightImage.extractGreen();
        this.rightImageBackUp = this.rightImage.clone();
        setLight(0);
    }

    @FXML
    void rgb_to_b() {
        this.rightImage = this.rightImage.extractBlue();
        this.rightImageBackUp = this.rightImage.clone();
        setLight(0);
    }

    @FXML
    void rgb_to_c() {
        this.rightImage = this.rightImage.extractCyan();
        this.rightImageBackUp = this.rightImage.clone();
        setLight(0);
    }

    @FXML
    void rgb_to_m() {
        this.rightImage = this.rightImage.extractMagenta();
        this.rightImageBackUp = this.rightImage.clone();
        setLight(0);
    }

    @FXML
    void rgb_to_y() {
        this.rightImage = this.rightImage.extractYellow();
        this.rightImageBackUp = this.rightImage.clone();
        setLight(0);
    }

    @FXML
    void rgb_to_h() {
        this.rightImage = this.rightImage.extractHue();
        this.rightImageBackUp = this.rightImage.clone();
        setLight(0);
    }

    @FXML
    void rgb_to_s() {
        this.rightImage = this.rightImage.extractSaturation();
        this.rightImageBackUp = this.rightImage.clone();
        setLight(0);
    }

    @FXML
    void rgb_to_i() {
        this.rightImage = this.rightImage.extractIntensity();
        this.rightImageBackUp = this.rightImage.clone();
        setLight(0);
    }
}
