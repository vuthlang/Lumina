package projet;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Firebase {

    private boolean isDataReady = false;
    private int lastKey;

    public void initialize() {
        try {
            // Charger le fichier JSON
            FileInputStream serviceAccount = new FileInputStream(
                    "./lumina-85044-firebase-adminsdk-a1dkh-59391434d2.json");

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

    public interface KeyCallback {
        void onKeyReceived(int key);
    }

    public void getKey(final KeyCallback callback) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Produit");

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

    public void ajouterProduit(int categorie, String produit, String descriptif, double prix, int quantite,
            String url) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Produit");

        getKey(new KeyCallback() {
            @Override
            public void onKeyReceived(int key) {
                if (isDataReady) {
                    Map<String, Object> produitMap = new HashMap<>();
                    produitMap.put("id", key + 1);
                    produitMap.put("id_categorie", categorie);
                    produitMap.put("nom_produit", produit);
                    produitMap.put("descriptif", descriptif);
                    produitMap.put("prix", prix);
                    produitMap.put("quantite", quantite);
                    produitMap.put("url_image", url);
                    ref.child(String.valueOf(key + 1)).setValueAsync(produitMap);
                } else {
                    System.out.println("Data non disponible");
                }
            }
        });
    }

    public CompletableFuture<List<HashMap<String, Object>>> fetchProduits() {
        CompletableFuture<List<HashMap<String, Object>>> completableFuture = new CompletableFuture<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Produit");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<HashMap<String, Object>> produits = new ArrayList<>();
                produits = (List<HashMap<String, Object>>) dataSnapshot.getValue();
                completableFuture.complete(produits); // Complète le CompletableFuture avec les données
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Échec de la lecture des données
                System.err.println("Failed to read value from Firebase: " + error.getMessage());
                completableFuture.completeExceptionally(error.toException());
            }
        });

        return completableFuture;
    }

    public CompletableFuture<List<HashMap<String, Object>>> fetchCategories() {
        CompletableFuture<List<HashMap<String, Object>>> completableFuture = new CompletableFuture<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categorie");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<HashMap<String, Object>> categories = new ArrayList<>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String id = childSnapshot.getKey();
                    String nom_categorie = childSnapshot.child("nom_categorie").getValue(String.class);
                    HashMap<String, Object> categorie = new HashMap<String, Object>();
                    categorie.put("id", id);
                    categorie.put("nom_categorie", nom_categorie);
                    categories.add(categorie);
                }
                completableFuture.complete(categories);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Échec de la lecture des données
                System.err.println("Failed to read value from Firebase: " + error.getMessage());
                completableFuture.completeExceptionally(error.toException());
            }
        });

        return completableFuture;
    }

    public void ajouterAuStock(String produitId, int quantiteAjoutee) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Produit").child(produitId)
                .child("quantite");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int quantiteActuelle = dataSnapshot.getValue(Integer.class);
                    int nouvelleQuantite = quantiteActuelle + quantiteAjoutee;

                    ref.setValue(Integer.valueOf(nouvelleQuantite), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                System.out.println(
                                        "Erreur lors de la mise à jour du stock: " + databaseError.getMessage());
                            } else {
                                System.out.println(
                                        "Stock mis à jour avec succès. Nouvelle quantité: " + nouvelleQuantite);
                            }
                        }
                    });
                } else {
                    System.out.println("Produit non trouvé.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Erreur lors de la lecture de la base de données: " + databaseError.getMessage());
            }
        });
    }
}
