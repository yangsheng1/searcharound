package com.test.searcharound.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.baidu.mapapi.*;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.search.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.test.searcharound.Demo.RearchDemo;
import com.test.searcharound.R;
import com.test.searcharound.Util.DemoApplication;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-16
 * Time: 上午4:28
 * To change this template use File | Settings | File Templates.
 */
public class MapRoute_Activity extends Activity {
    private MapController mapController;
    private LayoutInflater layoutInflater;
    private Intent intent;
    private RearchDemo rearchDemo = new RearchDemo();
    private Button map_list_rebackButton;
    private TextView maproute_name;
    private TextView maproute_adress;
    private TextView maproute_tell;
    //
    //UI相关
    LinearLayout mBtnDrive = null;    // 驾车搜索
    LinearLayout mBtnTransit = null;    // 公交搜索
    LinearLayout mBtnWalk = null;    // 步行搜索
    //  Button mBtnCusRoute = null; //自定义路线
    // Button mBtnCusIcon = null ; //自定义起终点图标

    //浏览路线节点相关
    Button mBtnPre = null;//上一个节点
    Button mBtnNext = null;//下一个节点
    int nodeIndex = -2;//节点索引,供浏览节点时使用
    MKRoute route = null;//保存驾车/步行路线数据的变量，供浏览节点时使用
    TransitOverlay transitOverlay = null;//保存公交路线图层数据的变量，供浏览节点时使用
    RouteOverlay routeOverlay = null;
    boolean useDefaultIcon = false;
    int searchType = -1;//记录搜索的类型，区分驾车/步行和公交
    private PopupOverlay pop = null;//弹出泡泡图层，浏览节点时使用
    private TextView popupText = null;//泡泡view
    private View viewCache = null;

    //地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
    //如果不处理touch事件，则无需继承，直接使用MapView即可
    MapView mMapView = null;    // 地图View
    //搜索相关
    MKSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用
    private GeoPoint end;
    private GeoPoint start;
    private MapController mMapController;
    private MKPlanNode stNode;
    private MKPlanNode enNode;

    //

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //初始化baiduMap
        DemoApplication app = (DemoApplication) this.getApplication();
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
        Log.d("", "================maprout========getX:" + rearchDemo.getX());
        Log.d("", "==============maprout==========getY:" + rearchDemo.getY());

        //目标点坐标

        double Y = Double.parseDouble(((rearchDemo.getY())));
        double X = Double.parseDouble(((rearchDemo.getX())));

        end = new GeoPoint((int)(Y*1E6),(int)(X*1E6));
        Log.d("","===========end=================="+end) ;
        start = new GeoPoint( (int) (34.238538 * 1E6 ),(int) (108.907764 * 1E6));
        Log.d("","===========start=================="+start) ;

        // 放置数据
        maproute_name = (TextView) findViewById(R.id.maproute_name);
        maproute_name.setText(rearchDemo.getName());
        maproute_adress = (TextView) findViewById(R.id.maproute_adress);
        maproute_adress.setText(rearchDemo.getAddress());
        maproute_tell = (TextView) findViewById(R.id.maproute_tell);
        Log.d("", "========================maproute_tell.getText():" + maproute_tell.getText());
        if (maproute_tell.getText().equals("")) {
            maproute_tell.setText("暂无电话");
        } else {
            maproute_tell.setText(rearchDemo.getTell());
        }
        ;


        //
        map_list_rebackButton = (Button) findViewById(R.id.map_list_rebackButton);
        map_list_rebackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 mMapView.onPause();
                mMapView.destroy();
                MapRoute_Activity.this.finish();
            }
        });

        //设置地图
        //初始化地图
        mMapView = (MapView)findViewById(R.id.maproute_view);
        mMapView.setBuiltInZoomControls(false);
        mMapView.getController().setZoom(18);
        mMapView.getController().enableClick(true);
        mMapView.getController().setCenter(end);


        //初始化按键
        mBtnDrive = (LinearLayout) findViewById(R.id.maproute_car);
        mBtnTransit = (LinearLayout) findViewById(R.id.maproute_bus);
        mBtnWalk = (LinearLayout) findViewById(R.id.maproute_walk);



        //按键点击事件
        View.OnClickListener clickListener = new View.OnClickListener() {
            public void onClick(View v) {
                //发起搜索
                SearchButtonProcess(v);
            }
        };
//        View.OnClickListener nodeClickListener = new View.OnClickListener(){
//            public void onClick(View v) {
//                //浏览路线节点
//                nodeClick(v);
//            }
//        };
//        View.OnClickListener customClickListener = new View.OnClickListener(){
//            public void onClick(View v) {
//                //自设路线绘制示例
//                intentToActivity();
//
//            }
//        };

//        View.OnClickListener changeRouteIconListener = new View.OnClickListener(){
//
//            @Override
//            public void onClick(View arg0) {
//                changeRouteIcon();
//            }
//
//        };

        mBtnDrive.setOnClickListener(clickListener);
        mBtnTransit.setOnClickListener(clickListener);
        mBtnWalk.setOnClickListener(clickListener);
//        mBtnPre.setOnClickListener(nodeClickListener);
//        mBtnNext.setOnClickListener(nodeClickListener);
//        mBtnCusRoute.setOnClickListener(customClickListener);
        //       mBtnCusIcon.setOnClickListener(changeRouteIconListener);
        //创建 弹出泡泡图层
      //  createPaopao();   \
         stNode = new MKPlanNode();
        stNode.pt = end;
         enNode = new MKPlanNode();
        enNode.pt = start;


        //地图点击事件处理
        mMapView.regMapTouchListner(new MKMapTouchListener() {

            @Override
            public void onMapClick(GeoPoint point) {
                //在此处理地图点击事件
                //消隐pop
                if (pop != null) {
                    pop.hidePop();
                }
            }

            @Override
            public void onMapDoubleClick(GeoPoint point) {

            }

            @Override
            public void onMapLongClick(GeoPoint point) {

            }

        });
        // 初始化搜索模块，注册事件监听
        mSearch = new MKSearch();
        mSearch.init(app.mBMapManager, new MKSearchListener() {

            public void onGetDrivingRouteResult(MKDrivingRouteResult res,
                                                int error) {
                //起点或终点有歧义，需要选择具体的城市列表或地址列表
                if (error == MKEvent.ERROR_ROUTE_ADDR) {
                    //遍历所有地址
//					ArrayList<MKPoiInfo> stPois = res.getAddrResult().mStartPoiList;
//					ArrayList<MKPoiInfo> enPois = res.getAddrResult().mEndPoiList;
//					ArrayList<MKCityListInfo> stCities = res.getAddrResult().mStartCityList;
//					ArrayList<MKCityListInfo> enCities = res.getAddrResult().mEndCityList;
                    return;
                }
                // 错误号可参考MKEvent中的定义
                if (error != 0 || res == null) {
                    Toast.makeText(MapRoute_Activity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                    return;
                }

                searchType = 0;
                routeOverlay = new RouteOverlay(MapRoute_Activity.this, mMapView);
                // 此处仅展示一个方案作为示例
                routeOverlay.setData(res.getPlan(0).getRoute(0));
                //清除其他图层
                mMapView.getOverlays().clear();
                //添加路线图层
                mMapView.getOverlays().add(routeOverlay);
                //执行刷新使生效
                mMapView.refresh();
                // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
                mMapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());
                //移动地图到起点
                mMapView.getController().animateTo(res.getStart().pt);
                //将路线数据保存给全局变量
                route = res.getPlan(0).getRoute(0);
                //重置路线节点索引，节点浏览时使用
                nodeIndex = -1;

            }

            public void onGetTransitRouteResult(MKTransitRouteResult res,
                                                int error) {
                //起点或终点有歧义，需要选择具体的城市列表或地址列表
                if (error == MKEvent.ERROR_ROUTE_ADDR) {
                    //遍历所有地址
//					ArrayList<MKPoiInfo> stPois = res.getAddrResult().mStartPoiList;
//					ArrayList<MKPoiInfo> enPois = res.getAddrResult().mEndPoiList;
//					ArrayList<MKCityListInfo> stCities = res.getAddrResult().mStartCityList;
//					ArrayList<MKCityListInfo> enCities = res.getAddrResult().mEndCityList;
                    return;
                }
                if (error != 0 || res == null) {
                    Toast.makeText(MapRoute_Activity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                    return;
                }

                searchType = 1;
                transitOverlay = new TransitOverlay(MapRoute_Activity.this, mMapView);
                // 此处仅展示一个方案作为示例
                transitOverlay.setData(res.getPlan(0));
                //清除其他图层
                mMapView.getOverlays().clear();
                //添加路线图层
                mMapView.getOverlays().add(transitOverlay);
                //执行刷新使生效
                mMapView.refresh();
                // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
                mMapView.getController().zoomToSpan(transitOverlay.getLatSpanE6(), transitOverlay.getLonSpanE6());
                //移动地图到起点
                mMapView.getController().animateTo(res.getStart().pt);
                //重置路线节点索引，节点浏览时使用
                nodeIndex = 0;

            }

            public void onGetWalkingRouteResult(MKWalkingRouteResult res,
                                                int error) {
                //起点或终点有歧义，需要选择具体的城市列表或地址列表
                if (error == MKEvent.ERROR_ROUTE_ADDR) {
                    //遍历所有地址
//					ArrayList<MKPoiInfo> stPois = res.getAddrResult().mStartPoiList;
//					ArrayList<MKPoiInfo> enPois = res.getAddrResult().mEndPoiList;
//					ArrayList<MKCityListInfo> stCities = res.getAddrResult().mStartCityList;
//					ArrayList<MKCityListInfo> enCities = res.getAddrResult().mEndCityList;
                    return;
                }
                if (error != 0 || res == null) {
                    Toast.makeText(MapRoute_Activity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                    return;
                }

                searchType = 2;
                routeOverlay = new RouteOverlay(MapRoute_Activity.this, mMapView);
                // 此处仅展示一个方案作为示例
                routeOverlay.setData(res.getPlan(0).getRoute(0));
                //清除其他图层
                mMapView.getOverlays().clear();
                //添加路线图层
                mMapView.getOverlays().add(routeOverlay);
                //执行刷新使生效
                mMapView.refresh();
                // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
                mMapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());
                //移动地图到起点
                mMapView.getController().animateTo(res.getStart().pt);
                //将路线数据保存给全局变量
                route = res.getPlan(0).getRoute(0);
                //重置路线节点索引，节点浏览时使用
                nodeIndex = -1;


            }

            public void onGetAddrResult(MKAddrInfo res, int error) {
            }

            public void onGetPoiResult(MKPoiResult res, int arg1, int arg2) {
            }

            public void onGetBusDetailResult(MKBusLineResult result, int iError) {
            }

            @Override
            public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
            }

            @Override
            public void onGetPoiDetailSearchResult(int type, int iError) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onGetShareUrlResult(MKShareUrlResult result, int type,
                                            int error) {
                // TODO Auto-generated method stub

            }
        });
        mSearch.walkingSearch("西安", enNode, "西安", stNode);
    }

    /**
     * 发起路线规划搜索示例
     *
     * @param v
     */
    void SearchButtonProcess(View v) {
        //重置浏览节点的路线数据
        route = null;
        routeOverlay = null;
        transitOverlay = null;

        // 处理搜索按钮响应

        // 对起点终点的name进行赋值，也可以直接对坐标赋值，赋值坐标则将根据坐标进行搜索



        // 实际使用中请对起点终点城市进行正确的设定
        if (mBtnDrive.equals(v)) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("驾车路线加载中");
            progressDialog.show();
            mBtnDrive.setBackgroundResource(R.drawable.tab_item_selected);
            mBtnTransit.setBackgroundResource(R.drawable.bg_tab_top);
            mBtnWalk.setBackgroundResource(R.drawable.bg_tab_top);
            mSearch.drivingSearch("西安", enNode, "西安", stNode);
            progressDialog.dismiss();

        } else if (mBtnTransit.equals(v)) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("公交路线加载中");
            progressDialog.show();
            mBtnDrive.setBackgroundResource(R.drawable.bg_tab_top);
            mBtnTransit.setBackgroundResource(R.drawable.tab_item_selected);
            mBtnWalk.setBackgroundResource(R.drawable.bg_tab_top);
            mSearch.transitSearch("西安", enNode, stNode);
            progressDialog.dismiss();
        } else if (mBtnWalk.equals(v)) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("步行路线加载中");
            progressDialog.show();
            mBtnDrive.setBackgroundResource(R.drawable.bg_tab_top);
            mBtnTransit.setBackgroundResource(R.drawable.bg_tab_top);
            mBtnWalk.setBackgroundResource(R.drawable.tab_item_selected);
            mSearch.walkingSearch("西安", enNode, "西安", stNode);
            progressDialog.dismiss();
        }
    }
    /**
     * 节点浏览示例
     * @param v
     */
//    public void nodeClick(View v){
//        viewCache = getLayoutInflater().inflate(R.layout.custom_text_view, null);
//        popupText =(TextView) viewCache.findViewById(R.id.textcache);
//        if (searchType == 0 || searchType == 2){
//            //驾车、步行使用的数据结构相同，因此类型为驾车或步行，节点浏览方法相同
//            if (nodeIndex < -1 || route == null || nodeIndex >= route.getNumSteps())
//                return;
//
//            //上一个节点
//            if (mBtnPre.equals(v) && nodeIndex > 0){
//                //索引减
//                nodeIndex--;
//                //移动到指定索引的坐标
//                mMapView.getController().animateTo(route.getStep(nodeIndex).getPoint());
//                //弹出泡泡
//                popupText.setBackgroundResource(R.drawable.popup);
//                popupText.setText(route.getStep(nodeIndex).getContent());
//                pop.showPopup(BMapUtil.getBitmapFromView(popupText),
//                        route.getStep(nodeIndex).getPoint(),
//                        5);
//            }
//            //下一个节点
//            if (mBtnNext.equals(v) && nodeIndex < (route.getNumSteps()-1)){
//                //索引加
//                nodeIndex++;
//                //移动到指定索引的坐标
//                mMapView.getController().animateTo(route.getStep(nodeIndex).getPoint());
//                //弹出泡泡
//                popupText.setBackgroundResource(R.drawable.popup);
//                popupText.setText(route.getStep(nodeIndex).getContent());
//                pop.showPopup(BMapUtil.getBitmapFromView(popupText),
//                        route.getStep(nodeIndex).getPoint(),
//                        5);
//            }
//        }
//        if (searchType == 1){
//            //公交换乘使用的数据结构与其他不同，因此单独处理节点浏览
//            if (nodeIndex < -1 || transitOverlay == null || nodeIndex >= transitOverlay.getAllItem().size())
//                return;
//
//            //上一个节点
//            if (mBtnPre.equals(v) && nodeIndex > 1){
//                //索引减
//                nodeIndex--;
//                //移动到指定索引的坐标
//                mMapView.getController().animateTo(transitOverlay.getItem(nodeIndex).getPoint());
//                //弹出泡泡
//                popupText.setBackgroundResource(R.drawable.popup);
//                popupText.setText(transitOverlay.getItem(nodeIndex).getTitle());
//                pop.showPopup(BMapUtil.getBitmapFromView(popupText),
//                        transitOverlay.getItem(nodeIndex).getPoint(),
//                        5);
//            }
//            //下一个节点
//            if (mBtnNext.equals(v) && nodeIndex < (transitOverlay.getAllItem().size()-2)){
//                //索引加
//                nodeIndex++;
//                //移动到指定索引的坐标
//                mMapView.getController().animateTo(transitOverlay.getItem(nodeIndex).getPoint());
//                //弹出泡泡
//                popupText.setBackgroundResource(R.drawable.popup);
//                popupText.setText(transitOverlay.getItem(nodeIndex).getTitle());
//                pop.showPopup(BMapUtil.getBitmapFromView(popupText),
//                        transitOverlay.getItem(nodeIndex).getPoint(),
//                        5);
//            }
//        }
//
//    }

    /**
     * 创建弹出泡泡图层
     */
    public void createPaopao() {

        //泡泡点击响应回调
        PopupClickListener popListener = new PopupClickListener() {
            @Override
            public void onClickedPopup(int index) {
                Log.v("click", "clickapoapo");
            }
        };
        pop = new PopupOverlay(mMapView, popListener);
    }
    /**
     * 跳转自设路线Activity
     */
//    public void intentToActivity(){
//        //跳转到自设路线演示demo
//        Intent intent = new Intent(this, CustomRouteOverlayDemo.class);
//        startActivity(intent);
//    }

    /**
     * 切换路线图标，刷新地图使其生效
     * 注意： 起终点图标使用中心对齐.
     */
//    protected void changeRouteIcon() {
//        Button btn = (Button)findViewById(R.id.customicon);
//        if ( routeOverlay == null && transitOverlay == null){
//            return ;
//        }
//        if ( useDefaultIcon ){
//            if ( routeOverlay != null){
//                routeOverlay.setStMarker(null);
//                routeOverlay.setEnMarker(null);
//            }
//            if ( transitOverlay != null){
//                transitOverlay.setStMarker(null);
//                transitOverlay.setEnMarker(null);
//            }
//            btn.setText("自定义起终点图标");
//            Toast.makeText(this,
//                    "将使用系统起终点图标",
//                    Toast.LENGTH_SHORT).show();
//        }
//        else{
//            if ( routeOverlay != null){
//                routeOverlay.setStMarker(getResources().getDrawable(R.drawable.icon_st));
//                routeOverlay.setEnMarker(getResources().getDrawable(R.drawable.icon_en));
//            }
//            if ( transitOverlay != null){
//                transitOverlay.setStMarker(getResources().getDrawable(R.drawable.icon_st));
//                transitOverlay.setEnMarker(getResources().getDrawable(R.drawable.icon_en));
//            }
//            btn.setText("系统起终点图标");
//            Toast.makeText(this,
//                    "将使用自定义起终点图标",
//                    Toast.LENGTH_SHORT).show();
//        }
//        useDefaultIcon = !useDefaultIcon;
//        mMapView.refresh();
//
//    }
    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mMapView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMapView.onRestoreInstanceState(savedInstanceState);
    }

}




