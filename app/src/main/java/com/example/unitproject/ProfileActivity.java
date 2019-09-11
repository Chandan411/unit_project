package com.example.unitproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton floatingActionButton, fab2;
    EditText txt_name, txt_personal_no, txt_mobile, txt_dob, txt_address;
    Button btn_save, btn_present, btn_civil;
    TextView txt_viewDetails, txt_dateTime;
    LinearLayout add_detail_layout, attendace_layout;
    private Toolbar mTopToolbar;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);

        mTopToolbar = findViewById(R.id.toolbar);
        mTopToolbar.setTitle("Add Sewadal Member Detail");
        mTopToolbar.setTitleTextColor(83669);

        txt_name = findViewById(R.id.edittext_name);
        txt_personal_no = findViewById(R.id.edittext_personal_no);
        txt_mobile = findViewById(R.id.edittext_mobile);
        txt_dob = findViewById(R.id.edittext_dob);
        txt_address = findViewById(R.id.edittext_address);
        btn_save = findViewById(R.id.button_save);
        txt_viewDetails = findViewById(R.id.textview_view_details);
        floatingActionButton = findViewById(R.id.fab);
        fab2 = findViewById(R.id.fab2);
        add_detail_layout = findViewById(R.id.layout_profile_detail);
        attendace_layout = findViewById(R.id.layout_attendance);
        txt_dateTime = findViewById(R.id.current_date_view);
        btn_present = findViewById(R.id.present);
        btn_civil = findViewById(R.id.civil);


        btn_save.setOnClickListener(this);
        btn_present.setOnClickListener(this);
        btn_civil.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);
        txt_viewDetails.setOnClickListener(this);
        fab2.setOnClickListener(this);
    }

    private boolean validateInputs(String name, String personal_no, String mobile, String dob, String address) {
        if (name.isEmpty()) {
            txt_name.setError("Name Required");
            return true;
        }
        if (personal_no.isEmpty()) {
            txt_personal_no.setError("Personal Number Required");
            return true;
        }
        if (mobile.isEmpty()) {
            txt_mobile.setError("Mobile Number Required");
            return true;
        }
        if (dob.isEmpty()) {
            txt_dob.setError("DOB Required");
            return true;
        }
        if (address.isEmpty()) {
            txt_address.setError("Address Required");
            return true;
        }
        return false;
    }

    public void saveDetails() {
        String name = txt_name.getText().toString().trim();
        String personal_number = txt_personal_no.getText().toString().trim();
        String mobile = txt_mobile.getText().toString().trim();
        String dob = txt_dob.getText().toString().trim();
        String address = txt_address.getText().toString().trim();

        if (!validateInputs(name, personal_number, mobile, dob, address)) {
            CollectionReference dbDetails = firestore.collection("details");
            try {

                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                Date date = df.parse(dob);

                Details details = new Details(
                        name,
                        Integer.parseInt(personal_number),
                        Long.parseLong(mobile),
                        date,
                        address
                );

                dbDetails.add(details)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(ProfileActivity.this, "Details Added Successfully", Toast.LENGTH_SHORT).show();
                                txt_name.getText().clear();
                                txt_personal_no.getText().clear();
                                txt_dob.getText().clear();
                                txt_mobile.getText().clear();
                                txt_address.getText().clear();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (NumberFormatException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void captureDateTime() {
        Date currentTime = Calendar.getInstance().getTime();
        txt_dateTime.setText("Date & Time : " + currentTime);
    }

    @Override
    public void onClick(View view) {
        if (view == btn_save) {
            saveDetails();
        }

        if (view == btn_present || view == btn_civil) {
            captureDateTime();
        }
        if (view == floatingActionButton) {
            add_detail_layout.setVisibility(View.VISIBLE);
            attendace_layout.setVisibility(View.GONE);
            YoYo.with(Techniques.FadeIn)
                    .duration(700)
                    .playOn(add_detail_layout);
        }

        if (view == fab2) {
            add_detail_layout.setVisibility(View.GONE);
            attendace_layout.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.FadeIn)
                    .duration(700)
                    .playOn(attendace_layout);

        }
        if (view == txt_viewDetails) {
            // Toast.makeText(this, "view details clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ShowDetailsActivity.class));
        }
    }
}
