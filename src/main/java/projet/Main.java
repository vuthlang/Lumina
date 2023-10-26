package projet;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        URL url = Paths.get("src/main/resources/projet/connexion.fxml").toUri().toURL();
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root);
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

    public static void redirection(Button bouton, String page) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(page));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) bouton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Firebase firebase = new Firebase();
        firebase.initialize();
        // firebase.ajouterProduit(1, "Aiguilles", "2mm", 1.50, 10, "https://cdn0.rascol.com/146215/465x465/cable-pivotant-swivel-pour-aiguilles-knitpro-80-cm.webp");

        launch();
    }

}