package no.uib.info331.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import no.uib.info331.R;
import no.uib.info331.adapters.GroupListViewAdapter;
import no.uib.info331.models.Group;
import no.uib.info331.models.User;

/**
 * Created by Per-Niklas Longberg on 02.10.2017.
 *
 * Activity that displays the list of groups using the custom adapter GroupListViewAdapter.class
 *
 */

public class ActiveGroupsList extends AppCompatActivity {

    private ArrayList<Group> groups; //this needs to be instantiated from the database
    private Group group;
    private TextView text;
    private ListView groupList;
    private String[] groupNameList;

    //Code for populating list goes here
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_groups);

        //groups = database.get(list_of_groups); Placeholder code - need DB endpoint

        groups = new ArrayList<>();

        // The code below is used for testing - while it is not supposed to be included in the final
        // build it will successfully display the list of groups. Might use some of this code later.

        User user1 = new User("user", "pass", "pic", 5);
        setContentView(R.layout.list_element_join_group);
        groupNameList = new String[100];
        for(int i=0; i < 100; i++){
            String goodName = getString(R.string.group_name_filler) + Integer.toString(i);
            Group g = new Group(goodName, user1);
            g.setPoints(i+10);
            groups.add(g);
        }

        for(int j = 0; j < 100; j++){
            if(j < groups.size()) {
                Group g = groups.get(j);
                String gName = g.getName();
                groupNameList[j] = gName;
            }
        }

        text = (TextView) findViewById(R.id.groupName);

        for(int k = 0; k < 100; k++){
            text.setText(groupNameList[k]);
        }

        setContentView(R.layout.activity_active_groups);
        groupList = (ListView) findViewById(R.id.active_groups_list);

        // End of test code.

        GroupListViewAdapter adapter = new GroupListViewAdapter(groups, getApplicationContext());
        groupList.setAdapter(adapter);

        // This method will be altered to start the activities of each groups own group page.
        // For now it does nothing.

        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Group g = groups.get(position);
                Snackbar.make(view, g.getName(), Snackbar.LENGTH_LONG)
                        .setAction("Feature to be implemented", null).show();
            }
        });
    }
}