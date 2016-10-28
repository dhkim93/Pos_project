package com.example.kcwoo326.pos.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.example.kcwoo326.pos.activity.MainActivity;

/**
 * Created by KimJinWoo on 2016-09-30.
 */
public class TableAdapter extends BaseAdapter {
    private Context mContext;
    int row;
    public Button button;
    private OnButtonClickListener onButtonClickListener;


    public TableAdapter(Context c, int row, OnButtonClickListener onButtonClickListener){
        this.mContext = c;
        this.row = row;
        this.onButtonClickListener = onButtonClickListener;
    }
    public int getCount(){
        return row;
    }
    public Object getItem(int position){
        return null;
    }
    public long getItemId(int position){
        return 0;
    }
    public View getView(final int position, View convertView, ViewGroup parent){


        button = new Button(mContext);
        button.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        button.setText(String.valueOf(MainActivity.listItem.get(position).getData(0)));
        if(MainActivity.listItem.get(position).getData(1).equals("0")){
            button.setBackgroundColor(Color.GREEN);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClickListener.onButtonClickListener(position);

            }
        });

        return button;
    }

    public interface OnButtonClickListener{
        public void onButtonClickListener(int position);
    }

}