package com.test.searcharound.Util;

import android.util.Log;
import com.test.searcharound.Demo.RearchDemo;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-21
 * Time: 下午3:08
 * To change this template use File | Settings | File Templates.
 */
public class HttpJson {


    public List<RearchDemo> getInformationList(String POIName, String X, String Y, String range, String page) throws JSONException, IOException {


        String Json = Tools.getJson("https://api.weibo.com/2/location/pois/search/by_geo.json?q=" + POIName + "&coordinate=" + X + "%2C" + Y + "&count=20" + "&range=" + range + "&page=" + page + "&access_token=2.00a8ielCING44E127b2c8372DD6jqB" );
        Log.d("", "Json===============================" + Json);
        JSONTokener jsonTokener = new JSONTokener(Json);

        List<RearchDemo> rearchDemoList = new ArrayList<RearchDemo>();
        JSONObject person = (JSONObject) jsonTokener.nextValue();

        JSONArray jsonArray = person.optJSONArray("poilist");
        if (jsonArray == null) {
            Log.d("", "jsonArray空===============================" + jsonArray);
            return null;
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            RearchDemo rearchDemo = new RearchDemo();
            JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
            rearchDemo.setAddress(jsonObject.optString("address"));
            rearchDemo.setDistance(jsonObject.optString("distance"));
            rearchDemo.setImageid(jsonObject.optString("imageid"));
            rearchDemo.setName(jsonObject.optString("name"));
            rearchDemo.setTell(jsonObject.optString("tel"));
            rearchDemo.setTimestamp(jsonObject.optString("timestamp"));
            rearchDemo.setType(jsonObject.optString("type"));
            rearchDemo.setX(jsonObject.optString("x"));
            rearchDemo.setY(jsonObject.optString("y"));
            rearchDemoList.add(rearchDemo);

            Log.d("", "rearchDemo===============================" + rearchDemo);
        }


        return rearchDemoList;

    }



    public JSONObject result(String name,int sr) throws IOException, JSONException {



        //url ="https://api.weibo.com/2/location/pois/search/by_location.json";
       String  url2="https://api.weibo.com/2/location/pois/search/by_location.json?q="+name+"&access_token=2.00a8ielCING44E127b2c8372DD6jqB";
        // url2 ="https://api.weibo.com/2/location/pois/search/by_location.json?q=%E4%B8%AD%E5%9B%BD%E8%8F%9C&access_token=2.00a8ielCING44E127b2c8372DD6jqB";
        //  result = requestServerData(url + "?q=" + name+"&access_token=2.00a8ielCING44E127b2c8372DD6jqB");


        //请求服务器
        HttpGet request = new HttpGet(url2);
        HttpClient httpClient = new DefaultHttpClient();

        HttpResponse response = httpClient.execute(request);

        String resultStr = EntityUtils.toString(response.getEntity(), "UTF-8");
        Log.d("resultStr", "============" + resultStr);
        Log.d("resultStr","============"+resultStr.toString());

        JSONObject jsonObject = new JSONObject(resultStr);


        return  jsonObject;
    }
}
