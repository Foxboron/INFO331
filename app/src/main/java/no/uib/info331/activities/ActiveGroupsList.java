package no.uib.info331.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import no.uib.info331.R;
import no.uib.info331.models.Group;

/**
 * Created by perni on 02.10.2017.
 */

public class ActiveGroupsList extends AppCompatActivity {

    private ArrayList<Group> groups; //this needs to be instantiated from the database


    //Code for populating list goes here
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_groups);
        //groups = database.get(list_of_groups);


    ListView groupList = (ListView) findViewById(R.id.active_groups_list);


    //THE CODE BELOW IS USED FOR TESTING, DO NOT INCLUDE IN FINAL BUILD
    //fikk ikke tid til å mekke as
    //THE CODE ABOVE IS USED FOR TESTING, DO NOT INCLUDE IN FINAL BUILD

    ArrayAdapter<Group> adapter = new ArrayAdapter<>(this.getApplicationContext(), R.layout.activity_active_groups, R.id.active_groups_list, groups);
    groupList.setAdapter(adapter);

    }
}