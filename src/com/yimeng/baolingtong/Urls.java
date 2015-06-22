package com.yimeng.baolingtong;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.text.TextUtils;
import android.util.Log;

public class Urls {

    /*
     * beta-search.okjob.me 求职搜索站点URL http://192.168.1.172:81
     * beta-person.okjob.me http://192.168.1.172:83 beta-company.okjob.me
     * 企业站点URL http://192.168.1.172:84 beta-learn.okjob.me 学习中心URL
     * http://192.168.1.172:85 beta-file.okjob.me http://192.168.1.172:86
     * beta-webcompany.okjob.me 企业WEB URL http://192.168.1.172:88
     * beta-appcompany.okjob.me 企业mobile URL http://192.168.1.172:89
     */
    public static boolean DEBUG = false;

    // 内网
    public static String BASE_URL = "http://www.tabdw.com/tabwapp/tabwapp";
  

 
    /**
     * 请求地址添加参数
     * 
     * @param url
     * @param parmas
     * @return
     */
    public static String getUrlAppendPath(String url, NameValuePair... parmas) {
        StringBuilder sb = new StringBuilder(url);
        if (parmas != null) {
            if (!url.endsWith("/")) {
                sb.append("/");
            }
            for (int i = 0; i < parmas.length; i++) {
                String key = parmas[i].getName();
                String value = parmas[i].getValue();
                if (TextUtils.isEmpty(value)) {
                    value = "";
                    continue;
                }
                try {
                    value = URLEncoder.encode(value, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                sb.append(key).append("/").append(value);
                if (i != parmas.length - 1) {
                    sb.append("/");
                }
            }
        }
        Log.e("URL", sb.toString());
        return sb.toString();
    }

    /**
     * 请求地址添加参数
     * 
     * @param url
     * @param parmas
     * @return
     */
    public static String getUrlAppendPath(String url, ArrayList<NameValuePair> parmas) {
        StringBuilder sb = new StringBuilder(url);
        if (parmas != null) {
            if (!url.endsWith("/")) {
                sb.append("/");
            }
            for (int i = 0; i < parmas.size(); i++) {
                String key = parmas.get(i).getName();
                String value = parmas.get(i).getValue();
                if (value == null) {
                    value = "";
                }
                try {
                    value = URLEncoder.encode(value, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                sb.append(key).append("/").append(value);
                if (i != parmas.size() - 1) {
                    sb.append("/");
                }
            }
        }
        Log.e("URL", sb.toString());
        return sb.toString();
    }
}
