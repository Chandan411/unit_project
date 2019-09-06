package com.example.unitproject;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShowDetailsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference detailsRef;

    private DetailsAdapter adapter;

    RecyclerView recyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);
        

        db = FirebaseFirestore.getInstance();
        detailsRef =  db.collection("details");

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = detailsRef.orderBy("name",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Details> options = new FirestoreRecyclerOptions.Builder<Details>()
                .setQuery(query,Details.class)
                .build();

        adapter = new DetailsAdapter(options);

        recyclerView  = findViewById(R.id.recyclerview_details);
        recyclerView.setHasFixedSize(true);
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
