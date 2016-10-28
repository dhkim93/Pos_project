package com.example.kcwoo326.customerapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kcwoo326.customerapp.R;
import com.example.kcwoo326.customerapp.adapter.ListViewAdapter;
import com.example.kcwoo326.customerapp.app.AppConfig;
import com.example.kcwoo326.customerapp.app.AppController;
import com.example.kcwoo326.customerapp.helper.SQLiteHandler;
import com.example.kcwoo326.customerapp.helper.SessionManager;
import com.example.kcwoo326.customerapp.listitem.ListItem;
import com.example.kcwoo326.customerapp.listitem.TableListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity{
    ListView listView;
    ArrayList<ListItem> listItem = new ArrayList<>();
    ArrayList<TableListItem> tableListItem = new ArrayList<>();
    ListViewAdapter adapter;
    ShopTableDown task;
    TextView txtView;
    SessionManager session;
    SQLiteHandler db;
    static String name;
    private ProgressDialog pDialog;
    private static final String TAG = MainActivity.class.getSimpleName();
    String imgUrl = "http://wlsdnghkd123.iptime.org:8080/imagelist/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);

        txtView = (TextView) findViewById(R.id.txtView);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ShopInfo.class);
                intent.putExtra("position", position);

                String[] strings = new String[]{listItem.get(position).getData(0), listItem.get(position).getData(1), listItem.get(position).getData(2), listItem.get(position).getData(3),
                        listItem.get(position).getData(4), listItem.get(position).getData(5), listItem.get(position).getData(6), listItem.get(position).getData(7)};
                intent.putExtra("string", strings);
                startActivity(intent);
            }
        });

        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        name = user.get("name");
        setTitle(name+"님 환영합니다!");

        //task = new ShopTableDown();
        //task.execute("http://wlsdnghkd123.iptime.org:8080/shoptable.php");
        shoptable();

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

    private class ShopTableDown extends AsyncTask<String, Integer, String> {

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

                        for (;;) {

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

            return jsonHtml.toString();


        }


        protected void onPostExecute(String str) {
            Log.e("Hiii", "onPostExecute");

            String shopnum, shopname, callnum, shoppic1, shoppic2, shoppic3, shoppic4, shoppic5;

            StringBuffer sb = new StringBuffer();
            try {

                JSONObject root = new JSONObject(str);
                JSONArray ja = root.getJSONArray("results");
                listItem.clear();
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    shopnum = jo.getString("shopnum");
                    shopname = jo.getString("shopname");
                    callnum = jo.getString("callnum");
                    shoppic1 = jo.getString("shoppic1");
                    shoppic2 = jo.getString("shoppic2");
                    shoppic3 = jo.getString("shoppic3");
                    shoppic4 = jo.getString("shoppic4");
                    shoppic5 = jo.getString("shoppic5");
                    listItem.add(new ListItem(shopnum, shopname, callnum, imgUrl+shoppic1, imgUrl+shoppic2, imgUrl+shoppic3, imgUrl+shoppic4, imgUrl+shoppic5));
                    sb.append(shopname + ", " + callnum + "\n");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter = new ListViewAdapter(getApplicationContext(), R.layout.item_list, listItem);
            listView.setAdapter(adapter);
            txtView.setText(sb.toString());
            //txtView.setText("tables : " + listItem.get(0).getData(0) + "\nempty : " + listItem.get(0).getData(1));
            Log.e("Hiii", "adapter");
        }


    }

    private void shoptable() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        Log.e("num", "1");
        pDialog.setMessage("Please wait ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SHOPTABLE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();
                String shopnum, shopname, callnum, shoppic1, shoppic2, shoppic3, shoppic4, shoppic5;
                StringBuffer sb = new StringBuffer();

                Log.e("num", "2");
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray ja = jObj.getJSONArray("results");
                    listItem.clear();
                    for (int i = 0; i < ja.length(); i++){
                        JSONObject jo = ja.getJSONObject(i);
                        shopnum = jo.getString("shopnum");
                        shopname = jo.getString("shopname");
                        callnum = jo.getString("callnum");
                        shoppic1 = jo.getString("shoppic1");
                        shoppic2 = jo.getString("shoppic2");
                        shoppic3 = jo.getString("shoppic3");
                        shoppic4 = jo.getString("shoppic4");
                        shoppic5 = jo.getString("shoppic5");
                        listItem.add(new ListItem(shopnum, shopname, callnum, imgUrl+shoppic1, imgUrl+shoppic2, imgUrl+shoppic3, imgUrl+shoppic4, imgUrl+shoppic5));
                        sb.append(shopname + ", " + callnum + "\n");
                        // sb.append("shopnum : " + shopnum + "\ntablenum : " + tablenum + "\nempty : " + empty + "\n");
                    }


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();
                }
                adapter = new ListViewAdapter(getApplicationContext(), R.layout.item_list, listItem);
                listView.setAdapter(adapter);
                txtView.setText(sb.toString());
                Log.e("num","4");
                //txtView.setText(sb.toString());
                Log.e("hiii", "셋리니어");

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Log.e("num", "7");
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }



}
