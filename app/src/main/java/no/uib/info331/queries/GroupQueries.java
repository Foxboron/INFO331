package no.uib.info331.queries;

import android.content.Context;
import android.util.Base64;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import no.uib.info331.R;
import no.uib.info331.models.Beacon;
import no.uib.info331.models.Group;
import no.uib.info331.models.User;
import no.uib.info331.models.messages.BackgroundBeaconEvent;
import no.uib.info331.models.messages.GroupListEvent;
import no.uib.info331.models.messages.UserListEvent;
import no.uib.info331.util.ApiClient;
import no.uib.info331.util.ApiInterface;
import no.uib.info331.util.DataManager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Group related queries against the backend
 * @author Edvard P. Bjørgen
 */

public class GroupQueries {

    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    DataManager dataManager = new DataManager();

    /**
     * Search for a group in the database and save it to sharedPreferences, also deletes the results when returned
     * @param context The activity context
     * @param query The query to search for
     * @return The list of groups where the name matches the query
     */
    public void getGroupsByStringFromDb(final Context context, String query) {
        User signedInUser = dataManager.getSavedObjectFromSharedPref(context, "currentlySignedInUser", new TypeToken<User>(){}.getType());
        String credentials = signedInUser.getUsername() + ":" + signedInUser.getPassword();

        final String prefKey = "searchGroupByUsername";
        final String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        Call<List<Group>> call = apiService.searchGroupByName(basic, query);

        call.enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                List<Group> allGroups = new ArrayList<>();

                if(response.code()==200) {
                    for(Group group: response.body()) {
                        allGroups.add(group);
                    }
                    EventBus.getDefault().post(new GroupListEvent(allGroups));
                    dataManager.storeObjectInSharedPref(context, prefKey, allGroups);
                } else {
                }
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
            }
        });

    }

    /**
     * Creates and writes a new group with a list of members to db.
     * @param groupName the name of the new group
     * @param ADDED_USERS_TO_GROUP an ArrayList of users to be included in the group
     * @param beacon the beacon for this group
     * @param context the context of the activity where this method is called from
     */
        public void registerGroupQuery(String groupName, final ArrayList<User> ADDED_USERS_TO_GROUP, final Beacon beacon, Context context){
            final DataManager dataManager = new DataManager();
            final User currentUser = dataManager.getSavedObjectFromSharedPref(context, "currentlySignedInUser", new TypeToken<User>(){}.getType());
            final String credentials = currentUser.getUsername() + ":" + currentUser.getPassword();
            final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            Group group = new Group(groupName, currentUser);
            final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<Group> call = apiService.createGroup(basic, group.getName());

            final boolean[] error = {false};
            call.enqueue(new Callback<Group>() {
                @Override
                public void onResponse(Call<Group> call, Response<Group> response) {
                    if(response.code()==200) {
                        final Group registeredGroup = response.body();
                        for (User member : ADDED_USERS_TO_GROUP) {
                            Call<ResponseBody> addUserCall = apiService.addUserToGroup(basic, registeredGroup.getId(), member.getID());
                            addUserCall.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if(response.code()!=200) {
                                        error[0] = true;
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    error[0] = true;
                                    t.printStackTrace();
                                }
                            });
                        }

                        Call<ResponseBody> addBeaconCall = apiService.addBeaconToGroup(basic, registeredGroup.getId(), beacon.getID());
                        addBeaconCall.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.code() != 200){
                                    error[0] = true;
                                } else {
                                    EventBus.getDefault().post(new BackgroundBeaconEvent(beacon));
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                error[0] = true;
                                t.printStackTrace();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<Group> call, Throwable t) {
                    error[0] = true;
                    t.printStackTrace();
                }
            });

        }

    /**
     * Updates the group, [NOT BEEN TESTED YET, MAY NOT WORK]
     * @param context the context of the activity where this method is called from
     * @param GROUP gorup object to update
     * @param USER the user that performs this action, ususally currently logged in user
     */
    public void updateGroup(final Context context, final Group GROUP, final User USER ){
        final String credentials = USER.getUsername() + ":" + USER.getPassword();
        final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        Call<Group> call = apiService.updateGroupById(basic, GROUP, GROUP.getId());

        final boolean[] error = {false};
        call.enqueue(new Callback<Group>() {
            @Override
            public void onResponse(Call<Group> call, Response<Group> response) {

                if(response.code()==200) {
                    Toast.makeText(context, "Added: " +  USER.getUsername() + " to " + GROUP.getName(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Group> call, Throwable t) {
                error[0] = true;
            }
        });

        if(!error[0]){

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
        }

    }

    public void getAllGroups(final Context context) {

        User signedInUser = dataManager.getSavedObjectFromSharedPref(context, "currentlySignedInUser", new TypeToken<User>(){}.getType());
        String credentials = signedInUser.getUsername() + ":" + signedInUser.getPassword();

        final String prefKey = "getAllGroups";
        final String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        Call<List<Group>> call = apiService.getAllGroups(basic);

        call.enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                List<Group> allGroups = new ArrayList<>();

                if(response.code()==200) {
                    for(Group group: response.body()) {
                        allGroups.add(group);
                    }
                    EventBus.getDefault().post(new GroupListEvent(allGroups));
                    dataManager.storeObjectInSharedPref(context, prefKey, allGroups);
                } else {
                }
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
            }
        });

    }

    public void getUsersInGroup(int groupId, Context context) {
        User signedInUser = dataManager.getSavedObjectFromSharedPref(context, "currentlySignedInUser", new TypeToken<User>(){}.getType());
        String credentials = signedInUser.getUsername() + ":" + signedInUser.getPassword();

        final String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        Call<Group> call = apiService.getGroupById(basic, groupId);
        call.enqueue(new Callback<Group>() {
            @Override
            public void onResponse(Call<Group> call, Response<Group> response) {
                if (response.code() == 200) {
                    EventBus.getDefault().post(new UserListEvent(response.body().getUsers()));
                }

            }

            @Override
            public void onFailure(Call<Group> call, Throwable t) {

            }
        });

    }
}