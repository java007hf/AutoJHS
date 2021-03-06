package com.xsoft.autojhs;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by benylwang on 2017/10/26.
 */

public class GetWebUtils {
    public static String get(String urlString) {
        URL url = null;
        String result = "";

        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream is = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
                String line = null;
                StringBuffer sb = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();

                result = sb.toString();

            } else {
                Log.d("MainActivity : ",httpURLConnection.getResponseCode() +"");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 获取参数指定的网页代码，将其返回给调用者，由调用者对其解析
     * 返回String
     * Chen.Zhidong
     * 2011-02-15
     */
    public static String posturl(String url) {
        InputStream is = null;
        String result = "";

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            return "Fail to establish http connection!" + e.toString();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();

            result = sb.toString();
        } catch (Exception e) {
            return "Fail to convert net stream!";
        }

        return result;
    }


    public static String geturl(String url) {
        InputStream is = null;
        String result = "";

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            return "Fail to establish http connection!" + e.toString();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();

            result = sb.toString();
        } catch (Exception e) {
            return "Fail to convert net stream!";
        }

        return result;
    }

//第三种

    /**
     * 获取指定地址的网页数据
     * 返回数据流
     * Chen.Zhidong
     * 2011-02-18
     */
    public static InputStream streampost(String remote_addr) {
        URL infoUrl = null;
        InputStream inStream = null;
        try {
            infoUrl = new URL(remote_addr);
            URLConnection connection = infoUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return inStream;
    }
}
