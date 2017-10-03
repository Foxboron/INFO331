package no.uib.info331.models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import no.uib.info331.R;



/**
 * Created by perni on 02.10.2017.
 *
 * This class should work because everyone on the internet says that just a normal adapter won't
 * work with objects in ListView
 *
 * Still a work in progress - reading up on guides and figuring out what's what at the moment
 * 
 **/


public class GroupAdapter extends ArrayAdapter<Group> {

    private Context context;
    private int layoutResourceID;
    private ArrayList<Group> groupList;
    private int lastPosition = -1;

    public GroupAdapter(ArrayList<Group> groups, Context context){
        super(context, R.layout.group_list_element, groups);
        this.context = context;
        this.groupList = groups;
    }

    /**
     * Original constructor - may be obsolete but tests need to be run on new
     *
    public GroupAdapter(Context context, int resource, List<Group> groups){
        super(context, resource, groups);
        this.context = context;
        this.layoutResourceID = resource;
        this.groupList = (ArrayList<Group>) groups;
    }

     **/

    private static class ViewHolder {
        TextView grpName;
        ImageView info;
    }


    /**
     * Original getView method - may be obsolete but will not delete until tests have been run on new method
     *
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        RecyclerView.ViewHolder holder = null;
        Group data = groupList.get(position);

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceID, parent, false);



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
    **/

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Group data = groupList.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.group_list_element, parent, false);
            viewHolder.grpName = (TextView) convertView.findViewById(R.id.groupName);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.groupIcon);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.grpName.setText(data.getName());
        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}
