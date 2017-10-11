package no.uib.info331.activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import no.uib.info331.R;
import no.uib.info331.models.User;

public class UserProfileActivity extends AppCompatActivity {

    User user;
    Context context;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_buildselect_title) TextView toolbarTitle;
    @BindView(R.id.circleimageview_user_profile_image) CircleImageView circleImageViewProfileImage;
    @BindView(R.id.textview_user_profile_points) TextView textViewUserPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);
        context = getApplicationContext();
        user = getUserFromLastActivity();
        initGui();
    }

    private void initGui() {
        initToolbar();
        loadImage();

        textViewUserPoints.setText(Integer.toString(user.getPoints()) + " " + getResources().getString(R.string.points).toLowerCase());

    }

    private void loadImage() {
        Drawable imageResource = ContextCompat.getDrawable(context, R.drawable.avatar2);

        String url = "url";
        Picasso.with(context)
                .load(url)
                .centerCrop()
                .fit()
                .placeholder(imageResource)
                .into(circleImageViewProfileImage);
    }

    private void initToolbar() {

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbarTitle.setText(user.getUsername());
            setMargins(toolbar, 0, getStatusBarHeight(), 0, 0);
        }
    }

    public User getUserFromLastActivity() {
        Bundle extras = getIntent().getExtras();
        String userStringObject;
        User user;
        Gson gson = new Gson();

        if (extras != null) {
            userStringObject = extras.getString("user");
            Type type = new TypeToken<User>(){}.getType();

            user = gson.fromJson(userStringObject, type);
            // and get whatever type user account id is
            return user;
        } else {
            user = new User("Not a valid user", "nan", "nan", 0);
            return user;
        }
    }


    public static void setMargins (View v, int left, int top, int right, int bottom) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            v.requestLayout();
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed(){
        super.onBackPressed();
    }
}
