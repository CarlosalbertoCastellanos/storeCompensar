package com.compensar.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.compensar.myapplication.models.UserModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private Button signUp;
    private EditText name, email, password;
    private TextView sigIn;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private ProgressBar progressBar;

    private static final String EMPTY_NAME_ERROR = "Name is empty!";
    private static final String EMPTY_EMAIL_ERROR = "Email is empty!";
    private static final String EMPTY_PASSWORD_ERROR = "Password is empty!";
    private static final String PASSWORD_LENGTH_ERROR = "Password must be at least 6 characters";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        signUp = findViewById(R.id.reg_btn);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email_reg);
        password = findViewById(R.id.password_reg);
        sigIn = findViewById(R.id.sign_in);

        sigIn.setOnClickListener(v -> startActivity(new Intent(RegistrationActivity.this, LoginActivity.class)));

        signUp.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            createUser();
        });
    }

    private void createUser() {
        String userName = name.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();

        // Validación de campos
        if (validateInputs(userName, userEmail, userPassword)) {
            auth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(this::onCreateUserComplete);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private boolean validateInputs(String userName, String userEmail, String userPassword) {
        if (TextUtils.isEmpty(userName)) {
            showToast(EMPTY_NAME_ERROR);
            return false;
        }
        if (TextUtils.isEmpty(userEmail)) {
            showToast(EMPTY_EMAIL_ERROR);
            return false;
        }
        if (TextUtils.isEmpty(userPassword)) {
            showToast(EMPTY_PASSWORD_ERROR);
            return false;
        }
        if (userPassword.length() < 6) {
            showToast(PASSWORD_LENGTH_ERROR);
            return false;
        }
        return true;
    }

    private void onCreateUserComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            String id = task.getResult().getUser().getUid();
            UserModel userModel = new UserModel(name.getText().toString().trim(), email.getText().toString().trim(), password.getText().toString().trim());
            database.getReference().child("User").child(id).setValue(userModel);
            showToast("Registration Successful");
            finish(); // Cierra la actividad después del registro
        } else {
            showToast("Error: " + task.getException().getMessage());
        }
        progressBar.setVisibility(View.GONE);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
