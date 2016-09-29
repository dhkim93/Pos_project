package com.example.kcwoo326.customerapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by KimJinWoo on 2016-07-04.
 */
public class ShopInfo extends Activity {

    //LinearLayout linear;
    ImageView imView;
    Bitmap bmImg;
    back task;
    PTableDown task2;
    PWaitingTableDown task3;
    ViewFlipper viewFlipper;
    float xAtDown, xAtUp;
    TextView vacant;
    String[] strings;
    int c, v, wt;
    Button waiitingNumBtn;
    TextView waitingTeam;
    View dialogView;


    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopinfo);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        final MapView mapView = new MapView(this);
        mapView.setDaumMapApiKey(getString(R.string.API_KEY));

        RelativeLayout container = (RelativeLayout) findViewById(R.id.mapView);
        container.addView(mapView);
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(37.581032, 126.925893), -3, true);

        MapPOIItem customMarker = new MapPOIItem();
        customMarker.setItemName("미스터 피자");
        customMarker.setTag(1);
        customMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(37.581032, 126.925893));
        customMarker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커타입을 커스텀 마커로 지정.
        customMarker.setCustomImageResourceId(R.drawable.map_pin_red); // 마커 이미지.
        customMarker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
        customMarker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.
        //x축 작으면 오른쪽, 크면 왼쪽으로 마커 이동, y축 작으면 밑으로 크면 위로 마커 위치 이동
        mapView.addPOIItem(customMarker);

        vacant = (TextView) findViewById(R.id.vacant);

        task2 = new PTableDown();
        task2.execute("http://wlsdnghkd123.iptime.org:8080/postable.php");

        task3 = new PWaitingTableDown();
        task3.execute("http://wlsdnghkd123.iptime.org:8080/ccaretable.php");

        //linear = (LinearLayout) findViewById(R.id.linear);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        Intent intent = getIntent();
        //int position = intent.getIntExtra("position", 0);
        strings = intent.getStringArrayExtra("string");


        for (int i = 3; i <8; i++) {
            task = new back();
            task.execute(strings[i]);
        }

        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(v != viewFlipper){
                    return false;
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    Log.d("onTouch", "down");
                    xAtDown = event.getX();
                }else if(event.getAction() == MotionEvent.ACTION_UP) {
                    Log.d("onTouch", "up");
                    xAtUp = event.getX();
                    if (xAtUp < xAtDown) {
                        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_left_in));
                        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_left_out));
                        viewFlipper.showNext();
                    } else if (xAtUp > xAtDown) {
                        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_right_in));
                        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.push_right_out));
                        viewFlipper.showPrevious();
                    }
                }
                return true;
            }
        });

    }
    public void waitingNum(View v){
        MyDialogBuilder dlg = new MyDialogBuilder(this);
        dlg.show();


    }

    private void insertToDatabase(String shopnum, String userid, String persons){

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;



            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ShopInfo.this, "Please Wait", null, true, true);
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
                    String shopnum = (String)params[0];
                    String userid = (String)params[1];
                    String persons = (String)params[2];

                    String link="http://wlsdnghkd123.iptime.org:8080/ccaretableinsert.php";
                    String data  = URLEncoder.encode("shopnum", "UTF-8") + "=" + URLEncoder.encode(shopnum, "UTF-8");
                    data += "&" + URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
                    data += "&" + URLEncoder.encode("persons", "UTF-8") + "=" + URLEncoder.encode(persons, "UTF-8");


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
        task.execute(shopnum, userid, persons);
    }
    private class back extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {

            // TODO Auto-generated method stub

            try {

                URL myFileUrl = new URL(urls[0]);

                HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();

                conn.setDoInput(true);

                conn.connect();

                //String json = DownloadHtml("http://서버주소/appdata.php");

                InputStream is = conn.getInputStream();


                bmImg = BitmapFactory.decodeStream(is);


            } catch (IOException e) {

                e.printStackTrace();

            }

            return bmImg;

        }


        protected void onPostExecute(Bitmap img) {
            imView = new ImageView(getApplicationContext());

            // LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT,1);
            // imView.setLayoutParams(ivParams);
            imView.setImageBitmap(bmImg);
            viewFlipper.addView(imView);
        }
    }

    public class PWaitingTableDown extends AsyncTask<String, Integer, String> {

        StringBuilder jsonHtml = new StringBuilder();
        ArrayList<TableListItem> tableListItem = new ArrayList<>();
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
            Log.e("Hiii", "doInEnd");
            return jsonHtml.toString();


        }


        protected void onPostExecute(String str) {
            Log.e("Hiii", "onPostExecute");
            String shopnum, userid, whetherthecall;
            StringBuffer sb = new StringBuffer();
            try {
                JSONObject root = new JSONObject(str);
                JSONArray ja = root.getJSONArray("results");
                wt=0;
                tableListItem.clear();
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    userid = jo.getString("userid");
                    whetherthecall = jo.getString("whetherthecall");
                    shopnum = jo.getString("shopnum");

                    if(shopnum.equals(strings[0]) && whetherthecall.equals("0")){
                        wt++;

                        if(userid.equals("wlsdnghkd123")) {
                            waiitingNumBtn = (Button) findViewById(R.id.waitingNumbtn);
                            waiitingNumBtn.setEnabled(false);
                            waiitingNumBtn.setText("대기번호 발급 완료");
                        }
                    }


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            waitingTeam = (TextView) findViewById(R.id.waitingTeam);
            waitingTeam.setText("현재 대기팀 : " + wt);
        }


    }

    public class PTableDown extends AsyncTask<String, Integer, String> {

        StringBuilder jsonHtml = new StringBuilder();
        ArrayList<TableListItem> tableListItem = new ArrayList<>();
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
            Log.e("Hiii", "doInEnd");
            return jsonHtml.toString();


        }


        protected void onPostExecute(String str) {
            Log.e("Hiii", "onPostExecute");
            String tablenum, shopnum;
            String empty;
            StringBuffer sb = new StringBuffer();
            try {
                JSONObject root = new JSONObject(str);
                JSONArray ja = root.getJSONArray("results");
                tableListItem.clear();
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    tablenum = jo.getString("tablenum");
                    empty = jo.getString("empty");
                    shopnum = jo.getString("shopnum");
                    tableListItem.add(new TableListItem(shopnum, tablenum, empty));

                    sb.append("shopnum : " + shopnum + "\ntablenum : " + tablenum + "\nempty : " + empty + "\n");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            for(int i=0; i<tableListItem.size(); i++){
                if(strings[0].equals(tableListItem.get(i).getData(0))) {
                    c++;
                    if(tableListItem.get(i).getData(2).equals("0")){
                        v++;
                    }
                }
            }
            vacant.setText("총 테이블 수 : " + c + "\n빈 테이블 수 : " + v );
            // txtView.setText("tables : "+listItem.get(0).getData(0)+"\nempty : "+listItem.get(0).getData(1));
            Log.e("Hiii", "txtView");
        }


    }

    class MyDialogBuilder extends AlertDialog.Builder {//여러 액티비티에서 쓰고자 한다면 외부 클래스로 만들면됨.

        NumberPicker numpick;

        InputMethodManager ime;
        public MyDialogBuilder(Context context) {
            super(context);
        }

        public AlertDialog create() {

            dialogView = (View) View.inflate(ShopInfo.this, R.layout.waiting_dialog, null);


            numpick = (NumberPicker) dialogView.findViewById(R.id.numpick);
            numpick.setMaxValue(100);
            numpick.setMinValue(1);
            numpick.setValue(2);
            numpick.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

            setView(dialogView);


            setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                        String p = String.valueOf(numpick.getValue());
                        insertToDatabase(strings[0], "wlsdnghkd123", p);
                        task3 = new PWaitingTableDown();
                        task3.execute("http://wlsdnghkd123.iptime.org:8080/ccaretable.php");
                        dialog.cancel();
                }
            });
            return super.create();
        }

    }


}

