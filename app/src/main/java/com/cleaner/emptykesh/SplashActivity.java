package com.cleaner.emptykesh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class SplashActivity extends AppCompatActivity {

  InterstitialAd mInterstitialAd;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);



    mInterstitialAd = new com.google.android.gms.ads.InterstitialAd(getApplicationContext());
    mInterstitialAd.setAdUnitId(getResources().getString(R.string.start_banner));
    mInterstitialAd.loadAd(new AdRequest.Builder().build());

    mInterstitialAd.setAdListener(new AdListener(){
      @Override
      public void onAdClosed() {
        super.onAdClosed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
      }
    });

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        if(mInterstitialAd.isLoaded()) {
          mInterstitialAd.show();
        }
        else{
          startActivity(new Intent(getApplicationContext(), MainActivity.class));
          finish();
        }
      }
    }, 4000);
  }
}