
package projet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public class CaissierController {
    @FXML
    private GridPane gridPane = new GridPane();

    @FXML
    private Button logOut = new Button();

    @FXML
    private Text numero = new Text();
    @FXML
    private Text categorie = new Text();
    @FXML
    private Text nom = new Text();

    private Firebase firebase = new Firebase();

    public void initialize() {

        loadProductsAndCategories("");
    }

    public void loadProductsAndCategories(String sortKey) {

        CompletableFuture<List<HashMap<String, Object>>> p = firebase.fetchProduits();
        CompletableFuture<List<HashMap<String, Object>>> c = firebase.fetchCategories();

        List<HashMap<String, Object>> produits = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> categories = new ArrayList<HashMap<String, Object>>();

        try {
            produits = p.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        try {
            categories = c.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        if (produits.get(0) == null) {
            produits.remove(0);
        }

        if (!sortKey.equals("")) {
            produits = sortListOfHashMapsBySpecificKey(produits, sortKey);
        }

        actualisation(categories, produits);

    }

    public String getNomCategorie(int id_categorie, List<HashMap<String, Object>> categories) {
        String nomCategorieTrouvee = "";
        for (HashMap<String, Object> categorie : categories) {
            String id = (String) categorie.get("id");
            if (Integer.parseInt(id) == id_categorie) {
                nomCategorieTrouvee = (String) categorie.get("nom_categorie");
                break;
            }
        }

        return nomCategorieTrouvee;
    }

    public void ajoutText(String data, int column, int row) {
        TextFlow textFlow = new TextFlow();
        textFlow.setTextAlignment(TextAlignment.CENTER);
        Text text = new Text(data);
        textFlow.getChildren().add(text);
        GridPane.setHalignment(textFlow, HPos.CENTER);
        gridPane.add(textFlow, column, row);
    }

    public static List<HashMap<String, Object>> sortListOfHashMapsBySpecificKey(List<HashMap<String, Object>> list,
            String sortByKey) {
        list.sort(new Comparator<HashMap<String, Object>>() {
            @Override
            public int compare(HashMap<String, Object> map1, HashMap<String, Object> map2) {
                if (map1.containsKey(sortByKey) && map2.containsKey(sortByKey)) {
                    Comparable<Object> value1 = (Comparable<Object>) map1.get(sortByKey);
                    Comparable<Object> value2 = (Comparable<Object>) map2.get(sortByKey);
                    return value1.compareTo(value2);
                }
                return 0;
            }
        });

        return list;
    }

    public void actualisation(List<HashMap<String, Object>> categories, List<HashMap<String, Object>> produits) {
        gridPane.getRowConstraints().clear();
        gridPane.getChildren().clear();

        List<Integer> nombres = new ArrayList<>();
        for (int i = 0; i <= 500; i++) {
            nombres.add(i);
        }

        for (HashMap<String, Object> produit : produits) {
            if (produit != null) {
                afficheUnProduit(produit, categories, nombres);
            }
        }
    }

    public void afficheUnProduit(HashMap<String, Object> produit, List<HashMap<String, Object>> categories,
            List<Integer> nombres) {
        int newRow = gridPane.getRowCount();

        int id = Integer.parseInt(produit.get("id").toString());
        Object categorieObject = produit.get("id_categorie");
        String categorie = (categorieObject != null)
                ? getNomCategorie(Integer.parseInt(categorieObject.toString()), categories)
                : "";
        String nom_produit = (String) produit.get("nom_produit");
        String prix = produit.get("prix").toString() + "€";
        String descriptif = (String) produit.get("descriptif");
        String quantite = produit.get("quantite").toString();

        ajoutText(String.valueOf(id), 0, newRow);
        ajoutText(categorie, 1, newRow);
        ajoutText(nom_produit, 2, newRow);
        ajoutText(prix, 3, newRow);
        ajoutText(descriptif, 4, newRow);
        ajoutText(quantite, 5, newRow);

        String style0 = "-fx-background-color: #c3c4c4, linear-gradient(#d6d6d6 50%, white 100%), radial-gradient(center 50% -40%, radius 200%, #e6e6e6 45%, rgba(230,230,230,0) 50%); -fx-background-radius: 30; -fx-background-insets: 0,1,1; -fx-text-fill: black; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 3, 0.0, 0, 1);";

        ComboBox<Integer> quantiteComboBox = new ComboBox<>(FXCollections.observableArrayList(nombres));
        quantiteComboBox.setId(String.valueOf(id));
        quantiteComboBox.getSelectionModel().select(0);
        quantiteComboBox.setStyle(style0);

        Button trashButton = new Button();
        ImageView trashImage = new ImageView(new Image("projet/trash.png"));
        trashImage.setFitWidth(15);
        trashImage.setFitHeight(15);
        trashButton.setGraphic(trashImage);
        trashButton.setOnAction(event -> supprimerStock(id, quantiteComboBox.getValue()));
        String style = "-fx-background-color: white; -fx-cursor: hand;";
        trashButton.setStyle(style);

        HBox hBox = new HBox(10, quantiteComboBox, trashButton);
        hBox.setAlignment(Pos.CENTER);

        StackPane stackPane = new StackPane(new Region(), hBox);
        stackPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        gridPane.add(stackPane, 6, newRow);
        gridPane.add(new Separator(), 0, newRow + 1, 7, 1);
    }

    @FXML
    public void logOut() {
        Main.redirection(logOut, "connexion.fxml");
    }

    @FXML
    public void nomProduitClick() {
        loadProductsAndCategories("nom_produit");
    }

    @FXML
    public void categorieClick() {
        loadProductsAndCategories("id_categorie");
    }

    @FXML
    public void numeroClick() {
        loadProductsAndCategories("id");
    }

    // TODO : Supprime du stock
    public void supprimerStock(int id, int quantite) {
        System.out.println(id + " " + quantite);
        // TODO : mettre à jour la page à la fin de la fonction
    }

}