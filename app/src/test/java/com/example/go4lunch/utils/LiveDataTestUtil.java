package com.example.go4lunch.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LiveDataTestUtil {

    public static <T> void observeForTesting(LiveData<T> liveData, OnObservedListener<T> block) {
        liveData.observeForever(ignored -> {
        });

        block.onObserved(liveData.getValue());
    }

    public interface OnObservedListener<T> {

        void onObserved(T liveData);

    }


        public static <T> T getOrAwaitValue(final LiveData<T> liveData) throws InterruptedException {
            final Object[] data = new Object[1];
            final CountDownLatch latch = new CountDownLatch(1);
            Observer<T> observer = new Observer<T>() {
                @Override
                public void onChanged(@Nullable T o) {
                    data[0] = o;
                    latch.countDown();
                    liveData.removeObserver(this);
                }
            };
            liveData.observeForever(observer);
            // Don't wait indefinitely if the LiveData is not set.
            if (!latch.await(2, TimeUnit.SECONDS)) {
                throw new RuntimeException("LiveData value was never set.");
            }
            //noinspection unchecked
            return (T) data[0];
        }

}

