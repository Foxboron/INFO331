package no.uib.info331.models;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import no.uib.info331.R;



/**
 * Created by perni on 02.10.2017.
 *
 * This class should work because everyone on the internet says that just a normal adapter won't
 * work with objects in ListView
 **/


public class GroupAdapter extends ArrayAdapter<Group> {

    Context context;
    int layoutResourceID;
    ArrayList<Group> groupList = null;

    public GroupAdapter(Context context, int resource, List<Group> groups){
        super(context, resource, groups);
        this.context = context;
        this.layoutResourceID = resource;
        this.groupList = (ArrayList<Group>) groups;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        HeaderHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceID, parent, false);

            holder = new HeaderHolder();
            holder.from = (TextView) row.findViewById(R.id.);
            holder.to = (TextView)row.findViewById(R.id.toTextView);
            holder.subject = (TextView)row.findViewById(R.id.subjectTextView);

            row.setTag(holder);
        }
        else
        {
            holder = (HeaderHolder) row.getTag();
        }

        Group group = data.get(position);
        holder.from.setText(item.getFrom());
        holder.to.setText(item.getTo());
        holder.subject.setText(item.getSubject());

        return row;
    }

    private class HeaderHolder {
        public TextView from;
        public TextView to;
        public TextView subject;

    }
}
