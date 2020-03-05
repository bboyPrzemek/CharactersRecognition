import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class ImagesTransformation {

    private static BufferedImage grayscale, binarized;

    public static BufferedImage getBinarizedImage(BufferedImage original) {
        grayscale = toGray(original);
        binarized = binarize(grayscale);
        return binarized;
    }

    public static int[] imageHistogram(BufferedImage input) {

        int[] histogram = new int[256];
        for (int i = 0; i < histogram.length; i++) histogram[i] = 0;
        for (int i = 0; i < input.getWidth(); i++) {
            for (int j = 0; j < input.getHeight(); j++) {
                int red = new Color(input.getRGB(i, j)).getRed();
                histogram[red]++;
            }
        }
        return histogram;
    }


    private static BufferedImage toGray(BufferedImage original) {

        int alpha, red, green, blue;
        int newPixel;

        BufferedImage lum = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {
                alpha = new Color(original.getRGB(i, j)).getAlpha();
                red = new Color(original.getRGB(i, j)).getRed();
                green = new Color(original.getRGB(i, j)).getGreen();
                blue = new Color(original.getRGB(i, j)).getBlue();

                red = (int) (0.21 * red + 0.71 * green + 0.07 * blue);
                newPixel = colorToRGB(alpha, red, red, red);
                lum.setRGB(i, j, newPixel);
            }
        }
        return lum;

    }

    private static int otsuTreshold(BufferedImage original) {

        int[] histogram = imageHistogram(original);
        int total = original.getHeight() * original.getWidth();

        float sum = 0;
        for (int i = 0; i < 256; i++) sum += i * histogram[i];

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for (int i = 0; i < 256; i++) {
            wB += histogram[i];
            if (wB == 0) continue;
            wF = total - wB;

            if (wF == 0) break;

            sumB += (float) (i * histogram[i]);
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;

            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        }

        return threshold;

    }

    private static BufferedImage binarize(BufferedImage original) {
        int red;
        int newPixel;
        int threshold = otsuTreshold(original);

        BufferedImage binarized = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

        for (int i = 0; i < original.getWidth(); i++) {
            for (int j = 0; j < original.getHeight(); j++) {

                red = new Color(original.getRGB(i, j)).getRed();
                int alpha = new Color(original.getRGB(i, j)).getAlpha();
                if (red > threshold) {
                    newPixel = 255;
                } else {
                    newPixel = 0;
                }
                newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
                binarized.setRGB(i, j, newPixel);
            }
        }
        return binarized;

    }

    private static int colorToRGB(int alpha, int red, int green, int blue) {
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel = newPixel << 8;
        newPixel += blue;
        return newPixel;
    }

    public static WritableImage scaleImage(Image source, int targetWidth, int targetHeight) {
        ImageView imageView = new ImageView(source);
        imageView.setPreserveRatio(false);
        imageView.setFitWidth(targetWidth);
        imageView.setFitHeight(targetHeight);
        return imageView.snapshot(null, null);
    }

    public static double[] getPixelsValuesFromBinaryImage(BufferedImage bufferedImage){
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

    public static BufferedImage cropImage(BufferedImage bufferedImage, int x, int y, int width, int height){
             return bufferedImage.getSubimage(x, y, width, height);
    }

    public static Point getStartPointCropImage(BufferedImage bufferedImage){
        Point point = new Point(-1,-1);
        for (int i = 0; i < bufferedImage.getHeight(); i++){
            for (int j = 0; j < bufferedImage.getWidth(); j++){
                if ((Integer.valueOf(point.getY()) == -1)&& (bufferedImage.getRGB(j,i) != -1)){
                    point.setY(i);
                }

            }
        }
        for (int i = 0; i < bufferedImage.getWidth(); i++){
            for (int j = 0; j < bufferedImage.getHeight(); j++){
                if ((Integer.valueOf(point.getX()) == -1) && (bufferedImage.getRGB(i,j) != -1)){
                    point.setX(i);
                }

            }
        }
        return point;
    }

    public static Point getLastPointCropImage(BufferedImage bufferedImage){
        Point point = new Point();
        for (int i = 0; i < bufferedImage.getHeight(); i++){
            for (int j = 0; j < bufferedImage.getWidth(); j++){
                if (bufferedImage.getRGB(j,i) != -1){
                    point.setY(i);
                }

            }
        }

        for (int i = 0; i < bufferedImage.getWidth(); i++){
            for (int j = 0; j < bufferedImage.getHeight(); j++){
                if ((bufferedImage.getRGB(i,j) != -1)){
                    point.setX(i);
                }

            }
        }
        return point;
    }

    public static Boolean isCanvasBlank(Canvas canvas) {
        Boolean isBlank = true;
        double[] pixels = getPixelsValuesFromBinaryImage(SwingFXUtils.fromFXImage(canvas.snapshot(null, null), null));
        for (int i = 0; i < pixels.length; i++) {
            if (pixels[i] != 0) {
                isBlank = false;
            }
        }
        return isBlank;
    }

    public static int getWidthCrop(Point pointB, Point pointA){
        return pointB.getX() - pointA.getX();
    }

    public static int getHeightCrop(Point pointB, Point pointA){
            return pointB.getY() - pointA.getY();
    }
}
