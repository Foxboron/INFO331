package no.uib.info331.queries;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import no.uib.info331.models.Beacon;
import no.uib.info331.models.User;
import no.uib.info331.util.ApiClient;
import no.uib.info331.util.ApiInterface;
import no.uib.info331.util.DataManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Beaconrelated queries against the backend
 * @author Fredrik V. Heimsæter
 */

public class BeaconQueries {

    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    DataManager dataManager = new DataManager();

    public List<Beacon> getBeaconsByStringFromDb(final Context context, String query){

        //username:password
        User signedInUser = dataManager.getSavedObjectFromSharedPref(context, "currentlySignedInUser", new TypeToken<User>(){}.getType());
        String credentials = signedInUser.getUsername() + ":" + signedInUser.getPassword();

        Log.d("TAG", credentials);
        final String prefKey = "searchBeaconByName";
        final String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        Call<List<Beacon>> call = apiService.searchBeaconByName(basic, query);
        call.enqueue(new Callback<List<Beacon>>() {
            @Override
            public void onResponse(Call<List<Beacon>> call, Response<List<Beacon>> response) {
                List<Beacon> allBeacons = new ArrayList<Beacon>();
                if (response.code() == 200){
                    for (Beacon beacon : response.body()) {
                        allBeacons.add(beacon);
                    }
                    dataManager.storeObjectInSharedPref(context, prefKey, allBeacons);
                }
            }

            @Override
            public void onFailure(Call<List<Beacon>> call, Throwable t) {
                Log.e("Search beacons", t.getMessage());

            }
        });

        Type type = new TypeToken<List<Beacon>>(){}.getType();

        List<Beacon> result = dataManager.getSavedObjectFromSharedPref(context, prefKey, type);
        dataManager.deleteSavedObjectFromSharedPref(context, prefKey);

        return result;


    }
}