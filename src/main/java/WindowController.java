import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.neuroph.core.NeuralNetwork;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class WindowController implements Initializable {

@FXML private Canvas canvas;
@FXML private Button clearBtn;
@FXML private Button checkBtn;
@FXML private Button trainBtn;
@FXML private Button saveBtn;
@FXML private ComboBox comboBox;
@FXML private Label output;
private DateTimeFormatter formatter;


    public void initialize(URL location, ResourceBundle resources) {
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        fillComboBoxWithDigits();
        comboBox.getSelectionModel().selectFirst();
        formatter = DateTimeFormatter.ofPattern(Utils.DATE_FORMATTER);


        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                event -> {
                    graphicsContext.beginPath();
                    graphicsContext.moveTo(event.getX(), event.getY());
                    graphicsContext.stroke();
                });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                event -> {
                    graphicsContext.setLineWidth(Utils.LINE_SIZE);
                    graphicsContext.lineTo(event.getX(), event.getY());
                    graphicsContext.stroke();
                });

        clearBtn.setOnMouseClicked(event -> {
                graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                output.setText("");

        });

        trainBtn.setOnMouseClicked(event -> {
            new MultiLayerNeuralNetwork().run();
        });

        saveBtn.setOnMouseClicked(event -> {
            handleSaveButton();
        });

        checkBtn.setOnMouseClicked(event -> {
            handleCheckButton();
        });
    }

    private void fillComboBoxWithDigits(){
        for (int i = 0; i <= 9; i++){
            comboBox.getItems().add(i);
        }
    }

    private int getIndexOfLargest(double[] array) {
        if (array == null || array.length == 0) return -1;
        int largest = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[largest]) largest = i;
        }
        return largest;
    }

    private void handleSaveButton(){
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(canvas.snapshot(null, null), null);
        Point startPoint = ImagesTransformation.getStartPointCropImage(bufferedImage);
        Point lastPoint = ImagesTransformation.getLastPointCropImage(bufferedImage);
        int width = ImagesTransformation.getWidthCrop(lastPoint, startPoint);
        int height = ImagesTransformation.getHeightCrop(lastPoint,startPoint);
        bufferedImage = ImagesTransformation.cropImage(bufferedImage,startPoint.getX(), startPoint.getY(), width,height);
        try {
            ImageIO.write(bufferedImage,"png", new File(DirectoryUtils.PATH + comboBox.getSelectionModel().getSelectedItem().toString() +"/"+formatter.format(LocalDateTime.now()) + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleCheckButton(){
        if (ImagesTransformation.isCanvasBlank(canvas)){
            output.setText(Utils.IF_NO_CANVAS_DRAW_TEXT);
        }else {
            NeuralNetwork loadedMlPerceptron = NeuralNetwork.createFromFile(MultiLayerNeuralNetwork.TRAINED_FILE_NAME);
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(canvas.snapshot(null, null), null);
            Point startPoint = ImagesTransformation.getStartPointCropImage(bufferedImage);
            Point lastPoint = ImagesTransformation.getLastPointCropImage(bufferedImage);
            int width = ImagesTransformation.getWidthCrop(lastPoint, startPoint);
            int height = ImagesTransformation.getHeightCrop(lastPoint, startPoint);
            bufferedImage = ImagesTransformation.cropImage(bufferedImage, startPoint.getX(), startPoint.getY(), width, height);
            bufferedImage = ImagesTransformation.getBinarizedImage(SwingFXUtils.fromFXImage(ImagesTransformation.scaleImage(SwingFXUtils.toFXImage(bufferedImage, null), Utils.IMG_WIDTH, Utils.IMG_HEIGHT), null));
            loadedMlPerceptron.setInput(ImagesTransformation.getPixelsValuesFromBinaryImage(bufferedImage));
            loadedMlPerceptron.calculate();
            output.setText(String.valueOf(getIndexOfLargest(loadedMlPerceptron.getOutput())));
        }
    }
}
