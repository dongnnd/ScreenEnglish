package com.example.dongnd.screenenglish;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Weather extends AppCompatActivity {

    private TextView  temperature;
    private ImageView weather;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        sharedPreferences=getSharedPreferences("data", MODE_PRIVATE);
        editor=sharedPreferences.edit();

        init();

    }

    public void init(){

        weather=(ImageView) findViewById(R.id.weather_img);
        if(sharedPreferences.getBoolean("stateWeather", false)){
            weather.setImageResource(R.drawable.switch_on);
        }else{
            weather.setImageResource(R.drawable.switch_off);
        }
        weather.setOnClickListener(listenerWeather);

        temperature=(TextView)findViewById(R.id.temperature);
    }

    public View.OnClickListener listenerWeather=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean stateWeather=sharedPreferences.getBoolean("stateWeather", false);
            if(stateWeather){
                editor.putBoolean("stateWeather", false);
                editor.commit();

                weather.setImageResource(R.drawable.switch_off);
            }else{
                editor.putBoolean("stateWeather", true);
                editor.commit();

                weather.setImageResource(R.drawable.switch_on);
            }
        }
    };

}
