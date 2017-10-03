package no.uib.info331.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.Chip;
import com.pchmn.materialchips.model.ChipInterface;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    @BindView(R.id.show_join_group) Button btnJoinGroupShow;

    @BindView(R.id.show_create_group) Button btnCreateGroupShow;
    @BindView(R.id.join_search_group) EditText editTextSearchJoinGroup;

    @BindView(R.id.chips_input) ChipsInput chipsInput;
    @BindView(R.id.add_group_name) LinearLayout addGroupNameLayout;


    boolean joinGroupBtnClicked;
    boolean createGroupBtnClicked;

    int shortAnimTime;
    int mediumAnimTime;

    Animations anim = new Animations();
    private ArrayList<Chip> contactList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join_group);
        ButterKnife.bind(this);
        initGui();
        btnClickListener();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            topYOfParentLayout = 0 - (chipsInput.getY() + createGroupCard.getY());

            chipsInput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("TESTETETE");

                }
            });

            chipsInput.addChipsListener(new ChipsInput.ChipsListener() {
                @Override
                public void onChipAdded(ChipInterface chip, int newSize) {
                    // chip added
                    // newSize is the size of the updated selected chip list
                }

                @Override
                public void onChipRemoved(ChipInterface chip, int newSize) {
                    // chip removed
                    // newSize is the size of the updated selected chip list
                }

                @Override
                public void onTextChanged(CharSequence text) {
                    // text changed
                    anim.moveViewToTranslationY(chipsInput,0, 200, Math.round(topYOfParentLayout), false);
                    anim.fadeBackgroundFromColorToColor(chipsInput, 200, ContextCompat.getColor(getApplicationContext(), R.color.colorTransp), Color.WHITE);
                    chipsInput.setBackgroundColor(Color.WHITE);
                    if(text.length() == 0){
                        anim.moveViewToTranslationY(chipsInput,0, 200, Math.round(0), false);
                        anim.fadeBackgroundFromColorToColor(chipsInput, 200, Color.WHITE, ContextCompat.getColor(getApplicationContext(), R.color.colorTransp));
                        chipsInput.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTransp));

                    }
                }
            });


        }
    }


    private void initGui() {
        shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mediumAnimTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);

        System.out.println("initgui"+ chipsInput.getY());



        joinGroupBtnClicked = false;
        createGroupBtnClicked = false;

        anim.fadeInView(textViewTitle, 200, shortAnimTime);

        contactList = new ArrayList<>();
        contactList.add(new Chip("Mega", "Magnus"));
        contactList.add(new Chip("Bjarne", "Betjent"));
        contactList.add(new Chip("Taco", "Tveit"));
        contactList.add(new Chip("Test", "testo"));

        chipsInput.setFilterableList(contactList);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


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

                anim.moveViewToTranslationY(chooseActionCard, 100 , mediumAnimTime, chooseActionCard.getHeight(), true);
                anim.fadeOutView(chooseActionCard, 0, shortAnimTime);

                anim.moveViewToTranslationY(joinGroupCard, 200, mediumAnimTime, joinGroupCard.getHeight(), true);
                anim.fadeOutView(joinGroupCard, 0, shortAnimTime);

            }
        });


    }

    public void onBackPressed() {


        if(joinGroupBtnClicked) {
            joinGroupBtnClicked = false;

            //Animates the card for choosing what to do
            anim.fadeInView(chooseActionCard, 0, shortAnimTime);
            anim.moveViewToTranslationY(chooseActionCard, 50 , shortAnimTime, 0, false);

        } else if(createGroupBtnClicked) {
            joinGroupBtnClicked = false;
            createGroupBtnClicked = false;

            //Animates the card for choosing what to do
            anim.fadeInView(joinGroupCard, 0, shortAnimTime);
            anim.moveViewToTranslationY(joinGroupCard, 100 , shortAnimTime, 0, false);

            anim.fadeInView(chooseActionCard, 0, shortAnimTime);
            anim.moveViewToTranslationY(chooseActionCard, 200 , shortAnimTime, 0, false);
        }  else{

            super.onBackPressed();
        }


    }




}
