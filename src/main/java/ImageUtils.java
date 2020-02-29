import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;


public class ImageUtils {

    public static WritableImage scaleImage(Image source, int targetWidth, int targetHeight) {
        ImageView imageView = new ImageView(source);
        imageView.setPreserveRatio(false);
        imageView.setFitWidth(targetWidth);
        imageView.setFitHeight(targetHeight);
        return imageView.snapshot(null, null);
    }

    public static double[] getPixelsValuesFromImage(BufferedImage bufferedImage){
        double[] pixelsArray = new double[bufferedImage.getWidth()*bufferedImage.getHeight()];
        WritableRaster wr = bufferedImage.getRaster() ;
        int pixelValue;
        int binaryPixelValue;
        int counter = 0;
        for (int y=0 ; y < bufferedImage.getHeight() ; y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                pixelValue = (wr.getSample(x, y, 0));
                binaryPixelValue = (pixelValue == 255) ? 0 : 1;
                pixelsArray[counter] = binaryPixelValue;
                counter++;
            }
        }
        return pixelsArray;
    }

    public static BufferedImage getImageByPath(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
        }
        return image;
    }
}
