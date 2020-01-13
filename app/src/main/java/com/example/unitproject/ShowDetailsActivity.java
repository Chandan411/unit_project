package com.example.unitproject;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
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
    SharedPreferences sharedPreferences;

    Button btn_ads;

    RecyclerView recyclerView;
    SearchView searchView;
    private RewardedVideoAd mRewardedVideoAd;

    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .setPersistenceEnabled(true)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        sharedPreferences = getSharedPreferences("myprefs", Context.MODE_PRIVATE);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);


        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewarded(RewardItem rewardItem) {
                Toast.makeText(getBaseContext(), "You get reward of : " + rewardItem.getAmount(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdLoaded() {
                Toast.makeText(getBaseContext(), "Ad loaded.", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRewardedVideoAdOpened() {
                Toast.makeText(getBaseContext(), "Ad opened.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoStarted() {
                Toast.makeText(getBaseContext(), "Ad started.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdClosed() {
                Toast.makeText(getBaseContext(), "Ad closed.", Toast.LENGTH_SHORT).show();
                mRewardedVideoAd.loadAd(getString(R.string.ad_unit_id), new AdRequest.Builder().build());

            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                Toast.makeText(getBaseContext(), "Ad left application.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                Toast.makeText(getBaseContext(), "Ad failed to load.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoCompleted() {
                Toast.makeText(getBaseContext(), "Reward Completed", Toast.LENGTH_SHORT).show();
            }
        });

        //Load Rewarded Ad
        mRewardedVideoAd.loadAd(getString(R.string.ad_unit_id), new AdRequest.Builder().build());

        btn_ads = findViewById(R.id.ads_btn);
        btn_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                }
            }
        });

        db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(settings);


        detailsRef = db.collection("details");

        setUpRecyclerView();

        SeeData();
        // ItemClicked();

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

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // getSearch(newText);
                search(newText);
                adapter.startListening();
                return false;

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        ItemClicked();
    }

    public void ItemClicked() {
        adapter.setOnItemClickListener(new DetailsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Details details = documentSnapshot.toObject(Details.class);
                String id = documentSnapshot.getId();

                Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                i.putExtra("user_name", details.getName());
                i.putExtra("user_personal", String.valueOf(details.getPersonal_number()));
                i.putExtra("user_mobile", String.valueOf(details.getMobile()));
                i.putExtra("user_dob", String.valueOf(details.getDob()));
                i.putExtra("user_address", details.getAddress());
                startActivity(i);

                //Putting users data(only one user) into sharedPrefs on click of UsersList
                sharedPreferences.edit().putString("username", details.getName()).apply();
                sharedPreferences.edit().putString("personal_no", String.valueOf(details.getPersonal_number())).apply();
                sharedPreferences.edit().putString("mobile", String.valueOf(details.getMobile())).apply();
                sharedPreferences.edit().putString("dob", String.valueOf(details.getDob())).apply();
                sharedPreferences.edit().putString("address", String.valueOf(details.getAddress())).apply();


               /* Intent intent = new Intent(getApplicationContext(), UserDetailsActivity.class);
                startActivity(intent);*/
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
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                //recyclerView.swapAdapter(adapter, true);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                adapter.startListening();
                ItemClicked();

            }
        });
    }

    public void SeeData() {

        Intent i = getIntent();
        String dateTime = i.getStringExtra("attendance_date");

        final DocumentReference documentReference = db.collection("details").document("name");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null) {

                    Log.e("test", "docValue :" + documentSnapshot.getString("name"));
                    Log.e("test", "docValue :" + documentSnapshot.getString("attendance"));
                }
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
