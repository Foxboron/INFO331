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
import com.intrusoft.squint.DiagonalView;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import no.uib.info331.R;
import no.uib.info331.models.User;
import no.uib.info331.util.LayoutAdjustments;

public class UserProfileActivity extends AppCompatActivity {

    User user;
    Context context;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.diagonalview_profile) DiagonalView diagonalViewProfile;
    @BindView(R.id.toolbar_buildselect_title) TextView toolbarTitle;
    @BindView(R.id.circleimageview_user_profile_image) CircleImageView circleImageViewProfileImage;
    @BindView(R.id.textview_user_profile_points) TextView textViewUserPoints;

    LayoutAdjustments layoutAdj = new LayoutAdjustments();

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
        loadCoverPicture();
        loadProfilePicture();
        textViewUserPoints.setText(Integer.toString(user.getPoints()) + " " + getResources().getString(R.string.points).toLowerCase());

    }

    private void loadCoverPicture() {

        int imgCode = 1 + (int)(Math.random() * ((6 - 1) + 1));
        String url = "http://pires.top/img/info331/cover-" + imgCode + ".jpg";
        Picasso.with(context)
                .load(url)
                .centerCrop()
                .fit()
                .into(diagonalViewProfile);
    }

    private void loadProfilePicture() {
        Drawable imageResource = ContextCompat.getDrawable(context, R.drawable.avatar2);

        int imgCode = 1;
        String url = "url";
        Picasso.with(context)
                .load(url)
                .centerCrop()
                .fit()
                .noFade()
                .placeholder(imageResource)
                .into(circleImageViewProfileImage);
    }

    private void initToolbar() {

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbarTitle.setText(user.getUsername());
            layoutAdj.setMargins(toolbar, 0, layoutAdj.getStatusBarHeight(getResources()), 0, 0);
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
