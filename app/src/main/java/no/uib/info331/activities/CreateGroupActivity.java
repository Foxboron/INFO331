package no.uib.info331.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import butterknife.OnClick;
import no.uib.info331.R;
import no.uib.info331.adapters.BeaconListViewAdapter;
import no.uib.info331.adapters.UserListViewAdapter;
import no.uib.info331.models.Beacon;
import no.uib.info331.models.Event;
import no.uib.info331.models.User;
import no.uib.info331.models.messages.UserListEvent;
import no.uib.info331.queries.GroupQueries;
import no.uib.info331.queries.UserQueries;
import no.uib.info331.util.Animations;
import no.uib.info331.util.DialogManager;
import no.uib.info331.util.TextValidator;

/**
 * Activity that lets the profileUser select members from db, give a name to a group and the creating it.
 *
 * @author Edvard P. Bjørgen, Fredrik V. Heimsæter
 *
 */
public class CreateGroupActivity extends AppCompatActivity {

    @BindView(R.id.cardview_create_group) CardView cardViewCreateGroup;
    @BindView(R.id.textview_create_join_group_title) TextView textViewTitle;
    @BindView(R.id.cardview_add_member) CardView cardViewAddMemberToNewGroup;
    @BindView(R.id.edittext_create_group_name) EditText editTextCreateGroupName;

    @BindView(R.id.relativelayout_member_search) RelativeLayout layoutBtnAddMember;
    @BindView(R.id.relativelayout_beacon_search) RelativeLayout layoutBtnAddBeacon;
    @BindView(R.id.listview_added_members) ListView listViewAddedMembersToGroup;
    @BindView(R.id.listview_added_beacons) ListView listViewAddedBeaconsToGroup;

    @BindView(R.id.btn_register_group_button) Button btnRegisterGroup;

    /*******************************Add Members to group Card***************************************/
    @BindView(R.id.edittext_create_search_for_users) EditText editTextSearchForUsers;
    @BindView(R.id.listview_add_member_list) ListView listViewMemberList;

    @BindView(R.id.imagebutton_search_for_user) ImageButton imageBtnSearchForUser;
    /***********************************************************************************************/

    boolean addMemberLayoutBtnClicked;

    int shortAnimTime;
    int mediumAnimTime;
    int longAnimTime;

    List<Beacon> listBeaconsToAddToGroup = new ArrayList<>();
    BeaconListViewAdapter listViewAdapterBeaconsAdded;

    Animations anim = new Animations();
    Context context;

    UserQueries userQueries = new UserQueries();
    UserListViewAdapter listViewAdapterSearcedMembersAdded;

    ArrayList<User> addedUsersToGroup = new ArrayList<>();
    UserListViewAdapter addedMembersUserListAdapter;
    GroupQueries groupQueries = new GroupQueries();
    DialogManager dialogManager = new DialogManager();
    TextValidator textValidator = new TextValidator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        ButterKnife.bind(this);
        context = getApplicationContext();

        initGui();
        initListeners();
        initListenerAddMemberView();
    }

    /**
     * Inits the listeners in the view for searchin and adding members
     */


    private void initGui() {
        shortAnimTime = anim.getShortAnimTime(context);
        mediumAnimTime = anim.getMediumAnimTime(context);
        longAnimTime = anim.getLongAnimTime(context);
        addMemberLayoutBtnClicked = false;

        anim.fadeInView(textViewTitle, 200, shortAnimTime);
        //Sets the members search card to gone and under the screen
        anim.moveViewToTranslationY(cardViewAddMemberToNewGroup, 0 , 0, 5000, false);

        anim.moveViewToTranslationY(cardViewCreateGroup, 0 , shortAnimTime, 5000, false);
        anim.fadeInView(cardViewCreateGroup, 0, shortAnimTime);
        anim.moveViewToTranslationY(cardViewCreateGroup, 100 , shortAnimTime, 0, false);

        addedMembersUserListAdapter = new UserListViewAdapter(context, R.layout.list_element_search_members, addedUsersToGroup);
        listViewAddedMembersToGroup.setAdapter(addedMembersUserListAdapter);
    }

    /**
     * Inits the list view for showing searched members
     * @param searchedUsers
     */
    private void initListViewMemberList(List<User> searchedUsers) {
        listViewAdapterSearcedMembersAdded = new UserListViewAdapter(context, R.layout.list_element_search_members, searchedUsers);
        listViewMemberList.setAdapter(listViewAdapterSearcedMembersAdded);

    }
    /**
     * Inits the list view for showing searched beacons
     * @param addedBeacons
     */
    private void initListViewAddedBeaconList(List<Beacon> addedBeacons) {
        listViewAdapterBeaconsAdded = new BeaconListViewAdapter(context, R.layout.list_element_beacon, addedBeacons);
        listViewAddedBeaconsToGroup.setAdapter(listViewAdapterBeaconsAdded);

    }
    private
    void initListeners() {
            /*
            *For showing the view for searching and adding members
            */
        layoutBtnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMemberLayoutBtnClicked = true;
                anim.fadeInView(cardViewAddMemberToNewGroup, 0, shortAnimTime);
                anim.moveViewToTranslationY(cardViewAddMemberToNewGroup,0 , shortAnimTime, 0, false);
            }
        });
            /*
             *Start the activity for searching for beacons
             */
        layoutBtnAddBeacon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateGroupActivity.this, AddBeaconToGroupActivity.class);
                startActivityForResult(i, 1);

            }
        });

        listViewAddedMembersToGroup.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = addedMembersUserListAdapter.getItem(i);
                addedMembersUserListAdapter.remove(user);
                addedMembersUserListAdapter.notifyDataSetChanged();
                Toast.makeText(context, "TEMPORARY WAY OF REMOVING USERS! User " + user.getUsername() + " removed", Toast.LENGTH_LONG).show();
                return false;
            }
        });


    }

    private void initListenerAddMemberView() {
        imageBtnSearchForUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = String.valueOf(editTextSearchForUsers.getText());
                userQueries.getUsersByStringFromDb(context, query);
            }
        });

        listViewMemberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                User user = listViewAdapterSearcedMembersAdded.getItem(position);
                dialogManager.createUserProfileDialogForCreateGroup(user, CreateGroupActivity.this, getResources(), addedMembersUserListAdapter);

            }

        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserListEvent(UserListEvent userListEvent){
        initListViewMemberList(userListEvent.getUserList());
    }



    /**
     * ButterKnife annotation listener. Registers the group with name and the members that have been
     * inputted
     */
    @OnClick(R.id.btn_register_group_button)
    public void registerGroup(){
        View focusView = null;
        Boolean wait = false;
        String groupName = editTextCreateGroupName.getText().toString();
        if(!textValidator.completeCheck(groupName)){
            focusView = editTextCreateGroupName;
            focusView.requestFocus();
            wait = true;
        }
        if(!wait){
            if(!listBeaconsToAddToGroup.isEmpty()){
                groupQueries.registerGroupQuery(groupName, addedUsersToGroup, listBeaconsToAddToGroup.get(0), context);
                userQueries.refreshUserQuery(context, getResources());
            }else {
                Intent i = new Intent(CreateGroupActivity.this, AddBeaconToGroupActivity.class);
                startActivityForResult(i, 1);
            }
        }
    }

    /**
     * ButterKnife annotation listener. Closes the search member view, plays animation and sets the
     * search input text field's focus to false
     */
    @OnClick(R.id.accept_searched_members_button)
    public void addMemberCardDisappear(){
        if(addMemberLayoutBtnClicked){
            addMemberLayoutBtnClicked = false;
            anim.fadeOutView(cardViewAddMemberToNewGroup, 0, longAnimTime);
            anim.moveViewToTranslationY(cardViewAddMemberToNewGroup, 100, shortAnimTime, cardViewAddMemberToNewGroup.getHeight()+2000, false);
            editTextSearchForUsers.clearFocus();
        }
    }

    /**
     * Receives the beacons to add from AddBeaconToGroupActivity
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                if (data.getStringExtra("addedBeacons") != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<Beacon>>(){}.getType();
                    String strEditText = data.getStringExtra("addedBeacons");
                    listBeaconsToAddToGroup = gson.fromJson(strEditText, type);
                    initListViewAddedBeaconList(listBeaconsToAddToGroup);
                } else {
                    Toast.makeText(context, "No beacons was added!", Toast.LENGTH_LONG).show();

                }
            }
        }
    }



    /**
     * Detects if the serach member view is open, then close it, if not; the super-method is executed.
     * also overrides Android's standard activity transition animation with a fadein/fadout anim.
     */
    public void onBackPressed() {

        if(addMemberLayoutBtnClicked){
            addMemberCardDisappear();
        } else if(!addMemberLayoutBtnClicked) {
            super.onBackPressed();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }
}
