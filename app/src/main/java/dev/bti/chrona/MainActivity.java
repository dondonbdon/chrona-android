package dev.bti.chrona;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import dev.bti.chrona.util.VoiceHelper;

public class MainActivity extends AppCompatActivity {

    FrameLayout mainContainer;
    FloatingActionButton mainVoiceActionFab, mainChatToggleFob;
    EditText mainPromptEdit;
    BottomNavigationView bottomNav; // Assuming you have this in your XML

    // Initialize to false to prevent NullPointerExceptions!
    boolean isChatOpen = false;
    boolean isTyping = false;

    private VoiceHelper voiceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initUi();
        initClicks();
        initTextWatcher();
        initBottomNav();
        initVoiceHelper();


    }

    private void initUi() {
        mainContainer = findViewById(R.id.mainContainer);
        mainVoiceActionFab = findViewById(R.id.mainVoiceActionFab);
        mainChatToggleFob = findViewById(R.id.mainChatToggleFob);
        mainPromptEdit = findViewById(R.id.mainPromptEdit);

        mainPromptEdit.setVisibility(View.GONE);
    }

    private void initClicks() {
        mainVoiceActionFab.setOnClickListener(v -> performAction());

        mainChatToggleFob.setOnClickListener(v -> {
            isChatOpen = !isChatOpen;
            toggleChat();
        });
    }

    private void performAction() {
        if (isTyping) {
            submitPrompt();
        } else {
            startListening();
        }
    }


    private void initBottomNav() {
        bottomNav = findViewById(R.id.mainBottomNavView);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                loadFragment(new HomeFragment());
                return true;
            } else if (itemId == R.id.nav_calendar) {
                loadFragment(new CalendarFragment());
                return true;
            } else if (itemId == R.id.nav_shared) {
                loadFragment(new SharedFragment());
                return true;
            } else if (itemId == R.id.nav_profile) {
                loadFragment(new ProfileFragment());
                return true;
            }
            return false;
        });
    }

    private void initVoiceHelper() {
        voiceHelper = new VoiceHelper(this, mainPromptEdit, () -> setChatToggleEnabled(true));
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainContainer, fragment)
                .commit();
    }


    private void initTextWatcher() {
        mainPromptEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean hasText = s.toString().trim().length() > 0;
                if (isTyping != hasText) {
                    isTyping = hasText;
                    toggleVoiceAction();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    private void toggleChat() {
        if (isChatOpen) {
            mainPromptEdit.setPivotX(mainPromptEdit.getWidth());
            mainPromptEdit.setPivotY(mainPromptEdit.getHeight());
            mainPromptEdit.setScaleX(0.3f);
            mainPromptEdit.setScaleY(0.3f);
            mainPromptEdit.setTranslationY(80f);
            mainPromptEdit.setAlpha(0f);
            mainPromptEdit.setVisibility(View.VISIBLE);

            mainPromptEdit.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .translationY(0f)
                    .alpha(1f)
                    .setInterpolator(new OvershootInterpolator(1.2f))
                    .setDuration(400)
                    .start();

            mainPromptEdit.setFocusableInTouchMode(true);
            mainPromptEdit.requestFocus();
        } else {
            mainPromptEdit.animate()
                    .scaleX(0.3f)
                    .scaleY(0.3f)
                    .translationY(80f)
                    .alpha(0f)
                    .setInterpolator(new AnticipateInterpolator(1.0f))
                    .setDuration(300)
                    .withEndAction(() -> {
                        mainPromptEdit.setVisibility(View.GONE);
                        mainPromptEdit.clearFocus();
                    })
                    .start();
        }
    }

    private void setChatToggleEnabled(boolean isEnabled) {
        mainChatToggleFob.setEnabled(isEnabled);
        mainChatToggleFob.animate()
                .alpha(isEnabled ? 1f : 0.5f)
                .setDuration(200)
                .start();
    }


    private void startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
            return;
        }

        setChatToggleEnabled(false);

        if (!isChatOpen) {
            isChatOpen = true;
            toggleChat();
        }

        voiceHelper.startListening();
    }

    private void submitPrompt() {
        String prompt = mainPromptEdit.getText().toString().trim();
        mainPromptEdit.setText("");

        setChatToggleEnabled(true);

        // TODO: Show loading animation here

        Toast.makeText(this, "Sending to WebSocket: " + prompt, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (voiceHelper != null) {
            voiceHelper.destroy();
        }
    }

    private void toggleVoiceAction() {
        mainVoiceActionFab.setImageResource(isTyping ? R.drawable.ic_send : R.drawable.ic_microphone);
    }
}