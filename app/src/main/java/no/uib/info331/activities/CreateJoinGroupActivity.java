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

import no.uib.info331.R;

/**
 * Micromanaging af here
 *
 * @author Edvard P. Bjørgen
 *
 */
public class CreateJoinGroupActivity extends AppCompatActivity {

    TextView textViewTitle;

    CardView chooseActionCard;
    CardView joinGroupCard;
    CardView createGroupCard;

    Button btnJoinGroupShow;
    Button btnCreateGroupShow;

    EditText editTextSearchJoinGroup;

    boolean joinGroupBtnClicked;

    boolean createGroupBtnClicked;
    int shortAnimTime;

    int mediumAnimTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join_group);

        initGui();
        btnClickListener();
    }

    private void initGui() {

        textViewTitle = (TextView) findViewById(R.id.text_create_join_group_title);

        chooseActionCard = (CardView) findViewById(R.id.choose_action_card);
        joinGroupCard = (CardView) findViewById(R.id.join_group_card);
        createGroupCard = (CardView) findViewById(R.id.create_group_card);

        btnJoinGroupShow = (Button) findViewById(R.id.show_join_group);
        btnCreateGroupShow = (Button) findViewById(R.id.show_create_group);

        shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mediumAnimTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);

        editTextSearchJoinGroup = (EditText) findViewById(R.id.join_search_group);

        joinGroupBtnClicked = false;
        createGroupBtnClicked = false;

        fadeInView(textViewTitle, 200, shortAnimTime);

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
                        .setDuration(mediumAnimTime)
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
                        .setDuration(mediumAnimTime)
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
