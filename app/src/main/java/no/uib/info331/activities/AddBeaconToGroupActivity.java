package no.uib.info331.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import butterknife.BindView;
import no.uib.info331.R;

public class AddBeaconToGroupActivity extends AppCompatActivity {

    @BindView(R.id.edittext_create_search_for_beacons) EditText editTextSearchForBeacons;
    @BindView(R.id.imagebutton_search_for_beacons) ImageButton imageBtnSearchForBeacons;

    @BindView(R.id.listview_beacon_search_result) ListView listViewBeaconSearchResult;
    @BindView(R.id.listview_added_beacons) ListView listViewAddedBeacons;
    @BindView(R.id.btn_accept_searched_beacons) Button btnAcceptRegisteredBeacons;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_beacon_to_group);
    initListeners();

    }

    private void initListeners() {

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
}
