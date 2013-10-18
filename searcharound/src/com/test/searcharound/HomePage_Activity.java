package com.test.searcharound;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomePage_Activity extends Activity {
    private ListView listview_home;
    private ArrayList<HashMap<String, ?>> data = new ArrayList<HashMap<String, ?>>();
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        //listView
        listview_home = (ListView) findViewById(R.id.listview_home);

        for (int i = 0; i < 10; i++) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("home_view", "餐饮服务");

            data.add(item);
        }


        final BaseAdapter baseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return data.size();  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Object getItem(int position) {
                return data.get(position);  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public long getItemId(int position) {
                return position;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    LayoutInflater layoutInflater = getLayoutInflater();
                    convertView = layoutInflater.inflate(R.layout.select_item_home, parent, false);
                }

                Map<String, Object> itemData = (Map<String, Object>) getItem(position);


                TextView serviceview = (TextView) convertView.findViewById(R.id.serviceview);

                serviceview.setText(itemData.get("home_view").toString());

                return convertView;  //To change body of implemented methods use File | Settings | File Templates.
            }
        };

        listview_home.setAdapter(baseAdapter);

        listview_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position,long itemId) {

                baseAdapter.notifyDataSetChanged();
            }
        });



    }



}
