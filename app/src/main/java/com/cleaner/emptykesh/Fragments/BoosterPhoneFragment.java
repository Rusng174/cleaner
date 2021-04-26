package com.cleaner.emptykesh.Fragments;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cleaner.emptykesh.Alaram_Booster;
import com.cleaner.emptykesh.R;
import com.cleaner.emptykesh.service.SharedPref;
import com.cleaner.emptykesh.service.TimeStampService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.skyfishjy.library.RippleBackground;

import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.ALARM_SERVICE;
import static com.cleaner.emptykesh.service.Constants.AD_CROSS_PAGE_ID;

public class BoosterPhoneFragment extends AbsFragment {

    public static ImageView optimizebutton;
    private View view;
    private DecoView arcView;
    private TextView scanning, centree, totalram, usedram, appused, appsfreed, processes, top, bottom, ramperct;
    private LinearLayout scanlay, optimizelay;
    private TimerTask timer = null;
    private TimerTask timer2 = null;
    private int x, y;
    private int counter = 0;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;

    private RippleBackground rippleBackground;

    private InterstitialAd mInterstitialAd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.phone_booster, container, false);

        rippleBackground = view.findViewById(R.id.content);
        arcView = (DecoView) view.findViewById(R.id.dynamicArcView2);
        scanning = (TextView) view.findViewById(R.id.scanning);
        scanlay = (LinearLayout) view.findViewById(R.id.scanlay);
        optimizelay = (LinearLayout) view.findViewById(R.id.optimizelay);
        optimizebutton = (ImageView) view.findViewById(R.id.optbutton);
        centree = (TextView) view.findViewById(R.id.centree);
        totalram = (TextView) view.findViewById(R.id.totalram);
        usedram = (TextView) view.findViewById(R.id.usedram);
        appsfreed = (TextView) view.findViewById(R.id.appsfreed);
        appused = (TextView) view.findViewById(R.id.appsused);
        processes = (TextView) view.findViewById(R.id.processes);
        top = (TextView) view.findViewById(R.id.top);
        bottom = (TextView) view.findViewById(R.id.bottom);
        ramperct = (TextView) view.findViewById(R.id.ramperct);
        sharedpreferences = getActivity().getSharedPreferences("waseem", Context.MODE_PRIVATE);
        try {
            Random ran3 = new Random();
            ramperct.setText(ran3.nextInt(60) + 40 + "%");

            optimizebutton.setBackgroundResource(0);
            optimizebutton.setImageResource(0);
            optimizebutton.setImageResource(R.drawable.optimize);

            if (sharedpreferences.getString("booster", "1").equals("0")) {
                optimizebutton.setImageResource(0);
                optimizebutton.setImageResource(R.drawable.optimized);

                centree.setText(sharedpreferences.getString("value", "50MB"));
                rippleBackground.startRippleAnimation();
            }

            start();

            rippleBackground.startRippleAnimation();
            optimizebutton.setOnClickListener((View.OnClickListener) v -> {
                if (sharedpreferences.getString("booster", "1").equals("1")) {
                    optimize();

                    editor = sharedpreferences.edit();
                    editor.putString("booster", "0");
                    editor.apply();

                    rippleBackground.startRippleAnimation();
                    Intent intent = new Intent(getActivity(), Alaram_Booster.class);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0,
                            intent, PendingIntent.FLAG_ONE_SHOT);

                    AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (100 * 1000), pendingIntent);
                } else {

                    LayoutInflater inflater1 = getActivity().getLayoutInflater();
                    View layout = inflater1.inflate(R.layout.my_toast, null);

                    ImageView image = (ImageView) layout.findViewById(R.id.image);

                    TextView text = (TextView) layout.findViewById(R.id.textView1);
                    text.setText("Phone Is Aleady Optimized");

                    Toast toast = new Toast(getActivity());
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 70);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        view.findViewById(R.id.privacy).setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cleaner-pro-master.flycricket.io/privacy.html"));
            startActivity(browserIntent);
        });

        return view;
    }

    public void optimize() {
        RotateAnimation rotate = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(5000);
        rotate.setInterpolator(new LinearInterpolator());

        ImageView image = view.findViewById(R.id.circularlines);

        image.startAnimation(rotate);

        arcView.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0, 100, 0)
                .setInterpolator(new AccelerateInterpolator())
                .build());

        arcView.addSeries(new SeriesItem.Builder(Color.parseColor("#FFFFFF"))
                .setRange(0, 100, 100)
                .setInitialVisibility(false)
                .setLineWidth(20f)
                .build());

        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.parseColor("#FFFFFF"))
                .setRange(0, 100, 0)
                .setLineWidth(20f)
                .build();

        SeriesItem seriesItem2 = new SeriesItem.Builder(Color.parseColor("#ffffff"))
                .setRange(0, 100, 0)
                .setLineWidth(20f)
                .build();
        int series1Index2 = arcView.addSeries(seriesItem2);

        arcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                .setDelay(500)
                .setDuration(2000)
                .setListener(new DecoEvent.ExecuteEventListener() {
                    @Override
                    public void onEventStart(DecoEvent decoEvent) {
                        bottom.setText("");
                        top.setText("");
                        centree.setText("Optimizing...");
                    }

                    @Override
                    public void onEventEnd(DecoEvent decoEvent) {

                    }
                })
                .build());

        arcView.addEvent(new DecoEvent.Builder(25).setIndex(series1Index2).setDelay(4000).setListener(new DecoEvent.ExecuteEventListener() {
            @Override
            public void onEventStart(DecoEvent decoEvent) {
                bottom.setText("");
                top.setText("");
                centree.setText("Optimizing...");
            }

            @Override
            public void onEventEnd(DecoEvent decoEvent) {
                bottom.setText("Found");
                top.setText("Storage");
                Random ran3 = new Random();
                ramperct.setText(ran3.nextInt(40) + 20 + "%");
            }
        }).build());

        ImageView img_animation = (ImageView) view.findViewById(R.id.waves);

        TranslateAnimation animation = new TranslateAnimation(0.0f, 1000.0f, 0.0f, 0.0f);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        animation.setDuration(5000);  // animation duration
        animation.setRepeatCount(0);
        animation.setInterpolator(new LinearInterpolator());// animation repeat count
        animation.setFillAfter(true);

        img_animation.startAnimation(animation);

        int counter = 0;
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                scanlay.setVisibility(View.VISIBLE);
                optimizelay.setVisibility(View.GONE);
                scanning.setText("SCANNING...");
                killall();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                scanlay.setVisibility(View.GONE);
                optimizelay.setVisibility(View.VISIBLE);
                optimizebutton.setImageResource(R.drawable.optimized);


                Random ran = new Random();
                x = ran.nextInt(100) + 30;

                Random ran2 = new Random();
                int proc = ran2.nextInt(10) + 5;

                centree.setText(getUsedMemorySize() - x + " MB");

                editor = sharedpreferences.edit();
                editor.putString("value", getUsedMemorySize() - x + " MB");
                editor.apply();

                Log.e("used mem", getUsedMemorySize() + " MB");
                Log.e("used mem", getTotalRAM());

                totalram.setText(getTotalRAM());
                usedram.setText(getUsedMemorySize() - x + " MB/ ");

                appsfreed.setText(getTotalRAM());
                appused.setText(Math.abs(getUsedMemorySize() - x - 30) + " MB/ ");

                processes.setText(y - proc + "");

                ads();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void start() {
        final Timer t = new Timer();
        timer = new TimerTask() {

            @Override
            public void run() {
                try {
                    getActivity().runOnUiThread(() -> {
                        counter++;
                        centree.setText(counter + "MB");
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
        t.schedule(timer, 30, 30);

        Random ran2 = new Random();
        final int proc = ran2.nextInt(60) + 30;

        arcView.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0, 100, 0)
                .setInterpolator(new AccelerateInterpolator())
                .build());

        arcView.addSeries(new SeriesItem.Builder(Color.parseColor("#FFFFFF"))
                .setRange(0, 100, 100)
                .setInitialVisibility(false)
                .setLineWidth(20f)
                .build());

//Create data series track
        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.parseColor("#FFFFFF"))
                .setRange(0, 100, 0)
                .setLineWidth(20f)
                .build();

        SeriesItem seriesItem2 = new SeriesItem.Builder(Color.parseColor("#ffffff"))
                .setRange(0, 100, 0)
                .setLineWidth(20f)
                .build();

        int series1Index2 = arcView.addSeries(seriesItem2);

        arcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                .setDelay(0)
                .setDuration(600)
                .build());


        arcView.addEvent(new DecoEvent.Builder(proc).setIndex(series1Index2).setDelay(2000).setListener(new DecoEvent.ExecuteEventListener() {
            @Override
            public void onEventStart(DecoEvent decoEvent) {

            }

            @Override
            public void onEventEnd(DecoEvent decoEvent) {
                t.cancel();
                timer.cancel();
                t.purge();

                centree.setText(getUsedMemorySize() + " MB");

                if (sharedpreferences.getString("booster", "1").equals("0")) {
                    centree.setText(sharedpreferences.getString("value", "50MB"));
                }

                final Timer t = new Timer();
                final Timer t2 = new Timer();

                try {
                    timer2 = new TimerTask() {

                        @Override
                        public void run() {
                            try {
                                getActivity().runOnUiThread(() -> {
                                    centree.setText(getUsedMemorySize() + " MB");

                                    if (sharedpreferences.getString("booster", "1").equals("0")) {
                                        centree.setText(sharedpreferences.getString("value", "50MB"));
                                    }

                                    t2.cancel();
                                    timer2.cancel();
                                    t2.purge();
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    };
                } catch (Exception e) {
                    e.printStackTrace();
                }

                t2.schedule(timer2, 100, 100);

            }
        }).build());
        Log.e("used mem", getUsedMemorySize() + " MB");
        Log.e("used mem", getTotalRAM());

        totalram.setText(getTotalRAM());
        usedram.setText(getUsedMemorySize() + " MB/ ");
        appsfreed.setText(getTotalRAM());
        appused.setText(getUsedMemorySize() - x - 30 + " MB/ ");

        Random ran = new Random();
        y = ran.nextInt(50) + 15;

        processes.setText(y + "");
    }

    public String getTotalRAM() {
        RandomAccessFile reader = null;
        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        double totRam = 0;
        String lastValue = "";
        try {
            try {
                reader = new RandomAccessFile("/proc/meminfo", "r");
                load = reader.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Get the Number value from the string
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);
            String value = "";
            while (m.find()) {
                value = m.group(1);
            }
            try {
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            totRam = Double.parseDouble(value);

            double mb = totRam / 1024.0;
            double gb = totRam / 1048576.0;
            double tb = totRam / 1073741824.0;

            if (tb > 1) {
                lastValue = twoDecimalForm.format(tb).concat(" TB");
            } else if (gb > 1) {
                lastValue = twoDecimalForm.format(gb).concat(" GB");
            } else if (mb > 1) {
                lastValue = twoDecimalForm.format(mb).concat(" MB");
            } else {
                lastValue = twoDecimalForm.format(totRam).concat(" KB");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lastValue;
    }

    public long getUsedMemorySize() {
        try {
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(ACTIVITY_SERVICE);
            activityManager.getMemoryInfo(mi);
            long availableMegs = mi.availMem / 1048576L;

            return availableMegs;
        } catch (Exception e) {
            return 200;
        }
    }

    public void killall() {
        List<ApplicationInfo> packages;
        PackageManager pm;
        pm = getActivity().getPackageManager();
        //get a list of installed apps.
        packages = pm.getInstalledApplications(0);

        ActivityManager mActivityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        String myPackage = getActivity().getApplicationContext().getPackageName();
        for (ApplicationInfo packageInfo : packages) {
            if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) continue;
            if (packageInfo.packageName.equals(myPackage)) continue;
            mActivityManager.killBackgroundProcesses(packageInfo.packageName);
        }
    }

    private void ads() {
        Long prevTime = SharedPref.INSTANCE.getTimeStamp(getActivity());
        Long curTime = System.currentTimeMillis();
        if (TimeStampService.isLessThen30Sec(prevTime, curTime)) {
            return;
        }

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(getContext(), AD_CROSS_PAGE_ID, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                mInterstitialAd.show(getActivity());
                Log.d("Cleaner", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.d("Cleaner", loadAdError.getMessage());
                mInterstitialAd = null;
            }
        });
    }
}
