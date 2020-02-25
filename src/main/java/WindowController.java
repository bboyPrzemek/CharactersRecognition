import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;



public class WindowController implements Initializable {

@FXML private Canvas canvas;
@FXML private Button clearBtn;

    public void initialize(URL location, ResourceBundle resources) {
       final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>(){
                        public void handle(MouseEvent event) {
                            graphicsContext.beginPath();
                            graphicsContext.moveTo(event.getX(), event.getY());
                            graphicsContext.stroke();
                        }

                });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>(){
                    public void handle(MouseEvent event) {
                        graphicsContext.setLineWidth(10);
                        graphicsContext.lineTo(event.getX(), event.getY());
                        graphicsContext.stroke();
                    }
                });

        clearBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                graphicsContext.clearRect(0,0, canvas.getWidth(),canvas.getHeight());
            }
        });

    }

}
