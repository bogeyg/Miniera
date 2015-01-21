package com.artincodes.miniera.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.artincodes.miniera.MainActivity;
import com.artincodes.miniera.R;

import java.util.Arrays;
import java.util.List;

public  class SocialFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    TextView textView;
    private static final String ARG_SECTION_NUMBER = "section_number";

    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SocialFragment newInstance(int sectionNumber) {
        SocialFragment fragment = new SocialFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public SocialFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_social, container, false);
        textView = (TextView)rootView.findViewById(R.id.textView);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                textView.setText("Hello "+user[0].getName());


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




}