package com.example.dongnd.screenenglish;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Weather extends AppCompatActivity {

    private TextView  temperature,temperate_current;
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
        temperature.setOnClickListener(lsTemperature);

        temperate_current=(TextView)findViewById(R.id.temperature_cureent);
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
                Toast.makeText(getApplicationContext(), "Bật 3G và GPS để cập nhật thời tiết", Toast.LENGTH_SHORT).show();
                editor.putBoolean("stateWeather", true);
                editor.commit();

                weather.setImageResource(R.drawable.switch_on);
            }
        }
    };

    public View.OnClickListener lsTemperature=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showDialog();
        }
    };

    public void showDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_check);
        builder.setTitle("Nhiệt độ: ");

        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(Weather.this, android.R.layout.select_dialog_singlechoice);
        adapter.add("°C");
        adapter.add("°F");

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               Toast.makeText(getApplicationContext(), adapter.getItem(i), Toast.LENGTH_SHORT).show();
                if(i==0){
                    temperate_current.setText("Hiển thị: °C");
                    editor.putString("temperate","C");
                    editor.commit();
                }else if(i==1){
                    temperate_current.setText("Hiển thị: °F");
                    editor.putString("temperate", "F");
                    editor.commit();
                }
            }
        });

        builder.show();
    }

}
