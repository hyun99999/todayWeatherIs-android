package com.example.layouttest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.ColorInt;

public class ColorPaletteDialog extends Activity {

    GridView colorGrid;
    Button closeBtn;
    TextView textView;

    ColorAdapter adapter;

    Context mContext;
    @ColorInt
    int selectedColor;

    public ColorPaletteDialog() {
        super();
        mContext = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_palette_dialog);

        colorGrid = findViewById(R.id.colorGrid);
        closeBtn = findViewById(R.id.closeBtn);
        textView = findViewById(R.id.textView);
        adapter = new ColorAdapter(this);
        colorGrid.setAdapter(adapter);

        colorGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedColor = (int)adapter.getItem(position);
                textView.setTextColor(selectedColor);
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("color", selectedColor);
                setResult(RESULT_OK, intent);
                ((ColorPaletteDialog)mContext).finish();
            }
        });
    }

}