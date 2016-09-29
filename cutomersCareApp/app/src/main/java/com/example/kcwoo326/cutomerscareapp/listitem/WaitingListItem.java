package com.example.kcwoo326.cutomerscareapp.listitem;

/**
 * Created by KimJinWoo on 2016-07-30.
 */
public class WaitingListItem {
    private String[] mData;


    public WaitingListItem(String[] data) {

        mData = data;

    }


    public WaitingListItem(String waitingnum, String userid, String persons, String issuingtime) {


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
