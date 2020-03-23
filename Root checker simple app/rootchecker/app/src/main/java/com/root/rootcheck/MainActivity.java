package com.root.rootcheck;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.appbrain.AdId;
import com.appbrain.AppBrain;
import com.appbrain.InterstitialBuilder;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;
    MyBilling billing = new MyBilling(this);
    TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.ic_root_info,
            R.drawable.ic_smartphone,
            R.drawable.ic_info
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        AppBrain.init(this);

        billing.onCreate();

        Toolbar toolbar = (Toolbar) findViewById(R.id.tabanim_toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.tabanim_viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabanim_tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();
        setFloating();

    }

    private void setFloating() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionRateUs();
            }
        });
    }
    private void actionRateUs() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + getPackageName()));
        startActivity(intent);
    }

    private void setupTabIcons() {
      //  tabLayout.getTabAt(0).setIcon(tabIcons[0]);
      //  tabLayout.getTabAt(1).setIcon(tabIcons[1]);
      //  tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new RootInfoFragment(), "Root Info");
        adapter.addFrag(new BuildInfoFragment(), "Build Info");
        adapter.addFrag(new AboutRootFragment(), "About Root");
        adapter.addFrag(new MoreAppsFragment(), "More Apps");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
            InterstitialBuilder.create().setAdId(AdId.EXIT).setFinishOnExit(this).show(this);
            return true;
        }

        if (id == R.id.action_more_apps) {
            InterstitialBuilder.create().show(this);
            return true;
        }
        if (id == R.id.action_more) {
            InterstitialBuilder.create().show(this);
            return true;
        }
        if (id == R.id.action_donate) {

            billing.purchaseRemoveAds();

            return true;
        }

        if (id == R.id.action_remove_ads) {

            Intent i = new Intent(android.content.Intent.ACTION_VIEW);
            i.setData(Uri.parse("market://details?id=com.root.rootcheckpro"));
            startActivity(i);

            return true;
        }

        if (id == R.id.action_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = getString(R.string.share_text);
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0
                || !InterstitialBuilder.create().setAdId(AdId.EXIT).setFinishOnExit(this).show(this)) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        billing.onActivityResult(requestCode, resultCode, data);

        if (!Constants.isAdsDisabled) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                getActionBar().setTitle("Root Checker Premium");
                getSupportActionBar().setTitle("Root Checker Premium");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        billing.onDestroy();
    }
}
