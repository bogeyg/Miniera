package com.artincodes.miniera.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.artincodes.miniera.MainActivity;
import com.artincodes.miniera.R;
import com.woodblockwithoutco.remotemetadataprovider.media.RemoteMetadataProvider;
import com.woodblockwithoutco.remotemetadataprovider.media.enums.MediaCommand;
import com.woodblockwithoutco.remotemetadataprovider.media.listeners.OnArtworkChangeListener;
import com.woodblockwithoutco.remotemetadataprovider.media.listeners.OnMetadataChangeListener;

public  class MusicFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private static final String NO_CLIENT = "Client state: NO CLIENT";
    protected static final String CLIENT_ACTIVE = "Client state: ACTIVE";
    private TextView textTitle;
    private TextView textAlbumTitle;
    private TextView textArtist;
    //private TextView mAlbumTextView;
    private ImageView buttonPlay;
    private ImageView buttonPrevious;
    private ImageView buttonNext;

    private ImageView imageAlbumArt;
    private RemoteMetadataProvider mProvider;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MusicFragment newInstance(int sectionNumber) {
        MusicFragment fragment = new MusicFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MusicFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_music, container, false);
        textTitle=(TextView)rootView.findViewById(R.id.textTitle);
        textAlbumTitle = (TextView)rootView.findViewById(R.id.textAlbum);
        textArtist = (TextView)rootView.findViewById(R.id.textArtist);
//        textAlbumTitle.setShadowLayer(3, 0, 2, Color.BLACK);
//        textArtist.setShadowLayer(3, 0, 2, Color.BLACK);
        //textAlbumTitle.setTypeface(MyActivity.robotoCond);
       // textSubTitles.setTypeface(MyActivity.robotoCond);
        imageAlbumArt = (ImageView)rootView.findViewById(R.id.imageArtWork);
        buttonPlay = (ImageView)rootView.findViewById(R.id.buttonPlay);
        buttonPrevious = (ImageView)rootView.findViewById(R.id.buttonPrev);
        buttonNext = (ImageView)rootView.findViewById(R.id.buttonNext);

        mProvider = RemoteMetadataProvider.getInstance(getActivity());


        mProvider.setOnMetadataChangeListener(new OnMetadataChangeListener() {
            @Override
            public void onMetadataChanged(String artist, String title,
                                          String album, String albumArtist, long duration) {
                //mArtistTextView.setText("ARTIST: "+artist);
                textTitle.setText(title);
                textAlbumTitle.setText(album);
                textArtist.setText(artist);
                //mAlbumTextView.setText("ALBUM: "+album);
                //mAlbumArtistTextView.setText("ALBUM ARTIST: "+albumArtist);
                //mDurationTextView.setText("DURATION: "+(duration/1000)+"s");
            }
        });

        mProvider.setOnArtworkChangeListener(new OnArtworkChangeListener() {
            @Override
            public void onArtworkChanged(Bitmap artwork) {
                imageAlbumArt.setImageBitmap(artwork);
            }
        });

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mProvider.sendMediaCommand(MediaCommand.PLAY_PAUSE)) {
                    Toast.makeText(getActivity().getApplicationContext(), "Failed to send PLAY_PAUSE_EVENT", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mProvider.sendMediaCommand(MediaCommand.PREVIOUS)) {
                    Toast.makeText(getActivity().getApplicationContext(), "Failed to send PREVIOUS_EVENT", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mProvider.sendMediaCommand(MediaCommand.NEXT)) {
                    Toast.makeText(getActivity().getApplicationContext(), "Failed to send NEXT_EVENT", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onResume() {
        super.onResume();

        //acquiring remote media controls
        mProvider.acquireRemoteControls();
    }

    @Override
    public void onPause() {
        super.onPause();

        //dropping remote media controls
        mProvider.dropRemoteControls(true);
    }

}