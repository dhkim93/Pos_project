package com.example.kcwoo326.posapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;


public class MainActivity extends Activity {
    String tableNum, empty;
    Button btn1, btn2, btn3, btn4, btn5, btn6;
    phpDown task;
    ArrayList<ListItem> listItem = new ArrayList<ListItem>();
    TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn6 = (Button) findViewById(R.id.btn6);

        txtView = (TextView) findViewById(R.id.txtView);

        task = new phpDown();
        task.execute("http://221.167.200.70:8080/postable.php");

    }

    public void btnClk(View v){

        switch(v.getId()){
            case R.id.btn1:
                tableNum = "1";
                if(listItem.get(0).getData(1).equals("0")) {
                    empty = "1";
                    btn1.setBackgroundColor(Color.GREEN);
                    insertToDatabase(tableNum, empty);
                }else{
                    empty = "0";
                    btn1.setBackgroundColor(Color.GRAY);
                    insertToDatabase(tableNum, empty);
                }
                break;
            case R.id.btn2:
                tableNum = "2";
                if(listItem.get(1).getData(1).equals("0")) {
                    empty = "1";
                    btn2.setBackgroundColor(Color.GREEN);
                    insertToDatabase(tableNum, empty);
                }else{
                    empty = "0";
                    btn2.setBackgroundColor(Color.GRAY);
                    insertToDatabase(tableNum, empty);
                }
                break;
            case R.id.btn3:
                tableNum = "3";
                if(listItem.get(2).getData(1).equals("0")) {
                    empty = "1";
                    btn3.setBackgroundColor(Color.GREEN);
                    insertToDatabase(tableNum, empty);
                }else{
                    empty = "0";
                    btn3.setBackgroundColor(Color.GRAY);
                    insertToDatabase(tableNum, empty);
                }
                break;
            case R.id.btn4:
                tableNum = "4";
                if(listItem.get(3).getData(1).equals("0")) {
                    empty = "1";
                    btn4.setBackgroundColor(Color.GREEN);
                    insertToDatabase(tableNum, empty);
                }else{
                    empty = "0";
                    btn4.setBackgroundColor(Color.GRAY);
                    insertToDatabase(tableNum, empty);
                }
                break;
            case R.id.btn5:
                tableNum = "5";
                if(listItem.get(4).getData(1).equals("0")) {
                    empty = "1";
                    btn5.setBackgroundColor(Color.GREEN);
                    insertToDatabase(tableNum, empty);
                }else{
                    empty = "0";
                    btn5.setBackgroundColor(Color.GRAY);
                    insertToDatabase(tableNum, empty);
                }
                break;
            case R.id.btn6:
                tableNum = "6";
                if(listItem.get(5).getData(1).equals("0")) {
                    empty = "1";
                    btn6.setBackgroundColor(Color.GREEN);
                    insertToDatabase(tableNum, empty);
                }else{
                    empty = "0";
                    btn6.setBackgroundColor(Color.GRAY);
                    insertToDatabase(tableNum, empty);
                }
                break;
        }
        task = new phpDown();
        task.execute("http://221.167.200.70:8080/postable.php");
    }


    private void insertToDatabase(String tables, String empty){

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;



            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                try{
                    String tableNum = (String)params[0];
                    String empty = (String)params[1];

                    String link="http://wlsdnghkd123.iptime.org:8080/posinsert.php";
                    String data  = URLEncoder.encode("tablenum", "UTF-8") + "=" + URLEncoder.encode(tableNum, "UTF-8");
                    data += "&" + URLEncoder.encode("empty", "UTF-8") + "=" + URLEncoder.encode(empty, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write( data );
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }
                catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }

            }
        }

        InsertData task = new InsertData();
        task.execute(tables,empty);
    }

    private class phpDown    extends AsyncTask<String, Integer, String> {
        String empty;
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
        String tablenum, shopnum;
        StringBuffer sb = new StringBuffer();
        try {

            JSONObject root = new JSONObject(str);
            JSONArray ja = root.getJSONArray("results");
            listItem.clear();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                tablenum = jo.getString("tablenum");
                empty = jo.getString("empty");
                shopnum = jo.getString("shopnum");
                listItem.add(new ListItem(tablenum, empty, shopnum));
                if (tablenum.equals("1")) {
                    Log.e("Hiii", "if1");
                    if(empty.equals("0")) {
                        btn1.setBackgroundColor(Color.GRAY);
                    }else{
                        btn1.setBackgroundColor(Color.GREEN);
                    }
                }else if(tablenum.equals("2")){
                    Log.e("Hiii", "if2");
                    if(empty.equals("0")) {
                        btn2.setBackgroundColor(Color.GRAY);
                    }else{
                        btn2.setBackgroundColor(Color.GREEN);
                    }
                }else if(tablenum.equals("3")){
                    Log.e("Hiii", "if3");
                    if(empty.equals("0")) {
                        btn3.setBackgroundColor(Color.GRAY);
                    }else{
                        btn3.setBackgroundColor(Color.GREEN);
                    }
                }else if(tablenum.equals("4")){
                    Log.e("Hiii", "if4");
                    if(empty.equals("0")) {
                        btn4.setBackgroundColor(Color.GRAY);
                    }else{
                        btn4.setBackgroundColor(Color.GREEN);
                    }

                }else if(tablenum.equals("5")){
                    Log.e("Hiii", "if5");
                    if(empty.equals("0")) {
                        btn5.setBackgroundColor(Color.GRAY);
                    }else{
                        btn5.setBackgroundColor(Color.GREEN);
                    }

                }else if(tablenum.equals("6")){
                    Log.e("Hiii", "if6");
                    if(empty.equals("0")) {
                        btn6.setBackgroundColor(Color.GRAY);
                    }else{
                        btn6.setBackgroundColor(Color.GREEN);
                    }
                }

                sb.append("shopnum : " + shopnum + "\ntablenum : " + tablenum + "\nempty : " + empty + "\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        txtView.setText(sb.toString());
        // txtView.setText("tables : "+listItem.get(0).getData(0)+"\nempty : "+listItem.get(0).getData(1));
        Log.e("Hiii", "txtView");
    }


}

}
