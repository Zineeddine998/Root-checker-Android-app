package com.root.rootcheck;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.stericson.RootTools.RootTools;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rishabh on 31/12/15.
 */
public class RootInfoFragment extends Fragment {
    @Bind(R.id.is_root)
    TextView root_text;
    @Bind(R.id.is_root_available)
    TextView is_root;
    @Bind(R.id.super_user_text)
    TextView su_text;
    @Bind(R.id.is_su_found)
    TextView is_su_found;
    @Bind(R.id.su_path)
    TextView su_path;
    @Bind(R.id.is_busy_box_installed)
    TextView is_busy_box_installed;
    @Bind(R.id.path)
    TextView path;
    private static final String ARG_TEXT = "text";
    private Tracker mTracker;

    InterstitialAd mInterstitialAd;

    public RootInfoFragment() {
    }

    public static RootInfoFragment newInstance(String text) {
        RootInfoFragment fragment = new RootInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TEXT, text);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.root_info, container, false);

        /*Google Analytics: send screen Name*/

        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getTracker(AnalyticsApplication.TrackerName.APP_TRACKER);

        // Send a screen view.
        mTracker.setScreenName("Root Info");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.ScreenViewBuilder()
                .set("RootInfo", "Root Info")
                .build());

        Log.d("on detail create view", "created");
        ButterKnife.bind(this, view);
        //GENERAL
        root_text.setText("ROOT ACCESS");
        su_text.setText("SUPER USER");

        //ROOT
        if (RootTools.isRootAvailable()) {
            is_root.setText("Device is rooted");
            is_root.setTextColor(this.getResources().getColor(R.color.green));
        } else {
            is_root.setText("Device is not rooted");
            is_root.setTextColor(this.getResources().getColor(R.color.dark_red));
        }
        //SU
        String[] paths = {"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su"};
        String p = "";
        Boolean found = false;
        for (String path : paths) {
            if (new File(path).exists()) {
                found = true;
                p += path;
            }
        }
        if (found) {
            is_su_found.setText("SU FOUND");
            is_su_found.setTextColor(this.getResources().getColor(R.color.green));
            su_path.setText(p);
        } else {
            is_su_found.setText("SU NOT FOUND");
            is_su_found.setTextColor(this.getResources().getColor(R.color.dark_red));
        }

        //Busy Box

        //ROOT
        if (RootTools.isBusyboxAvailable()) {
            is_busy_box_installed.setText("Busy Box is installed");
            is_busy_box_installed.setTextColor(this.getResources().getColor(R.color.green));
        } else {
            is_busy_box_installed.setText("Busy Box is not installed");
            is_busy_box_installed.setTextColor(this.getResources().getColor(R.color.dark_red));
        }
        //PATH
        //is_su_found.setText("SU FOUND");
        path.setText(String.valueOf(RootTools.getPath()));

        //Log.e("Ads Status", String.valueOf(Constants.isAdsDisabled));

        AdView mAdView = (AdView) view.findViewById(R.id.adView1);


        if (!Constants.isAdsDisabled) {

            mAdView.setVisibility(View.VISIBLE);

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

        CardView buttonOne = (CardView) view.findViewById(R.id.btn_remove_ads1);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                i.setData(Uri.parse("market://details?id=com.root.rootcheckpro"));
                startActivity(i);

            }
        });

        CardView simCard = (CardView) view.findViewById(R.id.card_info);
        simCard.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                i.setData(Uri.parse("market://details?id=com.simcard.siminfo"));
                startActivity(i);

            }
        });
        CardView deviceCard = (CardView) view.findViewById(R.id.device_info);
        deviceCard.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                i.setData(Uri.parse("market://details?id=device.deviceinfo"));
                startActivity(i);

            }
        });

        return view;
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("5EA6D09608ABE83531DB117120200FEE")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }
}

