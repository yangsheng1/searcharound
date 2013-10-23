package com.test.searcharound.Util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.test.searcharound.Activity.HomePage_Activity;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-21
 * Time: 下午4:08
 * To change this template use File | Settings | File Templates.
 */
public class MyLocation {

    private    String X;
    private   String Y;
    public static    String ADRESS;
    private MyLocationListener myLocationListener;

    public void getMyLocation(Context context, MyLocationListener listener) {

        this.myLocationListener = listener;

        Log.d("BaiduMapDemo", "getMyLocation");

        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setAddrType("all");
        option.setOpenGps(true);

        LocationClient locationClient = new LocationClient(context);
        locationClient.setLocOption(option);

        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                Log.d("BaiduMapDemo", "onReceiveLocation address ======ee============" + bdLocation.getAddrStr());
                Log.d("BaiduMapDemo", "onReceiveLocation Latitude =======ee=========" + bdLocation.getLatitude());
                Log.d("BaiduMapDemo", "onReceiveLocation Longitude=======ee======= " + bdLocation.getLongitude());
                final GeoPoint geoPoint = new GeoPoint((int) (bdLocation.getLatitude() * 1E6), (int) (bdLocation.getLongitude() * 1E6));


                myLocationListener.changeAddress(bdLocation);
            }

            @Override
            public void onReceivePoi(BDLocation bdLocation) {
                Log.d("BaiduMapDemo", "onReceivePoi ");
            }
        });

        locationClient.start();
        locationClient.requestLocation();


    }



    public static interface MyLocationListener{
        public void changeAddress(BDLocation bdLocation);
    }

}
