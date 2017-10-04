package no.uib.info331.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.Chip;
import com.pchmn.materialchips.model.ChipInterface;

import java.util.ArrayList;
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
import no.uib.info331.util.Animations;

/**
 * Micromanaging af here
 *
 * @author Edvard P. Bjørgen
 *
 */
public class CreateJoinGroupActivity extends AppCompatActivity {

    @BindView(R.id.text_create_join_group_title) TextView textViewTitle;

    private float topYOfParentLayout;

    //ButterKnife gui

    @BindView(R.id.choose_action_card) CardView chooseActionCard;
    @BindView(R.id.join_group_card) CardView joinGroupCard;

    @BindView(R.id.create_group_card) CardView createGroupCard;
    @BindView(R.id.add_member_card) CardView addMemberToNewGroupCard;
    @BindView(R.id.show_join_group) Button btnJoinGroupShow;

    @BindView(R.id.show_create_group) Button btnCreateGroupShow;
    @BindView(R.id.join_search_group) EditText editTextSearchJoinGroup;

    @BindView(R.id.chips_input) ChipsInput chipsInput;
    @BindView(R.id.member_search) RelativeLayout addMemberLayoutBtn;

    @BindView(R.id.number_of_members_added_text) TextView noOfMembersAddedTextView;


    boolean joinGroupBtnClicked;
    boolean createGroupBtnClicked;
    boolean addMemberLayoutBtnClicked;

    int shortAnimTime;
    int mediumAnimTime;
    int longAnimTime;

    Animations anim = new Animations();
    private ArrayList<Chip> contactList;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join_group);
        ButterKnife.bind(this);
        context = getApplicationContext();

        testLoadAllUsers();

        initGui();
        btnClickListener();

    }

    private void testLoadAllUsers() {
        private void loadSize() {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

            Call<List<User>> loadSizeCall = apiService.login(username, password);;
            loadSizeCall.enqueue(new Callback<List<Size>>() {
                @Override
                public void onResponse(Call<List<Size>> call, Response<List<Size>> response) {
                    for(Size size: response.body()) {
                        System.out.println(size.toString());
                    }
                }

                @Override
                public void onFailure(Call<List<Size>> call, Throwable t) {
                    System.out.println(t.getMessage());
                }
            });
        }
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
        anim.moveViewToTranslationY(addMemberToNewGroupCard,0 , 0, 2000, false);

        contactList = new ArrayList<>();
        contactList.add(new Chip("Kjell Gunnlaug", "MEVI 5.året"));
        contactList.add(new Chip("Gunnar Laugesen", "Infovit 4.året"));
        contactList.add(new Chip("Morten Fjell Potetskrell", "Journalistikk 4.året"));
        contactList.add(new Chip("Fat Finger Freddy", "Infovit 4.året"));
        contactList.add(new Chip("Tor Raymond Carlsen", "Infovit 4.året"));
        contactList.add(new Chip("Morten Mr.Go", "Infovit 4.året"));
        contactList.add(new Chip("Perikles DuPlantier", "Infovit 4.året"));
        contactList.add(new Chip("Eddie the Eagle", "Infovit 4.året"));

        chipsInput.setFilterableList(contactList);


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {


            chipsInput.addChipsListener(new ChipsInput.ChipsListener() {
                @Override
                public void onChipAdded(ChipInterface chip, int newSize) {
                    // chip added
                    // newSize is the size of the updated selected chip list
                    noOfMembersAddedTextView.setText("You have added " + newSize + " members to this group!");
                }

                @Override
                public void onChipRemoved(ChipInterface chip, int newSize) {
                    // chip removed
                    // newSize is the size of the updated selected chip list
                    noOfMembersAddedTextView.setText("You have added " + newSize + " members to this group!");

                }

                @Override
                public void onTextChanged(CharSequence text) {
                }
            });


        }
    }



    private void btnClickListener() {


        btnJoinGroupShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                joinGroupBtnClicked = true;

                anim.fadeOutView(chooseActionCard, 0, shortAnimTime);
                anim.moveViewToTranslationY(chooseActionCard,0 , shortAnimTime, chooseActionCard.getHeight(), true);

            }
        });


        btnCreateGroupShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createGroupBtnClicked = true;

                anim.fadeOutView(chooseActionCard, 0, longAnimTime);
                anim.moveViewToTranslationY(chooseActionCard, 0 , shortAnimTime, chooseActionCard.getHeight(), true);

                anim.fadeOutView(joinGroupCard, 0, longAnimTime);
                anim.moveViewToTranslationY(joinGroupCard, 100, shortAnimTime, joinGroupCard.getHeight(), true);

            }
        });

        addMemberLayoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMemberLayoutBtnClicked = true;



                anim.fadeInView(addMemberToNewGroupCard, 0, shortAnimTime);
                anim.moveViewToTranslationY(addMemberToNewGroupCard,0 , shortAnimTime, 0, false);
            }
        });


    }
    //ButterKnife magic? Yes it is
    @OnClick(R.id.accept_searched_members_button)
    public void addMemberCardDisappear(){
        if(addMemberLayoutBtnClicked){
            addMemberLayoutBtnClicked = false;
            anim.fadeOutView(addMemberToNewGroupCard, 0, longAnimTime);
            anim.moveViewToTranslationY(addMemberToNewGroupCard, 100, shortAnimTime, addMemberToNewGroupCard.getHeight(), false);
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
            anim.fadeInView(chooseActionCard, 0, shortAnimTime);
            anim.moveViewToTranslationY(chooseActionCard, 50 , shortAnimTime, 0, false);

        }

        else if(createGroupBtnClicked) {
            joinGroupBtnClicked = false;
            createGroupBtnClicked = false;

            //Animates the card for choosing what to do
            anim.fadeInView(joinGroupCard, 50, shortAnimTime);
            anim.moveViewToTranslationY(joinGroupCard, 50 , shortAnimTime, 0, false);

            anim.fadeInView(chooseActionCard, 0, shortAnimTime);
            anim.moveViewToTranslationY(chooseActionCard, 100 , shortAnimTime, 0, false);
        } else if( !createGroupBtnClicked && !joinGroupBtnClicked && !addMemberLayoutBtnClicked){

            super.onBackPressed();
        }




    }




}
