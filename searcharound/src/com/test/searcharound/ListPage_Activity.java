package com.test.searcharound;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-16
 * Time: 上午4:28
 * To change this template use File | Settings | File Templates.
 */
public class ListPage_Activity extends Activity {

    private Spinner spinner;
    private ImageButton reback;
    private Intent intent;
    private ImageButton mapbutton;
    private  String[] spn1Data = new String[] { "1000m内", "2000m内", "3000m内", "4000m内", "5000m内" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.list_page);


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

        spinner = (Spinner) findViewById(R.id.list_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Object item = arg0.getAdapter().getItem(arg2);

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

    }


}
