package no.uib.info331.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import no.uib.info331.models.UserAdapter;
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

    private LayoutAdjustments layoutAdj = new LayoutAdjustments();
    UserListViewAdapter userListViewAdapter;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_profile);
        context = getApplicationContext();

        //Group from the last activity
        currentGroup = getGroupFromLastActivity();

        initGui();


    }

    private void initGui() {
        ButterKnife.bind(this);
        initToolbar();
        initListViewMemberList(currentGroup.getUsers());
        initListeners();

    }

    private void initListeners() {
        listViewMemberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = userListViewAdapter.getItem(i);
                Gson gson = new Gson();
                String userStringObject = gson.toJson(user);
                Intent intent = new Intent(context, UserProfileActivity.class);
                intent.putExtra("user", userStringObject);
                startActivity(intent);


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
            toolbarTitle.setText("Group name");

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
            // and get whatever type user account id is
            return group;
        } else {
            group = new Group("Error", new User("Not a valid user", "nan", "nan", 0));
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
