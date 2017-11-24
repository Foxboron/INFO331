package no.uib.info331.util;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.RegionBootstrap;
import org.altbeacon.beacon.startup.BootstrapNotifier;

import no.uib.info331.R;
import no.uib.info331.activities.MonitoringActivity;

/**
 * Created by dyoung on 12/13/13.
 */
public class BeaconReferenceApplication extends Application implements BootstrapNotifier {
    private static final String TAG = "BeaconReferenceApp";
    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private boolean haveDetectedBeaconsSinceBoot = false;
    private MonitoringActivity monitoringActivity = null;
    private BeaconManager beaconManager;


    public void onCreate() {
        super.onCreate();
        beaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);

        // By default the AndroidBeaconLibrary will only find AltBeacons.  If you wish to make it
        // find a different type of beacon, you must specify the byte layout for that beacon's
        // advertisement with a line like below.  The example shows how to find a beacon with the
        // same byte layout as AltBeacon but with a beaconTypeCode of 0xaabb.  To find the proper
        // layout expression for other beacon types, do a web search for "setBeaconLayout"
        // including the quotes.
        //
        beaconManager.setForegroundScanPeriod(1100l);
        beaconManager.setBackgroundScanPeriod(1100l);
        beaconManager.setBackgroundBetweenScanPeriod(5000l);

        beaconManager.getBeaconParsers().clear();
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));

        beaconManager.setBackgroundScanPeriod(5l);
        Log.d(TAG, "setting up background monitoring for beacons and power saving");

        // wake up the app when a beacon is seen
        Region region = new Region("backgroundRegion",
                null, null, null);


        regionBootstrap = new RegionBootstrap(this, region);

        // simply constructing this class and holding a reference to it in your custom Application
        // class will automatically cause the BeaconLibrary to save battery whenever the application
        // is not visible.  This reduces bluetooth power usage by about 60%
        backgroundPowerSaver = new BackgroundPowerSaver(this);


        // If you wish to test beacon detection in the Android Emulator, you can use code like this:
        // BeaconManager.setBeaconSimulator(new TimedBeaconSimulator() );
        // ((TimedBeaconSimulator) BeaconManager.getBeaconSimulator()).createTimedSimulatedBeacons();
    }

            @Override
            public void didEnterRegion(Region arg0) {
                // In this example, this class sends a notification to the user whenever a Beacon
                // matching a Region (defined above) are first seen.
                Log.d(TAG, "did enter region.");
                if (!haveDetectedBeaconsSinceBoot) {
                    Log.d(TAG, "auto launching MainActivity");

                    // The very first time since boot that we detect an beacon, we launch the
                    // MainActivity
                    //Intent intent = new Intent(this, MonitoringActivity.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // Important:  make sure to add android:launchMode="singleInstance" in the manifest
                    // to keep multiple copies of this activity from getting created if the user has
                    // already manually launched the app.
                    //this.startActivity(intent);

                    sendNotification("New beacon, not active activity");

                    haveDetectedBeaconsSinceBoot = true;
                } else {
                    if (monitoringActivity != null) {
                        // If the Monitoring Activity is visible, we log info about the beacons we have
                        // seen on its display
                        monitoringActivity.logToDisplay("I see a beacon again" );
                    } else {
                        // If we have already seen beacons before, but the monitoring activity is not in
                        // the foreground, we send a notification to the user on subsequent detections.
                        Log.d(TAG, "Sending notification.");
                        sendNotification("Seen beacon, not active activity");
                    }
                }


            }

            @Override
            public void didExitRegion(Region region) {
                if (monitoringActivity != null) {
                    monitoringActivity.logToDisplay("I no longer see a beacon.");
                }
                sendNotification("Exit region");
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                if (monitoringActivity != null) {
                    monitoringActivity.logToDisplay("I have just switched from seeing/not seeing beacons: " + state);
                }
                sendNotification("I have just switched from seeing/not seeing beacons: " + state);


            }

            private void sendNotification(String notifString) {
                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(this)
                                .setContentTitle("Beacon Reference Test")
                                .setContentText(notifString)
                                .setSmallIcon(R.mipmap.ic_icon);

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

        }