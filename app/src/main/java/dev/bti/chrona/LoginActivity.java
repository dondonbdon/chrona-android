package dev.bti.chrona;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.Objects;
import java.util.logging.Logger;

import dev.bti.chrona.androidsdk.constants.CredentialType;
import dev.bti.chrona.androidsdk.dto.Credential;
import dev.bti.chrona.common.Common;
import dev.bti.chrona.util.SecureStorage;

public class LoginActivity extends AppCompatActivity {

    private AppCompatEditText loginEmailEditText;
    private AppCompatEditText loginPasswordEditText;
    private AppCompatButton loginButton;
    private LinearProgressIndicator loginProgressIndicator;
    private TextView loginForgotPassword, loginSignUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initUi();
        initClicks();
    }

    private void initUi() {
        loginEmailEditText = findViewById(R.id.loginCredentialEditText);
        loginPasswordEditText = findViewById(R.id.loginPasswordEditText);
        loginButton = findViewById(R.id.loginBtn);
        loginProgressIndicator = findViewById(R.id.loginProgressIndicator);
        loginForgotPassword = findViewById(R.id.loginForgotPassword);
        loginSignUpBtn = findViewById(R.id.loginSignUpBtn);
    }

    private void initClicks() {
        loginButton.setOnClickListener(v -> {
            if (!validate()) {
                return;
            }

            loginButton.setEnabled(false);
            loginSignUpBtn.setEnabled(false);
            loginForgotPassword.setEnabled(false);
            loginButton.setAlpha(0.4f);
            loginProgressIndicator.setVisibility(View.VISIBLE);
            loginProgressIndicator.setIndeterminate(true);

            login();
        });

        loginForgotPassword.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class)));
        loginSignUpBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));
    }

    private boolean validate() {
        boolean isValid = true;

        String email = Objects.requireNonNull(loginEmailEditText.getText()).toString().trim();

        if (email.isEmpty()) {
            loginEmailEditText.setError("Email is required");
            isValid = false;
        } else if (Common.Validators.isValidEmail(email)) {
            loginEmailEditText.setError("Email is invalid");
            isValid = false;
        } else {
            loginEmailEditText.setError(null);
        }

        String password = Objects.requireNonNull(loginPasswordEditText.getText()).toString().trim();
        if (password.isEmpty()) {
            loginPasswordEditText.setError("Password is required");
            isValid = false;
        } else {
            loginPasswordEditText.setError(null);
        }

        return isValid;
    }

    private void login() {
        Application.getHelpers().getAuthHelper().login(
                        new Credential(Objects.requireNonNull(loginEmailEditText.getText()).toString(), CredentialType.EMAIL),
                        Objects.requireNonNull(loginPasswordEditText.getText()).toString())
                .addOnSuccessListener(result -> new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    SecureStorage.saveAuthData(getApplicationContext(), result.getPayload());
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                }, 2500)).addOnFailureListener(e -> new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    loginButton.setEnabled(true);
                    loginSignUpBtn.setEnabled(true);
                    loginForgotPassword.setEnabled(true);
                    loginButton.setAlpha(1);
                    loginProgressIndicator.setVisibility(View.INVISIBLE);
                    loginProgressIndicator.setIndeterminate(false);
                    Toast.makeText(this, "Email or password are incorrect.", Toast.LENGTH_SHORT).show();
                    Logger.getLogger("Login").severe(e.toString());
                }, 2500)).execute();
    }
}