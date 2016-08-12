package com.example.kcwoo326.cutomerscareapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kcwoo326.cutomerscareapp.R;
import com.example.kcwoo326.cutomerscareapp.adapter.CListViewAdapter;
import com.example.kcwoo326.cutomerscareapp.adapter.ListViewAdapter;
import com.example.kcwoo326.cutomerscareapp.app.AppConfig;
import com.example.kcwoo326.cutomerscareapp.app.AppController;
import com.example.kcwoo326.cutomerscareapp.helper.SQLiteHandler;
import com.example.kcwoo326.cutomerscareapp.helper.SessionManager;
import com.example.kcwoo326.cutomerscareapp.listitem.CCareListItem;
import com.example.kcwoo326.cutomerscareapp.listitem.MainListItem;
import com.example.kcwoo326.cutomerscareapp.listitem.WaitingListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity{
    int MAX_PAGE = 2;
    Fragment cur_fragment = new Fragment();
    static ViewPager viewpager;
    PWaitingTableDown task;
    static ArrayList<MainListItem> mainListItems = new ArrayList<>();
    SessionManager session;
    SQLiteHandler db;
    static vpAdapter vpadapter;
    static String name, shopnum;

    static WaitingLayout waitingLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //task = new PWaitingTableDown();
        //task.execute("http://wlsdnghkd123.iptime.org:8080/ccaretable.php");

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        name = user.get("name");
        setTitle(name);

        shopnum = user.get("shopnum");
        Log.e("hiii", "name" + shopnum);
        vpadapter = new vpAdapter(getSupportFragmentManager());
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setAdapter(vpadapter);
    }
    public void fragmentUpdate(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.ccare_layout);

       fragmentManager.beginTransaction().replace(R.id.ccare_layout, new CCareLayout(), CCareLayout.TAG2).commitNow();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_loggout:
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    private class vpAdapter extends FragmentPagerAdapter {

        LayoutInflater inflater;

        public vpAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position < 0 || MAX_PAGE <= position)
                return null;
            switch (position) {
                case 0:
                    cur_fragment = new WaitingLayout();
                    break;

                case 1:
                    cur_fragment = new CCareLayout();
                    break;

            }
            return cur_fragment;
        }

        @Override
        public int getCount() {
            return MAX_PAGE;
        }

        public int getItemPosition(Object object){
           return POSITION_NONE;
        }
        public void dataSetChanged(){

        }

    }

    public static class WaitingLayout extends android.support.v4.app.Fragment {
        TextView callnum;
        TextView waitingteam;
        ListView waitingList;
        ListViewAdapter adapter;
        TextView noteam;
        private ProgressDialog pDialog;
        private static final String TAG1 = "1";
        static int wt = 0;
        static ArrayList<WaitingListItem> waitingListItem = new ArrayList<>();


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.waiting_layout, container, false);

            waitingList = (ListView) view.findViewById(R.id.waitingList);
            waitingteam = (TextView) view.findViewById(R.id.waitingteam);
            callnum = (TextView) view.findViewById(R.id.callnum);
            noteam = (TextView) view.findViewById(R.id.noteam);
            pDialog = new ProgressDialog(getActivity());
            pDialog.setCancelable(false);

            getCCareTable(shopnum, "0");
            Log.e("hiii", "onCreateView");

            return view;
        }

        public void onViewCreated(final View view, Bundle savedInstanceState) {

        }

        public void onResume() {

            super.onResume();
        }

        private void setCCareTable(final String waitingnum) {

            // Tag used to cancel the request
            String tag_string_req = "req_getccaretabledata";


            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_SETCCARETABLE, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    //Log.d(TAG, "Register Response: " + response.toString());

                    try {
                        getCCareTable(shopnum, "0");

                        ((MainActivity) getActivity()).fragmentUpdate();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Log.e(TAG, "Registration Error: " + error.getMessage());

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    // Posting params to register url
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("waitingnum", waitingnum);

                    return params;
                }
            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }


        private void getCCareTable(final String shopnum, final String whetherthecall) {

            // Tag used to cancel the request
            String tag_string_req = "req_getccaretabledata";

            pDialog.setMessage("Please Wait ...");
            showDialog();

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_CCARETABLE, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    //Log.d(TAG, "Register Response: " + response.toString());
                    hideDialog();
                    Log.e("hiii", "waiting의 getCCareTalbe함수이다.");
                    try {
                        JSONObject jObj = new JSONObject(response);

                        JSONArray ja = jObj.getJSONArray("results");
                        wt = 0;
                        waitingListItem.clear();
                        for (int i = 0; i < ja.length(); i++) {
                            wt++;
                            JSONObject jo = ja.getJSONObject(i);
                            String userid = jo.getString("userid");
                            String waitingnum = jo.getString("waitingnum");
                            String persons = jo.getString("persons");
                            String issuingtime = jo.getString("issuingtime");

                            waitingListItem.add(new WaitingListItem(waitingnum, userid, persons, issuingtime));
                        }

                        if (waitingListItem.isEmpty()) {
                            waitingList.setVisibility(View.GONE);
                            noteam.setVisibility(View.VISIBLE);
                            waitingteam.setText("현재 대기 팀 : " + wt);
                        } else {
                            Log.e("hiii", "값1 : " + waitingListItem.get(0).getData(2));
                            waitingteam.setText("현재 대기 팀 : " + wt);
                            adapter = new ListViewAdapter(getActivity(), R.layout.item_list, waitingListItem, new ListViewAdapter.OnButtonClickListener() {//어댑터에서 위젯등의 특정 행동 여부를 액티비티로 받아오고 싶을 떄 인터페이스사용!
                                public void onButtonClickListener(int position) {
                                    Log.e("hiii", "listener");
                                    // callnum.setText(waitingListItem.get(position).getData(2) + " 번 고객님");
                                    //((MainActivity) getActivity()).task.execute("http://wlsdnghkd123.iptime.org:8080/ccaretable.php");


                                    String waitingnum = waitingListItem.get(position).getData(0);
                                    setCCareTable(waitingnum);

                                    //viewpager.findViewWithTag("ccare");

                                }
                            });
                            waitingList.setAdapter(adapter);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Log.e(TAG, "Registration Error: " + error.getMessage());
                    Toast.makeText(getActivity(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting params to register url
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("shopnum", shopnum);
                    params.put("whetherthecall", whetherthecall);

                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }

        private void showDialog() {
            // if (!pDialog.isShowing())
            pDialog.show();
        }

        private void hideDialog() {
            //  if (pDialog.isShowing())
            pDialog.dismiss();
        }


    }

    public static class CCareLayout extends android.support.v4.app.Fragment {
        static ListView ccarelist;
        static ArrayList<CCareListItem> cCareListItems = new ArrayList<>();
        TextView nohistory;
        static CListViewAdapter cAdapter;
        private ProgressDialog pDialog;
        private static final String TAG2 = "2";
        static CCareLayout cCareLayout;
       


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Log.e("hiii", "onCreateView2");

            View view = inflater.inflate(R.layout.ccare_layout, container, false);
            view.setTag("ccare_layout");
            ccarelist = (ListView) view.findViewById(R.id.ccarelist);

            nohistory = (TextView) view.findViewById(R.id.nohistory);

            pDialog = new ProgressDialog(getActivity());
            pDialog.setCancelable(false);

            getCCareTable(shopnum, "1");
            return view;
        }

        public void onViewCreated(View view, Bundle savedInstanceState) {


            /*for (int i = 0; i < mainListItems.size(); i++) {

                if (mainListItems.get(i).getData(0).equals("123456") && mainListItems.get(i).getData(3).equals("1")) {

                    cCareListItems.add(new CCareListItem(mainListItems.get(i).getData()));
                    //mainListItems.get(i).getData(0), mainListItems.get(i).getData(1), mainListItems.get(i).getData(2),
                    //        mainListItems.get(i).getData(3), mainListItems.get(i).getData(4), mainListItems.get(i).getData(5)
                }
            }*/


        }

        public void onResume() {


            super.onResume();
        }

        private void setCCareTable(final String waitingnum) {

            // Tag used to cancel the request
            String tag_string_req = "req_getccaretabledata";


            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_SETCCARETABLE, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    try {

                        getCCareTable(shopnum, "1");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    // Posting params to register url
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("waitingnum", waitingnum);

                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }

    public void getCCareTable(final String shopnum, final String whetherthecall) {

        // Tag used to cancel the request
        String tag_string_req = "req_getccaretabledata";
        pDialog.setMessage("Please Wait ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CCARETABLE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Register Response: " + response.toString());
                    hideDialog();
                Log.e("hiii", "ccare의 getCCareTalbe함수이다.");
                try {

                    JSONObject jObj = new JSONObject(response);
                    // boolean error = jObj.getBoolean("error");
                    // if (!error) {
                    // User successfully stored in MySQL
                    // Now store the user in sqlite
                    // String uid = jObj.getString("uid");

                    JSONArray ja = jObj.getJSONArray("results");
                    cCareListItems.clear();
                    for (int i = 0; i < ja.length(); i++) {

                        JSONObject jo = ja.getJSONObject(i);
                        String userid = jo.getString("userid");
                        String waitingnum = jo.getString("waitingnum");
                        String persons = jo.getString("persons");
                        String issuingtime = jo.getString("issuingtime");

                        cCareListItems.add(new CCareListItem(waitingnum, userid, persons, issuingtime));
                    }

                    if (cCareListItems.isEmpty()) {
                        ccarelist.setVisibility(View.GONE);
                        nohistory.setVisibility(View.VISIBLE);
                    } else {
                        Log.e("hiii", "값2 : " + cCareListItems.get(0).getData(2));
                        cAdapter = new CListViewAdapter(getActivity(), R.layout.item_list, cCareListItems, new CListViewAdapter.OnCButtonClickListener() {//어댑터에서 위젯등의 특정 행동 여부를 액티비티로 받아오고 싶을 떄 인터페이스사용!
                            public void onCButtonClickListener(int position) {
                                Log.e("hiii", "listener2");
                                //((MainActivity) getActivity()).task.execute("http://wlsdnghkd123.iptime.org:8080/ccaretable.php");
                                String waitingnum = cCareListItems.get(position).getData(0);
                                setCCareTable(waitingnum);
                                vpadapter.notifyDataSetChanged();
                            }

                        });
                        ccarelist.setAdapter(cAdapter);
                        ccarelist.setSelection(cCareListItems.size());
                    }

                    // Inserting row in users table
                    //  db.addUser(shopnum, shopname, uid, created_at);


                    // } else {

                    // Error occurred in registration. Get the error
                    // message
                    //     String errorMsg = jObj.getString("error_msg");
                    //     Toast.makeText(getApplicationContext(),
                    //             errorMsg, Toast.LENGTH_LONG).show();
                    //  }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();

                params.put("shopnum", shopnum);
                params.put("whetherthecall", whetherthecall);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        // if (!pDialog.isShowing())
         pDialog.show();
    }

    private void hideDialog() {
        //  if (pDialog.isShowing())
        pDialog.dismiss();
    }



    }


    public class PWaitingTableDown extends AsyncTask<String, Integer, String> {

        StringBuilder jsonHtml = new StringBuilder();

        @Override

        protected String doInBackground(String... urls) {
            Log.e("Hiii", "doInBackground");


            try {

                // 연결 url 설정

                URL url = new URL(urls[0]);

                // 커넥션 객체 생성

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // 연결되었으면.

                if (conn != null) {

                    conn.setConnectTimeout(10000);

                    conn.setUseCaches(false);

                    // 연결되었음 코드가 리턴되면.

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                        for (; ; ) {

                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.

                            String line = br.readLine();

                            if (line == null) break;

                            // 저장된 텍스트 라인을 jsonHtml에 붙여넣음

                            jsonHtml.append(line + "\n");

                        }

                        br.close();

                    }

                    conn.disconnect();

                }

            } catch (Exception ex) {

                ex.printStackTrace();

            }
            Log.e("Hiii", "doInEnd");
            return jsonHtml.toString();


        }


        protected void onPostExecute(String str) {
            Log.e("Hiii", str);
            String shopnum, userid, waitingnum, whetherthecall, issuingtime, persons;
            StringBuffer sb = new StringBuffer();
            try {
                JSONObject root = new JSONObject(str);
                JSONArray ja = root.getJSONArray("results");
                mainListItems.clear();
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    userid = jo.getString("userid");
                    whetherthecall = jo.getString("whetherthecall");
                    shopnum = jo.getString("shopnum");
                    waitingnum = jo.getString("waitingnum");
                    issuingtime = jo.getString("issuingtime");
                    persons = jo.getString("persons");

                    mainListItems.add(new MainListItem(shopnum, userid, waitingnum, whetherthecall, issuingtime, persons));

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(mainListItems.isEmpty()){

            }
           // viewpager = (ViewPager) findViewById(R.id.viewpager);
           // viewpager.setAdapter(new adapter(getSupportFragmentManager()));

            task = new PWaitingTableDown();
        }
    }


}






