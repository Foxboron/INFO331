package no.uib.info331.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import no.uib.info331.R;
import no.uib.info331.adapters.BeaconListViewAdapter;
import no.uib.info331.adapters.UserListViewAdapter;
import no.uib.info331.models.Beacon;
import no.uib.info331.models.Event;
import no.uib.info331.models.Group;
import no.uib.info331.models.Score;
import no.uib.info331.models.User;
import no.uib.info331.models.messages.GroupScoreEvent;
import no.uib.info331.models.messages.ScoreEvent;
import no.uib.info331.queries.StatsQueries;
import no.uib.info331.util.ApiClient;
import no.uib.info331.util.ApiInterface;
import no.uib.info331.util.DataManager;
import no.uib.info331.util.LayoutAdjustments;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity that displays the group information, members. Lets users join the group, if not already
 * in it
 *
 * @author Per-Niklas Longberg, Edvard P. Bjørgen, Fredrik V. Heimsæter
 *
 */

public class GroupProfileActivity extends AppCompatActivity {
    private Group currentGroup;

    @BindView(R.id.toolbar_group_profile) Toolbar toolbar;
    @BindView(R.id.scrollview_group_profile) ScrollView scrollViewGroup;
    @BindView(R.id.textview_group_profile_toolbar_title) TextView toolbarTitle;
    @BindView(R.id.listview_show_members_in_group) ListView listViewMemberList;
    @BindView(R.id.textview_group_display_name) TextView textViewGroupDisplayName;
    @BindView(R.id.textview_group_pts) TextView textViewGroupPoints;
    @BindView(R.id.textview_group_personal_profile_points) TextView textViewPersonalPointsInGroup;
    @BindView(R.id.btn_join_group) Button btnJoinGroup;

    @BindView(R.id.listview_beacon_in_group) ListView listViewBeaconInGroup;

    private LayoutAdjustments layoutAdj = new LayoutAdjustments();
    UserListViewAdapter userListViewAdapter;
    BeaconListViewAdapter beaconListViewAdapter;
    Context context;
    User user;
    private DataManager dataManager = new DataManager();
    private StatsQueries statsQueries = new StatsQueries();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_profile);
        context = getApplicationContext();

        //Group from the last activity
        currentGroup = getGroupFromLastActivity();
        user = dataManager.getSavedObjectFromSharedPref(context, "currentlySignedInUser", new TypeToken<User>(){}.getType());

        initGui();
    }

    private void initGui() {
        ButterKnife.bind(this);
        textViewGroupDisplayName.setText(currentGroup.getName());
        initToolbar();
        checkIfUserIsAlreadyInGroup();
        initListViewMemberList(currentGroup.getUsers());
        initListViewBeaconInGroup();
        initListeners();
        initPointsInGroup();
        initPersonalPointsInGroup();
    }

    private void initListViewBeaconInGroup() {
        List<Beacon> beaconList = new ArrayList<>();
        beaconList.add(currentGroup.getBeacon());
        beaconListViewAdapter = new BeaconListViewAdapter(context, R.layout.list_element_beacon, beaconList);
        listViewBeaconInGroup.setAdapter(beaconListViewAdapter);
    }

    /**
     * Quick fix for checking profileUser is in group already, if so, disable the "join group" button
     */
    private void checkIfUserIsAlreadyInGroup() {
        for(User member : currentGroup.getUsers()){
            if(member.getUsername().equals(user.getUsername())){
                btnJoinGroup.setEnabled(false);
                btnJoinGroup.setText(R.string.already_in_group);
            }
        }
    }

    private void initListeners() {
        listViewMemberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = userListViewAdapter.getItem(i);
                if (user.getGroups() == null) {
                    Log.d("User.groups", "null");
                    String credentials = GroupProfileActivity.this.user.getUsername() + ":" + GroupProfileActivity.this.user.getPassword();
                    final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                    final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                    Call<User> call = apiService.getUserById(basic, user.getID());
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.code() == 200) {
                                Gson gson = new Gson();
                                String userStringObject = gson.toJson(response.body());
                                Intent intent = new Intent(context, UserProfileActivity.class);
                                intent.putExtra("profileUser", userStringObject);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.d("Whoops", "Fuck");
                        }
                    });
                } else {
                    Gson gson = new Gson();
                    String userStringObject = gson.toJson(user);
                    Intent intent = new Intent(context, UserProfileActivity.class);
                    intent.putExtra("profileUser", userStringObject);
                    startActivity(intent);
                }
            }
        });

        /**
         * When profileUser tries to join group, if profileUser is not already in group, it will then add the profileUser to the group
         * The button will be set to "Already joined". First it makes an API call, updating the group with a new profileUser
         * , it then updates the local in-memory profileUser, refreshes the list view of groups members. then it makes
         * a new API-call to get the profileUser again for updating SharedPref.
         *
         */
        btnJoinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!user.getGroups().contains(currentGroup)) {
                    String credentials = user.getUsername() + ":" + user.getPassword();
                    final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                    final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                    Call<ResponseBody> call = apiService.addUserToGroup(basic, currentGroup.getId(), user.getID());
                    call.enqueue(new Callback<ResponseBody>() {
                        // This adds the profileUser to group in DB
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 200) {
                                currentGroup.addMember(user);
                                initListViewMemberList(currentGroup.getUsers());
                                checkIfUserIsAlreadyInGroup();
                                Call<User> refreshUserCall = apiService.getUserById(basic, user.getID());
                                refreshUserCall.enqueue(new Callback<User>() {
                                    //This is for fetching the profileUser from db to update local storage
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        if (response.code() == 200) {
                                            User refreshedUser = response.body();
                                            refreshedUser.setPassword(user.getPassword());
                                            dataManager.storeObjectInSharedPref(getApplicationContext(), "currentlySignedInUser", refreshedUser);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                        Toast.makeText(context, getResources().getString(R.string.could_not_refres_user), Toast.LENGTH_LONG).show();
                                        btnJoinGroup.setEnabled(true);
                                        btnJoinGroup.setText(R.string.group_join);
                                        currentGroup.deleteMember(user);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(context, getResources().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        listViewMemberList.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                scrollViewGroup.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    private void initPointsInGroup() {
        statsQueries.getGroupScore(currentGroup.getId(), getApplicationContext());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGroupScoreEvent(GroupScoreEvent groupScoreEvent) {
        textViewGroupPoints.setText(groupScoreEvent.getScore().getScore() + " " + getText(R.string.points));
    }

    private void initPersonalPointsInGroup() {
        statsQueries.getUserScoreInGroup(user.getID(), currentGroup.getId(), getApplicationContext());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScoreEvent(ScoreEvent scoreEvent) {
        textViewPersonalPointsInGroup.setText(scoreEvent.getScore().getScore() + " " + getText(R.string.points));
    }

    private void initListViewMemberList(List<User> usersInGroup) {
        userListViewAdapter = new UserListViewAdapter(context, R.layout.list_element_search_members, usersInGroup);
        listViewMemberList.setAdapter(userListViewAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        initPointsInGroup();
        initPersonalPointsInGroup();
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbarTitle.setText(currentGroup.getName());

            layoutAdj.setMargins(toolbar, 0, layoutAdj.getStatusBarHeight(getResources()), 0, 0);
        }
    }

    /**
     * Gets a group element as a gson string.
     *
     * @return a group object, built from a gson string
     */
    public Group getGroupFromLastActivity() {
        Bundle extras = getIntent().getExtras();
        String groupStringObject;
        Group group;
        Gson gson = new Gson();

        if (extras != null) {
            groupStringObject = extras.getString("group");
            Type type = new TypeToken<Group>(){}.getType();

            group = gson.fromJson(groupStringObject, type);
            // and get whatever type profileUser account id is
            return group;
        } else {
            group = new Group("Error", new User("Not a valid profileUser", "nan", "nan", 0));
            return group;
        }
    }

    /**
     * Method from activity to display the "Back arrow"
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
