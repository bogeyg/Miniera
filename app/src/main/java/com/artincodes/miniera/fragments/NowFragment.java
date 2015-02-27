package com.artincodes.miniera.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.artincodes.miniera.MainActivity;
import com.artincodes.miniera.R;
import com.artincodes.miniera.utils.ListAdapter;
import com.artincodes.miniera.utils.ToDoListAdapter;
import com.artincodes.miniera.utils.TodoDBHelper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import me.drakeet.materialdialog.MaterialDialog;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NowFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    double latitude;
    double longitude;
    String URL = "";
    String API_KEY = "7263875678946287ab7659919ad6f";
    AsyncHttpClient client = new AsyncHttpClient();

    ListView listViewNotification;

    CircularProgressBar circularProgressBar;





    String[] optionTitles = {
            "0",
            "0",
            "0",
    };

    Integer[] optionIcons = {
            R.drawable.ic_call_white,
            R.drawable.ic_sms_white,
            R.drawable.ic_mail_white,
    };

    TextView addToDoButton;

    TodoDBHelper todoDBHelper;
    boolean todoAdded=false;
    Cursor c;

    ListView listViewToDo;

    String[] optionTest = {
    };



    private static final String ARG_SECTION_NUMBER = "section_number";
    public TextView temperatureText;
    public TextView temperatureDes;
    public TextView locationText;

    MaterialDialog mMaterialDialog;
    //DatePicker datePicker;


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static NowFragment newInstance(int sectionNumber) {
        NowFragment fragment = new NowFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public NowFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_now, container, false);

        todoDBHelper = new TodoDBHelper(getActivity());


        listViewNotification = (ListView) rootView.findViewById(R.id.notification_widget_list);
        listViewToDo=(ListView)rootView.findViewById(R.id.todo_widget_list);


        temperatureText = (TextView) rootView.findViewById(R.id.temperature);
        temperatureDes = (TextView) rootView.findViewById(R.id.temperature_desc);
        locationText = (TextView) rootView.findViewById(R.id.location);
        Typeface robotoCond = Typeface.createFromAsset(getActivity().getAssets(), "roboto_thin.ttf");
        //Typeface robotoCond2 = Typeface.createFromAsset(getActivity().getAssets(), "roboto_light.ttf");

        temperatureText.setTypeface(robotoCond);
        //temperatureDes.setTypeface(robotoCond2);
        //locationText.setTypeface(robotoCond2);
        //temperatureText.setShadowLayer(2, 0, 1, Color.BLACK);
        //temperatureDes.setShadowLayer(2, 0, 1, Color.BLACK);
        //locationText.setShadowLayer(2, 0, 1, Color.BLACK);



        circularProgressBar = (CircularProgressBar) rootView.findViewById(R.id.progress_weather);
        circularProgressBar.setVisibility(View.INVISIBLE);


        updateTodoUI();

        addToDoButton = (TextView)rootView.findViewById(R.id.todo_add_text);

        addToDoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),"Add TODO",Toast.LENGTH_SHORT).show();

                LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View convertView = layoutInflater.inflate(R.layout.add_todo_content, null);

                final EditText todoEditText = (EditText)convertView.findViewById(R.id.todoEditText);

                mMaterialDialog = new MaterialDialog(getActivity())
                        .setTitle("Add TO-DO")
                        .setContentView(convertView)
                        .setNegativeButton("CANCEL", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //datePicker = (DatePicker) convertView.findViewById(R.id.datePicker);
                                //Toast.makeText(getActivity(),datePicker.getDayOfMonth()+"",Toast.LENGTH_SHORT).show();



                                mMaterialDialog.dismiss();

                            }
                        })
                        .setPositiveButton("ADD", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String task = todoEditText.getText().toString();
                                //Toast.makeText(getActivity(),task,Toast.LENGTH_SHORT).show();
                                todoAdded = todoDBHelper.insertTodo(task);
                                updateTodoUI();
                                mMaterialDialog.dismiss();

                            }
                        });
                mMaterialDialog.show();


            }
        });


        //***** ASYNC TASK
        new LoadLogCount().execute();


        return rootView;
    }

    private void updateTodoUI() {

        c = todoDBHelper.getData();
        c.moveToFirst();
        int i=0;
        String tasks [] = new String[c.getCount()];
        while (!c.isAfterLast()){
            tasks[i]= c.getString(1);
            c.moveToNext();
            i++;
        }
        ToDoListAdapter toDoListAdapter;
        toDoListAdapter = new ToDoListAdapter(getActivity(), tasks);
        listViewToDo.setAdapter(toDoListAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }


    public void setWeather(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        URL = "http://api.worldweatheronline.com/free/v2/weather.ashx?" +
                "q=" + latitude + "," + longitude +
                "&format=json&" +
                "num_of_days=1&" +
                "includelocation=yes&" +
                "key="+API_KEY;
        client.get(URL, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                //temperatureDes.setText("Loading Weather...");
                circularProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                circularProgressBar.setVisibility(View.INVISIBLE);
                String res = new String(response);
                JSONObject myJson;

                try {
                    myJson = new JSONObject(res);
                    JSONObject dataObject = myJson.getJSONObject("data");
                    JSONArray currentCondition = dataObject.getJSONArray("current_condition");
                    //String test = currentCondition.toString();
                    JSONObject weather = currentCondition.getJSONObject(0);
                    String weatherC = weather.getString("temp_C");
                    JSONArray weatherDescArray = weather.getJSONArray("weatherDesc");
                    JSONObject weatherDesc = weatherDescArray.getJSONObject(0);
                    String weatherDes = weatherDesc.getString("value");
                    //String weatherDesc = weather.getString("weatherDesc");
                    temperatureText.setText(weatherC + (char) 0x00B0);
                    temperatureDes.setText(weatherDes);

                    JSONArray nearestArea = dataObject.getJSONArray("nearest_area");
                    JSONObject areaObj = nearestArea.getJSONObject(0);
                    JSONArray areaArray = areaObj.getJSONArray("areaName");
                    JSONObject areaOb = areaArray.getJSONObject(0);
                    String areaName = areaOb.getString("value");
                    locationText.setText(areaName);
                    //Toast.makeText(getActivity(),areaName, Toast.LENGTH_SHORT).show();
                    //JSONObject route = routesArray.getJSONObject(0);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Toast.makeText(getActivity(),res,Toast.LENGTH_SHORT).show();
                //temperatureText.setText("Kitti");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                locationText.setText("Failed");
                circularProgressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                locationText.setText("Retrying");
            }

            @Override
            public void onFinish() {

            }
        });
    }


    public int getMissedCallCount() {
        String[] projection = {CallLog.Calls.CACHED_NAME, CallLog.Calls.CACHED_NUMBER_LABEL, CallLog.Calls.TYPE};
        String where = CallLog.Calls.TYPE + "=" + CallLog.Calls.MISSED_TYPE+" AND NEW = 1";
        Cursor c = getActivity().getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, where,null,CallLog.Calls.DATE+" DESC");
        c.moveToFirst();
        //Log.d("CALL", "" + c.getCount()); //do some other operation
        ///Toast.makeText(getActivity(), c.getCount() + ",", Toast.LENGTH_SHORT).show();
        return c.getCount();
    }

    public int getUnreadSMSCount(){
        final Uri SMS_INBOX = Uri.parse("content://sms/inbox");

        Cursor c = getActivity().getContentResolver().query(SMS_INBOX, null, "read = 0", null, null);
        return c.getCount();
    }

    public int getUnreadMailsCount(){

       return 0;

    }



    public class LoadLogCount extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            optionTitles[0]=getMissedCallCount()+"";
            optionTitles[1]=getUnreadSMSCount()+"";
            //optionTitles[2]=getUnreadMailsCount()+"";
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            ListAdapter listAdapterObject;
            listAdapterObject = new ListAdapter(getActivity(), optionTitles, optionIcons,"#ffffff");
            listViewNotification.setAdapter(listAdapterObject);
        }
    }
}