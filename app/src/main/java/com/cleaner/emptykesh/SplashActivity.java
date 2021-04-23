package com.cleaner.emptykesh;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;

import io.branch.referral.Branch;
import io.branch.referral.validators.IntegrationValidator;


public class SplashActivity extends AppCompatActivity {


    TextView privacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        privacy = findViewById(R.id.privacy);

        //privacy
        privacy.setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cleaner-pro-master.flycricket.io/privacy.html"));
            startActivity(browserIntent);
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }



}