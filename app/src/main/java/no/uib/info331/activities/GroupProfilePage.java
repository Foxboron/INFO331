package no.uib.info331.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import no.uib.info331.R;
import no.uib.info331.models.Group;
import no.uib.info331.models.User;
import no.uib.info331.models.UserAdapter;

/**
 * Created by perni on 04.10.2017.
 */

public class GroupProfilePage extends AppCompatActivity {
    private ArrayList<User> users;
    private Group currentGroup;
    private ListView userList;
    private TextView points;
    private TextView gname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_profile);
        users = new ArrayList<>();
        User u = new User("haha", "yeye", "haha", 5);
        for(int i = 0; i < 10; i++){
            users.add(u);
        }
        users.add(u);

        currentGroup = new Group("Groupname haha", u, 5000);
        points = (TextView) findViewById(R.id.group_points);
        points.setText(Integer.toString(currentGroup.getPoints()));
        gname = (TextView) findViewById(R.id.group_display_name);
        gname.setText(currentGroup.getName());

        userList = (ExpandableListView) findViewById(R.id.exp_mem_list_grp);

        UserAdapter adapt = new UserAdapter(users, this.getApplicationContext());
        userList.setAdapter(adapt);

    }
}
