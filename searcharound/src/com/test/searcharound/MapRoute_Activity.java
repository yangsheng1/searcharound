package com.test.searcharound;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;
import com.baidu.mapapi.*;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;

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

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //初始化baiduMap
        initBMapManager();
        setContentView(R.layout.map_route);

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
