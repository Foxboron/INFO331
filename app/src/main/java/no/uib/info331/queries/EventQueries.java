package no.uib.info331.queries;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.google.gson.reflect.TypeToken;


import org.greenrobot.eventbus.EventBus;

import java.util.List;

import no.uib.info331.models.Event;
import no.uib.info331.models.User;
import no.uib.info331.models.messages.EventEvent;
import no.uib.info331.models.messages.EventListEvent;
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
                } else {
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
            }
        });
    }

    public void getLatestEvent(final Context context){
        final User signedInUser = dataManager.getSavedObjectFromSharedPref(context, "currentlySignedInUser", new TypeToken<User>(){}.getType());
        String credentials = signedInUser.getUsername() + ":" + signedInUser.getPassword();
        final String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        Call<List<Event>> call = apiService.getEventsForUser(basic, signedInUser.getID());
        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response.code() == 200) {
                    int latestIndex = response.body().size()-1;
                    if(!response.body().isEmpty()){
                        EventBus.getDefault().post(new EventEvent(response.body().get(latestIndex)));
                    }else{
                        System.out.println("There was no latest event");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {

            }
        });

    }

    public void getAllEventsForUser(final int USER_ID, final Context CONTEXT){
        User signedInUser = dataManager.getSavedObjectFromSharedPref(CONTEXT, "currentlySignedInUser", new TypeToken<User>(){}.getType());
        String credentials = signedInUser.getUsername() + ":" + signedInUser.getPassword();
        final String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        Call<List<Event>> call = apiService.getEventsForUser(basic, USER_ID);
        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (response.code() == 200) {
                    EventBus.getDefault().post(new EventListEvent(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {

            }
        });
    }
}
