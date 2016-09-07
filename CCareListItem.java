package com.example.kcwoo326.cutomerscareapp.listitem;

/**
 * Created by KimJinWoo on 2016-08-02.
 */
public class CCareListItem {
    private String[] mData;


    public CCareListItem(String[] data) {

        mData = data;

    }


    public CCareListItem(String waitingnum, String userid, String persons, String issuingtime) {


        mData = new String[4];


        mData[0] = waitingnum;

        mData[1] = userid;

        mData[2] = persons;

        mData[3] = issuingtime;

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
