package com.cleaner.emptykesh;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import io.branch.referral.Branch;
import io.branch.referral.validators.IntegrationValidator;


public class SplashActivity extends AppCompatActivity {

    private final Branch.BranchReferralInitListener branchReferralInitListener = (linkProperties, error) -> {
        // do stuff with deep link data (nav to page, display content, etc)

        if (error == null) {
            Log.i("BRANCH SDK SplashActiv", linkProperties.toString());
            // Retrieve deeplink keys from 'referringParams' and evaluate the values to determine where to route the user
            // Check '+clicked_branch_link' before deciding whether to use your Branch routing logic
        } else {
            Log.e("BRANCH SDK SplashActiv", error.getMessage());
        }
    };
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

}