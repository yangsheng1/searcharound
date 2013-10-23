package com.test.searcharound.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.test.searcharound.Util.ListData;
import com.test.searcharound.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-7-2
 * Time: 上午10:38
 * To change this template use File | Settings | File Templates.
 */
public class ThirdPage_Activity extends Activity {
    private ListView listview_third;
    private Intent intent ;
    private int firstitem;
    private int seconditem;
    private TextView third_title;
    private String[][][] thirddata = ListData.THRID_DATA;
    private ArrayList<HashMap<String, ?>> data = new ArrayList<HashMap<String, ?>>();

    private ImageButton thirdbutton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.third_page);


        thirdbutton = (ImageButton) findViewById(R.id.third_reback);
        thirdbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThirdPage_Activity.this.finish();
            }
        });


        //接收数据
        intent = getIntent();
       firstitem =  Integer.parseInt((intent.getStringExtra("firstitem")));
        seconditem =  Integer.parseInt((intent.getStringExtra("seconditem")));
        String second_item_data = intent.getStringExtra("seconddata");


        listview_third = (ListView) findViewById(R.id.listview_third);
        third_title = (TextView) findViewById(R.id.third_title);
        third_title.setText(second_item_data);




        final BaseAdapter baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return thirddata[firstitem][seconditem].length;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Object getItem(int position) {
                return thirddata[firstitem][seconditem][position];  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public long getItemId(int position) {
                return position;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    LayoutInflater layoutInflater = getLayoutInflater();
                    convertView = layoutInflater.inflate(R.layout.select_item, parent, false);
                }




                TextView serviceview = (TextView) convertView.findViewById(R.id.service_view);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent = new Intent(ThirdPage_Activity.this,ListPage_Activity.class);
                        intent.putExtra("list_search_data", thirddata[firstitem][seconditem][position]);
                        startActivity(intent);
                    }
                });


                serviceview.setText(thirddata[firstitem][seconditem][position].toString());

                return convertView;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };

        listview_third.setAdapter(baseAdapter);


    }
}
