package no.uib.info331.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import no.uib.info331.R;
import no.uib.info331.adapters.GroupListViewAdapter;
import no.uib.info331.models.Event;
import no.uib.info331.models.Group;
import no.uib.info331.models.Score;
import no.uib.info331.models.User;
import no.uib.info331.models.messages.EventEvent;
import no.uib.info331.models.messages.ScoreEvent;
import no.uib.info331.queries.EventQueries;
import no.uib.info331.queries.StatsQueries;
import no.uib.info331.util.Animations;
import no.uib.info331.util.ApiClient;
import no.uib.info331.util.ApiInterface;
import no.uib.info331.util.DataManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity that displays the profileUser relevant info, and is the "main" activity of the app.
 *
 * @author Edvard P. Bjørgen, Fredrik V. Heimsæter
 *
 */

public class DashboardActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.listview_dashboard_user_groups) ListView listViewGroupList;
    @BindView(R.id.scrollview_dashboard) ScrollView scrollViewDashboard;
    @BindView(R.id.textview_dashboard_latest_activity_text) TextView textViewLatestActivityText;
    @BindView(R.id.textview_dashboard_latest_activity_timestamp) TextView textViewLatestActivityTimestamp;
    @BindView(R.id.btn_dashboard_latest_activity) Button btnLatestActivityRefresh;
    @BindView(R.id.textview_dashboard_points) TextView textViewPoints;
    TextView toolbarTitle;
    ImageButton btnHamburgerMenu;

    DataManager dataManager = new DataManager();
    EventQueries eventQueries = new EventQueries();
    StatsQueries statsQueries = new StatsQueries();
    Animations anim = new Animations();
    Context context;

    private Drawer result;
    private User user;
    private GroupListViewAdapter userGroupsListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        context = getApplicationContext();
        user = dataManager.getSavedObjectFromSharedPref(context, "currentlySignedInUser", new TypeToken<User>(){}.getType());

        initGui();
    }

    private void initGui() {
        btnHamburgerMenu = (ImageButton) findViewById(R.id.btn_menu_dashboard);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_buildselect_title);

        anim.fadeInView(toolbarTitle, 200, 200);

        initToolbar();
        initDrawer();
        if(user.getGroups()==null) {
            Group noGroups = new Group("No groups", user);
            ArrayList<Group> listOfNoGroups = new ArrayList<>();
            listOfNoGroups.add(noGroups);
            initListViewGroupList(listOfNoGroups);
        } else {
            initListViewGroupList(user.getGroups());
        }
        initLastEvent();
        initPoints();
        initListeners();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScoreEvent(ScoreEvent scoreEvent){
        Log.d("EventBus", "Recieved");
        textViewPoints.setText(Integer.toString(scoreEvent.getScore().getScore()) + " " + getText(R.string.points));
    }

    private void initPoints() {
        statsQueries.getUserScore(user.getID(), getApplicationContext());
    }

    private void initLastEvent() {
        eventQueries.getLatestEvent(getApplicationContext());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventEvent(EventEvent eventEvent){
        Event event = eventEvent.getEvent();
        String text = "";
        if (event.getEvent().equals("Enter")) {
            text = getText(R.string.enter_area) + " " + event.getGroup().getName();
        } else if (event.getEvent().equals("Exit")) {
            text = getText(R.string.exit_area) + " " + event.getGroup().getName();
        } else {
            text = getText(R.string.weird_area) + " " + event.getGroup().getName();
        }
        textViewLatestActivityText.setText(text);
        textViewLatestActivityTimestamp.setText(event.getDate().toString());
    }

    private void initListeners() {
        btnHamburgerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.openDrawer();
            }
        });

        listViewGroupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Group group = userGroupsListViewAdapter.getItem(i);
                Gson gson = new Gson();
                String userStringObject = gson.toJson(group);
                Intent intent = new Intent(context, GroupProfileActivity.class);
                intent.putExtra("group", userStringObject);
                startActivity(intent);
            }
        });

        listViewGroupList.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                scrollViewDashboard.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        btnLatestActivityRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initLastEvent();
                initPoints();
            }
        });

    }

    private void initDrawer() {

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.login_splash)
                .addProfiles(
                        new ProfileDrawerItem().withName(user.getUsername()).withIcon(getResources().getDrawable(R.drawable.avatar2))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        Gson gson = new Gson();
                        String userStringObject = gson.toJson(user);
                        Intent intent = new Intent(context, UserProfileActivity.class);
                        intent.putExtra("profileUser", userStringObject);
                        startActivity(intent);
                        return false;
                    }
                })
                .build();

        //create the drawer and remember the `Drawer` result object
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(false)
                .withSelectedItem(-1)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new SectionDrawerItem().withName(R.string.groups),
                        new SecondaryDrawerItem().withName(R.string.my_groups).withIcon(R.drawable.ic_person_add).withSelectable(false).withIdentifier(0),
                        new SecondaryDrawerItem().withName(R.string.drawer_join_create_group).withIcon(R.drawable.ic_group).withSelectable(false).withIdentifier(1),
                        new SecondaryDrawerItem().withName("MonitorTest").withIcon(R.drawable.ic_bt_beacon).withSelectable(false).withIdentifier(2),
                        new DividerDrawerItem()
                ).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Intent intent = null;
                        switch((int) drawerItem.getIdentifier()){
                            case 0:
                                Toast.makeText(context, "My groups activity, TBD", Toast.LENGTH_SHORT).show();
                                break;

                            case 1:
                                intent = new Intent(context, CreateJoinGroupActivity.class);
                                startActivity(intent);
                                break;
                            case 2:
                                intent = new Intent(context, MonitoringActivity.class);
                                startActivity(intent);
                                break;

                            case 98:
                                dataManager.deleteSavedObjectFromSharedPref(context, "currentlySignedInUser");
                                intent = new Intent(context, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                break;
                            case 99:
                                Toast.makeText(context, "My groups activity, TBD", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                })
                .build();
        result.addStickyFooterItem(new PrimaryDrawerItem().withName(R.string.drawer_temporary_log_out).withIcon(R.drawable.ic_settings).withSelectable(false).withIdentifier(98));
        result.addStickyFooterItem(new PrimaryDrawerItem().withName(R.string.drawer_settings).withIcon(R.drawable.ic_settings).withSelectable(false).withIdentifier(99));
    }

    private void initToolbar() {

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbarTitle.setText(getResources().getString(R.string.dashboard));
        }
    }
    private void initListViewGroupList(List<Group> userGroups) {
        userGroupsListViewAdapter = new GroupListViewAdapter(context, R.layout.list_element_join_group, userGroups);
        listViewGroupList.setAdapter(userGroupsListViewAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        user = dataManager.getSavedObjectFromSharedPref(context, "currentlySignedInUser", new TypeToken<User>(){}.getType());
        initListViewGroupList(user.getGroups());
        initLastEvent();
        initPoints();
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
