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

public class UserAdapter extends ArrayAdapter<User> {

    private Context context;
    private ArrayList<User> userList;
    private int lastPosition = -1;

    public UserAdapter(ArrayList<User> users, Context context){
        super(context, R.layout.group_member_list_child, users);
        this.context = context;
        this.userList = users;
    }

    private static class ViewHolder {
        TextView grpName;
        ImageView info;
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        User data = userList.get(position);
        ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.group_member_list_child, parent, false);

            // Må konverteres til å finne usernavn
            viewHolder.grpName = (TextView) convertView.findViewById(R.id.group_member_list_username);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.user_profile_pic);

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.grpName.setText(data.getUsername());
        viewHolder.info.setTag(position);

        return convertView;
    }
}