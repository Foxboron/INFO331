package no.uib.info331.queries;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import no.uib.info331.R;
import no.uib.info331.activities.DashboardActivity;
import no.uib.info331.models.Event;
import no.uib.info331.models.User;
import no.uib.info331.models.messages.GroupListEvent;
import no.uib.info331.models.messages.UserListEvent;
import no.uib.info331.util.ApiClient;
import no.uib.info331.util.ApiInterface;
import no.uib.info331.util.DataManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * User-related queries against the backend
 * @author Edvard P. Bjørgen
 */

public class UserQueries {

    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    DataManager dataManager = new DataManager();

    /**
     * Search for users in the db and save the results in sharedPreferences
     * @param context The activity context
     * @param query The search query
     * @return The list of users where the username matches the query
     */
    public void getUsersByStringFromDb(final Context context, String query) {
        //username:password
        User signedInUser = dataManager.getSavedObjectFromSharedPref(context, "currentlySignedInUser", new TypeToken<User>(){}.getType());
        String credentials = signedInUser.getUsername() + ":" + signedInUser.getPassword();
        final String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);


        Call<List<User>> call = apiService.searchUserByUsername(basic, query);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> allUsers = new ArrayList<User>();
                System.out.println("onResponse ok");
                System.out.println("response code: " + response.code());

                if(response.code()==200) {
                    //System.out.println(response.body());

                    for(User user: response.body()) {
                        allUsers.add(user);

                    }
                    EventBus.getDefault().post(new UserListEvent(allUsers));

                } else {
                    System.out.println("APA: " + response.body());
                    System.out.println("response: " + response);
                }

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println(t.getMessage());
                System.out.println("FAIL");
            }
        });
    }

    /**
     * Get all the users from the database and print them to the console
     * @param CONTEXT The activity context
     */
    private void getAllUsersFromDb(final Context CONTEXT) {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        //username:password
        User signedInUser = dataManager.getSavedObjectFromSharedPref(CONTEXT, "currentlySignedInUser", new TypeToken<User>(){}.getType());
        String credentials = signedInUser.getUsername() + ":" + signedInUser.getPassword();

        final String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);


        Call<List<User>> call = apiService.getAllUsers(basic);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                if(response.code()==200) {
                    for(User user: response.body()) {
                        System.out.println(user.getUsername());
                    }

                } else {
                    System.out.println("APA: " + response.body());
                    System.out.println("response: " + response);
                }

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println(t.getMessage());

            }
        });

    }

    /**
     * Refreshes the locally stored user.
     * Gets the currently logged in user from SharedPref, and uses that user object to retrieve the
     * user from the db. With the "new" user-object from db, it then stores the retrieved user-object
     * in SharedPref, thus refreshing it.
     *
     * @param CONTEXT the context of the activity where this method is called from
     * @param RESOURCES resoruces from the application
     */
    public void refreshUserQuery(final Context CONTEXT, final Resources RESOURCES){
        final DataManager dataManager = new DataManager();
        final User currentUser = dataManager.getSavedObjectFromSharedPref(CONTEXT, "currentlySignedInUser", new TypeToken<User>(){}.getType());
        final String credentials = currentUser.getUsername() + ":" + currentUser.getPassword();
        final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        final boolean[] error = {false};
        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);



        if(!error[0]){
            Call<User> refreshUserCall = apiService.getUserById(basic, currentUser.getID());
            refreshUserCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.code() == 200) {
                        User refreshedUser = response.body();
                        refreshedUser.setPassword(currentUser.getPassword());
                        dataManager.storeObjectInSharedPref(CONTEXT, "currentlySignedInUser", refreshedUser);
                        Intent intent = new Intent(CONTEXT, DashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        CONTEXT.startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(CONTEXT, RESOURCES.getString(R.string.something_wrong), Toast.LENGTH_LONG).show();

                }
            });
        } else {
            Toast.makeText(CONTEXT, RESOURCES.getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
        }
    }

    public void getUserGroups(int userId, Context context) {
        User signedInUser = dataManager.getSavedObjectFromSharedPref(context, "currentlySignedInUser", new TypeToken<User>(){}.getType());
        String credentials = signedInUser.getUsername() + ":" + signedInUser.getPassword();

        final String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);


        Call<User> call = apiService.getUserById(basic, userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code() == 200) {
                    EventBus.getDefault().post(new GroupListEvent(response.body().getGroups()));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }


}
