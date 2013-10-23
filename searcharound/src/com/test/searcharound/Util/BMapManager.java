package com.test.searcharound.Util;

import android.content.Context;
import android.widget.Toast;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.test.searcharound.Activity.MapList_Activity;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-17
 * Time: 上午10:10
 * To change this template use File | Settings | File Templates.
 */
public class BMapManager  {
    private com.baidu.mapapi.BMapManager bMapManager;

    public static final String strKey = "8d0ce7b6adcae2bae226a8b3fe4a179b";

    /**
     * 初始化BaiduMapManager
     */
    public void initBMapManager(final Context content) {

        bMapManager = new com.baidu.mapapi.BMapManager(content);
        boolean resrult = bMapManager.init(strKey, new MKGeneralListener() {
            @Override
            public void onGetNetworkState(int i) {
                if (i == MKEvent.ERROR_NETWORK_CONNECT) {
                    Toast.makeText(content, "您的网络出错啦！", Toast.LENGTH_LONG).show();
                } else if (i == MKEvent.ERROR_NETWORK_DATA) {
                    Toast.makeText(content, "输入正确的检索条件！", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onGetPermissionState(int i) {
                if (i == MKEvent.ERROR_PERMISSION_DENIED) {
                    Toast.makeText(content, "请在 DemoApplication.java文件输入正确的授权Key！", Toast.LENGTH_LONG).show();
                }
            }
        });

        if (!resrult) {
            Toast.makeText(content, "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
      }


            }
}
