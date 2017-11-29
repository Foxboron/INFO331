package no.uib.info331.queries;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.google.gson.reflect.TypeToken;


import java.util.List;

import no.uib.info331.models.Event;
import no.uib.info331.models.User;
import no.uib.info331.util.ApiClient;
import no.uib.info331.util.ApiInterface;
import no.uib.info331.util.DataManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Fredrik V. Heimsæter
 */

public class EventQueries {

    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    DataManager dataManager = new DataManager();

    //TODO should not be void, should return something so that we can give approperiate feedback
    public void createEvent(int groupId, String event, Context context) {

        User signedInUser = dataManager.getSavedObjectFromSharedPref(context, "currentlySignedInUser", new TypeToken<User>(){}.getType());
        String credentials = signedInUser.getUsername() + ":" + signedInUser.getPassword();

        final String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        Call<Event> call = apiService.createEvent(basic, signedInUser.getID(), groupId, event);
        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if (response.code() == 200) {
                    Log.d("Event: ", "All is good");
                } else {
                    Log.d("Event: ", "Wrong status code");
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Log.d("Event: ", "Well, fuck");
                Log.d("Event: ", t.getMessage());
            }
        });
    }

    public void getLatestEvent(final Context context){
        User signedInUser = dataManager.getSavedObjectFromSharedPref(context, "currentlySignedInUser", new TypeToken<User>(){}.getType());
        String credentials = signedInUser.getUsername() + ":" + signedInUser.getPassword();
        final String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        Call<List<Event>> call = apiService.getEventsForUser(basic, signedInUser.getID());
        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response.code() == 200) {
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {

            }
        });

    }
}