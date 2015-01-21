package com.artincodes.miniera.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.artincodes.miniera.MainActivity;
import com.artincodes.miniera.R;
import com.artincodes.miniera.utils.DrawerAdapter;
import com.artincodes.miniera.utils.DrawerClickListener;
import com.artincodes.miniera.utils.DrawerLongClickListener;
import com.artincodes.miniera.utils.SortApps;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

import java.util.List;

public  class LauncherFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    DrawerAdapter drawerAdapterObject;
    GridView drawerGrid;

    public static boolean appLaunchable = true;

    public class Pac {
        public Drawable icon;
        public String name;
        public String label;
    }

    Pac[] pacs;
    PackageManager packageManager;
    CircularProgressBar circularProgressBar;

    MainActivity mainActivity = new MainActivity();

    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static LauncherFragment newInstance(int sectionNumber) {
        LauncherFragment fragment = new LauncherFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public LauncherFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_launcher, container, false);


        drawerGrid = (GridView) rootView.findViewById(R.id.content);
        circularProgressBar= (CircularProgressBar)rootView.findViewById(R.id.progress_view);

        new LoadApplicationTask().execute();

        //mainActivity.dropIcon.setMaxWidth(50);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    public void set_pacs() {

        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pacsList = packageManager.queryIntentActivities(mainIntent, 0);
        pacs = new Pac[pacsList.size()];

        for (int i = 0; i < pacsList.size(); i++) {

            pacs[i] = new Pac();
            pacs[i].icon = pacsList.get(i).loadIcon(packageManager);
            pacs[i].name = pacsList.get(i).activityInfo.packageName;
            pacs[i].label = pacsList.get(i).loadLabel(packageManager).toString();

        }
        new SortApps().exchange_sort(pacs);

    }



    public class PacReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            set_pacs();
        }

    }

    public class LoadApplicationTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            packageManager = getActivity().getPackageManager();
            set_pacs();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            drawerAdapterObject = new DrawerAdapter(getActivity(), pacs);
            drawerGrid.setAdapter(drawerAdapterObject);
            drawerGrid.setOnItemClickListener(new DrawerClickListener(getActivity(), pacs, packageManager));
            drawerGrid.setOnItemLongClickListener(new DrawerLongClickListener(getActivity(), pacs, packageManager));

            circularProgressBar.setVisibility(View.INVISIBLE);

            super.onPostExecute(result);
        }






    }
}