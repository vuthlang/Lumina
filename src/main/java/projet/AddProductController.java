package projet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class AddProductController {
    @FXML
    private TextField nomTextField = new TextField();
    @FXML
    private TextField urlTextField = new TextField();
    @FXML
    private TextArea descriptionTextArea = new TextArea();
    @FXML
    private TextField prixTextField = new TextField();
    @FXML
    private Text errorText = new Text();
    @FXML
    private ComboBox<String> categorieComboBox = new ComboBox<String>();
    @FXML
    private ComboBox<Integer> quantiteComboBox = new ComboBox<Integer>();
    @FXML
    private Button ajouterProduit = new Button();

    private String nomCategorie;
    int id_categorie = 0;

    public void initialize() {
        Firebase firebase = new Firebase();

        List<String> categories = new ArrayList<>();
        CompletableFuture<List<HashMap<String, Object>>> c = firebase.fetchCategories();
        List<HashMap<String, Object>> categoriesHashMap = new ArrayList<HashMap<String, Object>>();

        try {
            categoriesHashMap = c.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        for (HashMap<String, Object> category : categoriesHashMap) {
            String nomCategorie = (String) category.get("nom_categorie");
            categories.add(nomCategorie);
        }
        categorieComboBox.getItems().addAll(categories);

        List<Integer> nombres = new ArrayList();
        for (int i = 1; i <= 500; i++) {
            nombres.add(i);
        }
        quantiteComboBox.getItems().addAll(nombres);

        this.categorieComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.nomCategorie = newValue;
                if ("Papeterie".equals(this.nomCategorie)) {
                    this.id_categorie = 2;
                } else if ("Loisirs".equals(this.nomCategorie)) {
                    this.id_categorie = 1;
                } else if ("Produits laitiers".equals(this.nomCategorie)) {
                    this.id_categorie = 3;
                } else if ("Boissons".equals(this.nomCategorie)) {
                    this.id_categorie = 4;
                } else if ("Fruits".equals(this.nomCategorie)) {
                    this.id_categorie = 5;
                } else if ("L\u00e9gumes".equals(this.nomCategorie)) {
                    this.id_categorie = 6;
                } else if ("Electrom\u00e9nager".equals(this.nomCategorie)) {
                    this.id_categorie = 7;
                } else if ("Jouets".equals(this.nomCategorie)) {
                    this.id_categorie = 8;
                }
            }
        });

        this.ajouterProduit.setOnAction((event) -> {
            String nomCategorie = (String)this.categorieComboBox.getValue();
            if (nomCategorie != null && nomTextField.getText() != null && !nomTextField.getText().isEmpty() && urlTextField.getText() != null && !urlTextField.getText().trim().isEmpty() && descriptionTextArea.getText() != null && Double.parseDouble(prixTextField.getText()) != 0 && (int) quantiteComboBox.getValue() != 0) {
                ajouterProduit(
                    id_categorie, 
                    nomTextField.getText(),
                    urlTextField.getText(), 
                    descriptionTextArea.getText(), 
                    Double.parseDouble(prixTextField.getText()),
                    (int) quantiteComboBox.getValue());
            } else {
                errorText.setText("Veuillez remplir tous les champs.");
            }

        });
    }

    @FXML
    public void logOut() {
        Main.redirection(ajouterProduit, "connexion.fxml");
    }

    @FXML
    public void retour() {
        Main.redirection(ajouterProduit, "manager.fxml");
    }

    public void ajouterProduit(int categorie, String nom, String url, String description, double prix, int quantite) {
        Firebase produit = new Firebase();
        produit.ajouterProduit(categorie, nom, description, prix, quantite, url);

        // Redirection vers la page principale du manager
        Main.redirection(ajouterProduit, "manager.fxml");
    }
}
