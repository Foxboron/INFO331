package no.uib.info331.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import no.uib.info331.R;
import no.uib.info331.models.Group;

/**
 * Created by Per-Niklas Longberg on 02.10.2017.
 *
 * Custom adapter for populating ListView with our group objects.
 *
 **/

public class GroupListViewAdapter extends ArrayAdapter<Group> {

    private Context context;
    private ArrayList<Group> groupList;
    private int lastPosition = -1;

    public GroupListViewAdapter(ArrayList<Group> groups, Context context){
        super(context, R.layout.list_element_join_group, groups);
        this.context = context;
        this.groupList = groups;
    }


    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Group data = groupList.get(position);
        View row = convertView;

        GroupHolder groupHolder = null;
        final View result;

        if (convertView == null) {
            groupHolder = new GroupHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_element_join_group, parent, false);

            groupHolder.circleImageViewGroupIcon = (CircleImageView) row.findViewById(R.id.circleimageview_group_image);
            groupHolder.textViewGroupName = (TextView) row.findViewById(R.id.textview_group_name);

            result = convertView;
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        groupHolder.textViewGroupName.setText(data.getName());

        Drawable imageResource = ContextCompat.getDrawable(context, R.drawable.group);
        String url = "url";
        Picasso.with(context)
                .load(url)
                .centerCrop()
                .fit()
                .placeholder(imageResource)
                .into(groupHolder.circleImageViewGroupIcon);


        return convertView;
    }

    private static class GroupHolder {
        TextView textViewGroupName;
        ImageView circleImageViewGroupIcon;
    }
}

