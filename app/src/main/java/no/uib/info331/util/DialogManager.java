package no.uib.info331.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import no.uib.info331.R;
import no.uib.info331.activities.CreateGroupActivity;
import no.uib.info331.adapters.UserListViewAdapter;
import no.uib.info331.models.User;

/**
 * Class for managing dialog windows.
 * @author Edvard P. Bjørgen
 */

public class DialogManager {
    /**
     * Is used when users want to add a user in new group in CreateGroupActivity.
      * @param USER The user to display the information about
     * @param CONTEXT the context of the activity
     * @param RESOURCES the resources from the applicaiton
     * @param ADDED_USER_MEMBERS_LIST_ADAPTER the ListViewAdapter, for adding the user in to it, and notifying that data set has changed.
     */
    public void createUserProfileDialogForCreateGroup(final User USER, final Context CONTEXT, final Resources RESOURCES, final UserListViewAdapter ADDED_USER_MEMBERS_LIST_ADAPTER){
        final AlertDialog dialog = new AlertDialog.Builder(CONTEXT)
                .setView(R.layout.dialog_add_member_profile)
                .create();
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        TextView textViewDialogUserName = (TextView) dialog.findViewById(R.id.dialog_textview_add_member_username);
        textViewDialogUserName.setText(USER.getUsername());

        FloatingActionButton fab = (FloatingActionButton) dialog.findViewById(R.id.fabCal);
        if(fab != null)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar snack = Snackbar.make(view, RESOURCES.getString(R.string.member_added), Snackbar.LENGTH_LONG)
                            .setAction("Action", null);
                    View sbView = snack.getView();
                    //Set  typeface
                    TextView tv = (TextView) (sbView).findViewById(android.support.design.R.id.snackbar_text);
                    Typeface font = Typeface.createFromAsset(CONTEXT.getAssets(), "fonts/roboto_thin.ttf");
                    tv.setTypeface(font);
                    tv.setTextSize(16);
                    snack.show();

                    //Countdown to when to start the calendar intent, this for showing currentUser the snackbar of whats happening next TODO: This description is wrong
                    new CountDownTimer(800, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {}
                        public void onFinish() {
                            ADDED_USER_MEMBERS_LIST_ADAPTER.add(USER);
                            ADDED_USER_MEMBERS_LIST_ADAPTER.notifyDataSetChanged();
                            dialog.cancel();
                        }
                    }.start();
                }
            });


    }

}
