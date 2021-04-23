package com.cleaner.emptykesh;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.cleaner.emptykesh.Broadcast.AlarmReceiver;
import com.cleaner.emptykesh.PageAdapter.MyPagerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.branch.referral.Branch;
import io.branch.referral.validators.IntegrationValidator;

public class MainActivity extends FragmentActivity {

    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private ScheduledExecutorService scheduler;

    private final Branch.BranchReferralInitListener branchReferralInitListener = (linkProperties, error) -> {

        if (error == null) {
            Log.i("BRANCH SDK SplashActiv", linkProperties.toString());
        } else {
            Log.e("BRANCH SDK SplashActiv", error.getMessage());
        }
    };

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> System.exit(2));
        setContentView(R.layout.activity_main);

        int randomNum = 6 + (int) (Math.random() * 18);
        int randomNum2 = 6 + (int) (Math.random() * 18);

        Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar firingCal = Calendar.getInstance();
        Calendar currentCal = Calendar.getInstance();

        firingCal.set(Calendar.HOUR_OF_DAY, randomNum); // At the hour you wanna fire
        firingCal.set(Calendar.MINUTE, randomNum2); // Particular minute
        firingCal.set(Calendar.SECOND, 0); // particular second

        long intendedTime = firingCal.getTimeInMillis();
        long currentTime = currentCal.getTimeInMillis();

        if (intendedTime >= currentTime) {
            alarmManager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);
        } else {
            // set from next day
            // you might consider using calendar.add() for adding one day to the current day
            firingCal.add(Calendar.DAY_OF_MONTH, 1);
            intendedTime = firingCal.getTimeInMillis();

            alarmManager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        final Thread.UncaughtExceptionHandler oldHandler =
                Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(
                (paramThread, paramThrowable) -> {
                    if (oldHandler != null)
                        oldHandler.uncaughtException(
                                paramThread,
                                paramThrowable
                        );
                    else
                        System.exit(2);
                });

        sharedpreferences = getSharedPreferences("waseembest", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.phonebooster));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.battery_saver));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.cooler));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.cleaner));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ads();
    }

    private void ads() {
        MobileAds.initialize(this, initializationStatus -> {
        });

        if (sharedpreferences.getBoolean("onAdLoaded", false)) {
            return;
        }

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(MainActivity.this, "ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                mInterstitialAd.show(MainActivity.this);
                editor.putBoolean("onAdLoaded", true);
                editor.commit();
                Log.d("Cleaner", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.d("Cleaner", loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        editor.putString("button1", "0");
        editor.putString("button2", "0");
        editor.putString("button3", "0");
        editor.putString("button4", "0");
        editor.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (scheduler == null) {
            scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate(() -> Log.i("hello", "world"), 30, 30, TimeUnit.SECONDS);
        }

        Branch.sessionBuilder(this).withCallback(branchReferralInitListener).withData(getIntent() != null ? getIntent().getData() : null).init();

        IntegrationValidator.validate(MainActivity.this);

        Branch.getInstance().initSession();
    }

    @Override
    protected void onStop() {
        super.onStop();
        scheduler.shutdownNow();
        scheduler = null;
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