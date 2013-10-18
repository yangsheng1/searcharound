package com.test.searcharound;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-7-2
 * Time: 上午10:38
 * To change this template use File | Settings | File Templates.
 */
public class SecondPage_Activity extends Activity {

    private ImageButton secondbutton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.second_page);


        secondbutton = (ImageButton) findViewById(R.id.second_reback);
        secondbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SecondPage_Activity.this.finish();
            }
        });
    }
}
