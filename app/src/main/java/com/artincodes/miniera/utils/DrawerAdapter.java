package com.artincodes.miniera.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.artincodes.miniera.R;
import com.artincodes.miniera.fragments.LauncherFragment;

public class DrawerAdapter extends BaseAdapter {

    Context mContext;
    LauncherFragment.Pac[] pacsForAdapter;
    //AlphaAnimation animation1;


    public DrawerAdapter (Context c, LauncherFragment.Pac pacs[]){

        mContext = c;
        pacsForAdapter = pacs;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub

        return pacsForAdapter.length;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    static class ViewHolder{
        TextView text;
        ImageView icon;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub

        ViewHolder viewHolder;
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.drawer_item, null);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView)convertView.findViewById(R.id.icon_text);
            viewHolder.icon = (ImageView)convertView.findViewById(R.id.icon_image);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();

        }

        viewHolder.icon.setImageDrawable(pacsForAdapter[pos].icon);
        viewHolder.text.setText(pacsForAdapter[pos].label);

        //animation1 = new AlphaAnimation(0.0f, 1.0f);
        //animation1.setDuration(500);

        //convertView.startAnimation(animation1);
        convertView.animate().setDuration(200).alphaBy(1);
        return convertView;
    }


}