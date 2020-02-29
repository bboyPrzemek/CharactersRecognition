import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;

public class WindowController implements Initializable {

@FXML private Canvas canvas;
@FXML private Button clearBtn;
@FXML private Button checkBtn;

    public void initialize(URL location, ResourceBundle resources) {
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                        graphicsContext.beginPath();
                        graphicsContext.moveTo(event.getX(), event.getY());
                        graphicsContext.stroke();
                    }

                });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                        graphicsContext.setLineWidth(10);
                        graphicsContext.lineTo(event.getX(), event.getY());
                        graphicsContext.stroke();
                    }
                });

        clearBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            }
        });

        checkBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                 BufferedImage image = Binarization.getBinarizedImage(SwingFXUtils.fromFXImage(ImageUtils.scaleImage(canvas.snapshot(null, null), Utils.IMG_WIDTH, Utils.IMG_HEIGHT), null));
//                BufferedImage img = null;
//                try {
//                    img = ImageIO.read(new File("C:/Users/Przemek/Documents/NeuralNetwork/CharactersRecognitionJFX/src/main/resources/Letters/A.png"));
//                } catch (IOException e) {
//                }

//                ImageUtils.getPixelsValuesFromImage(image);
                //ImageUtils.getPixelsValuesFromImage(image);

                NeuralNetwork.learn();
                double matrix[] = ImageUtils.getPixelsValuesFromImage(image);
//                for (int i = 0; i < matrix.length; i++) {
//                    System.out.print(matrix[i] + " ");
//                    if ((((i + 1) % 25)) == 0) {
//                        System.out.println("\n");
//                    }
//                }
            }

        });
    }
}
