/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication49_imageprocessing;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;
import static javafx.scene.control.PopupControl.USE_PREF_SIZE;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import testresourcebundle.ImagePane;

/**
 *
 * @author Gabriel Nogueira
 */
public class FXMLDocumentController implements Initializable {

    Image image = new Image(getClass().getResource("eevee.png").toExternalForm());
    Image image2;
    byte[] imageByte = null;
    DecimalFormat df = new DecimalFormat();

    @FXML
    private Button button;
    @FXML
    private Slider slider_r;
    @FXML
    private Slider slider_g;
    @FXML
    private Slider slider_b;
    @FXML
    private Slider slider_th;
    @FXML
    private ImagePane pane_img1;
    @FXML
    private ImagePane pane_img2;
    @FXML
    private Label lbl_r;
    @FXML
    private Label lbl_g;
    @FXML
    private Label lbl_b;
    @FXML
    private Label lbl_th;
    @FXML
    private Label lbl_gray;
    @FXML
    private Slider slider_gray;
    @FXML
    private TextField txt_snapshotWidth;
    @FXML
    private TextField txt_snapshotHeight;
    @FXML
    private Label lbl_byteCount;
    @FXML
    private CheckBox box_grayscale;
    @FXML
    private Slider slider_compression;
    @FXML
    private Label lbl_compression;
    @FXML
    private AnchorPane pane_img1Container;
    @FXML
    private Slider slider_width;
    @FXML
    private Label lbl_width;
    @FXML
    private Slider slider_height;
    @FXML
    private Label lbl_height;
    @FXML
    private VBox vBox_colorSliders;
    @FXML
    private HBox hBox_graySlider;
    @FXML
    private ToggleButton btn_lockProportion;
    @FXML
    private TextField txt_codeSnapshot;
    @FXML
    private TextField txt_codeColorProcessing;

//    pane_img2.imageView.setImage(SnapshotUtils.makeTransparentForGray(new Image(new ByteArrayInputStream(imageByte)), 35));
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);
        // TODO
        Platform.runLater(() -> {
            pane_img1.imageView.setImage(image);
            vBox_colorSliders.setVisible(false);
            vBox_colorSliders.setManaged(false);
            setParameters();
            initListener();
        });
    }

    private void setParameters() {
        setSnapshotSize(Integer.parseInt(txt_snapshotWidth.getText()), Integer.parseInt(txt_snapshotHeight.getText()));
        imageByte = SnapshotUtils.getSnapshotAsByteArray(pane_img1.imageView, (float) slider_compression.getValue());
        lbl_byteCount.setText("" + imageByte.length);
        if (slider_gray.getValue() > 1) {
            image2 = SnapshotUtils.makeTransparentForGray(new Image(new ByteArrayInputStream(imageByte)), (int) slider_gray.getValue());
        } else {
            image2 = new Image(new ByteArrayInputStream(imageByte));
        }
        pane_img2.imageView.setImage(image2);
        releaseSnapshotSize(Integer.parseInt(txt_snapshotWidth.getText()), Integer.parseInt(txt_snapshotHeight.getText()));
        txt_codeSnapshot.setText("SnapshotUtils.getSnapshotAsByteArray(node, " + df.format((float) slider_compression.getValue()).toString() + ");");
    }

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        setParameters();
        slider_width.setValue(Double.parseDouble(txt_snapshotWidth.getText()));
        slider_height.setValue(Double.parseDouble(txt_snapshotHeight.getText()));
    }

    private void initListener() {

        slider_compression.valueProperty().addListener(e -> {
            setParameters();
            lbl_compression.setText(df.format((float) slider_compression.getValue()));
        });

        slider_width.valueProperty().addListener(e -> {
            if (btn_lockProportion.isSelected()) {
                slider_height.setValue(slider_width.getValue());
            }

            lbl_width.setText("" + (int) slider_width.getValue());
            txt_snapshotWidth.setText(lbl_width.getText());
            setParameters();
        });

        slider_height.valueProperty().addListener(e -> {
            if (btn_lockProportion.isSelected()) {
                slider_width.setValue(slider_height.getValue());
            }

            lbl_height.setText("" + (int) slider_height.getValue());
            txt_snapshotHeight.setText(lbl_height.getText());
            setParameters();
        });

        slider_r.valueProperty().addListener(e -> {
            image2 = SnapshotUtils.makeTransparent(new Image(new ByteArrayInputStream(imageByte)), (int) slider_r.getValue(), (int) slider_g.getValue(), (int) slider_b.getValue(), (int) slider_th.getValue());
            pane_img2.imageView.setImage(image2);
            lbl_r.setText("" + (int) slider_r.getValue());
        });

        slider_g.valueProperty().addListener(e -> {
            image2 = SnapshotUtils.makeTransparent(new Image(new ByteArrayInputStream(imageByte)), (int) slider_r.getValue(), (int) slider_g.getValue(), (int) slider_b.getValue(), (int) slider_th.getValue());
            pane_img2.imageView.setImage(image2);
            lbl_g.setText("" + (int) slider_g.getValue());
        });

        slider_b.valueProperty().addListener(e -> {
            image2 = SnapshotUtils.makeTransparent(new Image(new ByteArrayInputStream(imageByte)), (int) slider_r.getValue(), (int) slider_g.getValue(), (int) slider_b.getValue(), (int) slider_th.getValue());
            pane_img2.imageView.setImage(image2);
            lbl_b.setText("" + (int) slider_b.getValue());
        });

        slider_th.valueProperty().addListener(e -> {
            image2 = SnapshotUtils.makeTransparent(new Image(new ByteArrayInputStream(imageByte)), (int) slider_r.getValue(), (int) slider_g.getValue(), (int) slider_b.getValue(), (int) slider_th.getValue());
            pane_img2.imageView.setImage(image2);
            lbl_th.setText("" + (int) slider_th.getValue());
        });

        slider_gray.valueProperty().addListener(e -> {
            image2 = SnapshotUtils.makeTransparentForGray(new Image(new ByteArrayInputStream(imageByte)), (int) slider_gray.getValue());
            pane_img2.imageView.setImage(image2);
            lbl_gray.setText("" + (int) slider_gray.getValue());
            txt_codeColorProcessing.setText("SnapshotUtils.makeTransparentForGray(new Image(new ByteArrayInputStream(byteArray)), " + (int) slider_gray.getValue() + ");");
        });
    }

    private void setSnapshotSize(int w, int h) {
        pane_img1Container.setPrefSize(w, h);
        pane_img1Container.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
    }

    private void releaseSnapshotSize(int w, int h) {
        pane_img1Container.setPrefSize(w, h);
        pane_img1Container.setMaxSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
    }

    @FXML
    private void handle_changeImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        List<String> extList = new ArrayList<String>();
        extList.add("*.jpg");
        extList.add("*.JPG");
        extList.add("*.jpeg");
        extList.add("*.JPEG");
        extList.add("*.png");
        extList.add("*.PNG");
        extList.add("*.GIF");
        extList.add("*.gif");
        FileChooser.ExtensionFilter extFilterAll = new FileChooser.ExtensionFilter("Supported Images", extList);
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");
        FileChooser.ExtensionFilter extFilterjpg = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.PNG)", "*.PNG");
        FileChooser.ExtensionFilter extFilterpng = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        fileChooser.getExtensionFilters().addAll(extFilterAll, extFilterJPG, extFilterjpg, extFilterPNG, extFilterpng);

        //Show open file dialog
        File file = fileChooser.showOpenDialog(null);
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            image = SwingFXUtils.toFXImage(bufferedImage, null);
            pane_img1.imageView.setImage(image);
            setParameters();
        } catch (IOException ex) {
        }
    }

}
