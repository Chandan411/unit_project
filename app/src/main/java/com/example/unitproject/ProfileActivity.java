package com.example.unitproject;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import androidx.cardview.widget.CardView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton floatingActionButton, fab_markAttendance, fab_showdata;
    EditText txt_name, txt_personal_no, txt_mobile, txt_dob, txt_address;
    Button btn_save, btn_present, btn_civil;
    TextView txt_viewDetails, txt_dateTime, txt_user_detail;
    LinearLayout add_detail_layout, attendace_layout;
    private Toolbar mTopToolbar;
    private FirebaseFirestore firestore;
    CardView cardviewDetails;

    String attendance_type;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firestore = FirebaseFirestore.getInstance();
       /* FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);*/

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
        fab_markAttendance = findViewById(R.id.fab_markAttendance);
        fab_showdata = findViewById(R.id.fab_showdata);
        add_detail_layout = findViewById(R.id.layout_profile_detail);
        attendace_layout = findViewById(R.id.layout_attendance);
        txt_dateTime = findViewById(R.id.current_date_view);
        txt_user_detail = findViewById(R.id.user_detail);
        btn_present = findViewById(R.id.present);
        btn_civil = findViewById(R.id.civil);
        cardviewDetails = findViewById(R.id.cardView_details);


        btn_save.setOnClickListener(this);
        btn_present.setOnClickListener(this);
        btn_civil.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);
        txt_viewDetails.setOnClickListener(this);
        fab_markAttendance.setOnClickListener(this);
        fab_showdata.setOnClickListener(this);

        txt_user_detail.setVisibility(View.GONE);

        cardviewDetails.setVisibility(View.VISIBLE);

        YoYo.with(Techniques.FadeIn)
                .duration(700)
                .playOn(cardviewDetails);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel("MyNotifications", "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successful";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                        Toast.makeText(ProfileActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent i = getIntent();
        String name = i.getStringExtra("user_name");
        String personal_no = i.getStringExtra("user_personal");
        String mobile = i.getStringExtra("user_mobile");
        String dob = i.getStringExtra("user_dob");
        String address = i.getStringExtra("user_address");
        txt_user_detail.setText("DETAILS : "
                + "\nName : " + name
                + "\nPersonal No. : " + personal_no
                + "\nMobile : " + mobile
                + "\nDOB : " + dob
                + "\nAddress : " + address);
        txt_user_detail.setVisibility(View.VISIBLE);
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
            DocumentReference dbDetails = firestore.collection("details").document(name);
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

                dbDetails.set(details).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
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

    //=======================UPDATE USER DETAILS======================
    public void updateDetails() {
        Intent i = getIntent();
        String fetchedName = i.getStringExtra("user_name");
        String fetchedPersonalNumber = i.getStringExtra("user_personal");

        add_detail_layout.setVisibility(View.VISIBLE);
        attendace_layout.setVisibility(View.GONE);
        YoYo.with(Techniques.FadeIn)
                .duration(700)
                .playOn(add_detail_layout);

        txt_name.setText(fetchedName);
        txt_personal_no.setText(fetchedPersonalNumber);


        String name = txt_name.getText().toString().trim();
        String personal_number = txt_personal_no.getText().toString().trim();
        String mobile = txt_mobile.getText().toString().trim();
        String dob = txt_dob.getText().toString().trim();
        String address = txt_address.getText().toString().trim();

        if (!validateInputs(name, personal_number, mobile, dob, address)) {
            DocumentReference dbDetails = firestore.collection("details").document(name);
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

                dbDetails.set(details, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
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

    //=======================MARK ATTENDANCE OF THE USER======================
    public void markAttendance() {
        Intent i = getIntent();
        String name = i.getStringExtra("user_name");
        String personal_no = i.getStringExtra("user_personal");

        Date currentTime = Calendar.getInstance().getTime();

        i.putExtra("attendance_date", currentTime);
        Log.e("test", "date result:" + currentTime);

        CollectionReference dbAttendance = firestore.collection("user-attendance").document(name).collection("attendance");
        //CollectionReference dbAttendance = firestore.collection("attendance");
        final Map<String, Object> attend = new HashMap<>();
        attend.put("name", name);
        attend.put("personal_number", personal_no);
        attend.put("attendance", currentTime);
        attend.put("type", attendance_type);


        dbAttendance.add(attend).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(ProfileActivity.this, "Attendance Marked Successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ProfileActivity.this, UserDetailsActivity.class);
                startActivity(i);
            }
        });
    }

    public void captureDateTime() {
        markAttendance();
        Date currentTime = Calendar.getInstance().getTime();

        String simpleDate = new SimpleDateFormat("MM-dd-yyyy hh:mm a").format(currentTime);
        Log.v("output date ", simpleDate);

        txt_dateTime.setText("Date & Time : " + simpleDate);
    }

    @Override
    public void onClick(View view) {
        if (view == btn_save) {
            saveDetails();
        }

        if (view == btn_present) {
            attendance_type = "vardi";
            captureDateTime();   //Adding data to database

        }
        if (view == btn_civil) {
            attendance_type = "civil";
            captureDateTime();    //Adding data to database
        }
        if (view == floatingActionButton) {
            add_detail_layout.setVisibility(View.VISIBLE);
            attendace_layout.setVisibility(View.GONE);
            YoYo.with(Techniques.FadeIn)
                    .duration(700)
                    .playOn(add_detail_layout);
        }

        if (view == fab_markAttendance) {
            add_detail_layout.setVisibility(View.GONE);
            attendace_layout.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.FadeIn)
                    .duration(700)
                    .playOn(attendace_layout);

        }
        if (view == fab_showdata) {
            // Toast.makeText(this, "view details clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ShowDetailsActivity.class));
        }
        if (view == txt_viewDetails) {
            // Toast.makeText(this, "view details clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ShowDetailsActivity.class));

        }
    }
}
