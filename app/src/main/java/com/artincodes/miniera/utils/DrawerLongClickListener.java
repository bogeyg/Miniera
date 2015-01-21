package com.artincodes.miniera.utils;

/**
 * Created by jayadeep on 24/12/14.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemLongClickListener;
import com.artincodes.miniera.MainActivity;
import com.artincodes.miniera.R;
import com.artincodes.miniera.fragments.LauncherFragment;
import me.drakeet.materialdialog.MaterialDialog;

public class DrawerLongClickListener implements OnItemLongClickListener {

    Context mContext;
    LauncherFragment.Pac[] pacsForAdapter;
    PackageManager pmForListener;
    MaterialDialog mMaterialDialog = null;
    View convertView;
    View moreOptionView;
    ImageView appIcon;
    TextView appName;
    ImageView moreButton;
    Vibrator mVibrator;
    MaterialDialog moreDialog;

    String[] optionTitles = {
            "Uninstall",
            "App info",
            "View in Playstore",
    };

    Integer[] optionIcons = {
            R.drawable.ic_delete,
            R.drawable.ic_info,
            R.drawable.ic_play_store,
    };


    ListAdapter listAdapterObject;
    ListView listViewOptions;

    TextView room1;
    TextView room2;
    TextView room3;
    TextView room4;

    boolean roomSelected = false;
    int selectedRoom;


    public DrawerLongClickListener(Context c, LauncherFragment.Pac[] pacs, PackageManager pm) {
        mContext = c;
        pacsForAdapter = pacs;
        pmForListener = pm;
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {


        LauncherFragment.appLaunchable = false;
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.dialog_content, null);

        moreOptionView = layoutInflater.inflate(R.layout.more_dialog_content, null);


        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        mVibrator.vibrate(50);


        appIcon = (ImageView) convertView.findViewById(R.id.app_icon);
        appName = (TextView) convertView.findViewById(R.id.text_app_name);
        moreButton = (ImageView) convertView.findViewById(R.id.more_icon);
        mMaterialDialog = new MaterialDialog(mContext)
                .setTitle("Add to Home Screen?")
                        //.setMessage("Select room")
                .setNegativeButton("CANCEL", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "Negative", Toast.LENGTH_SHORT).show();
                        mMaterialDialog.dismiss();
                        setRoomSelectedFalse();
                    }
                })
                .setCanceledOnTouchOutside(true)
                .setContentView(convertView)
                .setPositiveButton("CONFIRM", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (roomSelected) {
                            //MainActivity.launcherImageLast.setImageDrawable(pacsForAdapter[position].icon);
                            //MainActivity.launchIntentLast = pacsForAdapter[position].name;
                            //Toast.makeText(mContext, MainActivity.launchIntentLast, Toast.LENGTH_SHORT).show();
                            setRoomSelectedFalse();

                            MainActivity.launchIntentStr[selectedRoom]=pacsForAdapter[position].name;
                            View currentView = MainActivity.dockGrid.getChildAt(selectedRoom);
                            ImageView currentIcon = (ImageView) currentView.findViewById(R.id.dock_icon_image);
                            currentIcon.setImageDrawable(pacsForAdapter[position].icon);
                            mMaterialDialog.dismiss();
                        } else
                            Toast.makeText(mContext, "Select Room for adding app", Toast.LENGTH_SHORT).show();
                    }
                });
        room1 = (TextView) convertView.findViewById(R.id.room1);
        room2 = (TextView) convertView.findViewById(R.id.room2);
        room3 = (TextView) convertView.findViewById(R.id.room3);
        room4 = (TextView) convertView.findViewById(R.id.room4);

        room1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRoom = 0;
                setRoomSelectedTrue();
                resetLabelBackground();
                v.setBackgroundResource(R.drawable.border_selected);

            }
        });
        room2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRoom = 1;
                setRoomSelectedTrue();
                resetLabelBackground();
                v.setBackgroundResource(R.drawable.border_selected);

            }
        });
        room3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRoom = 3;
                setRoomSelectedTrue();
                resetLabelBackground();
                v.setBackgroundResource(R.drawable.border_selected);
            }
        });
        room4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRoom=4;
                setRoomSelectedTrue();
                resetLabelBackground();
                v.setBackgroundResource(R.drawable.border_selected);
            }
        });

        appIcon.setImageDrawable(pacsForAdapter[position].icon);
        appName.setText(pacsForAdapter[position].label);

        mMaterialDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                LauncherFragment.appLaunchable = true;

            }
        });
        mMaterialDialog.show();

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
                listViewOptions = (ListView) moreOptionView.findViewById(R.id.more_options_list);
                listAdapterObject = new ListAdapter(mContext, optionTitles, optionIcons,"#000000");
                listViewOptions.setAdapter(listAdapterObject);

                moreDialog = new MaterialDialog(mContext)
                        .setTitle("More")
                                //.setMessage("More options")
                        .setContentView(moreOptionView)
                        .setPositiveButton("CANCEL", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                moreDialog.dismiss();
                            }
                        });

                listViewOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                        switch (pos) {
                            case 0:
                                //Toast.makeText(mContext,"Uninstall "+pacsForAdapter[position].label,Toast.LENGTH_SHORT).show();
                                Uri packageUri = Uri.parse("package:" + pacsForAdapter[position].name);
                                Intent uninstallIntent =
                                        new Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageUri);
                                mContext.startActivity(uninstallIntent);
                                moreDialog.dismiss();

                                break;
                            case 1:
                                //Toast.makeText(mContext,"Info "+pacsForAdapter[position].label,Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + pacsForAdapter[position].name));
                                mContext.startActivity(intent);
                                moreDialog.dismiss();

                                break;
                            case 2:
                                //Toast.makeText(mContext,"Playstore "+pacsForAdapter[position].label,Toast.LENGTH_SHORT).show();
                                mContext.startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("market://details?id=" + pacsForAdapter[position].name)));

                                moreDialog.dismiss();
                                break;
                            default:
                                break;

                        }

                    }
                });
                moreDialog.show();

            }
        });

        return false;
    }

    private void resetLabelBackground() {
        room1.setBackgroundResource(R.drawable.border);
        room2.setBackgroundResource(R.drawable.border);
        room3.setBackgroundResource(R.drawable.border);
        room4.setBackgroundResource(R.drawable.border);
    }

    private void setRoomSelectedTrue() {

        roomSelected = true;

    }

    private void setRoomSelectedFalse() {

        roomSelected = false;
    }
}