package projet;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ConnexionController {
    @FXML
    private TextField username = new TextField();
    @FXML
    private PasswordField pwd = new PasswordField();

    @FXML
    public void connect(){
        System.out.println("Username : " + username.getText());
        System.out.println("Mot de passe : " + pwd.getText());
    }
}
