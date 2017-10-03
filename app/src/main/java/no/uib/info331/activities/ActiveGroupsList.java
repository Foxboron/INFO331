package no.uib.info331.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
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
    private ListView groupList;

    //Code for populating list goes here
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_groups);
        //groups = database.get(list_of_groups); haha hvordan skal dette gå gutter
        groups = new ArrayList<>();
        groupList = (ListView) findViewById(R.id.active_groups_list);

        //THE CODE BELOW IS USED FOR TESTING, DO NOT INCLUDE IN FINAL BUILD
        User user1 = new User("user", "pass", "pic", 5);

        for(int i=0; i < 100; i++){
            Group g = new Group("temp", user1, 10);
            groups.add(g);
            text = (TextView) findViewById(R.id.groupName);
            //text.setText(getString(R.string.group_name_filler));
        }

        //THE CODE ABOVE IS USED FOR TESTING, DO NOT INCLUDE IN FINAL BUILD

        GroupAdapter adapter = new GroupAdapter(groups, getApplicationContext());
        groupList.setAdapter(adapter);

        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Group g = groups.get(position);

                Snackbar.make(view, g.getName(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });
    }
}