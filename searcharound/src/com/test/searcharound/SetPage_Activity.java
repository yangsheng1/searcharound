package com.test.searcharound;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

public class SetPage_Activity extends Activity {

    private ImageButton setbutton;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.set_page);



        setbutton = (ImageButton) findViewById(R.id.set_reback);
        setbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetPage_Activity.this.finish();;
            }
        });
    }


}
