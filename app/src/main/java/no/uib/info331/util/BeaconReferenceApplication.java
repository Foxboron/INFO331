package no.uib.info331.util;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;

import com.google.gson.reflect.TypeToken;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.RegionBootstrap;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import no.uib.info331.R;
import no.uib.info331.activities.MonitoringActivity;
import no.uib.info331.models.Beacon;
import no.uib.info331.models.Group;
import no.uib.info331.models.User;
import no.uib.info331.models.messages.BackgroundBeaconEvent;
import no.uib.info331.queries.EventQueries;

/**
 * Created by dyoung on 12/13/13.
 */
public class BeaconReferenceApplication extends Application implements BootstrapNotifier, BeaconConsumer {
    private static final String TAG = "BeaconReferenceApp";
    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private boolean haveDetectedBeaconsSinceBoot = false;
    private MonitoringActivity monitoringActivity = null;
    private BeaconManager beaconManager;
    private List<Region> regionList = new ArrayList<>();

    DataManager dataManager = new DataManager();
    EventQueries eventQueries = new EventQueries();
    User user;


    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        beaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);

        // By default the AndroidBeaconLibrary will only find AltBeacons.  If you wish to make it
        // find a different type of beacon, you must specify the byte layout for that beacon's
        // advertisement with a line like below.  The example shows how to find a beacon with the
        // same byte layout as AltBeacon but with a beaconTypeCode of 0xaabb.  To find the proper
        // layout expression for other beacon types, do a web search for "setBeaconLayout"
        // including the quotes.
        //
        beaconManager.bind(this);
        beaconManager.setForegroundScanPeriod(1100l);
        beaconManager.setBackgroundScanPeriod(1100l);
        beaconManager.setBackgroundBetweenScanPeriod(5000l);

        beaconManager.getBeaconParsers().clear();
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));

        beaconManager.setBackgroundScanPeriod(5l);

        // wake up the app when a beacon is seen
        //Region region = new Region("backgroundRegion", null, null, null);

        //Region is defined here as it's own beacon
       user = dataManager.getSavedObjectFromSharedPref(getApplicationContext(), "currentlySignedInUser", new TypeToken<User>(){}.getType());
        if(user != null) {
            if (user.getGroups() != null) {
                for (Group group : user.getGroups()) {
                    Beacon beacon = group.getBeacon();
                    if (beacon.getID() != 0) {
                        Region region = new Region(beacon.getName(), Identifier.parse(beacon.getUUID()), Identifier.parse(beacon.getMajor()), Identifier.parse(beacon.getMinor()));
                        regionList.add(region);
                    }
                }
            }
        }


        // simply constructing this class and holding a reference to it in your custom Application
        // class will automatically cause the BeaconLibrary to save battery whenever the application
        // is not visible.  This reduces bluetooth power usage by about 60%
        backgroundPowerSaver = new BackgroundPowerSaver(this);


        // If you wish to test beacon detection in the Android Emulator, you can use code like this:
        // BeaconManager.setBeaconSimulator(new TimedBeaconSimulator() );
        // ((TimedBeaconSimulator) BeaconManager.getBeaconSimulator()).createTimedSimulatedBeacons();
    }

    public void startMonitor(Region region) throws RemoteException {
        beaconManager.startMonitoringBeaconsInRegion(region);
    }

    @Override
    public void onBeaconServiceConnect() {
        try {

            for(Region region : regionList){
                new RegionBootstrap(this, region);
                startMonitor(region);
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void didEnterRegion(Region region) {
        // In this example, this class sends a notification to the user whenever a Beacon
        // matching a Region (defined above) are first seen.
        if (!haveDetectedBeaconsSinceBoot) {

            // The very first time since boot that we detect an beacon, we launch the
            // MainActivity
            //Intent intent = new Intent(this, MonitoringActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Important:  make sure to add android:launchMode="singleInstance" in the manifest
            // to keep multiple copies of this activity from getting created if the user has
            // already manually launched the app.
            //this.startActivity(intent);

            sendNotification("New beacon: " + region.getUniqueId() + ", not active activity", R.mipmap.ic_enter_icon);

            haveDetectedBeaconsSinceBoot = true;
        } else {
            if (monitoringActivity != null) {
                // If the Monitoring Activity is visible, we log info about the beacons we have
                // seen on its display
                monitoringActivity.logToDisplay("I see a beacon again, " + region.getId1() );
                sendNotification(region.getUniqueId() + " you have no entered a group region!", R.mipmap.ic_enter_icon);

            } else {
                // If we have already seen beacons before, but the monitoring activity is not in
                // the foreground, we send a notification to the user on subsequent detections.
                sendNotification(region.getUniqueId() + " you have no entered a group region!", R.mipmap.ic_enter_icon);
            }
        }

        user = dataManager.getSavedObjectFromSharedPref(getApplicationContext(), "currentlySignedInUser", new TypeToken<User>(){}.getType());
        for (Group group : user.getGroups()) {
            if (group.getBeacon().getUUID().equals(region.getId1().toString()) && group.getBeacon().getMajor().equals(region.getId2().toString()) && group.getBeacon().getMinor().equals(region.getId3().toString())){
                eventQueries.createEvent(group.getId(), "Enter", getApplicationContext());
            }
        }


    }

    @Override
    public void didExitRegion(Region region) {
        if (monitoringActivity != null) {
            monitoringActivity.logToDisplay("I no longer see a beacon. " + region.getId1());
        }
        for (Group group : user.getGroups()) {
            if (group.getBeacon().getUUID().equals(region.getId1().toString()) && group.getBeacon().getMajor().equals(region.getId2().toString()) && group.getBeacon().getMinor().equals(region.getId3().toString())){
                eventQueries.createEvent(group.getId(), "Exit", getApplicationContext());
            }
        }
        sendNotification(region.getUniqueId() + " you have now exited a group region!", R.mipmap.ic_exit_icon);
    }

    @Override
    public void didDetermineStateForRegion(int state, Region region) {
        if (monitoringActivity != null) {
            monitoringActivity.logToDisplay("I have just switched from seeing/not seeing beacons: " + state);
        }


    }

    private void sendNotification(String notifString, int icon) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("Beacon Reference Test")
                        .setContentText(notifString)
                        .setSmallIcon(icon);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(new Intent(this, MonitoringActivity.class));
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    public void setMonitoringActivity(MonitoringActivity activity) {
        this.monitoringActivity = activity;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBackgroundBeaconEvent(BackgroundBeaconEvent backgroundBeaconEvent) {
        Region region = new Region(backgroundBeaconEvent.getBeacon().getName(), Identifier.parse(backgroundBeaconEvent.getBeacon().getUUID()), Identifier.parse(backgroundBeaconEvent.getBeacon().getMajor()), Identifier.parse(backgroundBeaconEvent.getBeacon().getMinor()));
        regionList.add(region);
        onBeaconServiceConnect();
    }

}