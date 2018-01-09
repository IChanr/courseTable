package com.coursetable.android;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Caihan on 2017/12/9.
 */


public class api {
    private String IP = "http://222.205.193.20/default2.aspx";
    private String HOST = "222.205.192.20";
    private String yzmIP = "http://222.205.193.20/CheckCode.aspx";
    private String TAG = "输出-";
    private OkHttpClient client;
    private String Cookie = "";
    private String __VIEWSTATE;
    private Dialog dialog;

    public void init() {
        client = new OkHttpClient();
        Request request = new Request.Builder().url(IP).build();
        try {
            Response response = client.newCall(request).execute();
            String data = response.body().string();
            String cookiestring = response.header("Set-Cookie");
            String cookiedata[] = cookiestring.split(";");
            Cookie = cookiedata[0];
            Document document = Jsoup.parse(data);
            Elements elements = document.select("form[id=form1]");
            __VIEWSTATE = elements.select("input").attr("value");
            Log.d(TAG, "init: " + __VIEWSTATE);
            Log.d(TAG, "init: " + Cookie);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Login(final String userName, final String passWord, final String YZM) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody requestBody = new FormBody.Builder().add("__VIEWSTATE",
                        __VIEWSTATE).add("TextBox1", userName).add("TextBox2", passWord).add
                        ("TextBox3", YZM).add("RadioButtonList1", "%D1%A7%C9%FA").add
                        ("Button1", "").build();
                Request request = new Request.Builder().url(IP).addHeader("Cookie", Cookie)
                        .addHeader("HOST", HOST).addHeader("Referer", IP).post(requestBody).build();
 
            }
        }).start();
    }

    public void showProgress(Context context) {
        dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.progress_layout, null);

        Button btn = view.findViewById(R.id.btn_cancel);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        //dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(view);
        dialog.show();
    }

    public void dissmissProgress() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
