package projet;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.IOException;

public class Firebase {
    public void initialize() {
        try {
            // Charger le fichier JSON
            FileInputStream serviceAccount = new FileInputStream(".\\lumina-85044-firebase-adminsdk-a1dkh-59391434d2.json");

            // Initialiser les options de Firebase
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://lumina-85044-default-rtdb.europe-west1.firebasedatabase.app")                    
                    .build();
            // Initialiser Firebase avec les options
            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fetchProduits() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Produit");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.getValue();
                System.out.println("Data: \n" + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                System.err.println("Failed to read value from Firebase: " + error.getMessage());
            }
        });
    }
}
