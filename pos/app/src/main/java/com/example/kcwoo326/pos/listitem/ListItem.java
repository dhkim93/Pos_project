package com.example.kcwoo326.pos.listitem;

/**
 * Created by KimJinWoo on 2016-09-29.
 */

public class ListItem {


    private String[] mData;


    public ListItem(String[] data) {

        mData = data;

    }


    public ListItem(String tablenum, String empty, String shopnum) {


        mData = new String[3];

        mData[0] = tablenum;

        mData[1] = empty;

        mData[2] = shopnum;


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

