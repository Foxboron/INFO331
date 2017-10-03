package no.uib.info331.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.Chip;
import com.pchmn.materialchips.model.ChipInterface;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import no.uib.info331.R;
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
    @BindView(R.id.add_beacon_card) CardView addBeaconToNewGroupCard;
    @BindView(R.id.show_join_group) Button btnJoinGroupShow;

    @BindView(R.id.show_create_group) Button btnCreateGroupShow;
    @BindView(R.id.join_search_group) EditText editTextSearchJoinGroup;

    @BindView(R.id.chips_input) ChipsInput chipsInput;
    @BindView(R.id.beacon_search) RelativeLayout addBeaconLayoutBtn;

    @BindView(R.id.number_of_beacons_added_text) TextView noOfBeaconsAddedTextView;


    boolean joinGroupBtnClicked;
    boolean createGroupBtnClicked;
    boolean addBeaconLayoutBtnClicked;

    int shortAnimTime;
    int mediumAnimTime;
    int longAnimTime;

    Animations anim = new Animations();
    private ArrayList<Chip> contactList;
    Context context;

    int noOfBeaconsAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join_group);
        ButterKnife.bind(this);
        context = getApplicationContext();
        initGui();
        btnClickListener();

    }

    private void initGui() {
        shortAnimTime = anim.getShortAnimTime(context);
        mediumAnimTime = anim.getMediumAnimTime(context);
        longAnimTime = anim.getLongAnimTime(context);

        joinGroupBtnClicked = false;
        createGroupBtnClicked = false;
        addBeaconLayoutBtnClicked = false;

        anim.fadeInView(textViewTitle, 200, shortAnimTime);

        //Sets the beacon search card to gone and under the screen
        anim.moveViewToTranslationY(addBeaconToNewGroupCard,0 , 0, 2000, false);
        Drawable btIcon =  context.getDrawable(R.drawable.ic_bt_beacon_blue);

        contactList = new ArrayList<>();
        contactList.add(new Chip(btIcon,"642-gangstas", "Beacon for: " + "rom 642/SV-Bygget, UiB"));
        contactList.add(new Chip(btIcon, "Tacolause", "Beacon for: " + "rom 542/SV-Bygget, UiB"));
        contactList.add(new Chip(btIcon, "Hjelmeland Drikkelag", "Beacon for: " + "rom 639/SV-Bygget, UiB"));
        contactList.add(new Chip(btIcon, "Nørdså", "Beacon for: " + "rom 638/SV-Bygget Uib"));
        contactList.add(new Chip(btIcon, "Hemså", "Beacon for: " + "rom 123/SV-Bygget Uib"));
        contactList.add(new Chip(btIcon, "De svettaste", "Beacon for: " + "rom 923/SV-Bygget Uib"));
        contactList.add(new Chip(btIcon, "Mafioso", "Beacon for: " + "rom 123/Stein Rokkans hus, Uib"));
        contactList.add(new Chip(btIcon, "GeoGale", "Beacon for: " + "rom 1030/Stein Rokkans hus, Uib"));
        contactList.add(new Chip(btIcon, "Luringane", "Beacon for: " + "rom 1040/Stein Rokkans hus, Uib"));

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
                    noOfBeaconsAddedTextView.setText("You have added " + newSize + " beacons!");
                }

                @Override
                public void onChipRemoved(ChipInterface chip, int newSize) {
                    // chip removed
                    // newSize is the size of the updated selected chip list
                    noOfBeaconsAddedTextView.setText("You have added " + newSize + " beacons!");

                }

                @Override
                public void onTextChanged(CharSequence text) {
                    // text changed
//                    anim.moveViewToTranslationY(chipsInput,0, 200, Math.round(topYOfParentLayout), false);
//                    anim.fadeBackgroundFromColorToColor(chipsInput, 200, ContextCompat.getColor(getApplicationContext(), R.color.colorTransp), Color.WHITE);
//
//                    chipsInput.setBackgroundColor(Color.WHITE);
//                    if(text.length() == 0){
//                        anim.moveViewToTranslationY(chipsInput,0, 200, Math.round(0), false);
//                        anim.fadeBackgroundFromColorToColor(chipsInput, 200, Color.WHITE, ContextCompat.getColor(getApplicationContext(), R.color.colorTransp));
//                        chipsInput.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTransp));
//
//                    }
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

        addBeaconLayoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBeaconLayoutBtnClicked = true;

                anim.fadeInView(addBeaconToNewGroupCard, 0, shortAnimTime);
                anim.moveViewToTranslationY(addBeaconToNewGroupCard,0 , shortAnimTime, 0, false);
            }
        });


    }
    //ButterKnife magic? Yes it is
    @OnClick(R.id.accept_searched_beacons_button)
    public void addBeaconCardDisappear(){
        if(addBeaconLayoutBtnClicked){
            addBeaconLayoutBtnClicked = false;
            anim.fadeOutView(addBeaconToNewGroupCard, 0, longAnimTime);
            anim.moveViewToTranslationY(addBeaconToNewGroupCard, 100, shortAnimTime, addBeaconToNewGroupCard.getHeight(), false);
            return;
        }
    }

    public void onBackPressed() {


        addBeaconCardDisappear();

         if(joinGroupBtnClicked) {
            joinGroupBtnClicked = false;

            //Animates the card for choosing what to do
            anim.fadeInView(chooseActionCard, 0, shortAnimTime);
            anim.moveViewToTranslationY(chooseActionCard, 50 , shortAnimTime, 0, false);

        }

        else if(createGroupBtnClicked && !addBeaconLayoutBtnClicked) {
            joinGroupBtnClicked = false;
            createGroupBtnClicked = false;

            //Animates the card for choosing what to do
            anim.fadeInView(joinGroupCard, 50, shortAnimTime);
            anim.moveViewToTranslationY(joinGroupCard, 50 , shortAnimTime, 0, false);

            anim.fadeInView(chooseActionCard, 0, shortAnimTime);
            anim.moveViewToTranslationY(chooseActionCard, 100 , shortAnimTime, 0, false);
        } else if( !createGroupBtnClicked && !joinGroupBtnClicked && !addBeaconLayoutBtnClicked){

            super.onBackPressed();
        }




    }




}
