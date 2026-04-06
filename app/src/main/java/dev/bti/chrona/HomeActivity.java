package dev.bti.chrona;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import dev.bti.chrona.androidsdk.common.SuccessResponse;
import dev.bti.chrona.androidsdk.dto.UserDto;
import dev.bti.chrona.androidsdk.interfaces.OnSuccessListener;

public class HomeActivity extends AppCompatActivity {

    Button homeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        homeButton = findViewById(R.id.homeBtn);
        homeButton.setOnClickListener(v -> Application.getHelpers().getUserHelper().getUserProfile().addOnSuccessListener(new OnSuccessListener<UserDto>() {
            @Override
            public void onSuccess(SuccessResponse<UserDto> result) {

                Log.i("HOME", "onSuccess: " + result.getPayload());
            }
        }).execute());

    }


}