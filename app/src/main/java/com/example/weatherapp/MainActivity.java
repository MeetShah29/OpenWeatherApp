package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    EditText cityTxt;
    Button getWeather;
    TextView tempView;
    String url = "api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}";
    String apiKey = "c2e55dbff55eae26384b366dc3e593e0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityTxt = findViewById(R.id.cityEdittxt);
        getWeather = findViewById(R.id.weatherBtn);
        tempView = findViewById(R.id.tv);
//
//        DownloadTask task = new DownloadTask();
//        String result = null;
//
//        try {
//            result = task.execute("https://community-open-weather-map.p.rapidapi.com/weather?lat=0&lon=0&callback=test&id=2172797&lang=null&units=%2522metric%2522%20or%20%2522imperial%2522&mode=xml%252C%20html&q=London%252Cuk").get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        Log.i("Contents of URL", result);
    }

//    public class DownloadTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... urls) {
//            String result = "";
//            URL url;
//
//            HttpURLConnection connection = null;
//
//            try {
//                url = new URL(urls[0]);
//                connection = (HttpURLConnection) url.openConnection();
//
//                InputStream in = connection.getInputStream();
//                InputStreamReader reader = new InputStreamReader(in);
//
//                int data = reader.read();
//
//                while (data != -1) {
//                    char current = (char) data;
//                    result += current;
//                    data = reader.read();
//                }
//                return result;
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                return "Failed";
//            }
//        }
//    }

    public void getWeather(View view) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApi myapi = retrofit.create(WeatherApi.class);
        Call<Example> exampleCall = myapi.getweather(cityTxt.getText().toString().trim(), apiKey);
        exampleCall.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                if (response.code() == 404) {
                    Toast.makeText(MainActivity.this,"Please enter valid city",Toast.LENGTH_SHORT).show();
                }else if(!(response.isSuccessful())){
                    Toast.makeText(MainActivity.this,response.code(),Toast.LENGTH_SHORT).show();
                }
                Example mydata= response.body();
                Main main =mydata.getMain();
                Double temp = main.getTemp();
                Integer temperature= (int)(temp-273.15);
                tempView.setText(String.valueOf(temperature)+"C");
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();


            }
        });
    }
}