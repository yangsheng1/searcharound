package com.test.searcharound.Util;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-14
 * Time: 下午9:17
 * To change this template use File | Settings | File Templates.
 */
public class Tools {
    public static String getJson(String url) throws IOException {

        String strURL = URLEncoder.encode(url, "utf-8");
        Log.d("", "url===============================" + url);
        HttpGet request = new HttpGet(url);

        request.addHeader("Content-Type", "application/json");
        request.addHeader("charset", "UTF-8");
        HttpResponse httpResponse = new DefaultHttpClient().execute(request);

        String msg = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        Log.d("", "msg===============================" + msg);
        return msg;

    }

}
