package projet;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        URL url = Paths.get("src/main/resources/projet/connexion.fxml").toUri().toURL();
		Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root);
        // TODO : changer le titre de la page
        stage.setTitle("Lumina");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
    }

    /*public static void changement_page(Button bouton, String page){
        bouton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(page));
                    Parent root = fxmlLoader.load();
                    Stage stage = (Stage) bouton.getScene().getWindow();
                    stage.setScene(new Scene(root));
                } catch (IOException e) {
                    System.out.println("Fichier introuvable : \"" + page + "\"");
                }
            }
        });
    }*/

    public static void main(String[] args) {
        launch();
    }
    
}