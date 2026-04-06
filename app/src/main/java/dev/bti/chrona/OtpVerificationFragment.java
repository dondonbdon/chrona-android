package dev.bti.chrona;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.Locale;
import java.util.Objects;
import java.util.logging.Logger;

import dev.bti.chrona.androidsdk.dto.Credential;
import dev.bti.chrona.util.PasteDetectEditText;
import dev.bti.chrona.util.SwipeTouchListener;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OtpVerificationFragment extends Fragment {
    final Credential credential;
    final Boolean canSkip;

    PasteDetectEditText verificationEditZero;
    TextView verificationResendText, verificationEmailSecondaryText, verificationEmailText;
    AppCompatButton verificationBtn, verificationOptBtn;
    LinearProgressIndicator verificationProgressIndicator;

    Boolean resendOnTimeout = false;
    Boolean proceeding = false;
    Boolean firstSend = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_otp_verification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.setTag(this);
        view.setOnTouchListener(new SwipeTouchListener(view));

        initUi(view);
        initState();
        initClicks();
        initBackPressedListener();
    }

    private void initUi(View view) {
        verificationEditZero = view.findViewById(R.id.verificationEditZero);
        verificationResendText = view.findViewById(R.id.verificationResendText);
        verificationEmailText = view.findViewById(R.id.verificationEmailText);
        verificationEmailSecondaryText = view.findViewById(R.id.verificationEmailSecondaryText);
        verificationBtn = view.findViewById(R.id.verificationBtn);
        verificationOptBtn = view.findViewById(R.id.verificationOptBtn);
        verificationProgressIndicator = view.findViewById(R.id.verificationProgressIndicator);
    }

    private void initState() {
        if (canSkip) {
            verificationOptBtn.setVisibility(View.VISIBLE);
        } else {
            verificationOptBtn.setVisibility(View.GONE);
        }
    }

    private void initClicks() {
        verificationResendText.setOnClickListener(v -> {
            if (resendOnTimeout) {
                return;
            }

            setProgress(true);
            sendCode();
        });

        if (canSkip) {
            verificationOptBtn.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putBoolean("verified", false);
                requireActivity().getSupportFragmentManager().setFragmentResult("otp_verified", bundle);
            });
        }

        verificationBtn.setOnClickListener(v -> {
            if (!proceeding) {
                setProgress(true);
                sendCode();
                return;
            }

            if (Objects.requireNonNull(verificationEditZero.getText()).toString().isEmpty() ||
                    Objects.requireNonNull(verificationEditZero.getText()).toString().length() != 6) {
                verificationEditZero.setError("Please enter a valid code");
                setProgress(false);
                return;
            }

            verificationEditZero.setError(null);
            setProgress(true);
            verifyOtp();
        });

    }

    private void setResendTimer() {
        new CountDownTimer(20 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) millisUntilFinished / 1000;
                verificationResendText.setText(formatTime(secondsRemaining));
            }

            @Override
            public void onFinish() {
                verificationResendText.setText(R.string.resend);
                resendOnTimeout = false;
            }
        }.start();
    }

    private void sendCode() {
        resendOnTimeout = true;

        Application.getHelpers()
                .getSecurityHelper()
                .requestResetCode(credential)
                .addOnSuccessListener(result -> new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    setProgress(false);

                    if (firstSend) {
                        return;
                    }

                    setResendTimer();
                }, 1500))
                .addOnFailureListener(result -> {
                    Toast.makeText(requireContext(), "Failed to send code", Toast.LENGTH_SHORT).show();
                    Logger.getLogger("OtpVerificationFragment").warning("Failed to send code");
                }).execute();
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format(Locale.getDefault(), "Resend again in %02d:%02d", minutes, secs);
    }

    private void setProgress(boolean show) {
        if (firstSend && !show) {
            proceeding = true;
            firstSend = false;
            resendOnTimeout = false;
            String verificationText = String.format(Locale.getDefault(), "We sent a 6-digit code to %s", credential.mask());
            verificationEmailText.setText(verificationText);
            verificationEmailText.setVisibility(View.VISIBLE);
            verificationResendText.setVisibility(View.VISIBLE);
            verificationEmailSecondaryText.setText(R.string.enter_the_code_below);

            verificationBtn.setText(R.string.verify);
        }

        verificationProgressIndicator.setIndeterminate(show);
        verificationProgressIndicator.setVisibility(show ? View.VISIBLE : View.GONE);
        verificationResendText.setEnabled(!show);

        if (canSkip)
            verificationOptBtn.setAlpha(show ? 0.4f : 1f);

        verificationBtn.setEnabled(!show);
        verificationBtn.setAlpha(show ? 0.4f : 1f);
        verificationResendText.setAlpha(show ? 0.4f : 1f);
        verificationEditZero.setEnabled(!show);
    }

    private void verifyOtp() {
        Application.getHelpers().getSecurityHelper().validateResetCode(credential, Objects.requireNonNull(verificationEditZero.getText()).toString()).addOnSuccessListener(result -> {
                }).addOnSuccessListener(result -> {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("verified", true);
                    requireActivity().getSupportFragmentManager().setFragmentResult("otp_verified", bundle);
                    setProgress(false);
                }).addOnFailureListener(result -> setProgress(false))
                .execute();
    }

    private void initBackPressedListener() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Bundle result = new Bundle();
                result.putBoolean("verified", false);
                getParentFragmentManager().setFragmentResult("requestKey", result);

                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}
