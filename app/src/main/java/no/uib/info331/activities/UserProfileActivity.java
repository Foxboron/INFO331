package no.uib.info331.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.intrusoft.squint.DiagonalView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import no.uib.info331.R;
import no.uib.info331.adapters.EventListViewAdapter;
import no.uib.info331.adapters.GroupListViewAdapter;
import no.uib.info331.models.Event;
import no.uib.info331.models.Group;
import no.uib.info331.models.Score;
import no.uib.info331.models.User;
import no.uib.info331.models.messages.EventListEvent;
import no.uib.info331.models.messages.GroupListEvent;
import no.uib.info331.models.messages.ScoreEvent;
import no.uib.info331.queries.EventQueries;
import no.uib.info331.queries.StatsQueries;
import no.uib.info331.queries.UserQueries;
import no.uib.info331.util.ApiClient;
import no.uib.info331.util.ApiInterface;
import no.uib.info331.util.DataManager;
import no.uib.info331.util.LayoutAdjustments;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity that displays profileUser information, points and groups.
 *
 * @author  Edvard P. Bjørgen, Per-Niklas Longberg
 *
 */


public class UserProfileActivity extends AppCompatActivity {

    User profileUser;
    Context context;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.diagonalview_profile) DiagonalView diagonalViewProfile;
    @BindView(R.id.toolbar_buildselect_title) TextView toolbarTitle;
    @BindView(R.id.circleimageview_user_profile_image) CircleImageView circleImageViewProfileImage;
    @BindView(R.id.listview_profile_group_list) ListView listViewGroupList;
    @BindView(R.id.listview_latest_events_list) ListView listViewLatestEvents;
    @BindView(R.id.scrollview_user_profile) ScrollView scrollViewUserProfile;
    @BindView(R.id.textview_user_profile_points) TextView textViewPoints;
    LayoutAdjustments layoutAdj = new LayoutAdjustments();

    GroupListViewAdapter memberGroupListViewAdapter;
    EventListViewAdapter latestEventListViewAdapter;
    private User loggedInUser;
    private DataManager dataManager = new DataManager();
    StatsQueries statsQueries = new StatsQueries();
    EventQueries eventQueries = new EventQueries();
    UserQueries userQueries = new UserQueries();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        context = getApplicationContext();

        loggedInUser = dataManager.getSavedObjectFromSharedPref(context, "currentlySignedInUser", new TypeToken<User>(){}.getType());
        profileUser = getUserFromLastActivity();
        initGui();
    }

    private void initGui() {
        initToolbar();
        loadCoverPicture();
        loadProfilePicture();
        initListViewGroupList(profileUser.getGroups());
        initListeners();
        initPoints();
        initLatestEvents();
    }

    private void initLatestEvents() {
        eventQueries.getAllEventsForUser(profileUser.getID(), getApplicationContext());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventListEvent(EventListEvent eventListEvent){
        initListViewLatestEvents(eventListEvent.getEventList());
    }

    private void initPoints() {
        statsQueries.getUserScore(profileUser.getID(), getApplicationContext());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScoreEvent(ScoreEvent scoreEvent){
        textViewPoints.setText(Integer.toString(scoreEvent.getScore().getScore()) + " " + getText(R.string.points));
    }

    private void initListeners() {
        listViewGroupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Group group = memberGroupListViewAdapter.getItem(i);
                Gson gson = new Gson();
                String groupStringObject = gson.toJson(group);
                Intent intent = new Intent(context, GroupProfileActivity.class);
                intent.putExtra("group", groupStringObject);
                startActivity(intent);
            }
        });

        listViewGroupList.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                scrollViewUserProfile.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        listViewLatestEvents.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                scrollViewUserProfile.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    private void loadCoverPicture() {

        int imgCode = 1 + (int)(Math.random() * ((6 - 1) + 1));
        String url = "http://pires.top/img/info331/cover-" + imgCode + ".jpg";
        Picasso.with(context)
                .load(url)
                .centerCrop()
                .fit()
                .into(diagonalViewProfile);
    }

    private void loadProfilePicture() {
        Drawable imageResource = ContextCompat.getDrawable(context, R.drawable.avatar2);

        int imgCode = 1;
        String url = "url";
        Picasso.with(context)
                .load(url)
                .centerCrop()
                .fit()
                .noFade()
                .placeholder(imageResource)
                .into(circleImageViewProfileImage);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbarTitle.setText(profileUser.getUsername());
            layoutAdj.setMargins(toolbar, 0, layoutAdj.getStatusBarHeight(getResources()), 0, 0);
        }
    }

    /**
     * Gets the profileUser from the Extra that was put in the intent.
     * Retrieves the GSON string.
     * @return a User object
     */
    public User getUserFromLastActivity() {
        Bundle extras = getIntent().getExtras();
        String userStringObject;
        User user;
        Gson gson = new Gson();

        if (extras != null) {
            userStringObject = extras.getString("profileUser");
            Type type = new TypeToken<User>(){}.getType();

            user = gson.fromJson(userStringObject, type);
            // and get whatever type profileUser account id is
            return user;
        } else {
            user = new User("Not a valid profileUser", "nan", "nan", 0);
            return user;
        }
    }

    private void initListViewGroupList(List<Group> searchedGroups) {
        if (searchedGroups == null) {
            userQueries.getUserGroups(profileUser.getID(), getApplicationContext());
        } else {
            memberGroupListViewAdapter = new GroupListViewAdapter(context, R.layout.list_element_join_group, searchedGroups);
            listViewGroupList.setAdapter(memberGroupListViewAdapter);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGroupListEvent(GroupListEvent groupListEvent) {
        memberGroupListViewAdapter = new GroupListViewAdapter(context, R.layout.list_element_join_group, groupListEvent.getGroupList());
        listViewGroupList.setAdapter(memberGroupListViewAdapter);
    }

    private void initListViewLatestEvents(List<Event> latestEvents) {
        latestEventListViewAdapter = new EventListViewAdapter(context, R.layout.list_element_event, latestEvents);
        listViewLatestEvents.setAdapter(latestEventListViewAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        initLatestEvents();
        initPoints();
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed(){
        super.onBackPressed();
    }
}
