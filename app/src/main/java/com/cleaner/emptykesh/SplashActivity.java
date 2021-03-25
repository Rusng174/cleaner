package com.cleaner.emptykesh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONObject;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.validators.IntegrationValidator;


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
        } else  {
          startActivity(new Intent(getApplicationContext(), MainActivity.class));
          finish();
        }

      }
    }, 4000);
  }


  @Override public void onStart() {
    super.onStart();
    Branch.sessionBuilder(this).withCallback(branchReferralInitListener).withData(getIntent() != null ? getIntent().getData() : null).init();

    IntegrationValidator.validate(SplashActivity.this);

    Branch.getInstance().initSession();
  }
  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    setIntent(intent);
    // if activity is in foreground (or in backstack but partially visible) launching the same
    // activity will skip onStart, handle this case with reInitSession
    Branch.sessionBuilder(this).withCallback(branchReferralInitListener).reInit();
  }
  private final Branch.BranchReferralInitListener branchReferralInitListener = new Branch.BranchReferralInitListener() {
    @Override
    public void onInitFinished(JSONObject linkProperties, BranchError error) {
      // do stuff with deep link data (nav to page, display content, etc)

      if (error == null) {
        Log.i("BRANCH SDK ", linkProperties.toString());
        // Retrieve deeplink keys from 'referringParams' and evaluate the values to determine where to route the user
        // Check '+clicked_branch_link' before deciding whether to use your Branch routing logic
      } else {
        Log.e("BRANCH SDK MainActivity", error.getMessage());
      }
    }
  };

}