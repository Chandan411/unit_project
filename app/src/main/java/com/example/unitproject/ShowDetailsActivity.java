package com.example.unitproject;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShowDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    private DetailsAdapter adapter;
    private List<Details> detailsList;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        firestore = FirebaseFirestore.getInstance();

        progressBar = findViewById(R.id.progressbar);

        recyclerView = findViewById(R.id.recyclerview_details);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        detailsList = new ArrayList<>();
        adapter = new DetailsAdapter(this, detailsList);

        recyclerView.setAdapter(adapter);

        firestore.collection("details").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        progressBar.setVisibility(View.GONE);

                        if (!queryDocumentSnapshots.isEmpty()) {
                            //Document is not empty
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot : list) {

                                Details details = documentSnapshot.toObject(Details.class);
                                detailsList.add(details);

                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}
