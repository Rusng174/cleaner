package com.cleaner.emptykesh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.cleaner.emptykesh.Fragments.BoosterPhoneFragment;

public final class Alaram_Booster extends BroadcastReceiver {
    SharedPreferences.Editor editor;
    SharedPreferences sharedpreferences;

    @Override
    public void onReceive(Context context, Intent intent) {

        sharedpreferences = context.getSharedPreferences("waseem", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString("booster", "1");
        editor.apply();

        try {
            BoosterPhoneFragment.optimizebutton.setBackgroundResource(0);
            BoosterPhoneFragment.optimizebutton.setImageResource(0);
            BoosterPhoneFragment.optimizebutton.setImageResource(R.drawable.optimize);
        } catch (Exception e) {

        }

    }
}
