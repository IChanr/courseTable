package com.coursetable.android;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.coursetable.android.model.Course;

import java.util.Map;

/**
 * Created by Caihan on 2017/12/9.
 */

public class Myadapter extends BaseAdapter {
    private Map<Integer,Course> map;
    private Context mcontent;

    public Myadapter(Map<Integer, Course> map, Context mcontent) {
        this.map = map;
        this.mcontent = mcontent;
    }

    @Override
    public int getCount() {
        return 56 ;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view= LayoutInflater.from(mcontent).inflate(R.layout.adapet_item,null);
        TextView textView=view.findViewById(R.id.item_tv);
        if(map.get(position)==null){
            textView.setText("");
        }else {
            Course course=map.get(position);
            textView.setText(course.toString());
            textView.setBackgroundColor(Color.parseColor("#f2f2f2"));
        }
        return view;
    }
}
