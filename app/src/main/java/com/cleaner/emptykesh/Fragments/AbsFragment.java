package com.cleaner.emptykesh.Fragments;

import android.util.Log;

import androidx.fragment.app.Fragment;

public class AbsFragment extends Fragment {
    @Override
    public void onStart() {
        super.onStart();
        Log.d("EMPTYKESH_fragment", getClass().toString());
    }
}