package com.example.unitproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserDetailsActivity extends AppCompatActivity {

    TextView txt_user_details;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        firestore = FirebaseFirestore.getInstance();

        DocumentReference documentReference = firestore.collection("details").document();
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Details details = documentSnapshot.toObject(Details.class);
                            String str = details.getName()
                                    + "\n" + details.getPersonal_number()
                                    + "\n" + details.getDob()
                                    + "\n" + details.getMobile()
                                    + "\n" + details.getAddress();

                            Toast.makeText(UserDetailsActivity.this, "testtttt", Toast.LENGTH_SHORT).show();

                            Log.e("test", "str values : " + str);

                            txt_user_details.setText(str);

                        }
                    }
                });

        txt_user_details = findViewById(R.id.user_detail);
    }
}
