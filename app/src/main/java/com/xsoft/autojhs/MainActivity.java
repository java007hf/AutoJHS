package com.xsoft.autojhs;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private IJHSDataManager mIjhsDataManager;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");

            mIjhsDataManager = (IJHSDataManager)service;
            try {
                mIjhsDataManager.setConfig(1.5);
                mIjhsDataManager.registerCallback(mCallback);
                mIjhsDataManager.startScan();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindJHSService();

        test();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mIjhsDataManager.stopScan();
            mIjhsDataManager.unRegisterCallback(mCallback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        unbindJHSService();
    }

    private void bindJHSService() {
        Log.d(TAG, "bindJHSService");
        Intent intent = new Intent("com.xsoft.autojhs.JHSDataService");
        intent.setPackage("com.xsoft.autojhs");
        bindService(intent, mConnection, Service.BIND_AUTO_CREATE);
    }

    private void unbindJHSService() {
        Log.d(TAG, "unbindJHSService");
        unbindService(mConnection);
    }

    IJHSDataCallback.Stub mCallback = new IJHSDataCallback.Stub() {
        @Override
        public void onGetData(List<JHSItemData> datas) throws RemoteException {
            for(JHSItemData jhsItemData : datas) {
                Log.d(TAG, "jhsItemData = " + jhsItemData);
            }
        }
    };


    private void test() {
        JHSItemData jhsItemData = new JHSItemData();

        jhsItemData.settqgDetailUrl("detail.ju.taobao.com/home.htm?id=10000059115063&item_id=559253712104");

        Log.d(TAG, "idString = " + jhsItemData.getJuId());
        Log.d(TAG, "itemidString = " + jhsItemData.getItemId());
        Log.d(TAG, "getjuItemUrl = " + jhsItemData.getjuItemUrl());
        Log.d(TAG, "gettqgDetailUrl = " + jhsItemData.gettqgDetailUrl());
    }
}
