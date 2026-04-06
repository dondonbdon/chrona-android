package dev.bti.chrona;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.List;

import dev.bti.chrona.androidsdk.constants.CredentialType;
import dev.bti.chrona.androidsdk.dto.Credential;
import dev.bti.chrona.common.Common;


public class ForgotPasswordActivity extends AppCompatActivity {

    EditText forgotEmailEditText;
    LinearProgressIndicator forgotProgressIndicator;
    AppCompatButton forgotNextBtn;
    Credential credential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initUi();
        initClicks();
    }


    private void initUi() {
        forgotEmailEditText = findViewById(R.id.forgotEmailEditText);
        forgotProgressIndicator = findViewById(R.id.forgotProgressIndicator);
        forgotNextBtn = findViewById(R.id.forgotNextBtn);
    }

    private void initClicks() {
        forgotNextBtn.setOnClickListener(v -> {
            if (!validate()) {
                return;
            }

            setProgress(true);
            findUser();
        });
    }

    private boolean validate() {
        String content = forgotEmailEditText.getText() != null ? forgotEmailEditText.getText().toString().trim() : "";

        if (content.isEmpty()) {
            forgotEmailEditText.setError("Please enter a valid email or phone number");
            return false;
        }

        forgotEmailEditText.setError(null);

        if (content.contains("@")) {
            if (Common.Validators.isValidEmail(content)) {
                forgotEmailEditText.setError("Please enter a valid email");
                return false;
            }
            credential = new Credential(content, CredentialType.EMAIL);
        } else {
            if (Common.Validators.isValidPhoneNumber(content, "AU")) {
                forgotEmailEditText.setError("Please enter a valid phone number");
                return false;
            }
            credential = new Credential(content, CredentialType.PHONE);
        }

        return true;
    }

    private void setProgress(boolean show) {
        forgotNextBtn.setEnabled(!show);
        forgotProgressIndicator.setIndeterminate(show);
        forgotNextBtn.setAlpha(show ? 0.4f : 1f);
        forgotProgressIndicator.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        forgotEmailEditText.clearFocus();
    }

    private void findUser() {
        setProgress(true);
        Application.getHelpers().getAuthHelper().verifyCredentials(List.of(credential))
                .addOnSuccessListener(response -> new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    setProgress(false);

                    OtpVerificationFragment otpVerificationFragment = new OtpVerificationFragment(
                            response.getPayload().get(0).credential(), false);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom)
                            .replace(R.id.main, otpVerificationFragment)
                            .commit();
                    getSupportFragmentManager().setFragmentResultListener("otp_verified", this, (requestKey, result) -> {
                        Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                        intent.putExtra("credential", credential);
                        startActivity(intent);
                        finishAffinity();
                        finish();
                    });
                }, 2500)).addOnFailureListener(result -> new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    setProgress(false);

                    if (credential.getCredentialType() == CredentialType.EMAIL) {
                        Toast.makeText(getApplicationContext(), "User with email not found", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "User with phone number not found", Toast.LENGTH_SHORT).show();
                    }
                }, 2500)).execute();
    }
}