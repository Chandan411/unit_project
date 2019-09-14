package com.example.unitproject;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class ShowDetailsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference detailsRef;

    private DetailsAdapter adapter;

    RecyclerView recyclerView;

    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .setPersistenceEnabled(true)
            .build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        setUpRecyclerView();

    }

    private void setUpRecyclerView() {
        db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(settings);

        detailsRef = db.collection("details");

        Query query = detailsRef.orderBy("name",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Details> options = new FirestoreRecyclerOptions.Builder<Details>()
                .setQuery(query,Details.class)
                .build();

        db.collection("details")
                .addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("test", "Listen error", e);
                            return;
                        }

                        for (DocumentChange change : querySnapshot.getDocumentChanges()) {
                            if (change.getType() == DocumentChange.Type.ADDED) {
                                Log.d("test", "USER DETAILS:" + change.getDocument().getData());
                            }

                            String source = querySnapshot.getMetadata().isFromCache() ?
                                    "local cache" : "server";
                            Log.d("test", "Data fetched from " + source);
                        }

                    }
                });

        adapter = new DetailsAdapter(options);

        recyclerView  = findViewById(R.id.recyclerview_details);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
