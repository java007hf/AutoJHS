// IJHSDataManager.aidl
package com.xsoft.autojhs;
import com.xsoft.autojhs.IJHSDataCallback;
// Declare any non-default types here with import statements

interface IJHSDataManager {
    void registerCallback(IJHSDataCallback callback);
    void unRegisterCallback(IJHSDataCallback callback);
    void setConfig(double hb);
    void startScan();
    void stopScan();
}
