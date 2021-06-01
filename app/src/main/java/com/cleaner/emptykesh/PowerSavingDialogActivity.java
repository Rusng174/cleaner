package com.cleaner.emptykesh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleaner.emptykesh.dto.PowerItem;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class PowerSavingDialogActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PowerAdapter mAdapter;
    private List<PowerItem> items;
    private ImageView applied;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private TextView extendedtime, extendedtimedetail;

    private int hour;
    private int min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        setContentView(R.layout.powersaving_popup);
        sharedpreferences = getSharedPreferences("was", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        extendedtime = findViewById(R.id.addedtime);
        extendedtimedetail = findViewById(R.id.addedtimedetail);

        try {
            hour = Integer.parseInt(b.getString("hour").replaceAll("[^0-9]", ""))
                    - Integer.parseInt(b.getString("hournormal").replaceAll("[^0-9]", ""));
            min = Integer.parseInt(b.getString("minutes").replaceAll("[^0-9]", ""))
                    - Integer.parseInt(b.getString("minutesnormal").replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            hour = 3;
            min = 5;
        }

        if (hour == 0 && min == 0) {
            hour = 3;
            min = 5;
        }
        extendedtime.setText("(+" + hour + " h " + Math.abs(min) + " m)");
        extendedtimedetail.setText("Extended Battery Up to " + Math.abs(hour) + "h " + Math.abs(min) + "m");

        items = new ArrayList<>();
        applied = findViewById(R.id.applied);
        applied.setOnClickListener(v -> {
            editor.putString("mode", "1");
            editor.apply();

            Intent i = new Intent(getApplicationContext(), PowerSavingCompleteActivity.class);
            startActivity(i);

            finish();
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setItemAnimator(new SlideInLeftAnimator());
        recyclerView.getItemAnimator().setAddDuration(200);

        mAdapter = new PowerAdapter(items);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
        recyclerView.computeHorizontalScrollExtent();
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        final Handler handler1 = new Handler();
        handler1.postDelayed(() -> add("Limit Brightness Upto 80%", 0), 1000);

        final Handler handler2 = new Handler();
        handler2.postDelayed(() -> add("Decrease Device Performance", 1), 2000);

        final Handler handler3 = new Handler();
        handler3.postDelayed(() -> add("Close All Battery Consuming Apps", 2), 3000);

        final Handler handler4 = new Handler();
        handler4.postDelayed(() -> add("Closes System Services like Bluetooth,Screen Rotation,Sync etc.", 3), 4000);
    }

    public void add(String text, int position) {
        PowerItem item = new PowerItem();
        item.setText(text);
        items.add(item);
        mAdapter.notifyItemInserted(position);
    }
}
