package com.cleaner.emptykesh;

import android.app.Application;

import com.google.firebase.analytics.FirebaseAnalytics;

import io.branch.referral.Branch;

public class App extends Application {

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Branch.enableLogging();
        Branch.getAutoInstance(this);
    }
}
