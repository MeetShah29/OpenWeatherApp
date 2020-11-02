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
    String apiKey = " "; // Will get Api Key after sign up under MY Account-> My Api Key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityTxt = findViewById(R.id.cityEdittxt);
        getWeather = findViewById(R.id.weatherBtn);
        tempView = findViewById(R.id.tv);
    }

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