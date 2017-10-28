// IJHSDataCallback.aidl
package com.xsoft.autojhs;
import com.xsoft.autojhs.JHSItemData;
// Declare any non-default types here with import statements

interface IJHSDataCallback {
    void onGetData(out List<JHSItemData> datas);
}
