package com.xsoft.autojhs;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benylwang on 2017/10/28.
 */

public class JHSDataService extends Service {
    private static final String TAG = "JHSDataService";
    private static final boolean DEBUG = false;
    private List<IJHSDataCallback> mCallbacks = new ArrayList<>();
    private HandlerThread mWorkThread;
    private Handler mHandler;
    private static final int MSG_START_GET_DATA = 0X100;
    private static final int DELAY_TIME = 100;
    private double mMinHb = 0.1;
    private Object mLock = new Object();

    private IJHSDataManager.Stub mBinder = new IJHSDataManager.Stub() {
        @Override
        public void registerCallback(IJHSDataCallback callback) throws RemoteException {
            synchronized (mLock) {
                mCallbacks.add(callback);
            }
        }

        @Override
        public void unRegisterCallback(IJHSDataCallback callback) throws RemoteException {
            synchronized (mLock) {
                mCallbacks.remove(callback);
            }
        }

        @Override
        public void startScan() throws RemoteException {
            startGetData();
        }

        @Override
        public void stopScan() throws RemoteException {
            stopGetData();
        }

        @Override
        public void setConfig(double hb) throws RemoteException {
            mMinHb = hb;
            if(DEBUG) Log.d(TAG, "setConfig mMinHb = " + mMinHb);
        }
    };

    @Override
    public void onCreate() {
        if(DEBUG) Log.d(TAG, "onCreate");
        super.onCreate();
        initHandler();
    }

    private void startGetData() {
        if(DEBUG) Log.d(TAG, "startGetData");
        mHandler.removeMessages(MSG_START_GET_DATA);
        mHandler.sendEmptyMessage(MSG_START_GET_DATA);
    }

    private void stopGetData() {
        if(DEBUG) Log.d(TAG, "stopGetData");
        mHandler.removeMessages(MSG_START_GET_DATA);
    }

    private void initHandler() {
        mWorkThread = new HandlerThread("ScanData");
        mWorkThread.start();

        mHandler = new Handler(mWorkThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                getData(1);

                mHandler.sendEmptyMessageDelayed(MSG_START_GET_DATA, DELAY_TIME);
            }
        };
    }

    private void getData(int page) {
        long mills = System.currentTimeMillis();
        String urlString = String.format("https://ju.taobao.com/json/tg/ajaxGetItemsV2.json?page=%s&psize=100&type=0&label=flsremind&scene=fls&stype=psort&rank=&rank=&jview=all&includeForecast=false&ostimeLower=%s&_=%s", new Object[]{Integer.valueOf(page), Long.valueOf(mills), Long.valueOf(mills)});
        if(DEBUG) Log.d(TAG, "getData urlString = " + urlString);

        String json = GetWebUtils.geturl(urlString);
        LogUtil.d(TAG, "getData geturl json = " + json);

        try {
            JSONObject jsonObject = JSONObject.fromObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("itemList");
            int size = jsonArray.size();
            if(DEBUG) Log.d(TAG, "size " + size);
            List<JHSItemData> jhsItemDatas = new ArrayList<>(size);

            for(int i=0;i<jsonArray.size();i++) {
                JSONObject jb = jsonArray.getJSONObject(i);
                JSONObject jb_extend = jb.getJSONObject("extend");
                JSONObject jb_remind = jb.getJSONObject("remind");

                String flsHb = jb_extend.getString("flsHb");
                double flsHb_max = Double.valueOf(flsHb.split("_")[1])/100d;

                if(DEBUG) Log.d(TAG, "flsHb_max = " + flsHb_max + " mMinHb = " + mMinHb);
                if (flsHb_max >= mMinHb) {
                    if(DEBUG) Log.d(TAG, "too big flsHb_max = " + flsHb_max);
                    double flsHb_min = Double.valueOf(flsHb.split("_")[0])/100d;
                    String tqgDetailUrl = jb_extend.getString("tqgDetailUrl");
                    String remindNum = jb_remind.getString("remindNum");

                    JHSItemData jhsItemData = new JHSItemData();
                    jhsItemData.settqgDetailUrl(tqgDetailUrl);
                    jhsItemData.setFlsHb_max(flsHb_max);
                    jhsItemData.setFlsHb_min(flsHb_min);
                    jhsItemData.setRemindNum(remindNum);

                    jhsItemDatas.add(jhsItemData);
                } else {
                    if(DEBUG) Log.d(TAG, "too little flsHb_max = " + flsHb_max);
                }
            }

            notifyGetData(jhsItemDatas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void notifyGetData(List<JHSItemData> jhsItemDatas) {
        synchronized (mLock) {
            for (IJHSDataCallback callback : mCallbacks) {
                try {
                    callback.onGetData(jhsItemDatas);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void unInitHandler() {
        mWorkThread.quit();
        mWorkThread = null;
        mHandler = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if(DEBUG) Log.d(TAG, "onBind");
        return mBinder;
    }

    @Override
    public void onDestroy() {
        if(DEBUG) Log.d(TAG, "onDestroy");
        super.onDestroy();
        unInitHandler();
    }
}
