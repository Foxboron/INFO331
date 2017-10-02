package no.uib.info331.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.Chip;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import no.uib.info331.R;

/**
 * Micromanaging af here
 *
 * @author Edvard P. Bjørgen
 *
 */
public class CreateJoinGroupActivity extends AppCompatActivity {

    @BindView(R.id.text_create_join_group_title) TextView textViewTitle;

    @BindView(R.id.choose_action_card) CardView chooseActionCard;


    @BindView(R.id.join_group_card) CardView joinGroupCard;
    @BindView(R.id.create_group_card) CardView createGroupCard;

    @BindView(R.id.show_join_group) Button btnJoinGroupShow;
    @BindView(R.id.show_create_group) Button btnCreateGroupShow;

    @BindView(R.id.join_search_group) EditText editTextSearchJoinGroup;
    @BindView(R.id.chips_input) ChipsInput chipsInput;

    boolean joinGroupBtnClicked;

    boolean createGroupBtnClicked;
    int shortAnimTime;

    int mediumAnimTime;

    //Butterknfe gui

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join_group);
        ButterKnife.bind(this);
        initGui();
        btnClickListener();
    }

    private void initGui() {
        shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mediumAnimTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);

        joinGroupBtnClicked = false;
        createGroupBtnClicked = false;

        fadeInView(textViewTitle, 200, shortAnimTime);

        List<Chip> contactList = new ArrayList<>();
        contactList.add(new Chip("Mega", "Magnus"));
        contactList.add(new Chip("Bjarne", "Betjent"));
        contactList.add(new Chip("Taco", "Tveit"));
        contactList.add(new Chip("Test", "testo"));

        chipsInput.setFilterableList(contactList);

    }

    private void fadeInView(View view, int startDelay, int duration) {
        view.setAlpha(0);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha",  0, 1f);
        fadeIn.setStartDelay(startDelay);
        fadeIn.setDuration(duration);
        fadeIn.start();
    }
    private void fadeOutView(View view, int startDelay, int duration) {
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha",  1f, 0);
        fadeIn.setStartDelay(startDelay);
        fadeIn.setDuration(duration);
        fadeIn.start();
    }

    private void btnClickListener() {
        btnJoinGroupShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                joinGroupBtnClicked = true;
                chooseActionCard.animate()
                        .setDuration(shortAnimTime)
                        .alpha(joinGroupBtnClicked ? 0 : 1)
                        .translationY(view.getHeight())
                        .alpha(0.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                chooseActionCard.setVisibility(joinGroupBtnClicked ? View.GONE : View.VISIBLE);
                            }
                        });


            }
        });


        btnCreateGroupShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createGroupBtnClicked = true;
                chooseActionCard.animate()
                        .setDuration(shortAnimTime)
                        .translationY(view.getHeight())
                        .alpha(0.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                chooseActionCard.setVisibility(View.GONE);
                                chooseActionCard.animate()
                                        .translationY(chooseActionCard.getHeight());
                            }
                        });
                joinGroupCard.animate()
                        .setDuration(shortAnimTime)
                        .translationY(view.getHeight())
                        .alpha(0.0f)
                        .setStartDelay(200)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                joinGroupCard.setVisibility(View.GONE);
                                joinGroupCard.animate()
                                        .translationY(joinGroupCard.getHeight());
                            }
                        });


            }
        });


    }

    public void onBackPressed() {


        if(joinGroupBtnClicked) {
            joinGroupBtnClicked = false;
            //Animates the card for choosing what to do

            chooseActionCard.animate()
                    .alpha(1.0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            chooseActionCard.setVisibility(View.VISIBLE);
                            chooseActionCard.animate()
                                    .setDuration(shortAnimTime)
                                    .translationY(0);
                        }
                    });
        } else if(createGroupBtnClicked) {
            joinGroupBtnClicked = false;
            createGroupBtnClicked = false;
            //Animates the card for choosing what to do
            chooseActionCard.animate()
                    .setDuration(shortAnimTime)
                    .alpha(1.0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            chooseActionCard.setVisibility(View.VISIBLE);
                            chooseActionCard.animate()
                                    .translationY(0)
                                    .alpha(1.0f);
                        }
                    });

            joinGroupCard.animate()
                    .setDuration(shortAnimTime)

                    .setStartDelay(100)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            joinGroupCard.setVisibility(View.VISIBLE);
                            joinGroupCard.animate()
                                    .translationY(0)
                                    .alpha(1.0f);
                        }
                    });
        }  else{

            super.onBackPressed();
        }


    }




}
