package com.artincodes.miniera.utils;

/**
 * Created by jayadeep on 24/12/14.
 */

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.artincodes.miniera.fragments.LauncherFragment;

public class DrawerClickListener implements OnItemClickListener {

    Context mContext;
    LauncherFragment.Pac[] pacsForAdapter;
    PackageManager pmForListener;

    public DrawerClickListener(Context c, LauncherFragment.Pac[] pacs, PackageManager pm) {
        mContext = c;
        pacsForAdapter = pacs;
        pmForListener = pm;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
        // TODO Auto-generated method stub
        if (LauncherFragment.appLaunchable) {
            Intent launchIntent = pmForListener.getLaunchIntentForPackage(pacsForAdapter[pos].name);
            mContext.startActivity(launchIntent);
        }


    }


}