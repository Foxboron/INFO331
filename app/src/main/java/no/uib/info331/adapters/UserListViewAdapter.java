package no.uib.info331.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import no.uib.info331.R;
import no.uib.info331.models.User;

/**
 * Created by EddiStat on 05.10.2017.
 */

public class UserListViewAdapter extends ArrayAdapter<User> {

    private final Context context;
    private final int textViewResourceId;
    private final List<User> users;
    LayoutInflater inflater;

    public UserListViewAdapter(Context context, int textViewResourceId, List<User> users) {
        super(context, textViewResourceId, users);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.textViewResourceId = textViewResourceId;
        this.users = users;

    }

    public int getCount() {
        return users.size();
    }
    public User getItem(int position) {
        return users.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View row = convertView;
        UserHolder holder = null;

        if (row == null) {
            holder = new UserHolder(row);
            //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(textViewResourceId, parent, false);


            holder.userName.setText("John Doe");

            row.setTag(holder);


        } else {

            holder = (UserHolder) row.getTag();
        }

        Drawable imageResource = ContextCompat.getDrawable(context, R.drawable.avatar);

        String url = "";
        Picasso.with(context)
                .load(url)
                .centerCrop()
                .fit()
                .placeholder(imageResource)
                .into(holder.circleImageViewUserImage);

        row.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {



            }
        });

        return row;
    }


    public class UserHolder {
        @BindView(R.id.circleimageview_user_image) CircleImageView circleImageViewUserImage;
        @BindView(R.id.textview_username_list) TextView userName;

        public UserHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

}
