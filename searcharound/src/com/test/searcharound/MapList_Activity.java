package com.test.searcharound;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapView;

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
    private Button reback;
    private Button listbutton;
    private  String[] spn1Data2 = new String[] { "1000m内", "2000m内", "3000m内", "4000m内", "5000m内" };

    private MapView mapView;
    private LayoutInflater layoutInflater;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //初始化baiduMap
        initBMapManager();
        setContentView(R.layout.map_ist);

        layoutInflater = LayoutInflater.from(this);
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapView.setSatellite(true);


        reback = (Button) findViewById(R.id.map_list_reback);
        reback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapList_Activity.this.finish();
            }
        });

        listbutton = (Button) findViewById(R.id.list_change);
        listbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapList_Activity.this, MapList_Activity.class);
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
}


