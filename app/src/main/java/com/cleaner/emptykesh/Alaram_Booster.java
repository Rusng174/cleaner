package com.cleaner.emptykesh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.cleaner.emptykesh.Fragments.BoosterPhone;

public final class Alaram_Booster extends BroadcastReceiver {
  SharedPreferences.Editor editor;
  SharedPreferences sharedpreferences;
  @Override
  public void onReceive(Context context, Intent intent) {

    sharedpreferences = context.getSharedPreferences("waseem", Context.MODE_PRIVATE);
//        Toast.makeText(context, "Alarm worked.", Toast.LENGTH_LONG).show();



    /// when memory is orveloaded or increased


    editor = sharedpreferences.edit();
    editor.putString("booster", "1");
    editor.commit();

    try {
      BoosterPhone.optimizebutton.setBackgroundResource(0);
      BoosterPhone.optimizebutton.setImageResource(0);
      BoosterPhone.optimizebutton.setImageResource(R.drawable.optimize);
    }
    catch(Exception e)
    {

    }

  }
}
