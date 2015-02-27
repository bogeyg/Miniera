package com.artincodes.miniera.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.artincodes.miniera.MainActivity;
import com.artincodes.miniera.R;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

//import android.app.Fragment;

public class SocialFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");

    UiLifecycleHelper uiHelper;
    Session session;
    TextView textResponse;

    CircularProgressBar progressSocial;

    AsyncHttpClient client = new AsyncHttpClient();
    String URL = "";

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // To maintain FB Login session
        uiHelper = new UiLifecycleHelper(getActivity(), statusCallback);
        uiHelper.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_social, container, false);
        final LoginButton loginButton = (LoginButton) rootView.findViewById(R.id.loginFacebookButton);
        textResponse = (TextView) rootView.findViewById(R.id.textResponse);
        progressSocial = (CircularProgressBar)rootView.findViewById(R.id.progress_social);
        loginButton.setFragment(this);
        loginButton.setReadPermissions(Arrays.asList("email", "read_stream"));
        loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {

            @Override

            public void onUserInfoFetched(GraphUser user) {
                if (user != null) {
                    //Toast.makeText(getActivity(), user.getName(), Toast.LENGTH_SHORT).show();
                    loginButton.setVisibility(View.INVISIBLE);
                    getHomeFeed();

                } else {
//                    username.setText("You are not logged in.");
                    loginButton.setVisibility(View.VISIBLE);

                }

            }

        });

        session = Session.getActiveSession();

        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    private Session.StatusCallback statusCallback = new Session.StatusCallback() {

        @Override

        public void call(Session session, SessionState state,

                         Exception exception) {

            if (state.isOpened()) {
                Log.d("MainActivity", "Facebook session opened.");
            } else if (state.isClosed()) {
                Log.d("MainActivity", "Facebook session closed.");
            }

        }

    };


    @Override

    public void onResume() {
        super.onResume();
        uiHelper.onResume();

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
    }


    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        uiHelper.onSaveInstanceState(savedState);
    }

    public static void showHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "com.app.fblogin", PackageManager.GET_SIGNATURES); //Your package name here
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {

        }
    }


    private void getHomeFeed() {

//        textResponse.setText("Loading");

//        new Request(
//                session,
//                "/me/home",
//                null,
//                HttpMethod.GET,
//                new Request.Callback() {
//                    public void onCompleted(Response response) {
//            /* handle the result */
//                        textResponse.setText(response.toString());
//                        //Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();
//
//                        String temp = null;
//                        String feed = response.toString();
//                        int i=0;
//
//                        try {
//                            GraphObject graphObject  = response.getGraphObject();
//                            JSONObject jsonObject = graphObject.getInnerJSONObject();
//                            //Toast.makeText(getActivity(),jsonObject.length()+"",Toast.LENGTH_LONG).show();
//                            JSONArray data = null;
//                            //textResponse.setText("");
//                            do {
//                                data = jsonObject.getJSONArray("data");
//                            Toast.makeText(getActivity(),data.length()+"", Toast.LENGTH_LONG).show();
//                                JSONObject dataObject = data.getJSONObject(i);
//                                String message = dataObject.getString("message");
//                                JSONObject fromObject = dataObject.getJSONObject("from");
//                                String from = fromObject.getString("name");
//
//                                //temp = data.getString();
//
//                                textResponse.setText(textResponse.getText()+from + ": " + message+"\n\n\n");
//                                i++;
//                            }while (i<=data.length());
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        } catch (NullPointerException e){
//                            //e.printStackTrace();
//                            textResponse.setText("Sorry, We didn't contact facebook. Try reloading");
//                        }
//
//
//                    }
//
//                }
//        ).executeAsync();

        URL = "https://graph.facebook.com/me/home?access_token=" + session.getAccessToken();
        client.get(URL, new AsyncHttpResponseHandler() {
            @Override
            public  void onStart(){
            }
            @Override
            public void onSuccess(int i, Header[] headers, byte[] response) {

//                Toast.makeText(getActivity(),"Success",Toast.LENGTH_SHORT).show();
                String res = new String(response);
                JSONObject myJson;
                int index = 0;

                try {
                    myJson = new JSONObject(res);
                    //textResponse.setText(myJson.toString());
                    JSONArray data = myJson.getJSONArray("data");

                    Toast.makeText(getActivity(), data.length() + "", Toast.LENGTH_LONG).show();
//                    Toast.makeText(getActivity(), data.toString() + "", Toast.LENGTH_LONG).show();

                    String message,imageUrl,from;
                    JSONObject dataObject,fromObject;

                    for (index = 0; index < data.length(); index++) {
                        dataObject = data.getJSONObject(index);
                        message = dataObject.getString("message");
                        imageUrl = dataObject.getString("picture");
//                        String story = dataObject.getString("story");
                        fromObject = dataObject.getJSONObject("from");
                        from = fromObject.getString("name");

                        //temp = data.getString();
//                                i++;
                        textResponse.setText(textResponse.getText() + "xyxy\n" +from + ": " + message + "\n"+imageUrl+"\n\n");

                    }

                    textResponse.setText(textResponse.getText()+"\n\n"+data.toString());

                    progressSocial.setVisibility(View.GONE);






                } catch (JSONException e) {
                    e.printStackTrace();

                    textResponse.setText(textResponse.getText()+"\n\n"+res);

                    progressSocial.setVisibility(View.GONE);

                }




            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });


//        Toast.makeText(getActivity(),URL,Toast.LENGTH_LONG).show();


    }


}





