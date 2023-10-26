package projet;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AddProductController {
    @FXML
    private TextField nomTextField = new TextField();
    @FXML
    private TextField urlTextField = new TextField();
    @FXML
    private TextArea descriptionTextArea = new TextArea();
    @FXML
    private ComboBox<String> categorieComboBox = new ComboBox<String>();
    @FXML
    private ComboBox<Integer> quantiteComboBox = new ComboBox<Integer>();
    @FXML
    private Button ajouterProduit = new Button();

    public void initialize() {
        // TODO : Mettre toutes les catégories dans le ComboBox comme pour les quantités
        List<String> categories = new ArrayList<>();
        /*
         * for (int i = 1; i <= 500; i++) {
         * categories.add(i);
         * }
         */
        categorieComboBox.getItems().addAll(categories);

        List<Integer> nombres = new ArrayList<>();
        for (int i = 1; i <= 500; i++) {
            nombres.add(i);
        }
        quantiteComboBox.getItems().addAll(nombres);

        ajouterProduit.setOnAction(
                event -> ajouterProduit(
                        categorieComboBox.getValue(),
                        nomTextField.getText(),
                        urlTextField.getText(),
                        descriptionTextArea.getText(),
                        quantiteComboBox.getValue()));
    }

    @FXML
    public void logOut() {
        Main.redirection(ajouterProduit, "connexion.fxml");
    }

    // TODO : lien avec la base de données
    public void ajouterProduit(String categorie, String nom, String url, String description, int quantite) {
        // TODO : vérifier que les champs sont bien remplis

        // Redirection vers la page principale du manager
        Main.redirection(ajouterProduit, "manager.fxml");
    }
}
