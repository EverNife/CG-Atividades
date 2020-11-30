package br.com.finalcraft.unesp.java.cg.javafx.controller.paint;

import br.com.finalcraft.unesp.java.cg.data.image.ImageHelper;
import br.com.finalcraft.unesp.java.cg.data.wrapper.ImgWrapper;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.awt.image.BufferedImage;

public class PaintController {

    public static PaintController instance;

    public ImgWrapper imgWrapper;
    public ImgWrapper imgWrapperBackUp;

    public void openPaint(ImgWrapper imgWrapper){
        this.imgWrapper = imgWrapper;
        this.imgWrapperBackUp = imgWrapper.clone();

        BufferedImage bufferedImage = ImageHelper.convertToBufferedImage(this.imgWrapper);
        updateImageOnly(bufferedImage);
    }

    public void updateImageOnly(BufferedImage bufferedImage){
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);

        imageView.setImage(image);

        imageView.setFitWidth(image.getWidth());
        imageView.setFitHeight(image.getHeight());
    }

    @FXML
    void initialize() {
        instance = this;
        imageView.setPreserveRatio(true);
    }

    @FXML
    public BorderPane borderPane;

    @FXML
    private ImageView imageView;

    @FXML
    void onLine(ActionEvent event) {

    }

    @FXML
    void onPencil(ActionEvent event) {

    }

    @FXML
    void onRubber(ActionEvent event) {

    }

}
