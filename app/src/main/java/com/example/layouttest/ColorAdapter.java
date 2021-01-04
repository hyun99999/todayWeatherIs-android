package com.example.layouttest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class ColorAdapter extends BaseAdapter {

    Context mContext;
    public static final int[] colors = {0xff000000,0xff00007f,0xff0000ff,0xff007f00,0xff007f7f,0xff00ff00,0xff00ff7f,
            0xff00ffff,0xff7f007f,0xff7f00ff,0xff7f7f00,0xff7f7f7f,0xffff0000,0xffff007f,
            0xffff00ff,0xffff7f00,0xffff7f7f,0xffff7fff,0xffffff00,0xffffff7f,0xffffffff};


    int rowCount;
    int colCount;

    public ColorAdapter(Context context) {
        super();
        mContext = context;

        rowCount = 3;
        colCount = 7;
    }

    @Override
    public int getCount() {
        return rowCount * colCount;
    }

    @Override
    public Object getItem(int position) {
        return colors[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GridView.LayoutParams params = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT
                , GridView.LayoutParams.MATCH_PARENT);

        /* 그리드뷰의 각 아이템이 보이는 뷰 설정*/
        TextView view = new TextView(mContext);
        view.setText(" ");
        view.setLayoutParams(params);
        view.setPadding(4, 4, 4, 4);
        view.setBackgroundColor(colors[position]);
        view.setHeight(120);

        return view;
    }
}
