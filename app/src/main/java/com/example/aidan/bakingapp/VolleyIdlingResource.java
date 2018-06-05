package com.example.aidan.bakingapp;

import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

public class VolleyIdlingResource implements IdlingResource {
    @Nullable
    private volatile ResourceCallback resourceCallback;
    private AtomicBoolean isIdleNow = new AtomicBoolean(false);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return isIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        resourceCallback = callback;
    }

    @SuppressWarnings("ConstantConditions")
    public void setIsIdleNow(boolean idleNow) {
        isIdleNow.set(idleNow);
        if (idleNow && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }
    }
}
