package com.example.kcwoo326.customerapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends Activity {
    ListView listView;
    ArrayList<ListItem> listItem = new ArrayList<>();
    ArrayList<TableListItem> tableListItem = new ArrayList<>();
    ListViewAdapter adapter;
    ShopTableDown task;
    TextView txtView;
    String imgUrl = "http://wlsdnghkd123.iptime.org:8080/imagelist/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);

        txtView = (TextView) findViewById(R.id.txtView);



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



        task = new ShopTableDown();
        task.execute("http://wlsdnghkd123.iptime.org:8080/shoptable.php");

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

            String shopnum, shopname, callnum, shoppic1, shoppic2, shoppic3, shoppic4, shoppic5;;

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


}
