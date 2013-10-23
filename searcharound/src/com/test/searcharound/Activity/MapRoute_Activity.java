package com.test.searcharound.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.mapapi.*;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.test.searcharound.Demo.RearchDemo;
import com.test.searcharound.R;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-16
 * Time: 上午4:28
 * To change this template use File | Settings | File Templates.
 */
public class MapRoute_Activity extends Activity {
    private MapController mapController;
    private static com.baidu.mapapi.BMapManager bMapManager ;
    public static final String strKey = "8d0ce7b6adcae2bae226a8b3fe4a179b";
    private MapView mapView;
    private LayoutInflater layoutInflater;
    private Intent intent;
    private RearchDemo rearchDemo = new RearchDemo();
    private Button map_list_rebackButton;
    private TextView maproute_name;
    private TextView maproute_adress;
    private TextView maproute_tell;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //初始化baiduMap
       // initBMapManager();
        setContentView(R.layout.map_route);


        //接收Intent
        intent = getIntent();
        rearchDemo.setName(intent.getStringExtra("getName"));
        rearchDemo.setAddress(intent.getStringExtra("getAddress"));
        rearchDemo.setDistance(intent.getStringExtra("getDistance"));
        rearchDemo.setTell(intent.getStringExtra("getTell"));
        rearchDemo.setTimestamp(intent.getStringExtra("getTimestamp"));
        rearchDemo.setX(intent.getStringExtra("getX"));
        rearchDemo.setY(intent.getStringExtra("getY"));

        Log.d("", "========================getName:" + rearchDemo.getName());
        Log.d("", "========================getAddress:" + rearchDemo.getAddress());
        Log.d("", "========================getDistance:" + rearchDemo.getDistance());
        Log.d("", "========================getTell:" + rearchDemo.getTell());
        Log.d("", "========================getX:" + rearchDemo.getX());
        Log.d("", "========================getY:" + rearchDemo.getY());

        // 放置数据
        maproute_name = (TextView) findViewById(R.id.maproute_name);
        maproute_name.setText(rearchDemo.getName());
        maproute_adress = (TextView) findViewById(R.id.maproute_adress);
        maproute_adress.setText(rearchDemo.getAddress());
        maproute_tell = (TextView) findViewById(R.id.maproute_tell);
        if(maproute_tell.getText().equals("")){
            maproute_tell.setText("暂无电话");
        }else{
            maproute_tell.setText(rearchDemo.getTell());
        };


        //
        map_list_rebackButton = (Button)findViewById(R.id.map_list_rebackButton);
        map_list_rebackButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                MapRoute_Activity.this.finish();
            }
        });

        //设置地图
        layoutInflater = LayoutInflater.from(this);
        mapView = (MapView) findViewById(R.id.maproute_view);
        mapController = mapView.getController();
        mapController.setZoom(14);

        final GeoPoint geoPoint = new GeoPoint((int) (34.25934463685013 * 1E6), (int) (108.94721031188965 * 1E6));
        //设置中心点
        mapController.setCenter(geoPoint);

    }


    /**
     * 初始化BaiduMapManager
     */
    public void initBMapManager() {
        bMapManager = new com.baidu.mapapi.BMapManager(this);
        boolean resrult = bMapManager.init(strKey, new MKGeneralListener() {
            @Override
            public void onGetNetworkState(int i) {
                if (i == MKEvent.ERROR_NETWORK_CONNECT) {
                    Toast.makeText(MapRoute_Activity.this, "您的网络出错啦！", Toast.LENGTH_LONG).show();
                } else if (i == MKEvent.ERROR_NETWORK_DATA) {
                    Toast.makeText(MapRoute_Activity.this, "输入正确的检索条件！", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onGetPermissionState(int i) {
                if (i == MKEvent.ERROR_PERMISSION_DENIED) {
                    Toast.makeText(MapRoute_Activity.this, "请在 DemoApplication.java文件输入正确的授权Key！", Toast.LENGTH_LONG).show();
                }
            }
        });

        if (!resrult) {
            Toast.makeText(MapRoute_Activity.this, "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
        }


    }

}
