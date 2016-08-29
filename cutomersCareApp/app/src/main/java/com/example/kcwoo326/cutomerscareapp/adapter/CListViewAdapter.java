package com.example.kcwoo326.cutomerscareapp.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.kcwoo326.cutomerscareapp.R;
import com.example.kcwoo326.cutomerscareapp.listitem.CCareListItem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by KimJinWoo on 2016-08-02.
 */
public class CListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<CCareListItem> listViewItemList = new ArrayList<>() ;
    private int mResource;
    private Context mContext;
    Button cancel;
    private OnCButtonClickListener onCButtonClickListener;


    // ListViewAdapter의 생성자
    public CListViewAdapter(Context context, int layoutResource, ArrayList<CCareListItem> listItem, OnCButtonClickListener onCButtonClickListener) {
        Log.e("hiii", "ListViewAdapter2");
        this.listViewItemList = listItem;
        this.mContext = context;
        this.mResource = layoutResource;;
        this.onCButtonClickListener = onCButtonClickListener;
}

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.e("hiii","getView2" );
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ccare_list, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득

        TextView waitingnum = (TextView) convertView.findViewById(R.id.cwaitingnum) ;
        TextView cinfo = (TextView) convertView.findViewById(R.id.ccinfo) ;
        TextView issuingtime = (TextView) convertView.findViewById(R.id.cissuingtime) ;
        cancel = (Button) convertView.findViewById(R.id.cancel);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final CCareListItem listItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        waitingnum.setText(listItem.getData(0));
        if(listItem.getData(1).equals("")){
            cinfo.setText( "인원 : " + listItem.getData(2) + "명");
        }else  cinfo.setText("회원 id : "+listItem.getData(1) + " | 인원 : " + listItem.getData(2) + "명");
        issuingtime.setText(listItem.getData(3));
        Log.e("hiii","값 : "+ listItem.getData(2)+listItem.getData(1) );

        final CListViewAdapter lvAdapter;

       /* callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + listViewItem.getPNumber()));
                context.startActivity(intent);
            }
        });*/
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //insertToDatabase(listItem.getData(0));
                onCButtonClickListener.onCButtonClickListener(position);
                Log.e("hiii", "onButton2");
            }
        });

        return convertView;
    }

    private void insertToDatabase(String waitingnum){

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(mContext,"Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
            }

            @Override
            protected String doInBackground(String... params) {

                try{
                    String waitingnum = (String)params[0];


                    String link="http://wlsdnghkd123.iptime.org:8080/whetherthecallinsert.php";
                    String data  = URLEncoder.encode("waitingnum", "UTF-8") + "=" + URLEncoder.encode(waitingnum, "UTF-8");


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
        task.execute(waitingnum);
    }


    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String number) {

    }
    public interface OnCButtonClickListener{
        public void onCButtonClickListener(int position);
    }


}
