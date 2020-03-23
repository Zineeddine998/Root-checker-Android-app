package com.root.rootcheck;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class AboutRootFragment extends Fragment {
    private static final String ARG_TEXT = "text";
    private Tracker mTracker;
    InterstitialAd mInterstitialAd;

    public AboutRootFragment() {
    }

    public static AboutRootFragment newInstance(String text) {
        AboutRootFragment fragment = new AboutRootFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TEXT, text);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.about_root, container, false);

        /*Google Analytics: send screen Name*/

        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getTracker(AnalyticsApplication.TrackerName.APP_TRACKER);

        // Send a screen view.
        mTracker.setScreenName("About Root");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.ScreenViewBuilder()
                .set("AboutRoot", "About Root")
                .build());

        //Log.e("Ads Status", String.valueOf(Constants.isAdsDisabled));

        if (!Constants.isAdsDisabled) {
            AdView mAdView = (AdView) view.findViewById(R.id.adView3);

            AdRequest adRequest = new AdRequest.Builder().addTestDevice("ABB5948E67DE2D6336B525992B921143").build();

            mAdView.loadAd(adRequest);

            mInterstitialAd = new InterstitialAd(this.getActivity());
            mInterstitialAd.setAdUnitId("ca-app-pub-6553884734105254/1263909052");

            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    if (mInterstitialAd.isLoaded()) {
                        Log.d("loaded", "showing");
                        mInterstitialAd.show();
                    }
                }

                @Override
                public void onAdClosed() {
                    //requestNewInterstitial();
                }
            });

            requestNewInterstitial();
        }
        return view;
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("5EA6D09608ABE83531DB117120200FEE")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }
}

