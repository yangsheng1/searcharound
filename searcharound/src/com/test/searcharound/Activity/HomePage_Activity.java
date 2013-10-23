package com.test.searcharound.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.test.searcharound.*;
import com.test.searcharound.Util.ListData;
import com.test.searcharound.Util.MyLocation;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class HomePage_Activity extends Activity implements MyLocation.MyLocationListener{
    private ListView listview_home;
    private ImageButton setbutton;
    private ImageButton searchbutton;
    private Intent intent;
    private TextView mylocation;
    private ImageButton mylocation_refresh;
    private MyLocation myLocation;
    private  String adress;
    private  BDLocation bdLocation;

    private String[] firstdata = ListData.FIRST_DATA;
    ;

    private ArrayList<HashMap<String, ?>> data = new ArrayList<HashMap<String, ?>>();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home_page);

        mylocation = (TextView) findViewById(R.id.home_adress_textview);
        mylocation_refresh = (ImageButton) findViewById(R.id.home_mylocation_button);

         //首页定位
        myLocation = new MyLocation();
        myLocation.getMyLocation(HomePage_Activity.this, this);

        mylocation_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mylocation.setText( "定位中……");
                myLocation = new MyLocation();
                myLocation.getMyLocation(HomePage_Activity.this,HomePage_Activity.this);


            }
        });


        setbutton = (ImageButton) findViewById(R.id.set_button);
        searchbutton = (ImageButton) findViewById(R.id.search_button);

        setbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(HomePage_Activity.this, SetPage_Activity.class);
                startActivity(intent);
            }
        });

        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(HomePage_Activity.this, SearchPage_Activity.class);
                startActivity(intent);
            }
        });

        //listView
        listview_home = (ListView) findViewById(R.id.listview_home);


        final BaseAdapter baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return firstdata.length;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Object getItem(int position) {

                return firstdata[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    LayoutInflater layoutInflater = getLayoutInflater();
                    convertView = layoutInflater.inflate(R.layout.select_item_button, parent, false);
                }

//                Map<String, Object> itemData = (Map<String, Object>) getItem(position);


                TextView serviceview = (TextView) convertView.findViewById(R.id.serviceview);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent = new Intent(HomePage_Activity.this, ListPage_Activity.class);
                        intent.putExtra("list_search_data", firstdata[position]);
                        intent.putExtra("first_item", position + "");
                        startActivity(intent);
                    }
                });
                Button selectbutton = (Button) convertView.findViewById(R.id.select_button);
                selectbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent = new Intent(HomePage_Activity.this, SecondPage_Activity.class);
                        intent.putExtra("first_item_data", firstdata[position]);
                        intent.putExtra("first_item", position + "");
                        startActivity(intent);
                    }
                });

                serviceview.setText(firstdata[position].toString());

                return convertView;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };

        listview_home.setAdapter(baseAdapter);


        //

    }



    protected void onResume() {
        super.onResume();
    }

    @Override
    public void changeAddress(BDLocation bdLocation) {
       String   adress = bdLocation.getAddrStr() ;
          if(adress==null){
              mylocation.setText( "定位失败，请检查网络！" );
          } else{
            mylocation.setText( adress );
        }



    }
}
