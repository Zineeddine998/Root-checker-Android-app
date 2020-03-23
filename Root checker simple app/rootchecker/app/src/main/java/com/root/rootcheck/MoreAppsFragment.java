package com.root.rootcheck;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import butterknife.Bind;

public class MoreAppsFragment extends Fragment {
    private static final String ARG_TEXT = "text";
    private Tracker mTracker;
    InterstitialAd mInterstitialAd;

    public MoreAppsFragment() {
    }

    public static MoreAppsFragment newInstance(String text) {
        MoreAppsFragment fragment = new MoreAppsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TEXT, text);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.more_apps, container, false);

        /*Google Analytics: send screen Name*/

        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getTracker(AnalyticsApplication.TrackerName.APP_TRACKER);

        // Send a screen view.
        mTracker.setScreenName("More Apps");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.ScreenViewBuilder()
                .set("MoreApps", "More Apps")
                .build());

        CardView cpu_info = (CardView) view.findViewById(R.id.cpu_info_card);

        cpu_info.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                i.setData(Uri.parse("market://details?id=cpu.device.cpuinfo"));
                startActivity(i);

            }
        });

        CardView sim_info = (CardView) view.findViewById(R.id.sim_info_card);

        sim_info.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                i.setData(Uri.parse("market://details?id=phone.sim.siminfo"));
                startActivity(i);

            }
        });

        CardView music_player = (CardView) view.findViewById(R.id.music_player);

        music_player.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                i.setData(Uri.parse("market://details?id=com.reyansh.audio.audioplayer.free"));
                startActivity(i);

            }
        });

        //Log.e("Ads Status", String.valueOf(Constants.isAdsDisabled));
/*
        if (!Constants.isAdsDisabled) {
            AdView mAdView = (AdView) view.findViewById(R.id.adView3);

            AdRequest adRequest = new AdRequest.Builder().addTestDevice("ABB5948E67DE2D6336B525992B921143").build();

            mAdView.loadAd(adRequest);

            /*mInterstitialAd = new InterstitialAd(this.getActivity());
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
        }*/
        return view;
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("5EA6D09608ABE83531DB117120200FEE")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }
}

