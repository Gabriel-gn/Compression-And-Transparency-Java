/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication49_imageprocessing;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

/**
 *
 * @author gabri
 */
public class SnapshotUtils {

    public static byte[] getSnapshotAsByteArray(Node node, float compression) {
        byte[] res = null;
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.rgb(0, 0, 0)); //fundo preto puro para ser transformado em grayscale
        Image image = node.snapshot(parameters, null);
        try {
            BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
            bImage = getGrayscaleImage(bImage); //transforma a imagem em tons de cinza 8 bits.
            res = getCompressedBytes(bImage, compression); //compressão jpeg
//            System.out.println("Tamanho da imagem (bytes) a ser guardada: " + res.length);
        } catch (IOException ex) {
            Logger.getLogger(SnapshotUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public static BufferedImage getGrayscaleImage(BufferedImage src) {
        BufferedImage gImg = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_BYTE_GRAY); //8 bits grayscale
        WritableRaster wr = src.getRaster();
        WritableRaster gr = gImg.getRaster();
        for (int i = 0; i < wr.getWidth(); i++) {
            for (int j = 0; j < wr.getHeight(); j++) {
                gr.setSample(i, j, 0, wr.getSample(i, j, 0));
            }
        }
        gImg.setData(gr);
        return gImg;
    }

    public static Image makeTransparent(Image inputImage, int red, int green, int blue, int threshold) { //nesse caso só exclui uma componente específica
        int W = (int) inputImage.getWidth();
        int H = (int) inputImage.getHeight();
        WritableImage outputImage = new WritableImage(W, H);
        PixelReader reader = inputImage.getPixelReader();
        PixelWriter writer = outputImage.getPixelWriter();
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                int argb = reader.getArgb(x, y);

                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;

                if (r <= red + threshold && g <= g + threshold && b <= blue + threshold) {
                    argb &= 0x00FFFFFF;
                }
                writer.setArgb(x, y, argb);
            }
        }
        return outputImage;
    }

    public static Image makeTransparentForGray(Image inputImage, int grayValue) { //nesse caso só exclui uma componente específica
        int W = (int) inputImage.getWidth();
        int H = (int) inputImage.getHeight();
        WritableImage outputImage = new WritableImage(W, H);
        PixelReader reader = inputImage.getPixelReader();
        PixelWriter writer = outputImage.getPixelWriter();
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                int argb = reader.getArgb(x, y);

                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;

//                grayValue = grayValue + threshold;
                if (r <= grayValue && g <= grayValue && b <= grayValue) {
                    argb &= 0x00FFFFFF;
                }
                writer.setArgb(x, y, argb);
            }
        }
        return outputImage;
    }

    public static Image getByteArrayAsTransparentImage(byte[] byteArray) {
        return makeTransparent(new Image(new ByteArrayInputStream(byteArray)), 0, 0, 0, 30); //os bytes até 35 de preto são transformados em alpha
    }

    private static byte[] getCompressedBytes(BufferedImage image, float compression) throws IOException {
        ByteArrayOutputStream compressed = new ByteArrayOutputStream();
        ImageOutputStream outputStream = ImageIO.createImageOutputStream(compressed);

        ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
        jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpgWriteParam.setCompressionQuality(compression);
        jpgWriter.setOutput(outputStream);
        jpgWriter.write(null, new IIOImage(image, null, null), jpgWriteParam);
        jpgWriter.dispose();

        byte[] byteArray = compressed.toByteArray();
        compressed.close();
        outputStream.close();
        return byteArray;
    }

}
