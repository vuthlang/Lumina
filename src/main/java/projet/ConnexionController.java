package projet;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ConnexionController {
    @FXML
    Button manager = new Button();

    @FXML
    public void manager() {
        Main.redirection(manager, "manager.fxml");
    }

    @FXML
    public void caissier() {
        Main.redirection(manager, "caissier.fxml");
    }
}
