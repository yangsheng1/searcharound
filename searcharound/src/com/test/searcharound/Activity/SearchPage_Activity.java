package com.test.searcharound.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import com.test.searcharound.R;

public class SearchPage_Activity extends Activity {

    private ImageButton searchbutton;
    private Intent intent;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_page);

        searchbutton = (ImageButton) findViewById(R.id.search_reback);
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchPage_Activity.this.finish();;
            }
        });
    }
}
