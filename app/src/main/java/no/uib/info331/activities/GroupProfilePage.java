package no.uib.info331.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import no.uib.info331.R;
import no.uib.info331.models.Group;
import no.uib.info331.models.User;

/**
 * Created by perni on 04.10.2017.
 */

public class GroupProfilePage extends AppCompatActivity {
    private ArrayList<User> users;
    private Group currentGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_profile);


    }
}
