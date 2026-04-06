package dev.bti.chrona;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class HomeFragment extends Fragment {


    private HomeViewModel viewModel;
    private TextView tvWelcome;
    private Button btnRefresh;
    private ProgressBar progressBar;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initViewModel();
        initUi(view);
        initClicks();
        initObservers();

        // Initial action
        viewModel.fetchHomeData();
    }

    private void initViewModel() {

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    private void initUi(View view) {
        tvWelcome = view.findViewById(R.id.tvWelcome);
        btnRefresh = view.findViewById(R.id.btnRefresh);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void initClicks() {
        btnRefresh.setOnClickListener(v -> viewModel.fetchHomeData());
    }

    private void initObservers() {
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                btnRefresh.setEnabled(!isLoading);
            }
        });

        viewModel.getWelcomeText().observe(getViewLifecycleOwner(), text -> {
            if (text != null) {
                tvWelcome.setText(text);
            }
        });
    }
}