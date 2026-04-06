package dev.bti.chrona;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {


    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> screenTitle = new MutableLiveData<>();



    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getScreenTitle() {
        return screenTitle;
    }



    public void loadInitialData() {
        isLoading.setValue(true);

        screenTitle.setValue("Shared Data Ready");

        isLoading.setValue(false);
    }
}