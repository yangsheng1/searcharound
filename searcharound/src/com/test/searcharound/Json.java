package com.test.searcharound;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-17
 * Time: 上午11:27
 * To change this template use File | Settings | File Templates.
 */
public class Json extends Activity {
    private  String str;
    private  String url2;
    JSONObject gg = null;
    private  EditText nameEditText    ;
    private Button button;
    private TextView textView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.json);

        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textview);
        nameEditText = (EditText) findViewById(R.id.nameEditText);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Log.d("jsonObject","============"+gg);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                gg = result();
                            } catch (IOException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            } catch (JSONException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            }
                            str =  gg.toString();
                        }
                    });
                    thread.start();


                    Log.d("jsonObject","============"+gg);
                    Log.d("str","============"+str);
                    textView.setText(str);


            }
        });



    }

    private String requestServerData(String url) throws IOException {
        //请求服务器
        HttpGet request = new HttpGet(url);
        HttpClient httpClient = new DefaultHttpClient();

        HttpResponse response = httpClient.execute(request);
        String resultStr = EntityUtils.toString(response.getEntity(), "UTF-8");
        Log.d("resultStr","============"+resultStr);
        Log.d("resultStr","============"+resultStr.toString());
        return resultStr;
    }

    private JSONObject result() throws IOException, JSONException {
        final String name = nameEditText.getText().toString();

        String result = null;
        //url ="https://api.weibo.com/2/location/pois/search/by_location.json";
        url2="https://api.weibo.com/2/location/pois/search/by_location.json?q=%E4%B8%AD%E5%9B%BD%E8%8F%9C&access_token=2.00a8ielCING44E127b2c8372DD6jqB";
       // url2 ="https://api.weibo.com/2/location/pois/search/by_location.json?q=%E4%B8%AD%E5%9B%BD%E8%8F%9C&access_token=2.00a8ielCING44E127b2c8372DD6jqB";
           //  result = requestServerData(url + "?q=" + name+"&access_token=2.00a8ielCING44E127b2c8372DD6jqB");
               result = requestServerData(url2);

            JSONObject jsonObject = new JSONObject(result);



           return  jsonObject;
    }
}
