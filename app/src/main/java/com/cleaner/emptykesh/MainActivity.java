package com.cleaner.emptykesh;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.cleaner.emptykesh.Broadcast.AlarmReceiver;
import com.cleaner.emptykesh.PageAdapter.MyPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends FragmentActivity {

    public static TextView name;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    private ScheduledExecutorService scheduler;
    private boolean isVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        isVisible = true;
        if (scheduler == null) {
            scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate(() -> Log.i("hello", "world"), 30, 30, TimeUnit.SECONDS);

        }

    }


    // start Ad at 30 seconds

    @Override
    protected void onStop() {
        super.onStop();
        scheduler.shutdownNow();
        scheduler = null;
        isVisible = false;
    }
    //.. code

    public class MyException extends Exception {
        // special exception code goes here
    }


}