package com.test.searcharound;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-16
 * Time: 上午4:28
 * To change this template use File | Settings | File Templates.
 */
public class MapList_Activity extends Activity {

    private static  BMapManager bMapManager ;
    public static final String strKey = "8d0ce7b6adcae2bae226a8b3fe4a179b";
    private Spinner spinner;
    private ImageButton reback;
    private ImageButton listbutton;
    private  String[] spn1Data2 = new String[] { "1000m内", "2000m内", "3000m内", "4000m内", "5000m内" };
    private MapController mapController;
    private MapView mapView;

    private LayoutInflater layoutInflater;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //初始化baiduMap
        initBMapManager();
        setContentView(R.layout.map_ist);

        layoutInflater = LayoutInflater.from(this);
        mapView = (MapView) findViewById(R.id.mapview);
        mapController = mapView.getController();

        mapController.setZoom(14);

        getMyLocation();

        reback = (ImageButton) findViewById(R.id.map_list_reback);
        reback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapList_Activity.this, ListPage_Activity.class);
                MapList_Activity.this.finish();
                startActivity(intent);
            }
        });

        listbutton = (ImageButton) findViewById(R.id.list_change);
        listbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapList_Activity.this, ListPage_Activity.class);
                MapList_Activity.this.finish();
                startActivity(intent);
            }
        });


        spinner = (Spinner) findViewById(R.id.map_list_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        MySpinnerAdapter<String> spn2Adapter = new MySpinnerAdapter<String>(
                this, R.layout.list_spinner_item, R.id.text1, spn1Data2);
        spn2Adapter.setDropDownViewResource(R.layout.list_spinner_dropdown_item);
        spinner.setAdapter(spn2Adapter);



    }







    /**
     * 初始化BaiduMapManager
     */
    public void initBMapManager() {
        bMapManager = new BMapManager(this);
        boolean resrult = bMapManager.init(strKey, new MKGeneralListener() {
            @Override
            public void onGetNetworkState(int i) {
                if (i == MKEvent.ERROR_NETWORK_CONNECT) {
                    Toast.makeText(MapList_Activity.this, "您的网络出错啦！", Toast.LENGTH_LONG).show();
                } else if (i == MKEvent.ERROR_NETWORK_DATA) {
                    Toast.makeText(MapList_Activity.this, "输入正确的检索条件！", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onGetPermissionState(int i) {
                if (i == MKEvent.ERROR_PERMISSION_DENIED) {
                    Toast.makeText(MapList_Activity.this, "请在 DemoApplication.java文件输入正确的授权Key！", Toast.LENGTH_LONG).show();
                }
            }
        });

        if (!resrult) {
            Toast.makeText(MapList_Activity.this, "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
        }


    }

    private void getMyLocation() {
        Log.d("BaiduMapDemo", "getMyLocation");

        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setAddrType("all");
        option.setOpenGps(true);

        LocationClient locationClient = new LocationClient(this);
        locationClient.setLocOption(option);

        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                Log.d("BaiduMapDemo", "onReceiveLocation address " + bdLocation.getAddrStr());
                Log.d("BaiduMapDemo", "onReceiveLocation Latitude " + bdLocation.getLatitude());
                Log.d("BaiduMapDemo", "onReceiveLocation Longitude " + bdLocation.getLongitude());
                final GeoPoint geoPoint = new GeoPoint((int) (bdLocation.getLatitude() * 1E6), (int) (bdLocation.getLongitude() * 1E6));
                //设置中心点
                mapController.setCenter(geoPoint);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mapController.animateTo(geoPoint);
                    }
                }, 5000);


                Drawable marker = getResources().getDrawable(R.drawable.ic_current_loc);




                //double lng = bdLocation.getLongitude();
                //double lat = bdLocation.getLatitude();

//                double lng = 34.25934463685013;
//                double lat = 108.94721031188965;
//
//                int offset = (int) (new Random().nextFloat() * 10000);
//                GeoPoint point = new GeoPoint((int) (lng * 1E6) + offset, (int) (lat * 1E6) - offset);
//
//                myLocationOverlay.setMyLocation(point);
//
//                mapView.refresh();
            }

            @Override
            public void onReceivePoi(BDLocation bdLocation) {
                Log.d("BaiduMapDemo", "onReceivePoi ");
            }
        });

        locationClient.start();
        locationClient.requestLocation();

    }



}


