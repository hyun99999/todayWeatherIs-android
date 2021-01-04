package com.example.layouttest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;

public class FileList extends Activity {
    ListView listView;
    Button forecastBtn,customBtn;
    ArrayList <String> fName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_list);
        initView();
        String path = Environment.getExternalStorageState();
        if(path.equals(Environment.MEDIA_MOUNTED)){
            findFolder();
        }
        Intent intent = getIntent();
        double temp = intent.getExtras().getDouble("temp");
        String weatherMain=intent.getExtras().getString("weatherMain");

        forecastBtn = (Button)findViewById(R.id.forecastBtn);
        forecastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        customBtn=(Button)findViewById(R.id.customBtn);
        customBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CustomActivity.class);
                intent.putExtra("temp",temp);
                intent.putExtra("weatherMain",weatherMain);
                startActivity(intent);
            }
        });
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getApplicationContext(),GalleryList.class);
            intent.putExtra("positionTemp",fName.get(position));
            intent.putExtra("weatherMain",weatherMain);
            //Toast.makeText(getApplicationContext(),fName.get(position),Toast.LENGTH_SHORT).show();
            intent.putExtra("temp",temp);
            startActivity(intent);
        }
    });
    }
    private void findFolder() {
        fName = new ArrayList<String>();
        File files = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/gallery");
        if(files.exists()) {
            ArrayAdapter<String> filelist = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fName);
            if (files.listFiles().length > 0) {
                for (File file : files.listFiles()) {
                    fName.add(file.getName());
                }
            }
            files = null;
            listView.setAdapter(filelist);
        }
    }

    private void initView() {
        listView = (ListView)findViewById(R.id.listView);
    }
}
