package no.uib.info331.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import no.uib.info331.R;
import no.uib.info331.adapters.UserListViewAdapter;
import no.uib.info331.models.Group;
import no.uib.info331.models.User;
import no.uib.info331.queries.UserQueries;
import no.uib.info331.util.Animations;
import no.uib.info331.util.ApiClient;
import no.uib.info331.util.ApiInterface;
import no.uib.info331.util.DataManager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    @BindView(R.id.edittext_create_group_name) EditText editTextCreateGroupName;
    @BindView(R.id.relativelayout_member_search) RelativeLayout layoutBtnAddMember;
    @BindView(R.id.listview_added_members) ListView listViewAddedMembersToGroup;


    @BindView(R.id.search_for_users) EditText editTextSearchForUsers;
    @BindView(R.id.listview_add_member_list) ListView listViewMemberList;
    @BindView(R.id.imagebutton_search_for_user) ImageButton imageBtnSearchForUser;

    @BindView(R.id.imageview_add_member_logo) ImageView imageViewAddMemberIcon;
    @BindView(R.id.textview_add_member_underlogo) TextView textViewAddMemberUnderLogo;

    @BindView(R.id.button_register_group_button) Button buttonRegisterGroup;





    boolean joinGroupBtnClicked;
    boolean createGroupBtnClicked;
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
        anim.moveViewToTranslationY(cardAddMemberToNewGroup, 0 , 0, 5000, false);

        //addedUsersToGroup.add(new User("Testie", "", "", 345));

        addedMembersUserListAdapter = new UserListViewAdapter(context, R.layout.list_element_search_members, addedUsersToGroup);
        listViewAddedMembersToGroup.setAdapter(addedMembersUserListAdapter);



    }

    private void initListViewMemberList(List<User> searchedUsers) {
        searchedMembersUserListViewAdapter = new UserListViewAdapter(context, R.layout.list_element_search_members, searchedUsers);
        listViewMemberList.setAdapter(searchedMembersUserListViewAdapter);

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
                createUserProfileDialog(user);
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


        /*Only for testing user profile*/

        listViewAddedMembersToGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = addedMembersUserListAdapter.getItem(i);
                Gson gson = new Gson();
                String userStringObject = gson.toJson(user);
                Intent intent = new Intent(context, UserProfileActivity.class);
                intent.putExtra("user", userStringObject);
                startActivity(intent);


            }
        });



    }

    @OnClick(R.id.button_register_group_button)
    public void registerGroup(){
        String groupName = editTextCreateGroupName.getText().toString();
        DataManager dataManager = new DataManager();
        final User currentUser = dataManager.getSavedObjectFromSharedPref(getApplicationContext(), "currentlySignedInUser", new TypeToken<User>(){}.getType());
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
                    for (User member : addedUsersToGroup) {
                        Call<ResponseBody> addUserCall = apiService.addUserToGroup(basic, registeredGroup.getID(), member.getID());
                        addUserCall.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                System.out.println(response);
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
                }

            }

            @Override
            public void onFailure(Call<Group> call, Throwable t) {
                error[0] = true;
                t.printStackTrace();

            }
        });
        if(!error[0]){
            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(context, getResources().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
        }

    }

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


        TextView textViewDialogUserName = (TextView) dialog.findViewById(R.id.dialog_textview_add_member_username);
        textViewDialogUserName.setText(user.getUsername());
        fabAddUserToGroup(context, user, dialog);

    }
    private void fabAddUserToGroup(final Context CONTEXT, final User USER, final AlertDialog DIALOG) {
        FloatingActionButton fab = (FloatingActionButton) DIALOG.findViewById(R.id.fabCal);
        if(fab != null)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar snack = Snackbar.make(view, getResources().getString(R.string.member_added), Snackbar.LENGTH_LONG)
                            .setAction("Action", null);
                    View sbView = snack.getView();

                    //Set custom typeface
                    TextView tv = (TextView) (sbView).findViewById(android.support.design.R.id.snackbar_text);
                    Typeface font = Typeface.createFromAsset(getBaseContext().getAssets(), "fonts/roboto_thin.ttf");
                    tv.setTypeface(font);
                    tv.setTextSize(16);
                    snack.show();

                    //Countdown to when to start the calendar intent, this for showing user the snackbar of whats happening next
                    new CountDownTimer(800, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {}
                        public void onFinish() {
                            addedMembersUserListAdapter.add(USER);
                            addedMembersUserListAdapter.notifyDataSetChanged();
                            DIALOG.cancel();
                        }
                    }.start();
                }
            });
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