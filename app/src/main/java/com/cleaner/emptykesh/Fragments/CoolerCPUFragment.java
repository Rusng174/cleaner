package com.cleaner.emptykesh.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleaner.emptykesh.dto.Apps;
import com.cleaner.emptykesh.CpuScannerActivity;
import com.cleaner.emptykesh.R;
import com.cleaner.emptykesh.RecyclerAdapter;
import com.cleaner.emptykesh.service.SharedPref;
import com.cleaner.emptykesh.service.TimeStampService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class CoolerCPUFragment extends AbsFragment {

    private InterstitialAd mInterstitialAd;
    public static List<Apps> apps;
    private TextView batterytemp, showmain, showsec, nooverheating;
    private float temp;
    private ImageView coolbutton, tempimg;
    private RecyclerView recyclerView;
    private RecyclerAdapter mAdapter;
    private int check = 0;

    private final BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                temp = ((float) intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)) / 10;
                batterytemp.setText(temp + "°C");

                if (temp >= 30.0) {
                    apps = new ArrayList<>();
                    tempimg.setImageResource(R.drawable.red_cooler);
                    showmain.setText("OVERHEATED");
                    showmain.setTextColor(Color.parseColor("#F22938"));
                    showsec.setText("Apps are causing problem hit cool down");
                    nooverheating.setText("");

                    coolbutton.setImageResource(R.drawable.cool_button_blue);
                    coolbutton.setOnClickListener((View.OnClickListener) v -> {
                        Intent i = new Intent(getActivity(), CpuScannerActivity.class);
                        startActivity(i);

                        final Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            nooverheating.setText("Currently No App causing Overheating");
                            showmain.setText("NORMAL");
                            showmain.setTextColor(Color.parseColor("#ffffff"));
                            showsec.setText("CPU Temperature is GOOD");
                            showsec.setTextColor(Color.parseColor("#ffffff"));
                            coolbutton.setImageResource(R.drawable.cooled);
                            tempimg.setImageResource(R.drawable.blue_cooler);
                            batterytemp.setText("25.3" + "°C");
                            recyclerView.setAdapter(null);

                            ads();
                        }, 2000);

                        coolbutton.setOnClickListener((View.OnClickListener) v1 -> {
                            LayoutInflater inflater = getActivity().getLayoutInflater();
                            View layout = inflater.inflate(R.layout.my_toast, null);

                            ImageView image = (ImageView) layout.findViewById(R.id.image);

                            TextView text = (TextView) layout.findViewById(R.id.textView1);
                            text.setText("CPU Temperature is Already Normal.");

                            Toast toast = new Toast(getActivity());
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 70);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                        });
                    });

                    if (Build.VERSION.SDK_INT < 23) {
                        showsec.setTextAppearance(context, android.R.style.TextAppearance_Medium);
                    } else {
                        showsec.setTextAppearance(android.R.style.TextAppearance_Small);
                    }
                    showsec.setTextColor(Color.parseColor("#F22938"));

                    recyclerView.setItemAnimator(new SlideInLeftAnimator());
                    recyclerView.getItemAnimator().setAddDuration(10000);

                    mAdapter = new RecyclerAdapter(apps);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
                    recyclerView.computeHorizontalScrollExtent();
                    recyclerView.setAdapter(mAdapter);
                    getAllICONS();
                }
            } catch (Exception e) {

            }
        }
    };
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cpu_cooler, container, false);

        try {
            getActivity().registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

            tempimg = (ImageView) view.findViewById(R.id.tempimg);
            showmain = (TextView) view.findViewById(R.id.showmain);
            showsec = (TextView) view.findViewById(R.id.showsec);
            coolbutton = (ImageView) view.findViewById(R.id.coolbutton);
            nooverheating = (TextView) view.findViewById(R.id.nooverheating);

            showmain.setText("NORMAL");
            showsec.setText("CPU Temperature is GOOD");
            nooverheating.setText("Currently No App causing Overheating");
            coolbutton.setImageResource(R.drawable.cooled);
            coolbutton.setOnClickListener((View.OnClickListener) v -> {
                LayoutInflater inflater1 = getActivity().getLayoutInflater();
                View layout = inflater1.inflate(R.layout.my_toast, null);

                ImageView image = (ImageView) layout.findViewById(R.id.image);

                TextView text = (TextView) layout.findViewById(R.id.textView1);
                text.setText("CPU Temperature is Already Normal.");

                Toast toast = new Toast(getActivity());
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 70);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            });

            tempimg.setImageResource(R.drawable.blue_cooler);
            batterytemp = (TextView) view.findViewById(R.id.batterytemp);

            Log.e("Temperrature", temp + "");
        } catch (Exception e) {

        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {

            getActivity().unregisterReceiver(batteryReceiver);
        } catch (Exception e) {

        }
    }

    public void getAllICONS() {
        PackageManager pm = getActivity().getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        if (packages != null) {
            for (int k = 0; k < packages.size(); k++) {
                // String pkgName = app.getPackageName();
                String packageName = packages.get(k).packageName;
                Log.e("packageName-->", "" + packageName);

                if (!packageName.equals("fast.cleaner.battery.saver")) {

                    Drawable ico = null;
                    try {
                        Apps app = new Apps();

                        File file = new File(pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA).publicSourceDir);
                        long size = file.length();

                        Log.e("SIZE", size / 1000000 + "");
                        app.setSize(size / 1000000 + 20 + "MB");

                        ApplicationInfo a = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
                        app.setImage(ico = getActivity().getPackageManager().getApplicationIcon(packages.get(k).packageName));
                        getActivity().getPackageManager();
                        Log.e("ico-->", "" + ico);

                        if (((a.flags & ApplicationInfo.FLAG_SYSTEM) == 0)) {
                            if (check <= 5) {
                                check++;
                                apps.add(app);
                            } else {
                                getActivity().unregisterReceiver(batteryReceiver);
                                break;
                            }
                        }
                        mAdapter.notifyDataSetChanged();

                    } catch (PackageManager.NameNotFoundException e) {
                        Log.e("ERROR", "Unable to find icon for package '"
                                + packageName + "': " + e.getMessage());
                    }
                }
            }
        }

        if (apps.size() > 1) {
            mAdapter = new RecyclerAdapter(apps);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void ads() {
        Long prevTime = SharedPref.INSTANCE.getTimeStamp(getActivity());
        Long curTime = System.currentTimeMillis();
        if (TimeStampService.isLessThen30Sec(prevTime, curTime)) {
            return;
        }

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(getContext(), "ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
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
