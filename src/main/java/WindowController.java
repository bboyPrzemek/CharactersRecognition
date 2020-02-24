import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class WindowController implements Initializable {

    @FXML
    private FontAwesomeIcon closeBtn;
    @FXML
    private FontAwesomeIcon minimizeBtn;

    public void initialize(URL location, ResourceBundle resources) {

        closeBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
               System.exit(0);
            }
        });
        minimizeBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                ((Stage)((FontAwesomeIcon)event.getSource()).getScene().getWindow()).setIconified(true);
            }
        });
    }

}
