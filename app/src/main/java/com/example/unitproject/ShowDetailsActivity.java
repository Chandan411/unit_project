package com.example.unitproject;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
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
    SearchView searchView;

    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .setPersistenceEnabled(true)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(settings);

        detailsRef = db.collection("details");


        Client client = new Client("YourApplicationID", "YourAPIKey");
        Index index = client.getIndex("your_index_name");

        setUpRecyclerView();
        ItemClicked();

    }

    private void setUpRecyclerView() {

        Query query = detailsRef.orderBy("name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Details> options = new FirestoreRecyclerOptions.Builder<Details>()
                .setQuery(query, Details.class)
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

        recyclerView = findViewById(R.id.recyclerview_details);
        //recyclerView.setHasFixedSize(true);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = findViewById(R.id.search_bar);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

               /* Query query_search = db.collection("details");
                query_search.whereEqualTo("name",query);*/

                search(query);
                adapter.startListening();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // getSearch(newText);
                return false;

            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }

    public void ItemClicked() {
        adapter.setOnItemClickListener(new DetailsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Details details = documentSnapshot.toObject(Details.class);
                String id = documentSnapshot.getId();
                details.getName();
                details.getAddress();
                Log.d("test", "Name :" + details.getName() + " Address :" + details.getAddress());
               /* Toast.makeText(ShowDetailsActivity.this, "Name :" + details.getName()
                                + "Personal No. :" + details.getPersonal_number()
                                + "Mobile : " + details.getMobile()
                                + "DOB :" + details.getDob()
                                + "Address :" + details.getAddress()
                        , Toast.LENGTH_SHORT).show();*/

                Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                i.putExtra("user_name", details.getName());
                i.putExtra("user_personal", String.valueOf(details.getPersonal_number()));
                i.putExtra("user_mobile", String.valueOf(details.getMobile()));
                i.putExtra("user_dob", details.getDob());
                i.putExtra("user_address", details.getAddress());
                startActivity(i);
            }
        });
    }

    public void search(String s) {

        Query query = db.collection("details").whereEqualTo("name", s);

        final FirestoreRecyclerOptions<Details> options = new FirestoreRecyclerOptions.Builder<Details>()
                .setQuery(query, Details.class)
                .build();

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                adapter = new DetailsAdapter(options);
                recyclerView.swapAdapter(adapter, true);
                adapter.notifyDataSetChanged();
                adapter.startListening();
            }
        });
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
