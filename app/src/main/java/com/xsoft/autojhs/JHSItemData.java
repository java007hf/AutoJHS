package com.xsoft.autojhs;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;

public class JHSItemData implements Parcelable {
    private static final String TAG = "JHSItemData";
    private String itemId = "";
    private String juId = "";
    private double flsHb_min = 0;
    private double flsHb_max = 0;
    private String remindNum = "";
    //private String juItemUrl;     //ju.taobao.com/m/jusp/alone/detailwap/mtp.htm?item_id=559253712104
    //private String tqgDetailUrl;  //detail.ju.taobao.com/home.htm?id=10000059115063&item_id=559253712104

    public JHSItemData() {}

    public JHSItemData(Parcel in) {
        itemId = in.readString();
        juId = in.readString();
        flsHb_min = in.readDouble();
        flsHb_max = in.readDouble();
        remindNum = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemId);
        dest.writeString(juId);
        dest.writeDouble(flsHb_min);
        dest.writeDouble(flsHb_max);
        dest.writeString(remindNum);
    }

    @Override
    public String toString() {
        return "itemId = " + itemId
                + "  juId = " + juId
                + "  flsHb_min = " + flsHb_min
                + "  flsHb_max = " + flsHb_max
                + "  remindNum = " + remindNum;
    }

    public double getFlsHb_min() {
        return this.flsHb_min;
    }

    public double getFlsHb_max() {
        return this.flsHb_max;
    }

    public void setFlsHb_min(double min) {
        this.flsHb_min = min;
    }

    public void setFlsHb_max(double max) {
        this.flsHb_max = max;
    }

    public String getJuId() {
        return this.juId;
    }

    public String getItemId() {
        return this.itemId;
    }

    public String getRemindNum() {
            return this.remindNum;
        }

    public void setRemindNum(String remindNum) {
            this.remindNum = remindNum;
        }

    public void settqgDetailUrl(String tqgDetailUrl) {
        String idKeyString = "?id=";
        String itemidKeyString = "&item_id=";

        int idindex = tqgDetailUrl.indexOf(idKeyString);
        int itemidindex = tqgDetailUrl.indexOf(itemidKeyString);

        juId = tqgDetailUrl.substring(idindex+idKeyString.length(), itemidindex);
        itemId = tqgDetailUrl.substring(itemidindex+itemidKeyString.length());
    }

    public String gettqgDetailUrl() {
        String urlString = String.format("https://detail.ju.taobao.com/home.htm?id=%s&item_id=%s", new Object[]{Long.valueOf(juId), Long.valueOf(itemId)});
        return urlString;
    }

    public String getjuItemUrl() {
        String urlString = String.format("https://ju.taobao.com/m/jusp/alone/detailwap/mtp.htm?item_id=%s", new Object[]{Long.valueOf(itemId)});
        return urlString;
    }


    public static final Creator<JHSItemData> CREATOR = new Creator<JHSItemData>() {
        @Override
        public JHSItemData createFromParcel(Parcel in) {
            return new JHSItemData(in);
        }

        @Override
        public JHSItemData[] newArray(int size) {
            return new JHSItemData[size];
        }
    };

    public boolean equals(Object o) {
        boolean z = true;
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        JHSItemData e = (JHSItemData) o;
        if (itemId != e.itemId) {
            z = false;
        }
        return z;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
