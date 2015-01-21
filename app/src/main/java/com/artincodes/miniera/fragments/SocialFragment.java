package com.artincodes.miniera.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.artincodes.miniera.MainActivity;
import com.artincodes.miniera.R;
import com.facebook.*;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import java.util.Arrays;
import java.util.List;

public  class SocialFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    TextView textView;
    LoginButton authButton;
    UiLifecycleHelper uiHelper;
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
        authButton = (LoginButton) rootView.findViewById(R.id.authButton);
        uiHelper = new UiLifecycleHelper(getActivity(),statusCallback);
        uiHelper.onCreate(savedInstanceState);
        final GraphUser[] user = new GraphUser[1];

        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Button","Button Clicked");
                //requestPermissions();
            }
        });

        authButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser graphUser) {
                if (graphUser !=null){
//                    Toast.makeText(getActivity(),"Hello "+graphUser.getName(),Toast.LENGTH_SHORT)
//                            .show();
                    user[0] = graphUser;
                    textView.setText("Hello "+graphUser.getName());

                }
            }
        });

        authButton.setSessionStatusCallback(new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState sessionState, Exception e) {
                if (session.isOpened()) {
                    requestPermissions();
                    Log.i("FACEBOOK ON CALL","Access Token"+ session.getAccessToken());
                    Request.executeMeRequestAsync(session,
                            new Request.GraphUserCallback() {
                                @Override
                                public void onCompleted(GraphUser user, Response response) {
                                    if (user != null) {
                                        Log.e("FACEBOOK ON CALL", "User ID " + user.getId());
                                        Log.e("FACEBOOK ON CALL", "Email " + user.asMap().get("email"));
                                        textView.setText(user.asMap().get("email").toString());
                                    }
                                }
                            });
                }

            }
        });

        authButton.setOnErrorListener(new LoginButton.OnErrorListener() {
            @Override
            public void onError(FacebookException e) {

                Log.e("FB ERROR", "Error " + e.getMessage());

            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                textView.setText("Hello "+user[0].getName());


            }
        });

        return rootView;
    }

    private Session.StatusCallback statusCallback = new Session.StatusCallback(){

        @Override
        public void call(Session session, SessionState sessionState, Exception e) {
            if (sessionState.isOpened()){

                Log.e("CALLING","session opened");


            }else if (sessionState.isClosed()){

                Log.e("CALLING","session closed");

            }
        }
    };

    public void requestPermissions() {
        Session s = Session.getActiveSession();
        if (s != null)
            s.requestNewPublishPermissions(new Session.NewPermissionsRequest(
                    getActivity(), PERMISSIONS));
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
        uiHelper.onResume();
        //buttonsEnabled(Session.getActiveSession().isOpened());
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(getActivity(), requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        uiHelper.onSaveInstanceState(savedState);
    }
}