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

import no.uib.info331.R;

/**
 * Created by Per-Niklas Longberg on 02.10.2017.
 *
 * Custom adapter for populating ListView with our group objects.
 *
 **/

public class GroupAdapter extends ArrayAdapter<Group> {

    private Context context;
    private ArrayList<Group> groupList;
    private int lastPosition = -1;

    public GroupAdapter(ArrayList<Group> groups, Context context){
        super(context, R.layout.list_element_join_group, groups);
        this.context = context;
        this.groupList = groups;
    }

    private static class ViewHolder {
        TextView grpName;
        ImageView info;
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Group data = groupList.get(position);
        ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_element_join_group, parent, false);

            viewHolder.grpName = (TextView) convertView.findViewById(R.id.groupName);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.groupIcon);

            result = convertView;
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

        return convertView;
    }
}
