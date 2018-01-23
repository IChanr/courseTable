package com.coursetable.android;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.coursetable.android.model.Course;
import com.coursetable.android.model.Score;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ScoreActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private String IP = "http://222.205.193.20/default2.aspx";
    private String HOST = "http://222.205.193.20";
    private String imgIP = "http://222.205.193.20/CheckCode.aspx";
    private String Cookies;
    private String Years = "";
    private String Semester = "";
    private String __VIEWSTATE = "";
    private String CHENGJI__VIEWSTATE = "";
    private String Name;
    private OkHttpClient client;
    private String TAG = "输出内容----";
    private Spinner spYEARS, spSEMESTER;
    private Button btnFIND;
    private ListView listView;
    private Dialog dialog;
    private ImageView imgYzm;
    private List<Score> list = new ArrayList<>();
    private api mApi=new api();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        initView();

    }

    private void initView() {
        spSEMESTER = findViewById(R.id.sp_semester);
        spSEMESTER.setOnItemSelectedListener(this);
        spYEARS = findViewById(R.id.sp_years);
        spYEARS.setOnItemSelectedListener(this);
        btnFIND = findViewById(R.id.btn_find);
        btnFIND.setOnClickListener(this);
        listView = findViewById(R.id.list_score);
        client = new OkHttpClient();
    }

    private void getCookandImg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder().url(IP).build();
                try {
                    Response response = client.newCall(request).execute();
                    Document document = Jsoup.parse(response.body().string());
                    Elements elements = document.select("form[id=form1]");
                    __VIEWSTATE = elements.select("input").attr("value");
                    Log.d(TAG, "run: " + __VIEWSTATE);
                    String tempValue = response.header("Set-Cookie");
                    String[] data = tempValue.split(";");
                    Cookies = data[0];
                    Log.d(TAG, "run: " + Cookies);
                    request = new Request.Builder().url(imgIP).addHeader("Cookie", Cookies).build();
                    response = client.newCall(request).execute();
                    final InputStream inputStream = response.body().byteStream();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            imgYzm.setImageBitmap(bitmap);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_find:
                if (Semester.equals("") || Years.equals("")) {
                    Toast.makeText(ScoreActivity.this, "请选择好日期", Toast.LENGTH_SHORT).show();
                } else {
                    //就可以弹出登陆框获输入账号密码了
                    showDialog();
                }
                break;
            case R.id.img_dialog_yzm:
                getCookandImg();
                break;
            default:
                break;
        }
    }

    private void showDialog() {
        dialog = new Dialog(this);
        View view1 = LayoutInflater.from(ScoreActivity.this).inflate(R.layout.dialog_item, null);
        final EditText etName = view1.findViewById(R.id.et_dialog_username);
        final EditText etPass = view1.findViewById(R.id.et_dialog_password);
        final EditText etYzm = view1.findViewById(R.id.et_dialog_yzm);
        Button btnLogin = view1.findViewById(R.id.btn_dialog_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getdata(etName.getText().toString().trim(), etPass.getText().toString().trim(), etYzm.getText().toString().trim());
            }
        });
        imgYzm = view1.findViewById(R.id.img_dialog_yzm);
        imgYzm.setOnClickListener(this);
        getCookandImg();
        dialog.setContentView(view1);
        dialog.show();
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.y = 0;
        dialogWindow.setAttributes(lp);

    }

    private void getdata(final String name, final String pass, final String yzm) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                client = new OkHttpClient().newBuilder().followRedirects(false).followSslRedirects(false).build();
                RequestBody requestBody = new FormBody.Builder().add("__VIEWSTATE", __VIEWSTATE).add("TextBox1", name).add("TextBox2", pass).add("TextBox3", yzm).add("RadioButtonList1", "%D1%A7%C9%FA").add("Button1", "").build();
                Request request = new Request.Builder().url(IP).addHeader("Cookie", Cookies).post(requestBody).build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.code() == 302) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                mApi.showProgress(ScoreActivity.this);
                            }
                        });
                        Log.d(TAG, "run: " + "登陆成功");
                        String turnIP = HOST + response.header("Location");
                        Log.d(TAG, "run: " + turnIP);
                        Request request1 = new Request.Builder().url(turnIP).addHeader("Cookie", Cookies).build();
                        Response response1 = client.newCall(request1).execute();
                        Document document = Jsoup.parse(response1.body().string());
                        Elements elements = document.select("span[id=xhxm]");
                        String tempName = elements.html();
                        Name = tempName.substring(0, tempName.length() - 2);
                        Name = URLEncoder.encode(Name);

                        //用于获取成绩的__viewstate
                        String tempIP = HOST + "/xscj_gc.aspx?xh=" + name + "&xm=" + Name + "&gnmkdm=N121605";
                        Log.d(TAG, "run: " + tempIP);
                        Request request2 = new Request.Builder().url(tempIP).addHeader("Cookie", Cookies).addHeader("Referer", turnIP).build();
                        Response response2 = client.newCall(request2).execute();
                        Document document1 = Jsoup.parse(response2.body().string());
                        Elements elements1 = document1.select("form[id=Form1]");
                        CHENGJI__VIEWSTATE = elements1.select("input").attr("value");
                        Log.d(TAG, "run: " + CHENGJI__VIEWSTATE);

                        //开始获取成绩
                        RequestBody requestBody1 = new FormBody.Builder().add("__VIEWSTATE", CHENGJI__VIEWSTATE).add("ddlXN", Years).add("ddlXQ", Semester).add("Button1", "%B0%B4%D1%A7%C6%DA%B2%E9%D1%AF").build();
                        Request request3 = new Request.Builder().url(tempIP).addHeader("Cookie", Cookies).addHeader("Referer", tempIP).post(requestBody1).build();
                        Response response3 = client.newCall(request3).execute();
                        Document document2 = Jsoup.parse(response3.body().string());
                        //Log.d(TAG, "run: " + document2.html());
                        Elements elements2 = document2.select("table[id=Datagrid1]").select("tr");
                        //Log.d(TAG, "run: " + elements2.html());
                        //正则表达式
                        String zz = "2016-2017</td><td>.*?</td><td>.*?</td><td>.*?</td><td>.*?</td><td>.*?</td><td>.*?</td><td>.*?</td><td>.*?</td><td>";
                        Pattern pattern = Pattern.compile(zz);
                        String chengjiData = elements2.html().toString().replaceAll("\\s*", "");
                        Log.d(TAG, "run: " + chengjiData);
                        Matcher matcher = pattern.matcher(chengjiData);
                        while (matcher.find()) {
                            String[] data = matcher.group().split("</td><td>");
                            Score score = new Score(data[0], data[1], data[2], data[3], data[6], data[7], data[8]);
                            list.add(score);
                            Log.d(TAG, "run: " + score.toString());
                        }
                        Log.d(TAG, "run: 数据添加完毕 准备设置适配器");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog != null) {
                                    dialog.dismiss();
                                    mApi.dissmissProgress();
                                    Toast.makeText(ScoreActivity.this,"成绩如下!",Toast.LENGTH_SHORT).show();
                                    setList();
                                    listView.invalidate();
                                }
                            }
                        });
                    } else {
                        //登陆失败跳转到哪里了?
                        Document document = Jsoup.parse(response.body().string());
                        Elements elements = document.select("form[id=form1]").select("script");
                        final String tx = elements.html();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ScoreActivity.this, tx, Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.d(TAG, "run: " + elements.html());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void setList() {
        scoreAdapter scoreAdapter1 = new scoreAdapter(list, ScoreActivity.this);
        listView.setAdapter(scoreAdapter1);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.sp_semester:
                Semester = (String) spSEMESTER.getSelectedItem();
                Log.d(TAG, "onItemSelected: " + Semester);
                break;
            case R.id.sp_years:
                Years = (String) spYEARS.getSelectedItem();
                Log.d(TAG, "onItemSelected: " + Years);
                break;
            default:
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
