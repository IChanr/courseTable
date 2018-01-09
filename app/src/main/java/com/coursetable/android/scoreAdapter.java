package com.coursetable.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.coursetable.android.model.Score;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by H on 2018/1/9.
 */

public class scoreAdapter extends BaseAdapter {
    private List<Score> list;
    private Context mcontext;

    public scoreAdapter(List<Score> list, Context mcontext) {
        this.list = list;
        this.mcontext = mcontext;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1 = LayoutInflater.from(mcontext).inflate(R.layout.score_item, null);
        TextView tvCourseName = view1.findViewById(R.id.tv_coursename);
        TextView tvYears = view1.findViewById(R.id.tv_years);
        TextView tvSemester = view1.findViewById(R.id.tv_semester);
        TextView tvCredit = view1.findViewById(R.id.tv_credit);
        TextView tvPerfenmancepoints = view1.findViewById(R.id.tv_perfermancepoints);
        TextView tvScore = view1.findViewById(R.id.tv_score);
        Score score = list.get(i);
        tvCourseName.setText(score.getCourseName());
        tvYears.setText("学年:" + score.getYears());
        tvSemester.setText("    学期:" + score.getSemester());
        tvCredit.setText("  学分:" + score.getCredit());
        tvPerfenmancepoints.setText("绩点:" + score.getPerformancePoints());
        tvScore.setText("   成绩:" + score.getScore());
        return view1;
    }
}
