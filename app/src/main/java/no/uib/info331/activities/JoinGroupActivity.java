package no.uib.info331.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import no.uib.info331.R;

public class JoinGroupActivity extends AppCompatActivity {

    @BindView(R.id.join_group_card) CardView cardJoinGroup;


    @BindView(R.id.edittext_search_for_groups) EditText editTextSearchForGroups;
    @BindView(R.id.listview_add_group_list) ListView listViewGroupList;
    @BindView(R.id.imagebutton_search_for_group) ImageButton imageBtnSearchForGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
    }
}
