package com.cleaner.emptykesh;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cleaner.emptykesh.Broadcast.AlarmReceiver;
import com.cleaner.emptykesh.PageAdapter.MyPagerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.validators.IntegrationValidator;

public class MainActivity extends FragmentActivity {

  public static TextView name;
  SharedPreferences sharedpreferences;
  SharedPreferences.Editor editor;

  private AdView mAdView;
  private ScheduledExecutorService scheduler;
  private boolean isVisible;
  private InterstitialAd mInterstitialAd;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
      public void uncaughtException(Thread t, Throwable e) {

        System.exit(2);
      }
    });
    setContentView(R.layout.activity_main);

    prepareAd();

    mAdView = (AdView) findViewById(R.id.adView);
    AdRequest adRequest = new AdRequest.Builder().build();
    mAdView.loadAd(adRequest);

    if (isNetworkAvailable()) {
      mAdView.setVisibility(View.VISIBLE);
    }

    ////                         Notification                                              /////
    ////////////////////////////////////////////////////////////////////////////////////////////

    int randomNum = 6 + (int) (Math.random() * 18);

    int randomNum2 = 6 + (int) (Math.random() * 18);


//        Toast.makeText(this,randomNum+"",Toast.LENGTH_LONG).show();

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
      // you can add buffer time too here to ignore some small differences in milliseconds
      // set from today
      alarmManager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);
    } else {
      // set from next day
      // you might consider using calendar.add() for adding one day to the current day
      firingCal.add(Calendar.DAY_OF_MONTH, 1);
      intendedTime = firingCal.getTimeInMillis();

      alarmManager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////

    final Thread.UncaughtExceptionHandler oldHandler =
        Thread.getDefaultUncaughtExceptionHandler();

    Thread.setDefaultUncaughtExceptionHandler(
        new Thread.UncaughtExceptionHandler() {
          @Override
          public void uncaughtException(
              Thread paramThread,
              Throwable paramThrowable
          ) {
            //Do your own error handling here

            if (oldHandler != null)
              oldHandler.uncaughtException(
                  paramThread,
                  paramThrowable
              ); //Delegates to Android's error handling
            else
              System.exit(2); //Prevents the service/app from freezing
          }
        });


//        setTheme(R.style.AppTheme1);
//    name = (TextView) findViewById(R.id.name);
    sharedpreferences = getSharedPreferences("waseembest", Context.MODE_PRIVATE);
    editor = sharedpreferences.edit();


//        ImageView img_animation = (ImageView) findViewById(R.id.backbar);
//
//        TranslateAnimation animation = new TranslateAnimation(0.0f, 1000.0f, 0.0f, 0.0f);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
//        animation.setDuration(10000);  // animation duration
//        animation.setRepeatCount(0);
//        animation.setInterpolator(new LinearInterpolator());// animation repeat count
////        animation.setRepeatMode(2);   // repeat animation (left to right, right to left )
//        animation.setFillAfter(true);
//
//        img_animation.startAnimation(animation);


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


    ////// Create Tabs Layout.

    final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
    tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.phonebooster));
    tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.battery_saver));
    tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.cooler));
    tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.cleaner));
//    tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_baseline_attach_money));

    tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

    final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
    final MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
    viewPager.setAdapter(adapter);

    viewPager.setOffscreenPageLimit(4);
//        viewPager.setCurrentItem(4);

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

  public class MyException extends Exception {
    // special exception code goes here
  }


  private boolean isNetworkAvailable() {
    ConnectivityManager connectivityManager
        = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }

  public void  prepareAd() {
    mInterstitialAd = new InterstitialAd(this);
    mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial));
    mInterstitialAd.loadAd(new AdRequest.Builder().build());
  }



  // start Ad at 30 seconds


  @Override
  protected void onStart(){
    super.onStart();
    isVisible = true;
    if(scheduler == null){
      scheduler = Executors.newSingleThreadScheduledExecutor();
      scheduler.scheduleAtFixedRate(new Runnable() {
        public void run() {
          Log.i("hello", "world");
          runOnUiThread(new Runnable() {
            public void run() {
              if (mInterstitialAd.isLoaded() && isVisible) {
                mInterstitialAd.show();
              } else {
                Log.d("TAG"," Interstitial not loaded");
              }

              prepareAd();
            }
          });
        }
      }, 30, 30, TimeUnit.SECONDS);

    }

  }
  //.. code

  @Override
  protected void onStop() {
    super.onStop();
    scheduler.shutdownNow();
    scheduler = null;
    isVisible =false;
  }


}