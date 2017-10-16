package no.uib.info331.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import no.uib.info331.R;
import no.uib.info331.adapters.UserListViewAdapter;
import no.uib.info331.models.Group;
import no.uib.info331.models.User;
import no.uib.info331.queries.GroupQueries;
import no.uib.info331.util.DataManager;
import no.uib.info331.util.LayoutAdjustments;

/**
 * Created by perni on 04.10.2017.
 */

public class GroupProfileActivity extends AppCompatActivity {
    private ArrayList<User> users;
    private Group currentGroup;
    private ListView userList;
    private TextView points;
    private TextView gname;

    @BindView(R.id.toolbar_group_profile) Toolbar toolbar;
    @BindView(R.id.textview_group_profile_toolbar_title) TextView toolbarTitle;
    @BindView(R.id.listview_show_members_in_group) ListView listViewMemberList;
    @BindView(R.id.textview_group_display_name) TextView textViewGroupDisplayName;
    @BindView(R.id.textview_group_pts) TextView textViewGroupPoints;
    @BindView(R.id.btn_join_group) Button btnJoinGroup;

    private LayoutAdjustments layoutAdj = new LayoutAdjustments();
    UserListViewAdapter userListViewAdapter;
    GroupQueries groupQueries = new GroupQueries();
    Context context;
    User currentUser;
    private DataManager dataManager = new DataManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_profile);
        context = getApplicationContext();

        //Group from the last activity
        currentGroup = getGroupFromLastActivity();
        currentUser = dataManager.getSavedObjectFromSharedPref(context, "currentlySignedInUser", new TypeToken<User>(){}.getType());

        initGui();


    }

    private void initGui() {
        ButterKnife.bind(this);
        textViewGroupDisplayName.setText(currentGroup.getName());
        textViewGroupPoints.setText( getResources().getString(R.string.points)+ ": "+String.valueOf(currentGroup.getPoints()));
        initToolbar();
        checkIfUserIsAlreadyInGroup();
        initListViewMemberList(currentGroup.getUsers());
        initListeners();

    }


    /**
     * Quick fix for checking ig currentUser is in group already, if so, disable the "join group" button
     */
    private void checkIfUserIsAlreadyInGroup() {
        for(User member : currentGroup.getUsers()){
            if(member.getUsername().equals(currentUser.getUsername())){
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
                Gson gson = new Gson();
                String userStringObject = gson.toJson(user);
                Intent intent = new Intent(context, UserProfileActivity.class);
                intent.putExtra("currentUser", userStringObject);
                startActivity(intent);
            }
        });

        btnJoinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //adds currently logged in currentUser to  on the screengroup
                User updatedUser = currentUser;
                ArrayList userCurrentGroups;
                if (updatedUser.getGroups() == null) {
                    userCurrentGroups = new ArrayList<Group>();
                } else {
                    userCurrentGroups = updatedUser.getGroups();
                }
                userCurrentGroups.add(updatedUser);
                updatedUser.setGroups(userCurrentGroups);

                currentGroup.addMember(updatedUser);

                //TODO: Add so taht we can add individual users to existing groups, not creating a new group from the same one and adding to it
                //groupQueries.updateGroup(context, currentGroup, updatedUser);


                //Uopdate the user object with new group
                // TODO: Gets an OutOfMemory exception, user object too large becuase it has 15-20 groups. The nesting is too much
                //dataManager.updateSavedObjectInSharedPref(getApplicationContext(), "currentlySignedInUser", tempUser);;

                //TODO: Find a way to get response from AddUserGroup, this adds to UI-listView even if something wet wrong.
               // userListViewAdapter.add(currentUser);
                userListViewAdapter.notifyDataSetChanged();
                checkIfUserIsAlreadyInGroup();
            }
        });
    }

    private void initListViewMemberList(List<User> usersInGroup) {
        userListViewAdapter = new UserListViewAdapter(context, R.layout.list_element_search_members, usersInGroup);
        listViewMemberList.setAdapter(userListViewAdapter);

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

    public Group getGroupFromLastActivity() {
        Bundle extras = getIntent().getExtras();
        String groupStringObject;
        Group group;
        Gson gson = new Gson();

        if (extras != null) {
            groupStringObject = extras.getString("group");
            Type type = new TypeToken<Group>(){}.getType();

            group = gson.fromJson(groupStringObject, type);
            // and get whatever type currentUser account id is
            return group;
        } else {
            group = new Group("Error", new User("Not a valid currentUser", "nan", "nan", 0));
            return group;
        }
    }

    /**
     * Method from activity to dispaly the "Back arrow"
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
