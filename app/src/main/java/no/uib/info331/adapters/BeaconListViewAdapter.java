package no.uib.info331.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import no.uib.info331.R;
import no.uib.info331.models.Beacon;
import no.uib.info331.models.User;

/**
 * Custom adapter for populating ListViews with beacons.
 *
 * @author Edvard Pires Bjørgen
 *
 **/

public class BeaconListViewAdapter extends ArrayAdapter<Beacon> {

    private final Context context;
    private final int textViewResourceId;
    private final List<Beacon> beacons;
    LayoutInflater inflater;

    public BeaconListViewAdapter(Context context, int textViewResourceId, List<Beacon> beacons) {
        super(context, textViewResourceId, beacons);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.textViewResourceId = textViewResourceId;
        this.beacons = beacons;

    }

    public int getCount() {
        return beacons.size();
    }
    public Beacon getItem(int position) {
        return beacons.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View row = convertView;
        UserHolder holder = null;

        if (row == null) {
            holder = new UserHolder();
            row = inflater.inflate(textViewResourceId, parent, false);

            holder.textViewBeaconName = (TextView) row.findViewById(R.id.textview_beacon_name);
            holder.textViewBeaconUuid = (TextView) row.findViewById(R.id.textview_beacon_uuid);
            holder.textViewBeaconMajorMinor = (TextView) row.findViewById(R.id.textview_major_minor);

            row.setTag(holder);
        } else {

            holder = (UserHolder) row.getTag();
        }
        holder.textViewBeaconName.setText(beacons.get(position).getName());
        holder.textViewBeaconUuid.setText("UUID: " + beacons.get(position).getUUID());
        holder.textViewBeaconMajorMinor.setText("Major: " + beacons.get(position).getMajor() + " Minor: " + beacons.get(position).getMinor());
        return row;
    }


    static class UserHolder {
        TextView textViewBeaconName;
        TextView textViewBeaconUuid;
        TextView textViewBeaconMajorMinor;
    }

}
