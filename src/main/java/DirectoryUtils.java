import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DirectoryUtils {

    public static final String PATH =  "C:/Users/Przemek/Documents/NeuralNetwork/CharactersRecognitionJFX/src/main/resources/Letters/";

    public static BufferedImage getImageByPath(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
        }
        return image;
    }


    public static List<String> getFileNamesFromPath(String path) {
        List<String> fileNames = new ArrayList<String>();
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                fileNames.add(file.getName());
            }
        }
        return fileNames;
    }
}
