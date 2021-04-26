package com.cleaner.emptykesh.service

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object SharedPref {
    private const val PREF_NAME = "pref_name"

    private fun getPref(context: Context): SharedPreferences? {
        return context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
        )
    }

    private fun getString(context: Context, key: String): String {
        return getString(context, key, "")
    }

    private fun getString(
            context: Context?,
            key: String,
            defaultValue: String
    ): String {
        return (if (context != null && getPref(context) != null) {
            getPref(context)!!.getString(key, defaultValue)
        } else {
            defaultValue
        }).toString()
    }

    private fun saveString(
            context: Context?,
            key: String,
            value: String
    ) {
        if (context != null && getPref(context) != null) {
            getPref(context)!!.edit().putString(key, value).apply()
        } else {
            Log.e("emptykesh", "context is null")
        }
    }

    public fun saveTimeStamp(context: Context?, timestamp: Long) {
        saveLong(context, "timestamp", timestamp)
    }

    public fun getTimeStamp(context: Context?): Long {
        return getLong(context, "timestamp", 0)
    }

    private fun saveLong(
            context: Context?,
            key: String,
            value: Long
    ) {
        if (context != null && getPref(context) != null) {
            getPref(context)!!.edit().putLong(key, value).apply()
        } else {
            Log.e("emptykesh", "context is null")
        }
    }

    private fun getLong(
            context: Context?,
            key: String,
            defaultValue: Long
    ): Long {
        return (if (context != null && getPref(context) != null) {
            getPref(context)!!.getLong(key, defaultValue)
        } else {
            defaultValue
        })
    }

}
