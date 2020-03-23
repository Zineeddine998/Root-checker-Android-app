package com.root.rootcheck;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

import android.app.Application;

import java.util.HashMap;

/**
 * Created by jiteshdugar on 29/12/15.
 */
public class AnalyticsApplication extends Application {
    private static final String PROPERTY_ID = "UA-71837612-4";
    public enum TrackerName {
        APP_TRACKER,
        GLOBAL_TRACKER,
        E_COMMERCE_TRACKER,
    }
   /* @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }*/
    /**
     * Enum used to identify the tracker that needs to be used for tracking.
     * <p/>
     * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
     * storing them all in Application object helps ensure that they are created only once per
     * application instance.
     */
    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
            Tracker t = analytics.newTracker(PROPERTY_ID);
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }
}
