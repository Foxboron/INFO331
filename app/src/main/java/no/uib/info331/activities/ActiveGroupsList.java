package no.uib.info331.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import no.uib.info331.R;
import no.uib.info331.models.Group;
import no.uib.info331.models.GroupAdapter;
import no.uib.info331.models.User;

/**
 * Created by perni on 02.10.2017.
 */

public class ActiveGroupsList extends AppCompatActivity {

    private ArrayList<Group> groups; //this needs to be instantiated from the database
    private Group group;
    private TextView text;

    //Code for populating list goes here
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_groups);
        //groups = database.get(list_of_groups); haha hvordan skal dette gå gutter
        groups = new ArrayList<>();
        ListView groupList = (ListView) findViewById(R.id.active_groups_list);

        //THE CODE BELOW IS USED FOR TESTING, DO NOT INCLUDE IN FINAL BUILD
        User user1 = new User("user", "pass", "pic", 5);
        User user2 = new User("user2", "pass11", "pic", 5);
        User user3 = new User("user3", "pass1", "pic", 5);

        group = new Group("testgroup", user1, 15);

        boolean addmember = group.addMember(user2);
        addmember = group.addMember(user3);
        addmember = group.addMember(user1);

        groups.add(group);
        Group group1 = new Group("testgroup2", user1, 15);
        Group group2 = new Group("testgroup3", user1, 15);
        Group group3 = new Group("testgroup4", user1, 15);
        Group group4 = new Group("testgroup5", user1, 15);
        Group group5 = new Group("testgroup6", user1, 15);
        Group group6 = new Group("testgroup7", user1, 15);
        Group group7 = new Group("testgroup8", user1, 15);
        Group group8 = new Group("testgroup9", user1, 15);
        Group group9 = new Group("testgroup10", user1, 15);
        groups.add(group1);
        groups.add(group2);
        groups.add(group3);
        groups.add(group4);
        groups.add(group5);
        groups.add(group6);
        groups.add(group7);
        groups.add(group8);
        groups.add(group9);

        for(int i=0; i < 100; i++){
            Group g = new Group("temp", user1, 10);
            groups.add(g);
        }

        text = (TextView) findViewById(R.id.groupName);
        //text.setText("test");
        //THE CODE ABOVE IS USED FOR TESTING, DO NOT INCLUDE IN FINAL BUILD

        GroupAdapter adapter = new GroupAdapter(groups, getApplicationContext());
        groupList.setAdapter(adapter);
    }
}