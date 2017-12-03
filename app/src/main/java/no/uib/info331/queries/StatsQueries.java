package no.uib.info331.queries;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import no.uib.info331.R;
import no.uib.info331.models.Score;
import no.uib.info331.models.User;
import no.uib.info331.models.messages.GroupScoreEvent;
import no.uib.info331.models.messages.ScoreEvent;
import no.uib.info331.util.ApiClient;
import no.uib.info331.util.ApiInterface;
import no.uib.info331.util.DataManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Fredrik V. Heimsæter
 * @version 1.0
 */

public class StatsQueries {

    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    DataManager dataManager = new DataManager();

    public StatsQueries() {
    }

    public void getUserScore(int userId, Context context){
        User signedInUser = dataManager.getSavedObjectFromSharedPref(context, "currentlySignedInUser", new TypeToken<User>(){}.getType());
        String credentials = signedInUser.getUsername() + ":" + signedInUser.getPassword();
        final String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        Call<Score> call = apiService.getStatsForUser(basic, userId);
        call.enqueue(new Callback<Score>() {
            @Override
            public void onResponse(Call<Score> call, Response<Score> response) {
                if (response.code() == 200){
                    EventBus.getDefault().post(new ScoreEvent(response.body()));

                }
            }

            @Override
            public void onFailure(Call<Score> call, Throwable t) {

            }
        });
    }

    public void getUserScoreInGroup(int userId, int groupId, Context context) {
        User signedInUser = dataManager.getSavedObjectFromSharedPref(context, "currentlySignedInUser", new TypeToken<User>(){}.getType());
        String credentials = signedInUser.getUsername() + ":" + signedInUser.getPassword();
        final String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        Call<Score> call = apiService.getStatsForUserInGroup(basic, userId, groupId);
        call.enqueue(new Callback<Score>() {
            @Override
            public void onResponse(Call<Score> call, Response<Score> response) {
                if (response.code() == 200){
                    EventBus.getDefault().post(new ScoreEvent(response.body()));

                }
            }

            @Override
            public void onFailure(Call<Score> call, Throwable t) {

            }
        });
    }
    public void getGroupScore(int groupId, Context context){
        User signedInUser = dataManager.getSavedObjectFromSharedPref(context, "currentlySignedInUser", new TypeToken<User>(){}.getType());
        String credentials = signedInUser.getUsername() + ":" + signedInUser.getPassword();
        final String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        Call<Score> call = apiService.getStatsForGroup(basic, groupId);
        call.enqueue(new Callback<Score>() {
            @Override
            public void onResponse(Call<Score> call, Response<Score> response) {
                if (response.code() == 200){
                    EventBus.getDefault().post(new GroupScoreEvent(response.body()));
                }
            }

            @Override
            public void onFailure(Call<Score> call, Throwable t) {

            }
        });
    }


}
