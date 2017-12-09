package com.coursetable.android;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.coursetable.android.model.Course;
import com.coursetable.android.sql.MySql;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ui_Main extends AppCompatActivity implements View.OnClickListener {
    private Map<Integer, Course> map = new HashMap<>();
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private String IP = "http://222.205.193.20/default2.aspx";
    private String HOST = "222.205.192.20";
    private String yzmIP = "http://222.205.193.20/CheckCode.aspx";
    private String TAG = "输出-";
    private OkHttpClient client;
    private String Cookie = "";
    private String __VIEWSTATE = "";
    private ImageView imgDialogYzm;
    private GridView gridView;
    private Myadapter myadapter;
    private MySql mySql;
    private SQLiteDatabase db;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui__main);
        initView();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                api a = new api();
                a.init();
            }
        }).start();

    }

    private void initView() {
        Log.d(TAG, "initView: 12-9");
        mySql = new MySql(ui_Main.this, "table.db", null, 1);
        db = mySql.getWritableDatabase();
        readSql();
        myadapter = new Myadapter(map, ui_Main.this);
        gridView = findViewById(R.id.gridview);
        gridView.setAdapter(myadapter);

        client = new OkHttpClient().newBuilder().followRedirects(false).followSslRedirects(false)
                .build();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView
                .OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_menu_cjcx:
                        Toast.makeText(ui_Main.this, "成绩查询开发中", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_menu_setting:
                        Toast.makeText(ui_Main.this, "设置开发中", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_menu_tsgc:
                        Toast.makeText(ui_Main.this, "图书馆藏开发中", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_fresh:
                loginDialog();
                break;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
            default:
                break;
        }
        return true;
    }

    private void loginDialog() {
        dialog = new Dialog(ui_Main.this);
        View view = LayoutInflater.from(ui_Main.this).inflate(R.layout.dialog_item, null);
        imgDialogYzm = view.findViewById(R.id.img_dialog_yzm);
        Button btnLogin = view.findViewById(R.id.btn_dialog_login);
        final EditText name = view.findViewById(R.id.et_dialog_username);
        final EditText pass = view.findViewById(R.id.et_dialog_password);
        final EditText yzm = view.findViewById(R.id.et_dialog_yzm);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String names = name.getText().toString().trim();
                String passs = pass.getText().toString().trim();
                String yzms = yzm.getText().toString().trim();
                if (names.equals("") && passs.equals("") && yzms.equals("")) {
                    Toast.makeText(ui_Main.this, "不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    Login(names, passs, yzms);
                }
            }
        });
        imgDialogYzm.setOnClickListener(this);
        dialog.setContentView(view);
        new Thread(new Runnable() {
            @Override
            public void run() {
                loginInit();
            }
        }).start();
        ;

        dialog.show();
        Window dialogWindow = dialog.getWindow();
//        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.y = 0;
        dialogWindow.setAttributes(lp);


    }


    public Bitmap loginInit() {

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

            request = new Request.Builder().url(yzmIP).addHeader("Cookie", Cookie).build();
            Response response1 = client.newCall(request).execute();
            final Bitmap bitmap = BitmapFactory.decodeStream(response1.body().byteStream());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imgDialogYzm.setImageBitmap(bitmap);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void Login(final String userName, final String passWord, final String YZM) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.delete("tableInfo", null, null);
                map.clear();
                RequestBody requestBody = new FormBody.Builder().add("__VIEWSTATE",
                        __VIEWSTATE).add("TextBox1", userName).add("TextBox2", passWord).add
                        ("TextBox3", YZM).add("RadioButtonList1", "%D1%A7%C9%FA").add
                        ("Button1", "").build();
                Request request = new Request.Builder().url(IP).addHeader("Cookie", Cookie)
                        .addHeader("HOST", HOST).addHeader("Referer", IP).post(requestBody).build();


                try {

                    Response response = client.newCall(request).execute();
                    if (response.code() == 302) {
                        String tempIP = "http://222.205.193.20" + response.header("Location");
                        Log.d(TAG, "run: " + tempIP);

                        Request request1 = new Request.Builder().url(tempIP).addHeader("Cookie",
                                Cookie)

                                .addHeader("Referer", IP).get().build();
                        Response response1 = client.newCall(request1).execute();
                        Document document = Jsoup.parse(response1.body().string());
                        Elements elements = document.select("ul");
                        Log.d(TAG, "run: " + elements.html());
                        String stuName = elements.select("span[id=xhxm]").html();
                        stuName = stuName.substring(0, stuName.length() - 2);
                        stuName = URLEncoder.encode(stuName);
                        Log.d(TAG, "run: " + stuName);
                        String tempIP2 = "http://222.205.193.20/xskbcx" +
                                ".aspx?xh=" + userName + "&xm=" + stuName + "&gnmkdm=N121603";
                        Request request2 = new Request.Builder().url(tempIP2).addHeader("Cookie",
                                Cookie).addHeader("Referer", "http://222.205.193.20/xs_main" +
                                ".aspx?xh=1606117035").get().build();
                        Response response2 = client.newCall(request2).execute();
                        Document document1 = Jsoup.parse(response2.body().string());
                        Log.d(TAG, "run: " + document1.html());
                        int test=100;
                        Elements elements1 = document1.select("table[id=Table1]");
                        String zhengze = ">.{4,15}<br>.{4},.{2}.{3}-\\d+.{2}<br>.{1,4}<br>.{3," +
                                "8}<br>";
                        String zhengze2=">?[^<>br]{4,15}<br>.{4},.{2}.{3}-\\d+.{2}<br>.{1,4}<br>" +
                                ".{3,8}<br>";
                        //12-8 正则修改
                        Pattern pattern = Pattern.compile(zhengze2);
                        Matcher matcher = pattern.matcher(elements1.html());
                        while (matcher.find()) {
                            String[] data = matcher.group().substring(1).split("<br>");
                            Course course = new Course(data[0], data[2], data[1], data[3]);
                            map.put(course.position(course.getCourseTime()), course);
                            writeSql(course);
                            Log.d(TAG, "run: " + course.toString());
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                myadapter = new Myadapter(map, ui_Main.this);
                                gridView.setAdapter(myadapter);
                                Toast.makeText(ui_Main.this, "数据更新完成!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Log.d(TAG, "run: 登陆错误提示"+response.body().string());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void readSql() {
        Cursor cursor = db.query("tableInfo", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Course course = new Course(cursor.getString(cursor.getColumnIndex("courseName")),
                        cursor.getString(cursor.getColumnIndex("courseTeacher")), cursor.getString
                        (cursor.getColumnIndex("courseTime")), cursor.getString(cursor
                        .getColumnIndex("courseLocation")));
                map.put(course.position(course.getCourseTime()), course);
                Log.d(TAG, "readSql: " + course.toString());
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG, "readSql: " + "完全没读取到数据");
        }
    }

    private void deleteAllSql() {
        db.delete("tableInfo", null, null);
    }

    private void writeSql(Course data) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("courseName", data.getCourseName());
        contentValues.put("courseTeacher", data.getCourseTeacher());
        contentValues.put("courseTime", data.getCourseTime());
        contentValues.put("courseLocation", data.getCourseLocation());
        db.insert("tableInfo", null, contentValues);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_dialog_yzm:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loginInit();
                    }
                }).start();
                break;
            default:
                break;
        }
    }
}