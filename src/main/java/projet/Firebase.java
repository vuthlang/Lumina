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
import java.util.HashMap;
import java.util.Map;

public class Firebase {

    private boolean isDataReady = false;
    private int lastKey; 

    public void initialize() {
        try {
            // Charger le fichier JSON
            FileInputStream serviceAccount = new FileInputStream("./lumina-85044-firebase-adminsdk-a1dkh-59391434d2.json");

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

    public interface KeyCallback {
        void onKeyReceived(int key);
    }

    public void getKey(final KeyCallback callback) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("ProduitTest");

        ref.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    lastKey = Integer.parseInt(childSnapshot.getKey());
                }
                isDataReady = true;
                callback.onKeyReceived(lastKey);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public void ajouterProduit(int categorie, String produit, String descriptif, double prix, int quantite, String url) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("ProduitTest");

        getKey(new KeyCallback() {
            @Override
            public void onKeyReceived(int key) {
                if (isDataReady) {
                    Map<String, Object> produitMap = new HashMap<>();
                    produitMap.put("id", key+1);
                    produitMap.put("id_categorie", categorie);
                    produitMap.put("nom_produit", produit);
                    produitMap.put("descriptif", descriptif);
                    produitMap.put("prix", prix);
                    produitMap.put("quantite", quantite);
                    produitMap.put("url_image", url);
                    ref.child(String.valueOf(key+1)).setValueAsync(produitMap);
                } else {
                    System.out.println("Data non disponible");
                }
            }
        });
    }
}
