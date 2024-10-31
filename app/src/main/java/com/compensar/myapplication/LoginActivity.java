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

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button signIn;
    private EditText email, password;
    private TextView signUp;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    private static final String EMPTY_EMAIL_ERROR = "Email is empty!";
    private static final String EMPTY_PASSWORD_ERROR = "Password is empty!";
    private static final String PASSWORD_LENGTH_ERROR = "Password length must be at least 6 characters";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        signIn = findViewById(R.id.login_btn);
        email = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);
        signUp = findViewById(R.id.sign_up);

        signUp.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegistrationActivity.class)));

        signIn.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            loginUser();
        });
    }

    private void loginUser() {
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();

        // Validación de campos
        if (validateLoginInputs(userEmail, userPassword)) {
            auth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(this::onLoginComplete);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private boolean validateLoginInputs(String userEmail, String userPassword) {
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

    private void onLoginComplete(@NonNull Task<AuthResult> task) {
        progressBar.setVisibility(View.GONE);
        if (task.isSuccessful()) {
            showToast("Login Successfully");
            // Navega a la siguiente actividad
        } else {
            showToast("Error: " + task.getException().getMessage());
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
