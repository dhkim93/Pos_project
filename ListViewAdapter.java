package com.example.kcwoo326.customerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by KimJinWoo on 2016-06-03.
 */
public class ListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListItem> listViewItemList = new ArrayList<ListItem>() ;
    private int mResource;
    private Context mContext;
    // ListViewAdapter의 생성자
    public ListViewAdapter(Context context, int layoutResource, ArrayList<ListItem> listItem) {
        this.listViewItemList = listItem;
        this.mContext = context;
        this.mResource = layoutResource;
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_list, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득

        TextView shopName = (TextView) convertView.findViewById(R.id.shopName) ;
        TextView callNum = (TextView) convertView.findViewById(R.id.callNum) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final ListItem listItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        shopName.setText(listItem.getData(1));
        callNum.setText(listItem.getData(2));

        final ListViewAdapter lvAdapter;

       /* callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + listViewItem.getPNumber()));
                context.startActivity(intent);
            }
        });*/

        return convertView;
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
}
