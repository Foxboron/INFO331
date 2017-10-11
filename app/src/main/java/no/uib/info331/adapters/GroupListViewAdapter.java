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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import no.uib.info331.R;
import no.uib.info331.models.Group;
import no.uib.info331.models.User;

/**
 * Created by Per-Niklas Longberg on 02.10.2017.
 *
 * Custom adapter for populating ListView with our group objects.
 *
 **/

public class GroupListViewAdapter extends ArrayAdapter<Group> {

    private final int textViewResourceId;
    private Context context;
    private List<Group> groupList;
    private int lastPosition = -1;
    LayoutInflater inflater;

    public GroupListViewAdapter(Context context, int textViewResourceId, List<Group> groups){
        super(context, R.layout.list_element_join_group, groups);
        this.context = context;
        this.groupList = groups;
        inflater = LayoutInflater.from(context);
        this.textViewResourceId = textViewResourceId;

    }


    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        Group data = groupList.get(position);

        GroupHolder groupHolder = null;
        final View result;

        if (row == null) {

            groupHolder = new GroupHolder();
            inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(textViewResourceId, parent, false);

            groupHolder.circleImageViewGroupIcon = (CircleImageView) row.findViewById(R.id.circleimageview_group_image);
            groupHolder.textViewGroupName = (TextView) row.findViewById(R.id.textview_group_name);

            result = row;
            row.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) row.getTag();
            result = row;
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


        return row;
    }

    private static class GroupHolder {
        TextView textViewGroupName;
        ImageView circleImageViewGroupIcon;
    }
}

