package com.root.rootcheck;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;

import com.root.rootcheck.Constants;
import com.root.rootcheck.util.IabHelper;
import com.root.rootcheck.util.IabResult;
import com.root.rootcheck.util.Inventory;
import com.root.rootcheck.util.Purchase;
import com.root.rootcheck.utils.MyLog;

public class MyBilling {
    // Debug tag, for logging
    static final String TAG = "GatePuzzle";

    static final String SKU_REMOVE_ADS = "com.root.rootcheck";

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10111;

    Activity activity;

    // The helper object
    IabHelper mHelper;

    String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlaqGaElGfzJ8YvcM23/DCfoVRMU/RxhjFEGwNkcRv7rJUvk4aov/+mLtGxE/YV/wxjrZStkAzbOS8UhbwiNPtujwY493fRnbPKDuprn8DtbrN4bZnUM4+EgWIxh4XRNX3csdiosjvLeFFX1Y6zd647FS9ITd8dQ8k0Z0NzLzDTogQ7J5v18yf2yYjWBeIq7gBu93EIYKzDEpPrz1iyxoC8khd2ElXCCKAN0AC5QWGqMDQsvcM+ibFyoZPAEoeKe1rEZVI4dcrS1L7Ck3925xSeM6OyJivh+JqzEuS6TJ4lnK2ZivkSs18gx1TecGqvZfNLz8yGWUzSN8FH/bHra5bwIDAQAB";;
    Boolean isAdsDisabled = false;
    String payload = "ANY_PAYLOAD_STRING";

    public MyBilling(Activity launcher) {
        this.activity = launcher;
    }

    public void onCreate() {

        // Create the helper, passing it our context and the public key to
        // verify signatures with
        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(activity, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set
        // this to false).
        mHelper.enableDebugLogging(false);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    // complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed off in the meantime? If so, quit.
                if (mHelper == null)
                    return;

                // IAB is fully set up. Now, let's get an inventory of stuff we
                // own.
                MyLog.e(TAG, "Setup successful. Querying inventory.");
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });
    }

    // Listener that's called when we finish querying the items and
    // subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {
            MyLog.e(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null)
                return;

            // Is it a failure?
            if (result.isFailure()) {
                // complain("Failed to query inventory: " + result);
                return;
            }

            MyLog.e(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // Do we have the premium upgrade?
            Purchase removeAdsPurchase = inventory.getPurchase(SKU_REMOVE_ADS);
            Constants.isAdsDisabled = (removeAdsPurchase != null && verifyDeveloperPayload(removeAdsPurchase));
            if(Constants.isAdsDisabled) {
                removeAds();
            }

            MyLog.e(TAG, "User has "
                    + (Constants.isAdsDisabled ? "REMOVED ADS"
                    : "NOT REMOVED ADS"));

            // setWaitScreen(false);
            MyLog.e(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    // User clicked the "Remove Ads" button.
    public void purchaseRemoveAds() {

        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {

                mHelper.launchPurchaseFlow(activity, SKU_REMOVE_ADS,
                        RC_REQUEST, mPurchaseFinishedListener, payload);

            }
        });
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + ","
                + data);
        if (mHelper == null)
            return true;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            return false;
        } else {

            Log.d(TAG, "onActivityResult handled by IABUtil.");

            return true;
        }

    }

    /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct.
         * It will be the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase
         * and verifying it here might seem like a good approach, but this will
         * fail in the case where the user purchases an item on one device and
         * then uses your app on a different device, because on the other device
         * you will not have access to the random string you originally
         * generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different
         * between them, so that one user's purchase can't be replayed to
         * another user.
         *
         * 2. The payload must be such that you can verify it even when the app
         * wasn't the one who initiated the purchase flow (so that items
         * purchased by the user on one device work on other devices owned by
         * the user).
         *
         * Using your own server to store and verify developer payloads across
         * app installations is recommended.
         */
        return true;
    }

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: "
                    + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null)
                return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                return;
            }

            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(SKU_REMOVE_ADS)) {
                // bought the premium upgrade!
                removeAds();

            }
        }
    };

    private void removeAds() {
        isAdsDisabled = true;
        Constants.isAdsDisabled = true;
    }

    // We're being destroyed. It's important to dispose of the helper here!

    public void onDestroy() {

        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.dispose();
            mHelper = null;
        }
    }

    void complain(String message) {
        //Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }

    void alert(final String message) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {

                AlertDialog.Builder bld = new AlertDialog.Builder(activity);
                bld.setMessage(message);
                bld.setNeutralButton("OK", null);
                Log.d(TAG, "Showing alert dialog: " + message);
                bld.create().show();
            }
        });
    }

}

