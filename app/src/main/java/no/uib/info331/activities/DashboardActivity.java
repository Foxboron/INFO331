package no.uib.info331.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;


import butterknife.BindView;
import no.uib.info331.R;
import no.uib.info331.models.User;
import no.uib.info331.util.Animations;
import no.uib.info331.util.DataManager;


public class DashboardActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    TextView toolbarTitle;

     ImageButton btnHamburgerMenu;
    TextView textViewUserPoints;

    DataManager dataManager = new DataManager();
    Animations anim = new Animations();
    Context context;

    private AccountHeader headerResult = null;
    private Drawer result;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        context = getApplicationContext();
        user = dataManager.getSavedObjectFromSharedPref(context, "currentlySignedInUser", new TypeToken<User>(){}.getType());

        initGui();
    }

    private void initGui() {
        btnHamburgerMenu = (ImageButton) findViewById(R.id.btn_menu_dashboard);
        textViewUserPoints = (TextView) findViewById(R.id.textview_dashboard_points);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_buildselect_title);

        anim.fadeInView(toolbarTitle, 200, 200);

        initToolbar();
        initDrawer();
        initListeners();

        textViewUserPoints.setText(Integer.toString(user.getPoints()) + " " + getResources().getString(R.string.points).toLowerCase());


    }

    private void initListeners() {
        btnHamburgerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.openDrawer();
            }
        });
    }

    private void initDrawer() {

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.login_splash)
                .addProfiles(
                        new ProfileDrawerItem().withName(user.getUsername()).withIcon(getResources().getDrawable(R.drawable.avatar2))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        Gson gson = new Gson();
                        String userStringObject = gson.toJson(user);
                        Intent intent = new Intent(context, UserProfileActivity.class);
                        intent.putExtra("user", userStringObject);
                        startActivity(intent);
                        return false;
                    }
                })
                .build();

        //create the drawer and remember the `Drawer` result object
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(false)
                .withSelectedItem(-1)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new SectionDrawerItem().withName(R.string.groups),
                        new SecondaryDrawerItem().withName(R.string.my_groups).withIcon(R.drawable.ic_person_add).withSelectable(false).withIdentifier(0),
                        new SecondaryDrawerItem().withName(R.string.drawer_join_create_group).withIcon(R.drawable.ic_group).withSelectable(false).withIdentifier(1),
                        new DividerDrawerItem()

                        )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Intent intent = null;
                        switch((int) drawerItem.getIdentifier()){
                            case 0:
                                Toast.makeText(context, "My groups activity, TBD", Toast.LENGTH_SHORT).show();
                            break;

                            case 1:

                                intent = new Intent(context, CreateJoinGroupActivity.class);
                                startActivity(intent);
                                break;

                            case 98:
                                dataManager.deleteSavedObjectFromSharedPref(context, "currentlySignedInUser");
                                intent = new Intent(context, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                break;
                            case 99:
                                Toast.makeText(context, "My groups activity, TBD", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                })
                .build();
        result.addStickyFooterItem(new PrimaryDrawerItem().withName(R.string.drawer_temporary_log_out).withIcon(R.drawable.ic_settings).withSelectable(false).withIdentifier(98));
        result.addStickyFooterItem(new PrimaryDrawerItem().withName(R.string.drawer_settings).withIcon(R.drawable.ic_settings).withSelectable(false).withIdentifier(99));
    }


    private void initToolbar() {

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbarTitle.setText("");
        }
    }



}
