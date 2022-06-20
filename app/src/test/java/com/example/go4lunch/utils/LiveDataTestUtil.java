package com.example.go4lunch.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class LiveDataTestUtil {

    public static <T> void observeForTesting(LiveData<T> liveData, OnObservedListener<T> block) {
        liveData.observeForever(ignored -> {
        });

        block.onObserved(liveData.getValue());
    }

    public interface OnObservedListener<T> {

        void onObserved(T liveData);

    }
}

