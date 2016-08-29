package com.example.kcwoo326.customerapp;

/**
 * Created by KimJinWoo on 2016-07-04.
 */


public class ListItem {



    private String[] mData;



    public ListItem(String[] data ){

        mData = data;

    }



    public ListItem(String shopnum, String shopname, String callnum, String shoppic1, String shoppic2, String shoppic3, String shoppic4, String shoppic5){



        mData = new String[8];

        mData[0] = shopnum;
        mData[1] = shopname;
        mData[2] = callnum;
        mData[3] = shoppic1;
        mData[4] = shoppic2;
        mData[5] = shoppic3;
        mData[6] = shoppic4;
        mData[7] = shoppic5;





    }



    public String[] getData(){

        return mData;

    }



    public String getData(int index){

        return mData[index];

    }



    public void setData(String[] data){

        mData = data;

    }







}





