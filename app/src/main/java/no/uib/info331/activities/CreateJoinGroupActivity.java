package no.uib.info331.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import no.uib.info331.R;
import no.uib.info331.models.User;
import no.uib.info331.util.Animations;
import no.uib.info331.util.ApiClient;
import no.uib.info331.util.ApiInterface;
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

    @BindView(R.id.member_search) RelativeLayout layoutBtnAddMember;

    @BindView(R.id.number_of_members_added_text) TextView textViewNoOfMembersAdded;

    @BindView(R.id.search_for_users) EditText editTextSearchForUsers;

    @BindView(R.id.add_member_list) ListView listViewMemberList;


    boolean joinGroupBtnClicked;
    boolean createGroupBtnClicked;
    boolean addMemberLayoutBtnClicked;

    int shortAnimTime;
    int mediumAnimTime;
    int longAnimTime;

    Animations anim = new Animations();
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join_group);
        ButterKnife.bind(this);
        context = getApplicationContext();

        //getAllUsers();
        initGui();
        btnClickListener();

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

        initListViewMemberList();


    }

    private void initListViewMemberList() {



    }

    private void btnClickListener() {


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