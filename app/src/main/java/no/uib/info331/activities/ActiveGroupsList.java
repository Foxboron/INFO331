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


    //Code for populating list goes here
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_groups);
        //groups = database.get(list_of_groups); haha hvordan skal dette gå gutter

        ListView groupList = (ListView) findViewById(R.id.active_groups_list);

        //THE CODE BELOW IS USED FOR TESTING, DO NOT INCLUDE IN FINAL BUILD
        User user1 = new User("user", "pass", "pic", 5);
        User user2 = new User("user2", "pass11", "pic", 5);
        User user3 = new User("user3", "pass1", "pic", 5);

        Group group = new Group("testgroup", user1, 15);

        group.addMember(user2);
        group.addMember(user3);

        TextView test = (TextView) findViewById(R.id.groupName);
        test.setText(group.getName());
        //THE CODE ABOVE IS USED FOR TESTING, DO NOT INCLUDE IN FINAL BUILD

        GroupAdapter adapter = new GroupAdapter(groups, getApplicationContext());
        groupList.setAdapter(adapter);
    }
}