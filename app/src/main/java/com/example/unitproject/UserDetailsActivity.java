package com.example.unitproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class UserDetailsActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    TextView txt_user_details, attendance_count;
    String TAG = "UNIT PROJECT";
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        sharedPreferences = getSharedPreferences("myprefs", Context.MODE_PRIVATE);

        txt_user_details = findViewById(R.id.user_data);
        attendance_count = findViewById(R.id.attendance_count);

        Intent i = getIntent();
        final String name = i.getStringExtra("user_name");
        final String personal_no = i.getStringExtra("user_personal");
        final String mobile = i.getStringExtra("user_mobile");
        final String dob = i.getStringExtra("user_dob");
        final String address = i.getStringExtra("user_address");

        final String username = sharedPreferences.getString("username", "");

        this.db = FirebaseFirestore.getInstance();
        CollectionReference dbAttendance = db.collection("user-attendance").document(username).collection("attendance");
        dbAttendance.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                queryDocumentSnapshots.getDocuments();
                int size = queryDocumentSnapshots.size();
                Log.e(TAG, "Number of Documents : " + queryDocumentSnapshots.size());

                txt_user_details.setText("Name : " + username);
                                       /* +"\nPersonal No : "+personal_no
                                        +"\nMobile : "+mobile
                                        +"\nDate Of Birth : "+dob
                                        +"\nAddress : "+address);*/
                attendance_count.setText("Total Attendance : " + size);
            }
        });

    }


}
