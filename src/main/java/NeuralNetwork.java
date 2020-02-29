import javafx.embed.swing.SwingFXUtils;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

import java.awt.image.BufferedImage;


public class NeuralNetwork {

    public static void learn(){

        DataSet trainingSet = new DataSet(Utils.INPUT_SIZE, Utils.OUTPUT_SIZE);
        String path;
        BufferedImage bufferedImage;
        double[] output = new double[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        for (int i = 0; i <= 25; i++) {
            output[i] = 1;
            path = "C:/Users/Przemek/Documents/NeuralNetwork/CharactersRecognitionJFX/src/main/resources/Letters/" + i + ".png";
            bufferedImage = Binarization.getBinarizedImage(SwingFXUtils.fromFXImage(ImageUtils.scaleImage(SwingFXUtils.toFXImage(ImageUtils.getImageByPath(path), null), Utils.IMG_WIDTH, Utils.IMG_HEIGHT), null));
            trainingSet.addRow(new DataSetRow(ImageUtils.getPixelsValuesFromImage(bufferedImage), output));
            output[i] = 0;
        }
    }
}
