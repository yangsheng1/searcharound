package com.test.searcharound.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.*;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.test.searcharound.Demo.RearchDemo;
import com.test.searcharound.Util.HttpJson;
import com.test.searcharound.Util.MyLocation;
import com.test.searcharound.Util.MySpinnerAdapter;
import com.test.searcharound.R;
import org.json.JSONException;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-16
 * Time: 上午4:28
 * To change this template use File | Settings | File Templates.
 */
public class ListPage_Activity extends Activity implements MyLocation.MyLocationListener {

    private Spinner spinner;
    private ImageButton reback;
    private Intent intent;
    private ImageButton mapbutton;
    private TextView listpage_title;
    private String X;
    private String Y;
    private int page = 1;
    private String range;
    private static BMapManager bMapManager;
    public static final String strKey = "8d0ce7b6adcae2bae226a8b3fe4a179b";
    private MapController mapController;
    private MapView mapView;
    private PoiOverlay<OverlayItem> itemItemizedOverlay;

    private String[] spn1Data = new String[]{"1000m内", "2000m内", "3000m内", "4000m内", "5000m内"};
    private ListView listview_listpage;
    private List<RearchDemo> rearchDemoList = new ArrayList<RearchDemo>();
    private String ReData;
    private String str;
    private LayoutInflater layoutInflater;
    private LinearLayout list_more;
    private TextView list_more_text;
    private View mapPopWindow;
    private double XX;
    private double YY;
    private PoiOverlay<OverlayItem> centerOverlay;
    private  RelativeLayout map_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //初始化baiduMap
        initBMapManager();
        setContentView(R.layout.list_page);


        listview_listpage = (ListView) findViewById(R.id.listview_listpage);
        map_layout = (RelativeLayout) findViewById(R.id.map_layout);
        map_layout.setVisibility(View.GONE);

        //mapview
        layoutInflater = LayoutInflater.from(this);
        mapView = (MapView) findViewById(R.id.mapview);
        mapController = mapView.getController();

        mapController.setZoom(15);

        



        //接收跳转页面 传输的数据
        intent = getIntent();
        ReData = intent.getStringExtra("list_search_data");
        listpage_title = (TextView) findViewById(R.id.listpage_title);
        listpage_title.setText(ReData);

        //返回
        reback = (ImageButton) findViewById(R.id.list_rebackButton);
        reback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListPage_Activity.this.finish();
            }
        });
        //切换
        mapbutton = (ImageButton) findViewById(R.id.map_list);
        mapbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listview_listpage.getVisibility() != View.GONE) {
                    listview_listpage.setVisibility(View.GONE);
                    map_layout.setVisibility(View.VISIBLE);
                } else {
                    listview_listpage.setVisibility(View.VISIBLE);
                    map_layout.setVisibility(View.GONE);
                }

//                intent = new Intent(ListPage_Activity.this, MapList_Activity.class);
//                ListPage_Activity.this.finish();
//                startActivity(intent);
            }
        });


        //显示范围
        spinner = (Spinner) findViewById(R.id.list_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Object item = arg0.getAdapter().getItem(arg2);
                str = spn1Data[arg2];
                Log.d("", "========sr================" + str);
                range = str.replace("m内", "");
                Log.d("", "========range================" + range);
                MyLocation myLocation = new MyLocation();
                myLocation.getMyLocation(ListPage_Activity.this, ListPage_Activity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });

        MySpinnerAdapter<String> spn1Adapter = new MySpinnerAdapter<String>(
                this, R.layout.list_spinner_item, R.id.text1, spn1Data);
        spn1Adapter.setDropDownViewResource(R.layout.list_spinner_dropdown_item);
        spinner.setAdapter(spn1Adapter);

        //listview 接收数据
        MyLocation myLocation = new MyLocation();
        myLocation.getMyLocation(ListPage_Activity.this, this);


        //mapview接收数据

    }


    public void changeAddress(BDLocation bdLocation) {
        X = String.valueOf(bdLocation.getLongitude());
        Y = String.valueOf(bdLocation.getLatitude());
        XX = bdLocation.getLongitude();
         YY = bdLocation.getLatitude();
        Log.d("", "========x:" + X + "================y:" + Y);
        reciveListView(X, Y,range, String.valueOf(page));
    }

    private void reciveListView(final String X, final String Y ,String rang, final String page) {
        //ListView Json 接收数据


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中...");


        AsyncTask<Integer, Integer, Integer> task = new AsyncTask<Integer, Integer, Integer>() {

            //后台运行
            @Override
            protected Integer doInBackground(Integer... integers) {

                HttpJson httpJson = new HttpJson();
                try {
                    rearchDemoList = httpJson.getInformationList(ReData, X, Y, range, page);
                    Log.d("", "rearchDemoList===============================" + rearchDemoList);
                } catch (JSONException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }


                return 0;
            }

            @Override
            //异步之前运行
            protected void onPreExecute() {
                progressDialog.show();

            }

            //之后运行
            protected void onPostExecute(Integer result) {

                progressDialog.dismiss();


          //listview显示   baseAdapter
                final BaseAdapter baseAdapter = new BaseAdapter() {
                    private TextView nameview;
                    private TextView adressview;
                    private TextView distanceview;
                    @Override
                    public int getCount() {
                        return rearchDemoList.size();  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public Object getItem(int position) {
                        return rearchDemoList.get(position);
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }

                    @Override
                    public View getView(final int position, View convertView, ViewGroup parent) {

                        LayoutInflater layoutInflater = getLayoutInflater();
                        convertView = layoutInflater.inflate(R.layout.inft_item, parent, false);
                        convertView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                intent = new Intent(ListPage_Activity.this, MapRoute_Activity.class);
                                intent.putExtra("getName",rearchDemoList.get(position).getName());
                                intent.putExtra("getAddress",rearchDemoList.get(position).getAddress());
                                intent.putExtra("getDistance",rearchDemoList.get(position).getDistance());
                                intent.putExtra("getTell",rearchDemoList.get(position).getTell());
                                intent.putExtra("getTimestamp",rearchDemoList.get(position).getTimestamp());
                                intent.putExtra("getX",rearchDemoList.get(position).getX());
                                intent.putExtra("getY",rearchDemoList.get(position).getY());

                                startActivity(intent);
                            }
                        });
                        nameview = (TextView) convertView.findViewById(R.id.name_View);
                        adressview = (TextView) convertView.findViewById(R.id.address_View);
                        distanceview = (TextView) convertView.findViewById(R.id.dis_View);

                        nameview.setText(rearchDemoList.get(position).getName());
                        adressview.setText(rearchDemoList.get(position).getAddress());
                        distanceview.setText(rearchDemoList.get(position).getDistance());
                        return convertView;
                    }
                };

                listview_listpage.setAdapter(baseAdapter);

        //Mapview显示

                mapController.setZoom(18);
                //设置中心点s
                mapController.setCenter(new GeoPoint((int)(YY* 1E6), (int)(XX* 1E6)));
                Drawable center = getResources().getDrawable(R.drawable.ic_current_loc);
                centerOverlay = new PoiOverlay<OverlayItem>(center, mapView);
                GeoPoint cenpoint = new GeoPoint((int)(YY* 1E6), (int)(XX* 1E6));

                OverlayItem overlaycent = new OverlayItem(cenpoint,"","");
                centerOverlay.addItem(overlaycent);

                //地图扎点
                Drawable marker = getResources().getDrawable(R.drawable.ic_loc_normal);
                itemItemizedOverlay = new PoiOverlay<OverlayItem>(marker, mapView);
              //  for (int i = 0; i < 10; i++) {
                mapPopWindow = layoutInflater.inflate(R.layout.map_pop_window, null);
                for(final RearchDemo rearchDemo:rearchDemoList){
                   double X = Double.parseDouble(rearchDemo.getX());
                    double Y = Double.parseDouble((rearchDemo.getY()));
                    Log.d("", "x=====================" + X +"   ==========y:"+Y);
                    GeoPoint point = new GeoPoint((int)(Y* 1E6), (int)(X* 1E6));
                    Log.d("", "point  ==============y:"+point);
                    OverlayItem overlayItem = new OverlayItem(point,rearchDemo.getName()  , rearchDemo.getAddress());
                    itemItemizedOverlay.addItem(overlayItem);


        }

                mapView.getOverlays().add(centerOverlay);
                mapView.getOverlays().add(itemItemizedOverlay);



                mapPopWindow.setVisibility(View.GONE);
                mapView.addView(mapPopWindow);

            }
        };

        task.execute(0);

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
                    Toast.makeText(ListPage_Activity.this, "您的网络出错啦！", Toast.LENGTH_LONG).show();
                } else if (i == MKEvent.ERROR_NETWORK_DATA) {
                    Toast.makeText(ListPage_Activity.this, "输入正确的检索条件！", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onGetPermissionState(int i) {
                if (i == MKEvent.ERROR_PERMISSION_DENIED) {
                    Toast.makeText(ListPage_Activity.this, "请在 DemoApplication.java文件输入正确的授权Key！", Toast.LENGTH_LONG).show();
                }
            }
        });

        if (!resrult) {
            Toast.makeText(ListPage_Activity.this, "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
        }


    }

    class PoiOverlay<OverlayItem> extends ItemizedOverlay {

        public PoiOverlay(Drawable drawable, MapView mapView) {
            super(drawable, mapView);
        }

        @Override
        protected boolean onTap(int i) {
            Log.d("BaiduMapDemo", "onTap " + i);
            com.baidu.mapapi.map.OverlayItem item = itemItemizedOverlay.getItem(i);

            final GeoPoint point = item.getPoint();
            final String title = item.getTitle();
            final String content = item.getSnippet();

         //   String distanct = item.get

            TextView titleTextView = (TextView) mapPopWindow.findViewById(R.id.titleTextView);
            TextView contentTextView = (TextView) mapPopWindow.findViewById(R.id.contentTextView);
            mapPopWindow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = new Intent(ListPage_Activity.this, MapRoute_Activity.class);
                    intent.putExtra("getName", title);
                    intent.putExtra("getAddress",content );
//                    intent.putExtra("getDistance", rearchDemo.getDistance());
//                    intent.putExtra("getTell", rearchDemo.getTell());
//                    intent.putExtra("getTimestamp", rearchDemo.getTimestamp());
                    intent.putExtra("getX", point.getLatitudeE6()+"");
                    intent.putExtra("getY", point.getLongitudeE6()+"");

                    startActivity(intent);
                }
            });
            titleTextView.setText(title);
            contentTextView.setText(content);
            contentTextView.setVisibility(View.VISIBLE);

            MapView.LayoutParams layoutParam = new MapView.LayoutParams(
                    //控件宽,继承自ViewGroup.LayoutParams
                    MapView.LayoutParams.WRAP_CONTENT,
                    //控件高,继承自ViewGroup.LayoutParams
                    MapView.LayoutParams.WRAP_CONTENT,
                    //使控件固定在某个地理位置
                    point,
                    0,
                    -40,
                    //控件对齐方式
                    MapView.LayoutParams.BOTTOM_CENTER);

            mapPopWindow.setVisibility(View.VISIBLE);

            mapPopWindow.setLayoutParams(layoutParam);

            mapController.animateTo(point);

            return super.onTap(i);
        }

        @Override
        public boolean onTap(GeoPoint geoPoint, MapView mapView) {
            Log.d("BaiduMapDemo", "onTap geoPoint " + geoPoint);

            mapPopWindow.setVisibility(View.GONE);

            return super.onTap(geoPoint, mapView);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
