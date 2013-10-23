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

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-7-2
 * Time: 上午10:38
 * To change this template use File | Settings | File Templates.
 */
public class SecondPage_Activity extends Activity {

    private ImageButton secondbutton;
    private String[][] seconddata = ListData.SECOND_DATA;
    private String[][][] thirddata = ListData.THRID_DATA;
    private int firstitem;
    private Intent intent;
    private ListView listview_second;
    private TextView second_title;
    private TextView serviceview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.second_page);


        //接收数据
        intent = getIntent();

        firstitem = Integer.parseInt((intent.getStringExtra("first_item")));

        String firstitem_data = intent.getStringExtra("first_item_data");


        //listView
        listview_second = (ListView) findViewById(R.id.listview_second);

        second_title = (TextView) findViewById(R.id.second_title);
        second_title.setText(firstitem_data);

        final BaseAdapter baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return seconddata[firstitem].length;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Object getItem(int position) {

                return seconddata[firstitem][position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    LayoutInflater layoutInflater = getLayoutInflater();


                    if (thirddata[firstitem][position].length == 0) {
                        convertView = layoutInflater.inflate(R.layout.select_item, parent, false);
                        serviceview = (TextView) convertView.findViewById(R.id.service_view);
                        convertView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                intent = new Intent(SecondPage_Activity.this, ListPage_Activity.class);
                                intent.putExtra("list_search_data", seconddata[firstitem][position]);
                                startActivity(intent);
                            }
                        });



                    } else {
                        convertView = layoutInflater.inflate(R.layout.select_item_button, parent, false);
                        serviceview = (TextView) convertView.findViewById(R.id.serviceview);
                        convertView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                intent = new Intent(SecondPage_Activity.this, ListPage_Activity.class);
                                intent.putExtra("list_search_data", seconddata[firstitem][position]);
                                startActivity(intent);
                            }
                        });
                        Button selectbutton = (Button) convertView.findViewById(R.id.select_button);
                        selectbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                intent = new Intent(SecondPage_Activity.this, ThirdPage_Activity.class);
                                intent.putExtra("seconddata", seconddata[firstitem][position]);
                                intent.putExtra("firstitem", firstitem+"");
                                intent.putExtra("seconditem", position + "");
                                startActivity(intent);
                            }
                        });

                    }

                }
                serviceview.setText(seconddata[firstitem][position].toString());
                return convertView;
            }
        };

        listview_second.setAdapter(baseAdapter);


        secondbutton = (ImageButton) findViewById(R.id.second_reback);
        secondbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SecondPage_Activity.this.finish();
            }
        });
    }
}
