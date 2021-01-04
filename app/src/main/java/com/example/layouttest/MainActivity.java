package com.example.layouttest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static String BaseUrl = "https://api.openweathermap.org/";
//    public static String lat = "37.57";
//    public static String lon = "126.98";
    //동영상에서 위도경도 바꿔서 도시이름과 날씨 바뀌는거 보여주기.
    public static double lat;
    public static double lon;
    public static double temp;
    private final static String ServiceKey = "9069b5b58e772835054265a4768784c8";
    public static String weatherMain;
    Button customBtn,galleryBtn;
    ImageView weatherIcon,recommendView;
    TextView weatherText, cityText,tempText;
    ConstraintLayout constraintLayout;
    File[] imageFiles;
    String imageFname;
    String tempFile;
    myPictureView mypicture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recommendView=(ImageView)findViewById(R.id.recommendView);
        weatherIcon = (ImageView) findViewById(R.id.weatherIcon);
        cityText=(TextView)findViewById(R.id.cityText);
        tempText = (TextView) findViewById(R.id.tempText);
        weatherText = (TextView) findViewById(R.id.weatherText);
        customBtn = (Button) findViewById(R.id.customBtn);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        mypicture = (myPictureView) findViewById(R.id.myPictureView);

        //위도 경도 구하기
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final LocationListener gpsLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
//                lat = location.getLatitude();
//                lon = location.getLongitude();
                lon = 126.98;
                lat = 37.57;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( MainActivity.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                    0 );
        }
        else{
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            lat = location.getLatitude();
//            lon = location.getLongitude();
            lon = 126.98;
            lat = 37.57;
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
        }



        //RETROFIT
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService service = retrofit.create(RetrofitService.class);
        Call<WeatherTest> call = service.getWeather(Double.toString(lat),Double.toString(lon), ServiceKey);
        //비동기  처리
        call.enqueue(new Callback<WeatherTest>() {
            @Override
            public void onResponse(Call<WeatherTest> call, Response<WeatherTest> response) {
                if (response.isSuccessful()) {
                    WeatherTest weatherTest = response.body();

                    //도시와 온도 표현
                    temp = Math.round((weatherTest.main.temp-274.15)*10)/10.0;

                    tempFile = Double.toString(Math.round(temp));
                    setPictureVeiw(tempFile);

                    //배경설정
                    //String stringBuilder = weatherTest.name + "\n" + temp;
                    cityText.setText(weatherTest.name);
                    tempText.setText(Double.toString(temp));

                    //날씨 아이콘,날씨 텍스트,배경화면 바꿈. json 데이터가 배열v로 시작하면 인덱스 0먼에서 찾아야함.
                    weatherMain = weatherTest.weather.get(0).main;
                    if(weatherMain.equals("Clear")){
                            weatherIcon.setImageResource(R.drawable.clear_sky_icon);
                            weatherText.setText("맑음");
                            constraintLayout.setBackgroundResource(R.drawable.clear_background);
                        }

                    else if(weatherMain.equals("Clouds")) {
                        weatherIcon.setImageResource(R.drawable.cloud_icon);
                        weatherText.setText("흐림");
                        constraintLayout.setBackgroundResource(R.drawable.clouds_background);
                    }

                    else if(weatherMain.equals("Drizzle")) {
                        weatherIcon.setImageResource(R.drawable.rain_icon);
                        weatherText.setText("이슬비");
                        constraintLayout.setBackgroundResource(R.drawable.rain_background);
                    }

                    else if(weatherMain.equals("Rain")) {
                        weatherIcon.setImageResource(R.drawable.shower_rain_icon);
                        weatherText.setText("비");
                        constraintLayout.setBackgroundResource(R.drawable.rain_background);
                    }

                    else if(weatherMain.equals("Thunderstorm")) {
                        weatherIcon.setImageResource(R.drawable.thunderstorm_icon);
                        weatherText.setText("천둥");
                        constraintLayout.setBackgroundResource(R.drawable.thunderstorm_background);
                    }
                    else if(weatherMain.equals("Snow")){
                        weatherIcon.setImageResource(R.drawable.snow_icon);
                        weatherText.setText("눈");
                        constraintLayout.setBackgroundResource(R.drawable.snow_background);
                    }
                    else {
                        weatherIcon.setImageResource(R.drawable.mist_icon);
                        weatherText.setText("안개");
                        constraintLayout.setBackgroundResource(R.drawable.mist_background);
                    }

                    Log.v("Test", "on Response");
                }
                else
                    Log.v("Test", "on unSuccess");
            }

            @Override
            public void onFailure(Call<WeatherTest> call, Throwable t) {
                Log.e("onFailure", t.toString());
                tempText.setText("fail");
            }
        });

        customBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CustomActivity.class);
                //intent 로 custom 화면에서 온도를 다룰 수 있도록 함.
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
    }
    public void setPictureVeiw(String tempFile){
        //날씨추천
        //Toast.makeText(getApplicationContext(),tempFile,Toast.LENGTH_LONG).show();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/gallery/" + tempFile + "/";
        //파일의유무확인
        File file = new File(path);
        if(!file.exists()){
            //file.mkdirs();
            Toast.makeText(getApplicationContext(), "날씨는 오늘 어떤가요? 날씨를 추가해주세요.", Toast.LENGTH_LONG).show();
            //mypicture.imagePath=null;
        }
        else {
            int index;
            Random ran = new Random();
            //날씨추천
            //Toast.makeText(getApplicationContext(), "날씨는오늘", Toast.LENGTH_LONG).show();
            imageFiles = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/gallery/" + tempFile).listFiles();
            index = ran.nextInt(imageFiles.length)+0;
            imageFname = imageFiles[index].toString();
            //imageFname = imageFiles[0].toString();
            Bitmap bitmap = BitmapFactory.decodeFile(imageFname);
            recommendView.setImageBitmap(bitmap);

            //인덱스 length 사이로 랜덤하게 설정하면 추천날씨.
        }
    }

}
