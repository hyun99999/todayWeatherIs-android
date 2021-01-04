package com.example.layouttest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomActivity extends AppCompatActivity {
    public final static int REQUEST_CODE_COLOR_PALETTE_DIALOG = 101;

    static String emotion;
    final int GET_GALLERY_IMAGE = 200;
    //ImageView imageView2;
    ImageButton insertPhoto,textBtn,downBtn;
    Button forecastBtn,galleryBtn;
    TextView output;
    View dialogView;
    ConstraintLayout con;
    //스티커
    LinearLayout canvasLayout,stickerLayout;
    PaintBoard board;
    double temp,tempFile;
    MyGraphicView stickerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom);
        canvasLayout=(LinearLayout)findViewById(R.id.canvasLayout);
        downBtn=(ImageButton)findViewById(R.id.downBtn);
        textBtn = (ImageButton) findViewById(R.id.textBtn);

        //intent 이용한 temp 받기
        Intent intent = getIntent();
        temp = intent.getExtras().getDouble("temp");
        String weatherMain=intent.getExtras().getString("weatherMain");
        output = (TextView)findViewById(R.id.ouput);
        output.setText(Double.toString(temp));
        //메인가는버튼
        forecastBtn = (Button)findViewById(R.id.forecastBtn);
        forecastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
        con=(ConstraintLayout)findViewById(R.id.con);
        //배경설정
        if(weatherMain.equals("Clear")){
            con.setBackgroundResource(R.drawable.clear_background);
        }

        else if(weatherMain.equals("Clouds")) {
            con.setBackgroundResource(R.drawable.clouds_background);
        }

        else if(weatherMain.equals("Drizzle")) {
            con.setBackgroundResource(R.drawable.rain_background);
        }

        else if(weatherMain.equals("Rain")) {
            con.setBackgroundResource(R.drawable.rain_background);
        }

        else if(weatherMain.equals("Thunderstorm")) {
            con.setBackgroundResource(R.drawable.thunderstorm_background);
        }
        else if(weatherMain.equals("Snow")){
            con.setBackgroundResource(R.drawable.snow_background);
        }
        else {
            con.setBackgroundResource(R.drawable.mist_background);
        }
        textBtn.setEnabled(false);
        downBtn.setEnabled(false);

        //카메라 이미지 받기
        //imageView2=(ImageView)findViewById(R.id.imageView2);
        insertPhoto = (ImageButton)findViewById(R.id.insertPhoto);
        insertPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent,GET_GALLERY_IMAGE);
                insertPhoto.setEnabled(false);
                textBtn.setEnabled(true);
                downBtn.setEnabled(true);
            }
        });


        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( CustomActivity.this, new String[] {  Manifest.permission.READ_EXTERNAL_STORAGE  },
                            0 );
                }
                if (ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions( CustomActivity.this, new String[] {  Manifest.permission.WRITE_EXTERNAL_STORAGE },
                            0 );
                }
                //온도대로 파일만들기
                tempFile = Math.round(temp);

                String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/gallery/" + tempFile + "/";
                File file = new File(path);
                if(!file.exists()){
                    file.mkdirs();
                    Toast.makeText(getApplicationContext(), "폴더가 생성되었습니다.", Toast.LENGTH_SHORT).show();

                }
                SimpleDateFormat day = new SimpleDateFormat("yyyyMMddHHmmss");
                Date date = new Date();

                canvasLayout.buildDrawingCache();
                Bitmap captureView = canvasLayout.getDrawingCache();

                FileOutputStream fos=null;
                try {
                    fos = new FileOutputStream(path+day.format(date)+".jpeg");
                    captureView.compress(Bitmap.CompressFormat.JPEG,100,fos);

                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path  + day.format(date) + ".JPEG")));
                    Toast.makeText(getApplicationContext(),"저장!",Toast.LENGTH_SHORT).show();
                    fos.flush();
                    fos.close();
                    canvasLayout.destroyDrawingCache();

                } catch (FileNotFoundException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dlg
                /*
                dialogView = (View)View.inflate(CustomActivity.this,R.layout.activity_color_palette_dialog,null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(CustomActivity.this);
                dlg.setView(dialogView);
                //dlg.setPositiveButton("확인",null);
                dlg.show();

                 */

                Intent intent = new Intent(getApplicationContext(), ColorPaletteDialog.class);
                startActivityForResult(intent, REQUEST_CODE_COLOR_PALETTE_DIALOG);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_COLOR_PALETTE_DIALOG){
            if(resultCode == RESULT_OK){
                @ColorInt int color = data.getIntExtra("color", 0xff000000);
                board.setPaintColor(color);
            }
        }
        else if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();
                //Uri selectedImage = data.getData();
                /*
                //스티커추가
                stickerLayout = (LinearLayout)findViewById(R.id.stickerLayout);
                stickerView=(MyGraphicView)new MyGraphicView(this);
                stickerLayout.addView(stickerView);

                canvasLayout = (LinearLayout) findViewById(R.id.canvasLayout);
                backgroundView=(MyGraphicView2) new MyGraphicView2(this);
                backgroundView.setBackground(img);
                canvasLayout.addView(backgroundView);
                 */
                Drawable drawable = new BitmapDrawable(img);
                canvasLayout.setBackground(drawable);
                board=new PaintBoard(this);
                //board.setBackground(img);
                canvasLayout.addView(board);

            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //캔버스에 스티커그리기
    private static class MyGraphicView extends View{
        Bitmap sticker;
        int dx, dy;
        public MyGraphicView(Context context){
            super(context);
            //본인이 느끼기에 따라 이모티콘이 달라짐
            sticker = BitmapFactory.decodeResource(context.getResources(),R.drawable.clear_sky_icon);
            dx=430;
            dy=650;
        }
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            sticker = Bitmap.createScaledBitmap(sticker,400,400,false);
            canvas.drawBitmap(sticker,dx,dy,null);

        }
        public boolean onTouchEvent(MotionEvent event){
            switch (event.getAction()){

                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
// 주루룩 drag했을 경우 히스토리가 모두 기록되어서 전달됨
                    int length=event.getHistorySize();
                    float sx, sy, ex, ey;

                    if (length != 0) {
                        sx = event.getHistoricalX(0);
                        sy = event.getHistoricalY(0);
                        ex = event.getHistoricalX(length-1);
                        ey = event.getHistoricalY(length-1);

                        dx += (int)(ex-sx);
                        dy += (int)(ey-sy);
                    }
                    invalidate();
                    break;
            }
            return true;
        }
    }


}
