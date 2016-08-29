package com.example.kcwoo326.cutomerscareapp.listitem;

/**
 * Created by KimJinWoo on 2016-08-03.
 */
public class MainListItem {
    private String[] mData;


    public MainListItem(String[] data) {

        mData = data;

    }


    public MainListItem(String shopnum, String userid, String waitingnum, String whetherthecall, String issuingtime, String persons) {


        mData = new String[6];

        mData[0] = shopnum;

        mData[1] = userid;

        mData[2] = waitingnum;

        mData[3] = whetherthecall;

        mData[4] = issuingtime;

        mData[5] = persons;



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