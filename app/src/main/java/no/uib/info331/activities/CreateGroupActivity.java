package no.uib.info331.activities;

import android.content.Context;
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


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import no.uib.info331.R;
import no.uib.info331.adapters.UserListViewAdapter;
import no.uib.info331.models.User;
import no.uib.info331.queries.GroupQueries;
import no.uib.info331.queries.UserQueries;
import no.uib.info331.util.Animations;
import no.uib.info331.util.DialogManager;
/**
 * Activity that lets the user select members from db, give a name to a group and the creating it.
 *
 * @author Edvard P. Bjørgen, Fredrik V. Heimsæter
 *
 */
public class CreateGroupActivity extends AppCompatActivity {

    //ButterKnife gui
    @BindView(R.id.textview_create_join_group_title) TextView textViewTitle;

    @BindView(R.id.cardview_create_group) CardView cardViewCreateGroup;
    @BindView(R.id.cardview_add_member) CardView cardViewAddMemberToNewGroup;

    @BindView(R.id.edittext_create_group_name) EditText editTextCreateGroupName;
    @BindView(R.id.relativelayout_member_search) RelativeLayout layoutBtnAddMember;
    @BindView(R.id.listview_added_members) ListView listViewAddedMembersToGroup;

    @BindView(R.id.edittext_create_search_for_users) EditText editTextSearchForUsers;
    @BindView(R.id.listview_add_member_list) ListView listViewMemberList;
    @BindView(R.id.imagebutton_search_for_user) ImageButton imageBtnSearchForUser;

    @BindView(R.id.btn_register_group_button) Button btnRegisterGroup;

    boolean addMemberLayoutBtnClicked;

    int shortAnimTime;
    int mediumAnimTime;
    int longAnimTime;

    Animations anim = new Animations();
    Context context;

    UserQueries userQueries = new UserQueries();
    UserListViewAdapter searchedMembersUserListViewAdapter;

    ArrayList<User> addedUsersToGroup = new ArrayList<>();
    UserListViewAdapter addedMembersUserListAdapter;
    GroupQueries groupQueries = new GroupQueries();
    DialogManager dialogManager = new DialogManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        ButterKnife.bind(this);
        context = getApplicationContext();

        //getAllUsers();
        initGui();
        initListeners();
    }

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
        searchedMembersUserListViewAdapter = new UserListViewAdapter(context, R.layout.list_element_search_members, searchedUsers);
        listViewMemberList.setAdapter(searchedMembersUserListViewAdapter);

    }
    private
    void initListeners() {

        layoutBtnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMemberLayoutBtnClicked = true;
                anim.fadeInView(cardViewAddMemberToNewGroup, 0, shortAnimTime);
                anim.moveViewToTranslationY(cardViewAddMemberToNewGroup,0 , shortAnimTime, 0, false);
            }
        });

        imageBtnSearchForUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = String.valueOf(editTextSearchForUsers.getText());
                List<User> userSearch = userQueries.getUsersByStringFromDb(context, query);
                try {
                    initListViewMemberList(userSearch);
                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(context, getResources().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();

                }
            }
        });


        listViewMemberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                User user = searchedMembersUserListViewAdapter.getItem(position);
                dialogManager.createUserProfileDialogForCreateGroup(user, CreateGroupActivity.this, getResources(), addedMembersUserListAdapter);

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

    /**
     * ButterKnife annotation listener. Registers the group with name and the members that have been
     * inputted
     */
    @OnClick(R.id.btn_register_group_button)
    public void registerGroup(){
        String groupName = editTextCreateGroupName.getText().toString();
        groupQueries.registerGroupQuery(groupName, addedUsersToGroup, context);
        userQueries.refreshUserQuery(context, getResources());
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


}
