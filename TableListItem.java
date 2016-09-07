package com.example.kcwoo326.customerapp;

/**
 * Created by KimJinWoo on 2016-07-14.
 */
public class TableListItem {
    private String[] mData;


    public TableListItem(String[] data) {

        mData = data;

    }


    public TableListItem(String shopnum, String tablenum, String empty) {


        mData = new String[3];

        mData[0] = shopnum;

        mData[1] = tablenum;

        mData[2] = empty;


    }


    public String[] getData() {

        return mData;

    }


    public String getData(int index) {

        return mData[index];

    }


    public void setData(String[] data) {

        mData = data;

    }
}
