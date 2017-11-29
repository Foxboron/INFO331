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
import no.uib.info331.models.Event;

/**
 * Custom adapter for populating ListViews with beacons.
 *
 * @author Edvard Pires Bjørgen
 *
 **/

public class EventListViewAdapter extends ArrayAdapter<Event> {

    private final Context context;
    private final int textViewResourceId;
    private final List<Event> events;
    LayoutInflater inflater;

    public EventListViewAdapter(Context context, int textViewResourceId, List<Event> events) {
        super(context, textViewResourceId, events);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.textViewResourceId = textViewResourceId;
        this.events = events;

    }

    public int getCount() {
        return events.size();
    }
    public Event getItem(int position) {
        return events.get(position);
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


            holder.textViewEventGroupName = (TextView) row.findViewById(R.id.textview_event_group);
            holder.textViewEnterExitEvent = (TextView) row.findViewById(R.id.textview_enter_exit_event);
            holder.textViewDateTime = (TextView) row.findViewById(R.id.textview_date_time);

            row.setTag(holder);
        } else {

            holder = (UserHolder) row.getTag();
        }
        holder.textViewEventGroupName.setText("Event for: " + events.get(position).getGroup().getName());
        holder.textViewEnterExitEvent.setText("Last type of activty: " + events.get(position).getEvent());
        holder.textViewDateTime.setText("Time: " + events.get(position).getDate().toString());
        return row;
    }


    static class UserHolder {
        TextView textViewEventGroupName;
        TextView textViewEnterExitEvent;
        TextView textViewDateTime;
    }

}
