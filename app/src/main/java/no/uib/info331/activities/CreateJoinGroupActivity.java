package no.uib.info331.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import no.uib.info331.R;
import no.uib.info331.adapters.UserListViewAdapter;
import no.uib.info331.models.User;
import no.uib.info331.queries.UserQueries;
import no.uib.info331.util.Animations;
import no.uib.info331.util.DataManager;

/**
 * Micromanaging af here
 *
 * @author Edvard P. Bjørgen
 *
 */
public class CreateJoinGroupActivity extends AppCompatActivity {

    @BindView(R.id.text_create_join_group_title) TextView textViewTitle;


    //ButterKnife gui

    @BindView(R.id.choose_action_card) CardView cardChooseAction;
    @BindView(R.id.join_group_card) CardView cardJoinGroup;

    @BindView(R.id.create_group_card) CardView cardCreateGroup;
    @BindView(R.id.add_member_card) CardView cardAddMemberToNewGroup;
    @BindView(R.id.show_join_group) Button btnJoinGroupShow;

    @BindView(R.id.show_create_group) Button btnCreateGroupShow;
    @BindView(R.id.join_search_group) EditText editTextSearchJoinGroup;

    @BindView(R.id.member_search) RelativeLayout layoutBtnAddMember;

    @BindView(R.id.number_of_members_added_text) TextView textViewNoOfMembersAdded;

    @BindView(R.id.search_for_users) EditText editTextSearchForUsers;
    @BindView(R.id.listview_add_member_list) ListView listViewMemberList;
    @BindView(R.id.imagebutton_search_for_user) ImageButton imageBtnSearchForUser;

    @BindView(R.id.imageview_add_member_logo) ImageView imageViewAddMemberIcon;
    @BindView(R.id.textview_add_member_underlogo) TextView textViewAddMemberUnderLogo;



    boolean joinGroupBtnClicked;
    boolean createGroupBtnClicked;
    boolean addMemberLayoutBtnClicked;

    int shortAnimTime;
    int mediumAnimTime;
    int longAnimTime;

    Animations anim = new Animations();
    Context context;
    DataManager dataManager = new DataManager();

    UserQueries userQueries = new UserQueries();
    UserListViewAdapter userListViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join_group);
        ButterKnife.bind(this);
        context = getApplicationContext();

        //getAllUsers();
        initGui();
        listeners();

    }


    private void initGui() {
        shortAnimTime = anim.getShortAnimTime(context);
        mediumAnimTime = anim.getMediumAnimTime(context);
        longAnimTime = anim.getLongAnimTime(context);

        joinGroupBtnClicked = false;
        createGroupBtnClicked = false;
        addMemberLayoutBtnClicked = false;

        anim.fadeInView(textViewTitle, 200, shortAnimTime);

        //Sets the members search card to gone and under the screen
        anim.moveViewToTranslationY(cardAddMemberToNewGroup,0 , 0, 2000, false);

        List<User> allUsers = userQueries.getUsersByStringFromDb(context, "edd", "edd", "edd");

    }

    private void initListViewMemberList(List<User> searchedUsers) {
        userListViewAdapter = new UserListViewAdapter(context, R.layout.list_element_search_members, searchedUsers);
        listViewMemberList.setAdapter(userListViewAdapter);


    }

    private void listeners() {


        btnJoinGroupShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinGroupBtnClicked = true;

                anim.fadeOutView(cardChooseAction, 0, shortAnimTime);
                anim.moveViewToTranslationY(cardChooseAction,0 , shortAnimTime, cardChooseAction.getHeight(), true);

            }
        });


        btnCreateGroupShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createGroupBtnClicked = true;

                anim.fadeOutView(cardChooseAction, 0, longAnimTime);
                anim.moveViewToTranslationY(cardChooseAction, 0 , shortAnimTime, cardChooseAction.getHeight(), true);

                anim.fadeOutView(cardJoinGroup, 0, longAnimTime);
                anim.moveViewToTranslationY(cardJoinGroup, 100, shortAnimTime, cardJoinGroup.getHeight(), true);

            }
        });

        layoutBtnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMemberLayoutBtnClicked = true;
                anim.fadeInView(cardAddMemberToNewGroup, 0, shortAnimTime);
                anim.moveViewToTranslationY(cardAddMemberToNewGroup,0 , shortAnimTime, 0, false);
            }
        });

        editTextSearchForUsers.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //TODO: insert code
            }
        });

        imageBtnSearchForUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = String.valueOf(editTextSearchForUsers.getText());
                List<User> userSearch = userQueries.getUsersByStringFromDb(context, query, "edd", "edd");
                initListViewMemberList(userSearch);

            }
        });

        listViewMemberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                User user = userListViewAdapter.getItem(position);
                createUserProfileDialog(user);
            }

        });
    }
    //ButterKnife magic? Yes it is
    @OnClick(R.id.accept_searched_members_button)
    public void addMemberCardDisappear(){
        if(addMemberLayoutBtnClicked){
            addMemberLayoutBtnClicked = false;
            anim.fadeOutView(cardAddMemberToNewGroup, 0, longAnimTime);
            anim.moveViewToTranslationY(cardAddMemberToNewGroup, 100, shortAnimTime, cardAddMemberToNewGroup.getHeight(), false);
        }
    }

    public void createUserProfileDialog(User user){
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(R.layout.dialog_add_member_profile)
                .create();
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void onBackPressed() {

        if(addMemberLayoutBtnClicked){
            addMemberCardDisappear();
            return;
        }

        if(joinGroupBtnClicked) {
            joinGroupBtnClicked = false;

            //Animates the card for choosing what to do
            anim.fadeInView(cardChooseAction, 0, shortAnimTime);
            anim.moveViewToTranslationY(cardChooseAction, 50 , shortAnimTime, 0, false);

        }

        else if(createGroupBtnClicked) {
            joinGroupBtnClicked = false;
            createGroupBtnClicked = false;

            //Animates the card for choosing what to do
            anim.fadeInView(cardJoinGroup, 50, shortAnimTime);
            anim.moveViewToTranslationY(cardJoinGroup, 50 , shortAnimTime, 0, false);

            anim.fadeInView(cardChooseAction, 0, shortAnimTime);
            anim.moveViewToTranslationY(cardChooseAction, 100 , shortAnimTime, 0, false);
        } else if( !createGroupBtnClicked && !joinGroupBtnClicked && !addMemberLayoutBtnClicked){

            super.onBackPressed();
        }




    }




}