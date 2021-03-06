package no.uib.info331.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import no.uib.info331.R;
import no.uib.info331.adapters.BeaconListViewAdapter;
import no.uib.info331.models.Beacon;
import no.uib.info331.models.messages.BeaconListEvent;
import no.uib.info331.queries.BeaconQueries;
import no.uib.info331.util.DataManager;

public class AddBeaconToGroupActivity extends AppCompatActivity {

    BeaconQueries beaconQueries = new BeaconQueries();
    DataManager dataManager = new DataManager();

    @BindView(R.id.edittext_create_search_for_beacons) EditText editTextSearchForBeacons;
    @BindView(R.id.imagebutton_search_for_beacons) ImageButton imageBtnSearchForBeacons;

    @BindView(R.id.listview_beacon_search_result) ListView listViewBeaconSearchResult;
    @BindView(R.id.listview_added_beacons) ListView listViewBeaconsAdded;
    @BindView(R.id.btn_accept_searched_beacons) Button btnAcceptRegisteredBeacons;

    Context context;

    BeaconListViewAdapter listViewAdapterBeaconsSearchResult;
    BeaconListViewAdapter listViewAdapterBeaconsAdded;

    List<Beacon> arrayListBeaconsAdded = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_beacon_to_group);
        context = getApplicationContext();
        ButterKnife.bind(this);
        initListeners();
        initListViewBeaconsAdded(arrayListBeaconsAdded);
    }

    private void initListeners() {
        imageBtnSearchForBeacons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = String.valueOf(editTextSearchForBeacons.getText());
                beaconQueries.getBeaconsByStringFromDb(context, query);

            }
        });

        btnAcceptRegisteredBeacons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        listViewBeaconSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Beacon beacon = listViewAdapterBeaconsSearchResult.getItem(position);
                listViewAdapterBeaconsAdded.add(beacon);
                listViewAdapterBeaconsAdded.notifyDataSetChanged();
            }

        });

    }
     @Subscribe(threadMode = ThreadMode.MAIN)
     public void onBeaconListEvent(BeaconListEvent beaconListEvent) {
         initListViewBeaconSearchList(beaconListEvent.getBeaconList());
     }

    private void initListViewBeaconSearchList(List<Beacon> beaconSearch) {
        listViewAdapterBeaconsSearchResult = new BeaconListViewAdapter(context, R.layout.list_element_beacon, beaconSearch);
        listViewBeaconSearchResult.setAdapter(listViewAdapterBeaconsSearchResult);

    }
    private void initListViewBeaconsAdded(List<Beacon> beaconsAdded) {
        listViewAdapterBeaconsAdded = new BeaconListViewAdapter(context, R.layout.list_element_beacon, beaconsAdded);
        listViewBeaconsAdded.setAdapter(listViewAdapterBeaconsAdded);
    }


    /**
     * Method from activity to display the "Back arrow"
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Gson gson = new Gson();
        String addedBeaconsString = gson.toJson(arrayListBeaconsAdded);
        Intent intent = new Intent();
        intent.putExtra("addedBeacons", addedBeaconsString);
        setResult(RESULT_OK, intent);
        super.onBackPressed();

    }

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
}
