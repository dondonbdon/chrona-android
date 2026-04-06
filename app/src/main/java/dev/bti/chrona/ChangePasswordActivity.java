package dev.bti.chrona;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.logging.Logger;

import dev.bti.chrona.androidsdk.dto.Credential;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText changePasswordPasswordEditText, changePasswordConfirmPasswordEditText;
    LinearProgressIndicator forgotProgressIndicator;
    AppCompatButton changePasswordNextBtn;

    Credential credential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);

        initUi();
        initData();
        initClicks();
    }


    private void initUi() {
        changePasswordPasswordEditText = findViewById(R.id.changePasswordPasswordEditText);
        changePasswordConfirmPasswordEditText = findViewById(R.id.changePasswordConfirmPasswordEditText);
        forgotProgressIndicator = findViewById(R.id.changePasswordProgressIndicator);
        changePasswordNextBtn = findViewById(R.id.changePasswordNextBtn);
    }

    private void initData() {
        credential = getIntent().getParcelableExtra("credential");
    }

    private void initClicks() {
        changePasswordNextBtn.setOnClickListener(v -> {
            if (!validate()) {
                return;
            }

            setProgress(true);
            changePassword();
        });
    }

    private boolean validate() {
        boolean isValid = true;

        String password = changePasswordPasswordEditText.getText().toString();
        if (password.isEmpty() || password.length() < 6) {
            changePasswordPasswordEditText.setError("Password must be at least 6 characters long.");
            isValid = false;
        } else {
            changePasswordPasswordEditText.setError(null);
        }

        String passwordConfirm = changePasswordConfirmPasswordEditText.getText().toString();
        if (!passwordConfirm.equals(password)) {
            changePasswordConfirmPasswordEditText.setError("Password must be at least 6 characters long.");
            isValid = false;
        } else {
            changePasswordConfirmPasswordEditText.setError(null);
        }

        return isValid;
    }

    private void setProgress(boolean show) {
        changePasswordNextBtn.setEnabled(!show);
        forgotProgressIndicator.setIndeterminate(show);

        if (show) {
            changePasswordNextBtn.setAlpha(0.4f);
            forgotProgressIndicator.setVisibility(View.VISIBLE);
        } else {
            changePasswordNextBtn.setAlpha(1);
            forgotProgressIndicator.setVisibility(View.INVISIBLE);
        }
    }

    private void changePassword() {
        Application.getHelpers()
                .getAuthHelper().resetPassword(credential,
                        changePasswordConfirmPasswordEditText.getText().toString())
                .addOnSuccessListener(result -> {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> startActivity(new Intent(getApplicationContext(), LoginActivity.class)), 2500);
                    setProgress(false);
                }).addOnFailureListener(e -> {
                    Logger.getLogger("Change Password").severe(e.getMessage());
                    setProgress(false);
                }).execute();
    }
}