package no.uib.info331.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import no.uib.info331.R;
import no.uib.info331.models.User;
import no.uib.info331.util.DataManager;

/**
 * Activity that determines what action to do when started, based on the device and/or profileUser.
 * If first time use, the slpash screen will send the profileUser to LoginActivity. If profileUser has logged in
 * before and do not have a group, it will send the profileUser to CreateJoinGroupActivity, if profileUser has
 * logged in before and has registered in a group, it will be sent to DashboardActivity
 *
 * @author  Fredrik V. Heimsæter
 *
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(sharedPreferences.contains("currentlySignedInUser")) {
            DataManager dataManager = new DataManager();
            User user = dataManager.getSavedObjectFromSharedPref(getApplicationContext(), "currentlySignedInUser", new TypeToken<User>(){}.getType());
            if(user.getGroups()==null || user.getGroups().size()< 1){
                Intent intent = new Intent(getApplicationContext(), CreateJoinGroupActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
