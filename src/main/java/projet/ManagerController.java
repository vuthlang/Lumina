package projet;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class ManagerController {
    @FXML
    private GridPane gridPane = new GridPane();

    @FXML
    private Button addProductButton = new Button();

    public void initialize() {
        // TODO : récupérer les produits de la base pour tous les afficher
        String[] data = {"1", "Fruits", "Pomme", "5,50", "Super pomme", "2" };
        for (int i = 0; i < 39; i++) {
            afficherProduits(data);
        }

        addProductButton.setOnAction(event -> {
            Main.redirection(addProductButton, "addProduit.fxml");
        });
    }

    public void afficherProduits(String[] data) {
        // TODO : mettre l'id en fonction du produit
        int id = 4;

        int newRow = gridPane.getRowCount();

        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPrefHeight(500);

        gridPane.getRowConstraints().add(rowConstraints);

        for (int i = 0; i < data.length; i++) {
            Text text = new Text(data[i]);
            text.setTextAlignment(TextAlignment.CENTER);
            GridPane.setHalignment(text, HPos.CENTER);
            gridPane.add(text, i, newRow);
        }

        HBox hBox = new HBox();

        TextField textField = new TextField();
        hBox.getChildren().add(textField);

        Button trashButton = new Button();
        ImageView trashImage = new ImageView(new Image("projet/trash.png"));
        trashButton.setGraphic(trashImage);
        trashImage.setFitWidth(15);
        trashImage.setFitHeight(15);
        trashButton.setOnAction(event -> supprimerStock(id, Integer.parseInt(textField.getText())));
        hBox.getChildren().add(trashButton);

        Button addButton = new Button();
        ImageView addImage = new ImageView(new Image("projet/add.png"));
        addButton.setGraphic(addImage);
        addImage.setFitWidth(15);
        addImage.setFitHeight(15);
        addButton.setOnAction(event -> ajouterStock(id, Integer.parseInt(textField.getText())));
        hBox.getChildren().add(addButton);

        gridPane.add(hBox, 6, newRow);

        gridPane.add(new Separator(), 0, newRow + 1, 6, 1);
    }

    @FXML
    public void logOut() {
        Main.redirection(addProductButton, "connexion.fxml");
    }

    // TODO : Supprime du stock
    public void supprimerStock(int id, int quantite) {
        // TODO : mettre à jour la page à la fin de la fonction
    }

    // TODO : Ajoute du stock
    public void ajouterStock(int id, int quantite) {
        // TODO : mettre à jour la page à la fin de la fonction
    }

}
