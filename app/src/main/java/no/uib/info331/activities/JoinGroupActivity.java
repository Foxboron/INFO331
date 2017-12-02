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
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import no.uib.info331.R;
import no.uib.info331.adapters.GroupListViewAdapter;
import no.uib.info331.models.Group;
import no.uib.info331.models.messages.GroupListEvent;
import no.uib.info331.queries.GroupQueries;
import no.uib.info331.util.Animations;

/**
 * Activity that lets the profileUser search for groups in db and click on it in the searched groups list,
 * they  are then taken to the group profile page, GroupProfileActivity
 *
 * @author Edvard P. Bjørgen
 *
 */

public class JoinGroupActivity extends AppCompatActivity {

    @BindView(R.id.join_group_card) CardView cardViewJoinGroup;
    @BindView(R.id.edittext_search_for_groups) EditText editTextSearchForGroups;
    @BindView(R.id.listview_add_group_list) ListView listViewGroupList;
    @BindView(R.id.imagebutton_search_for_group_join) ImageButton imageBtnSearchForGroups;

    private GroupListViewAdapter searchedGroupListViewAdapter;
    private GroupQueries groupQueries = new GroupQueries();
    private Animations anim = new Animations();
    private Context context;
    private int shortAnimTime;

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
        shortAnimTime = anim.getShortAnimTime(context);
        anim.moveViewToTranslationY(cardViewJoinGroup, 0 , shortAnimTime, 5000, false);
        anim.fadeInView(cardViewJoinGroup, 0, shortAnimTime);
        anim.moveViewToTranslationY(cardViewJoinGroup, 100 , shortAnimTime, 0, false);
    }

    private void initListeners() {

        imageBtnSearchForGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = String.valueOf(editTextSearchForGroups.getText());
                groupQueries.getGroupsByStringFromDb(context, query);
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGroupListEvent(GroupListEvent groupListEvent){
        initListViewGroupList(groupListEvent.getGroupList());

    }

    private void initListViewGroupList(List<Group> searchedGroups) {
        searchedGroupListViewAdapter = new GroupListViewAdapter(context, R.layout.list_element_join_group, searchedGroups);
        listViewGroupList.setAdapter(searchedGroupListViewAdapter);
    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
<<<<<<< HEAD

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }
=======
>>>>>>> formatting code to prettier standards
}
