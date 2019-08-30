package com.example.unitproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText txt_email, txt_password;
    private Button signUp;
    private TextView signIn;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        txt_email = findViewById(R.id.editTextEmail);
        txt_password = findViewById(R.id.editTextPassword);
        signUp = findViewById(R.id.buttonSignup);
        signIn = findViewById(R.id.textSignin);

        signUp.setOnClickListener(this);
        signIn.setOnClickListener(this);

    }

    public void registerUser() {
        String email = txt_email.getText().toString().trim();
        final String password = txt_password.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            //email is empty
            Toast.makeText(this, "Enter email address", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            //password is empty
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        //Show progress bar
        progressDialog.setMessage("Registering please wait...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Registration success & start login activity
                            Log.d("Firebase", "Successfully Registered");
                            Toast.makeText(RegisterActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registeration failed", Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                            txt_password.setText("");
                        }
                    }
                });

    }


    @Override
    public void onClick(View view) {
        if (view == signUp) {
            registerUser();
        }
        if (view == signIn) {
            //will open login activity
            startActivity(new Intent(this, MainActivity.class));
        }

    }
}
