package com.artincodes.miniera;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.*;
import android.widget.*;
import com.artincodes.miniera.fragments.*;
import com.artincodes.miniera.utils.ImageAdapter;
import com.facebook.AppEventsLogger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private GoogleApiClient mGoogleApiClient;

    PackageManager packageManager;

    public ImageView dropIcon;

    BroadcastReceiver _broadcastReceiver;
    private final SimpleDateFormat _sdfWatchTime = new SimpleDateFormat("h:mm,EEEEEEEEE,d MMMMMM", Locale.US);
    private CharSequence mTitle;
    TextView textTime;
    TextView textDay;
    TextView textDate;
    ImageView wallpaperImage;
    FragmentManager fragmentManager;
    FrameLayout container;
    public static GridView dockGrid;
    public static ImageView dragIcon;
    public static String[] launchIntentStr = {
            null,
            null,
            null,
            null,
            null
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

//        Toast.makeText(getApplicationContext(), Build.VERSION.SDK_INT+"",Toast.LENGTH_SHORT).show();

        final ActionBar actionBar = getSupportActionBar();
        dragIcon = (ImageView) findViewById(R.id.dragIcon);
        dockGrid = (GridView) findViewById(R.id.app_drawer);
        dockGrid.setAdapter(new ImageAdapter(getBaseContext()));
        dockGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //Toast.makeText(getBaseContext(),launchIntentStr[position],Toast.LENGTH_SHORT).show();
                        launchApp(launchIntentStr[position]);

                        break;
                    case 1:
                        //Toast.makeText(getBaseContext(),"App 2",Toast.LENGTH_SHORT).show();
                        launchApp(launchIntentStr[position]);
                        break;
                    case 2:

                        fragmentManager = getFragmentManager();

                        if (fragmentManager.getBackStackEntryCount() == 0) {
                            //Toast.makeText(getBaseContext(),fragmentManager.getBackStackEntryCount()+"",Toast.LENGTH_SHORT).show();
                            //Toast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_SHORT).show();


                            fragmentManager.beginTransaction()
                                    .replace(R.id.container, LauncherFragment.newInstance(position + 1))
                                    .addToBackStack(null)
                                    .commit();
                            actionBar.hide();
                            dockGrid.setVisibility(View.GONE);

                        }
                        break;
                    case 3:
                        //Toast.makeText(getBaseContext(),"App 3",Toast.LENGTH_SHORT).show();
                        launchApp(launchIntentStr[position]);

                        break;
                    case 4:
                        //Toast.makeText(getBaseContext(),"App 4",Toast.LENGTH_SHORT).show();
                        launchApp(launchIntentStr[position]);

                        break;

                }
            }
        });

        packageManager = getPackageManager();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();

        dropIcon = new ImageView(this);

        wallpaperImage = (ImageView) findViewById(R.id.wallpaper_view);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);

        View customNav = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_toolbar, null); // layout which contains your button.

        //*******ActionView actionView = (ActionView) customNav.findViewById(R.id.action);
        textTime = (TextView) customNav.findViewById(R.id.text_time);
        textDay = (TextView) customNav.findViewById(R.id.text_day);
        textDate = (TextView) customNav.findViewById(R.id.text_date);

        actionBar.setCustomView(customNav, lp1);

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;

        //final Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_wallpaper, options);

        wallpaperImage.setImageDrawable(getWallpaper());

        //wallpaperImage.setImageBitmap(bmp);

        //buttonLauncher = (ImageView) findViewById(R.id.button_launcher);

        container = (FrameLayout) findViewById(R.id.container);


    }

    private void launchApp(String currentLaunchIntent) {

        if (currentLaunchIntent != null) {
            Intent launchIntent = packageManager.getLaunchIntentForPackage(currentLaunchIntent);
            getApplicationContext().startActivity(launchIntent);

        } else {
            Toast.makeText(getBaseContext(), "Room Empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void setDateTime() {

        String dateTime = _sdfWatchTime.format(new Date());
        String[] parts = dateTime.split(",");
        textTime.setText(parts[0]);
        textDate.setText(parts[2]);
        textDay.setText(parts[1]);

    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        switch (position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, NowFragment.newInstance(position + 1))
                        .commit();
                break;
            case 2:
                if (Build.VERSION.SDK_INT < 18)
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, MusicFragment.newInstance(position + 1))
                            .commit();
                else
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, MusicFragmentv18.newInstance(position + 1))
                            .commit();
                break;
            case 3:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, SocialFragment.newInstance(position + 1))
                        .commit();
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {


        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
            //getFragmentManager().popBackStackImmediate();
        } else {
            getFragmentManager().popBackStack();
            getSupportActionBar().show();
            dockGrid.setVisibility(View.VISIBLE);
//            floatingActionsMenu.setVisibility(View.VISIBLE);

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        //nowFragment = new NowFragment();
        //mGoogleApiClient.connect();

        setDateTime();

        _broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context ctx, Intent intent) {
                if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
                    setDateTime();

                }
            }
        };

        registerReceiver(_broadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));

    }

    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mGoogleApiClient.disconnect();
        super.onStop();
        unregisterReceiver(_broadcastReceiver);
    }


    @Override
    public void onConnected(Bundle bundle) {

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        mLocationRequest.setInterval(900000); // Update location 15 minutes

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Toast.makeText(getBaseContext(), "Could not Connect to Play Services, Please Update", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLocationChanged(Location location) {

        NowFragment fragment = (NowFragment) getFragmentManager().findFragmentById(R.id.container);
        fragment.setWeather(location);

    }


}
