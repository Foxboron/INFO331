package no.uib.info331.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import no.uib.info331.R;
import no.uib.info331.adapters.GroupListViewAdapter;
import no.uib.info331.models.Group;
import no.uib.info331.queries.GroupQueries;

public class JoinGroupActivity extends AppCompatActivity {

    @BindView(R.id.edittext_search_for_groups) EditText editTextSearchForGroups;
    @BindView(R.id.listview_add_group_list) ListView listViewGroupList;
    @BindView(R.id.imagebutton_search_for_group_join) ImageButton imageBtnSearchForGroups;


    private GroupListViewAdapter searchedGroupListViewAdapter;
    private GroupQueries groupQueries = new GroupQueries();
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
        ButterKnife.bind(this);
        initGui();
        initListeners();
    }

    private void initGui() {
        context = getApplicationContext();
    }

    private void initListeners() {
        imageBtnSearchForGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = String.valueOf(editTextSearchForGroups.getText());
                List<Group> groupSearch = groupQueries.getGroupsByStringFromDb(context, query);
                try {
                    if(groupSearch != null){
                        initListViewGroupList(groupSearch);
                        searchedGroupListViewAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Can't return results", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(context, getResources().getString(R.string.something_wrong), Toast.LENGTH_LONG).show();

                }
            }
        });

        listViewGroupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Group group = searchedGroupListViewAdapter.getItem(i);
                Gson gson = new Gson();
                String userStringObject = gson.toJson(group);
                Intent intent = new Intent(context, GroupProfileActivity.class);
                intent.putExtra("group", userStringObject);
                startActivity(intent);


            }
        });

    }

    private void initListViewGroupList(List<Group> searchedGroups) {
        searchedGroupListViewAdapter = new GroupListViewAdapter(context, R.layout.list_element_join_group, searchedGroups);
        listViewGroupList.setAdapter(searchedGroupListViewAdapter);

    }
}
