package com.cleaner.emptykesh;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleaner.emptykesh.Classes.Apps;
import com.cleaner.emptykesh.Fragments.CoolerCPU;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class Cpu_Scanner extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;
    ImageView scanner, img_animation, cpu;
    Scan_Cpu_Apps mAdapter;
    RecyclerView recyclerView;
    List<Apps> app = null;
    TextView cooledcpu;
    RelativeLayout rel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cpu_scanner);
        scanner = (ImageView) findViewById(R.id.scann);
        cpu = (ImageView) findViewById(R.id.cpu);
        cooledcpu = (TextView) findViewById(R.id.cpucooler);
        img_animation = (ImageView) findViewById(R.id.heart);
        rel = (RelativeLayout) findViewById(R.id.rel);
        app = new ArrayList<>();

        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1500);
        rotate.setRepeatCount(3);
        rotate.setInterpolator(new LinearInterpolator());
        scanner.startAnimation(rotate);

        TranslateAnimation animation = new TranslateAnimation(0.0f, 1000.0f, 0.0f, 0.0f);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        animation.setDuration(5000);  // animation duration
        animation.setRepeatCount(0);
        animation.setInterpolator(new LinearInterpolator());// animation repeat count
        animation.setFillAfter(true);

        img_animation.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img_animation.setImageResource(0);
                img_animation.setBackgroundResource(0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setItemAnimator(new SlideInLeftAnimator());

        mAdapter = new Scan_Cpu_Apps(CoolerCPU.apps);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
        recyclerView.computeHorizontalScrollExtent();
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        try {
            final Handler handler1 = new Handler();
            handler1.postDelayed(() -> add("Limit Brightness Upto 80%", 0), 0);

            final Handler handler2 = new Handler();
            handler2.postDelayed(() -> {
                remove(0);
                add("Decrease Device Performance", 1);
            }, 900);

            final Handler handler3 = new Handler();
            handler3.postDelayed(() -> {
                remove(0);
                add("Close All Battery Consuming Apps", 2);
            }, 1800);

            final Handler handler4 = new Handler();
            handler4.postDelayed(() -> {
                remove(0);
                add("Closes System Services like Bluetooth,Screen Rotation,Sync etc.", 3);
            }, 2700);

            final Handler handler5 = new Handler();
            handler5.postDelayed(() -> {
                remove(0);
                add("Closes System Services like Bluetooth,Screen Rotation,Sync etc.", 4);
            }, 3700);
//
            final Handler handler6 = new Handler();
            handler6.postDelayed(() -> {
                remove(0);
                add("Closes System Services like Bluetooth,Screen Rotation,Sync etc.", 5);
            }, 4400);

            final Handler handler7 = new Handler();
            handler7.postDelayed((Runnable) () -> {
                add("Closes System Services like Bluetooth,Screen Rotation,Sync etc.", 6);
                remove(0);

                final RippleBackground rippleBackground = findViewById(R.id.content);
                rippleBackground.startRippleAnimation();

                img_animation.setImageResource(0);
                img_animation.setBackgroundResource(0);
                cpu.setImageResource(R.drawable.green_circle);
                scanner.setImageResource(R.drawable.task_complete);
                ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.flipping);
                anim.setTarget(scanner);
                anim.setDuration(3000);
                anim.start();

                rel.setVisibility(View.GONE);

                cooledcpu.setText("Cooled CPU to 25.3°C");
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation1) {
                        img_animation.setImageResource(0);
                        img_animation.setBackgroundResource(0);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation1) {
                        rippleBackground.stopRippleAnimation();
                        final Handler handler61 = new Handler();
                        handler61.postDelayed(() -> {
                            finish();
                        }, 1000);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation1) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation1) {

                    }
                });

            }, 5500);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void add(String text, int position) {
        try {
            mAdapter.notifyItemInserted(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remove(int position) {
        mAdapter.notifyItemRemoved(position);
        try {
            CoolerCPU.apps.remove(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
    }
}