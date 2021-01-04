package com.example.layouttest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import java.io.File;

public class GalleryList extends Activity {
    ImageButton previewBtn,reviewBtn;
    Button customBtn,forecastBtn,galleryBtn;
    myPictureView mypicture;
    TextView tempView;
    int curNum;
    File[] imageFiles;
    String imageFname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_view);

        mypicture=(myPictureView)findViewById(R.id.myPictureView);

        Intent intent = getIntent();
        double temp = intent.getExtras().getDouble("temp");
        String positionTemp = intent.getExtras().getString("positionTemp");
        String weatherMain = intent.getExtras().getString("weatherMain");

        //Toast.makeText(getApplicationContext(),positionTemp,Toast.LENGTH_SHORT).show();
        tempView=(TextView)findViewById(R.id.tempView);
        tempView.setText(positionTemp);

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
        galleryBtn=(Button)findViewById(R.id.galleryBtn);
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),FileList.class);
                intent.putExtra("temp",temp);
                intent.putExtra("weatherMain",weatherMain);
                startActivity(intent);
            }
        });

        imageFiles = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/gallery/"+ positionTemp).listFiles();
        imageFname = imageFiles[0].toString();

        mypicture.imagePath=imageFname;

        reviewBtn=(ImageButton)findViewById(R.id.reviewBtn);
        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(curNum<=0){
                    Toast.makeText(getApplicationContext(),"첫번째 날씨입니다!",Toast.LENGTH_SHORT).show();
                }else{
                    curNum--;
                    imageFname = imageFiles[curNum].toString();
                    mypicture.imagePath=imageFname;
                    mypicture.invalidate();
                }
            }
        });
        previewBtn=(ImageButton)findViewById(R.id.previewBtn);
        previewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(curNum>=imageFiles.length-1){
                    Toast.makeText(getApplicationContext(), "마지막 날씨입니다!", Toast.LENGTH_SHORT).show();
                }else {
                    curNum++;
                    imageFname = imageFiles[curNum].toString();
                    mypicture.imagePath=imageFname;
                    mypicture.invalidate();
                }
            }
        });
    }
}
