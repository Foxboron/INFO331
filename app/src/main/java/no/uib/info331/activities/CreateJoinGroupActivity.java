package no.uib.info331.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import no.uib.info331.R;

/**
 * Micromanaging af here
 *
 * @author Edvard P. Bjørgen
 *
 */
public class CreateJoinGroupActivity extends AppCompatActivity {

    TextView title;

    CardView chooseActionCard;
    CardView joinGroupCard;
    CardView createGroupCard;

    Button btnJoinGroupShow;
    Button btnCreateGroupShow;

    boolean joinGroupBtnClicked;
    boolean createGroupBtnClicked;

    int longAnimTime;
    Animation slideDownAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join_group);

        initGui();
        btnClickListener();
    }

    private void initGui() {

        title = (TextView) findViewById(R.id.text_create_join_group_title);

        chooseActionCard = (CardView) findViewById(R.id.choose_action_card);
        joinGroupCard = (CardView) findViewById(R.id.join_group_card);
        createGroupCard = (CardView) findViewById(R.id.create_group_card);

        btnJoinGroupShow = (Button) findViewById(R.id.show_join_group);
        btnCreateGroupShow = (Button) findViewById(R.id.show_create_group);

        longAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        slideDownAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.down_slide);

        joinGroupBtnClicked = false;
        createGroupBtnClicked = false;

    }

    private void btnClickListener() {
        btnJoinGroupShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                joinGroupBtnClicked = true;
                chooseActionCard.animate()
                        .setDuration(longAnimTime)
                        .alpha(joinGroupBtnClicked ? 0 : 1)
                        .translationY(view.getHeight())
                        .alpha(0.0f)
                        .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        chooseActionCard.setVisibility(joinGroupBtnClicked ? View.GONE : View.VISIBLE);

                        //TEST vvv

                        Intent intent = new Intent(getApplicationContext(), ActiveGroupsList.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        //TEST ^^^
                    }
                });


            }
        });


        btnCreateGroupShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createGroupBtnClicked = true;
                chooseActionCard.animate()
                        .setDuration(longAnimTime)
                        .alpha(joinGroupBtnClicked ? 0 : 1)
                        .translationY(view.getHeight())
                        .alpha(0.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                chooseActionCard.setVisibility(joinGroupBtnClicked ? View.GONE : View.VISIBLE);
                            }
                        });
                joinGroupCard.animate()
                        .setDuration(longAnimTime)
                        .alpha(createGroupBtnClicked ? 0 : 1)
                        .translationY(view.getHeight())
                        .alpha(0.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                joinGroupCard.setVisibility(createGroupBtnClicked ? View.GONE : View.VISIBLE);
                            }
                        });


            }
        });


    }



}
