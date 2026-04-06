package dev.bti.chrona;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> welcomeText = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public LiveData<String> getWelcomeText() {
        return welcomeText;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void fetchHomeData() {
        isLoading.setValue(true);


        welcomeText.setValue("Welcome back to Chrona!");

        isLoading.setValue(false);
    }
}