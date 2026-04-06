package dev.bti.chrona;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.List;
import java.util.Objects;

import dev.bti.chrona.androidsdk.constants.CredentialType;
import dev.bti.chrona.androidsdk.dto.Credential;
import dev.bti.chrona.androidsdk.dto.CredentialVerificationResult;
import dev.bti.chrona.androidsdk.dto.UserRequestDto;
import dev.bti.chrona.common.Common;
import dev.bti.chrona.util.SecureStorage;

public class SignUpActivity extends AppCompatActivity {

    UserRequestDto userRequestDto;
    private Button signUpBtn;
    private TextView signUpLoginBtn;
    private AppCompatEditText signUpNameEditText, signUpUsernameEditText, signUpEmailEditText, signUpPasswordEditText;

    private LinearProgressIndicator signUpProgressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initUi();
        initDTO();
        initClicks();
    }

    private void initUi() {
        signUpBtn = findViewById(R.id.signUpBtn);
        signUpLoginBtn = findViewById(R.id.signUpLoginBtn);
        signUpNameEditText = findViewById(R.id.signUpNameEditText);
        signUpUsernameEditText = findViewById(R.id.signUpUsernameEditText);
        signUpEmailEditText = findViewById(R.id.signUpEmailEditText);
        signUpPasswordEditText = findViewById(R.id.signUpPasswordEditText);
        signUpProgressIndicator = findViewById(R.id.signUpProgressIndicator);
    }

    private void initDTO() {
        userRequestDto = new UserRequestDto();

    }

    private void initClicks() {

        signUpBtn.setOnClickListener(v -> {
            if (!validate()) {
                return;
            }

            signUpBtn.setEnabled(false);
            signUpLoginBtn.setEnabled(false);
            signUpBtn.setAlpha(0.4f);
            signUpProgressIndicator.setVisibility(View.VISIBLE);
            signUpProgressIndicator.setIndeterminate(true);

            checkUserExists();
        });

        signUpLoginBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), LoginActivity.class)));
    }

    private boolean validate() {
        boolean isValid = true;

        String fullName = Objects.requireNonNull(signUpNameEditText.getText()).toString().trim();

        if (!Common.Validators.isValidName(fullName)) {
            signUpNameEditText.setError("Please enter valid name (total length: 2-50 characters).");
            isValid = false;
        } else {
            signUpNameEditText.setError(null);

        }

        String email = Objects.requireNonNull(signUpEmailEditText.getText()).toString();
        if (Common.Validators.isValidEmail(email)) {
            signUpEmailEditText.setError("Email is invalid.");
            isValid = false;
        } else {
            signUpEmailEditText.setError(null);
        }

        String username = Objects.requireNonNull(signUpUsernameEditText.getText()).toString();
        if (username.isEmpty() || username.length() < 3 || username.length() > 16) {
            signUpPasswordEditText.setError("Username must be between 3 and 16 characters.");
            isValid = false;
        } else {
            signUpPasswordEditText.setError(null);
        }

        String password = Objects.requireNonNull(signUpPasswordEditText.getText()).toString();
        if (password.isEmpty() || password.length() < 6) {
            signUpPasswordEditText.setError("Password must be at least 6 characters long.");
            isValid = false;
        } else {
            signUpPasswordEditText.setError(null);
        }


        return isValid;
    }

    private void checkUserExists() {
        String email = Objects.requireNonNull(signUpEmailEditText.getText()).toString().trim();
        String username = Objects.requireNonNull(signUpUsernameEditText.getText()).toString().trim();

        userRequestDto.setEmail(email);
        userRequestDto.setUsername(username);

        Application.getHelpers().getAuthHelper().verifyCredentials(
                        List.of(
                                Credential.of(email),
                                Credential.of(username)
                        ))
                .addOnSuccessListener(response -> {
                    boolean hasConflict = false;

                    // 1. Loop through the backend results
                    for (CredentialVerificationResult result : response.getPayload()) { // Or response.getPayload() depending on your CallHandler setup
                        if (result.exists()) {
                            hasConflict = true;

                            // 2. Pinpoint exactly which credential is taken and show an error!
                            if (result.credential().getCredentialType() == CredentialType.EMAIL) {
                                signUpEmailEditText.setError("This email is already in use");
                            } else if (result.credential().getCredentialType() == CredentialType.USERNAME) {
                                signUpUsernameEditText.setError("This username is taken");
                            }
                        }
                    }

                    // 3. If there's an error, stop the loading animation so they can fix it
                    if (hasConflict) {
                        signUpBtn.setEnabled(true);
                        signUpLoginBtn.setEnabled(true);
                        signUpBtn.setAlpha(1f);
                        signUpProgressIndicator.setVisibility(View.INVISIBLE);
                        signUpProgressIndicator.setIndeterminate(false);
                    } else {
                        // 4. All clear! Create the user.
                        createUser();
                    }

                }).addOnFailureListener(errorResponse -> {
                    // This now only triggers for REAL network errors (like no wifi or 500 server error)
                    Log.e("SIGN UP FLOW", "Network Error: " + errorResponse.getMessage());
                    signUpBtn.setEnabled(true);
                    signUpProgressIndicator.setVisibility(View.INVISIBLE);
                })
                .execute();
    }

    private void createUser() {
        String fullName = Objects.requireNonNull(signUpNameEditText.getText()).toString().trim();
        String[] nameParts = fullName.split(" ");
        userRequestDto.setFirstName(nameParts[0]);
        userRequestDto.setLastName(nameParts.length > 1 ? nameParts[1] : "");

        userRequestDto.setUsername(Objects.requireNonNull(signUpUsernameEditText.getText()).toString().trim());
        userRequestDto.setEmail(Objects.requireNonNull(signUpEmailEditText.getText()).toString().trim());
        userRequestDto.setPassword(Objects.requireNonNull(signUpPasswordEditText.getText()).toString());

        Application.getHelpers().getAuthHelper().register(userRequestDto)
                .addOnSuccessListener(result -> {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        SecureStorage.saveAuthData(
                                getApplicationContext(),
                                result.getPayload()
                        );

                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                    }, 2500);
                })
                .addOnFailureListener(errorResponse -> {
                    Log.e("SIGN UP FLOW", "createUser: " + errorResponse.getMessage());
                    signUpBtn.setEnabled(true);
                    signUpProgressIndicator.setVisibility(View.INVISIBLE);
                })
                .execute();
    }
}