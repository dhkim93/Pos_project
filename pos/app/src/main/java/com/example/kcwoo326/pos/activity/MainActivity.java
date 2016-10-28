package com.example.kcwoo326.pos.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.kcwoo326.pos.R;
import com.example.kcwoo326.pos.adapter.TableAdapter;
import com.example.kcwoo326.pos.app.AppConfig;
import com.example.kcwoo326.pos.app.AppController;
import com.example.kcwoo326.pos.helper.SQLiteHandler;
import com.example.kcwoo326.pos.helper.SessionManager;
import com.example.kcwoo326.pos.listitem.ListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    static public ArrayList<ListItem> listItem = new ArrayList<ListItem>();
    TextView txtView;
    LinearLayout linearLayout;
    TableAdapter tableAdapter;
    GridView gridView;
    SessionManager session;
    SQLiteHandler db;
    int rows;
    int r;
    static String name, shopnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        gridView = (GridView) findViewById(R.id.gridView);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
       //txtView = (TextView) findViewById(R.id.txtView);

        db        = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        name = user.get("name");
        setTitle(name + "님 환영합니다.");

        shopnum = user.get("shopnum");

        showDialog();

        postable();

    }

    @Override
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


    private void postable() {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        Log.e("num", "1");
        pDialog.setMessage("Please wait ...");

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_POSTABLE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();
                String tablenum, shopnum, empty;
                StringBuffer sb = new StringBuffer();

                Log.e("num", "2");
                try {
                        JSONObject jObj = new JSONObject(response);
                        JSONObject row = jObj.getJSONObject("num_results");
                        rows = row.getInt("rows");
                        listItem.clear();
                        sb.append("rows : "+rows);
                        JSONArray ja = jObj.getJSONArray("results");
                        for (int i = 0; i < ja.length(); i++){
                            JSONObject jo = ja.getJSONObject(i);
                            tablenum = jo.getString("tablenum");
                            empty = jo.getString("empty");
                            shopnum = jo.getString("shopnum");
                            listItem.add(new ListItem(tablenum, empty, shopnum));
                           // sb.append("shopnum : " + shopnum + "\ntablenum : " + tablenum + "\nempty : " + empty + "\n");
                        }

                        // Launch main activit
                        tableAdapter = new TableAdapter(getApplicationContext(), rows, new TableAdapter.OnButtonClickListener() {
                            @Override
                            public void onButtonClickListener(int position) {

                                Log.e("hiii", "버튼클릭");

                                String tablenum = listItem.get(position).getData(0).toString().trim();

                                if(listItem.get(position).getData(1).equals("0")){

                                    posinsert(tablenum, "1");

                                }else if(listItem.get(position).getData(1).equals("1")) {
                                    String empty = "0";
                                    posinsert(tablenum, "0");
                                }

                                }

                        });
                        gridView.setAdapter(tableAdapter);

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();
                }
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

    private void posinsert(final String tablenum, final String empty) {
        // Tag used to cancel the request
        String tag_string_req = "req_posinsert";
        Log.e("num", "1");
        pDialog.setMessage("Please wait ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_POSINSERT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                String tablenum, shopnum, empty;


                Log.e("num", "2");
                try {
                    postable();
                } catch (Exception e) {
                    // JSON error
                }
                Log.e("num", "4");
                //txtView.setText(sb.toString());
                postable();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override

            protected Map<String, String> getParams() {

                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tablenum", tablenum);
                params.put("empty", empty);

                return params;
            }
        };
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

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}
