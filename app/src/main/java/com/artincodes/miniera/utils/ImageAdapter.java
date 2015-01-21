package com.artincodes.miniera.utils;

/**
 * Created by jayadeep on 1/1/15.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.artincodes.miniera.R;


public class ImageAdapter extends BaseAdapter {
    private Context context;
    //private final String[] mobileValues;

    public ImageAdapter(Context context) {
        this.context = context;
        //this.mobileValues = mobileValues;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.dock_item, null);

            // set value into textview

            // set image based on selected text
            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.dock_icon_image);

            //String mobile = mobileValues[position];

            if (position ==2) {
                imageView.setImageResource(R.drawable.ic_apps_white);
            } else {

                imageView.setImageResource(R.drawable.ic_dock_empty);
            }

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
