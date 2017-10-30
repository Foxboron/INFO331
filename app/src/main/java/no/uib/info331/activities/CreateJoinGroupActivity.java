package no.uib.info331.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import no.uib.info331.R;
import no.uib.info331.adapters.CustomPagerAdapter;
import no.uib.info331.models.Group;
import no.uib.info331.models.User;
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
    @BindView(R.id.btn_skip_group_selection) Button btnSkipGroupSelection;

    int shortAnimTime;
    int longAnimTime;

    Animations anim = new Animations();
    Context context;

    ViewPager pager;
    private GestureDetectorCompat mDetector;
    private User user;
    private DataManager dataManager = new DataManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_join_group);
        ButterKnife.bind(this);
        context = getApplicationContext();
        user = dataManager.getSavedObjectFromSharedPref(context, "currentlySignedInUser", new TypeToken<User>(){}.getType());


        //getAllUsers();
        initGui();
        initPager();
        initListeners();

        /**
         * If user does not have user group arraylist set, create a new empty list
         * This is for avoiding NPE's later on
         */
        if(user.getGroups() == null ){
            createEmptyGroupListForUser();
        }


    }

    private void initPager() {
        CustomPagerAdapter adapter = new CustomPagerAdapter(this);
        pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setAdapter(adapter);

    }


    private void initGui() {
        shortAnimTime = anim.getShortAnimTime(context);
        longAnimTime = anim.getLongAnimTime(context);
        anim.fadeInView(textViewTitle, 200, shortAnimTime);
        hideButtonIfUserHasGroup();

    }

    /**
     * If user has groups, do not show the skip button
     */
    private void hideButtonIfUserHasGroup() {
        if(user.getGroups() != null){
            //TODO: Find a better solution for this
            if (user.getGroups().size() > 0 ) {
                btnSkipGroupSelection.setVisibility(View.GONE);
            }
        }

    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            return mDetector.onTouchEvent(motionEvent);}
    };


    private void initListeners() {
        mDetector = new GestureDetectorCompat(this,  new MyGestureListener());

        pager.setOnTouchListener(touchListener);


        btnSkipGroupSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEmptyGroupListForUser();
                Intent intent = new Intent(context, DashboardActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Creates ad writes an empty list to the user
     */
    private void createEmptyGroupListForUser() {
        ArrayList<Group> emptyGroupArray = new ArrayList<>();
        user.setGroups(emptyGroupArray);
        User refreshedUser = user;
        dataManager.storeObjectInSharedPref(getApplicationContext(), "currentlySignedInUser", refreshedUser);
    }

    protected void onResume() {
        super.onResume();

        anim.fadeInView(cardChooseAction, 0, shortAnimTime);
        anim.moveViewToTranslationY(cardChooseAction, 50 , shortAnimTime, 0, false);
    }


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {


        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            Log.d("TAG", "onFlingY: "+ velocityY);
            Log.d("TAG", "onFlingX: "+ velocityX);
            if(velocityY > 500 && velocityX < 2000 && velocityX > -2000){
                switch (pager.getCurrentItem()){
                    case 0:
                        anim.fadeOutView(cardChooseAction, 0, longAnimTime);
                        anim.moveViewToTranslationY(cardChooseAction, 0 , shortAnimTime, cardChooseAction.getHeight(), true);

                        Intent intent = new Intent(context, CreateGroupActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;

                    case 1:
                        anim.fadeOutView(cardChooseAction, 0, shortAnimTime);
                        anim.moveViewToTranslationY(cardChooseAction,0 , shortAnimTime, cardChooseAction.getHeight(), true);

                        Intent intent2 = new Intent(context, JoinGroupActivity.class);
                        startActivity(intent2);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        break;
                }
                return true;
            }else{
                return false;
            }
        }
    }


}
