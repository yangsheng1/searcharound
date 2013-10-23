package com.test.searcharound.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.test.searcharound.Demo.RearchDemo;
import com.test.searcharound.Util.HttpJson;
import com.test.searcharound.Util.MyLocation;
import com.test.searcharound.Util.MySpinnerAdapter;
import com.test.searcharound.R;
import org.json.JSONException;
import org.json.JSONObject;

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
    private String page ;
    private String range;

    private String[] spn1Data = new String[]{"1000m内", "2000m内", "3000m内", "4000m内", "5000m内"};
    private ListView listview_listpage;
    private List<RearchDemo> rearchDemoList = new ArrayList<RearchDemo>();
    private String ReData;
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.list_page);


        //接收跳转页面 传输的数据
        intent = getIntent();
        ReData = intent.getStringExtra("list_search_data");
        listpage_title = (TextView) findViewById(R.id.listpage_title);
        listpage_title.setText(ReData);


        reback = (ImageButton) findViewById(R.id.list_rebackButton);
        reback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListPage_Activity.this.finish();
            }
        });
        mapbutton = (ImageButton) findViewById(R.id.map_list);
        mapbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(ListPage_Activity.this, MapList_Activity.class);
                ListPage_Activity.this.finish();
                startActivity(intent);
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
                Log.d("", "========sr================" +str);
                  range = str.replace("m内","");
                Log.d("", "========range================" +range);
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

          //接收数据
        MyLocation myLocation = new MyLocation();
        myLocation.getMyLocation(ListPage_Activity.this, this);

    }



    public void changeAddress(BDLocation bdLocation) {
        X = String.valueOf(bdLocation.getLongitude());
        Y = String.valueOf(bdLocation.getLatitude());
        Log.d("", "========x:" + X + "================y:" + Y);
        reciveListView(X,Y);
    }

    private void reciveListView(final String X, final String Y) {
        //ListView Json 接收数据


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中...");


        AsyncTask<Integer, Integer, Integer> task = new AsyncTask<Integer, Integer, Integer>() {

            //后台运行
            @Override
            protected Integer doInBackground(Integer... integers) {

                HttpJson httpJson = new HttpJson();
                try {
                    rearchDemoList = httpJson.getInformationList(ReData, X, Y , range, page);
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
                //list
                listview_listpage = (ListView) findViewById(R.id.listview_listpage);

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

                        if (convertView == null) {
                            LayoutInflater layoutInflater = getLayoutInflater();

                            convertView = layoutInflater.inflate(R.layout.inft_item, parent, false);
                            nameview = (TextView) convertView.findViewById(R.id.name_View);
                            adressview = (TextView) convertView.findViewById(R.id.address_View);
                            distanceview = (TextView) convertView.findViewById(R.id.dis_View);
                            convertView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });


                        }


                        nameview.setText(rearchDemoList.get(position).getName());
                        adressview.setText(rearchDemoList.get(position).getAddress());
                        distanceview.setText(rearchDemoList.get(position).getDistance());
                        return convertView;
                    }
                };

                listview_listpage.setAdapter(baseAdapter);
            }
        };

        task.execute(0);

    }
}
