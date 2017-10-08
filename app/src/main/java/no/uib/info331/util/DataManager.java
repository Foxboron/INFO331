package no.uib.info331.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by EddiStat on 05.10.2017.
 */

public class DataManager {

    public void storeObjectInSharedPref(Context context, String sharedPrefKey, Object objectToStore) {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String serializedObject = gson.toJson(objectToStore);
        editor.putString(sharedPrefKey, serializedObject);
        editor.apply();
    }


  /*to retrive object stored in preference
    mObject = getSavedObjectFromSharedPref(context, "mObjectKey", SampleClass.class);*/

    public <GenericClass> GenericClass getSavedObjectFromSharedPref(Context context, String sharedPrefKey, Type type) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);


        if (sharedPreferences.contains(sharedPrefKey)) {
            final Gson gson = new Gson();
            return gson.fromJson(sharedPreferences.getString(sharedPrefKey, ""), type);
        }

        return null;
    }


    public void deleteSavedObjectFromSharedPref(Context context, String sharedPrefKey){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().remove(sharedPrefKey).apply();
    }
}
