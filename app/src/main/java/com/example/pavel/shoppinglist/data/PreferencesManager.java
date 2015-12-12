package com.example.pavel.shoppinglist.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;


public class PreferencesManager {




    public static void saveData(Context context, String listName, String savingTex) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(listName, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("", savingTex);
        editor.commit();

    }

    public static void saveData(Context context, String listName, JSONArray jsonArray) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(listName, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("", jsonArray.toString());
        editor.commit();

    }


    public static String loadObject(Context context, String listName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(listName, context.MODE_PRIVATE);
        return sharedPreferences.getString("", "");
    }

    public static JSONArray loadObjectInJSONArray(Context context, String listName) throws JSONException {
        SharedPreferences sharedPreferences = context.getSharedPreferences(listName, context.MODE_PRIVATE);
        return new JSONArray(sharedPreferences.getString("", ""));
    }


    public static void delFile(Context context, String name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
