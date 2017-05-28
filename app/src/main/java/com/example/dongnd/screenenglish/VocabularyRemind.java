package com.example.dongnd.screenenglish;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import recevier.AlarmReceiver;
import recevier.BootReceiver;

public class VocabularyRemind extends AppCompatActivity {

    private TextView rd_kichhoat, rd_settime, rd_time, rd_setdate, rd_date;
    private ImageView rd_img;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_remind);

        sharedPreferences=getSharedPreferences("data", MODE_PRIVATE);
        editor=sharedPreferences.edit();

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        init();
        loadState();
    }

    public void init(){

        rd_kichhoat=(TextView)findViewById(R.id.rd_kichhoat);


        rd_img=(ImageView)findViewById(R.id.rd_img);

        rd_img.setOnClickListener(kichhoat);

        rd_settime=(TextView)findViewById(R.id.rd_settime);
        rd_settime.setOnClickListener(setTime);
        rd_time=(TextView)findViewById(R.id.rd_time);

        rd_setdate=(TextView)findViewById(R.id.rd_setdate);
        rd_setdate.setOnClickListener(setDate);
        rd_date=(TextView)findViewById(R.id.rd_date);


    }

    public void loadState(){
        if(sharedPreferences.getBoolean("stateRemind",false)){
            rd_img.setImageResource(R.drawable.switch_on);
        }else{
            rd_img.setImageResource(R.drawable.switch_off);
        }

        if(sharedPreferences.getInt("hourRemind", -1)==-1){
            rd_time.setText("Thời gian nhắc nhở: --:--");
        }else{
            rd_time.setText("Thời gian nhắc nhở: "+sharedPreferences.getInt("hourRemind",0)+":"+
            sharedPreferences.getInt("minuteRemind", 0));
        }

        String day=sharedPreferences.getString("dayRemind","");
        if(day!=""){

            rd_date.setText(getDate(day));
        }else{
            rd_date.setText("Bạn chưa thiết lập");
        }

    }

    public String getDate(String day){
        String result="";
        String[] arrDay=day.split(",");
        for(int i=0;i<arrDay.length;i++){
            switch (arrDay[i]){
                case "Thứ hai":
                    if(result==""){
                        result+="T2";
                    }else{
                        result+=",T2";
                    }
                    break;
                case "Thứ ba":
                    if(result==""){
                        result+="T3";
                    }else{
                        result+=",T3";
                    }
                    break;
                case "Thứ tư":
                    if(result==""){
                        result+="T4";
                    }else{
                        result+=",T4";
                    }
                    break;
                case "Thứ năm":
                    if(result==""){
                        result+="T5";
                    }else{
                        result+=",T5";
                    }
                    break;
                case "Thứ sáu":
                    if(result==""){
                        result+="T6";
                    }else{
                        result+=",T6";
                    }
                    break;
                case "Thứ bảy":
                    if(result==""){
                        result+="T7";
                    }else{
                        result+=",T7";
                    }
                    break;
                case "Chủ nhật":
                    if(result==""){
                        result+="CN";
                    }else{
                        result+=",CN";
                    }
                    break;
            }
        }
        return result;
    }

    public View.OnClickListener kichhoat=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean stateRemind=sharedPreferences.getBoolean("stateRemind", false);
            if(stateRemind){
                editor.putBoolean("stateRemind", false);
                editor.commit();
                rd_img.setImageResource(R.drawable.switch_off);
            }else{

                int hourRemind=sharedPreferences.getInt("hourRemind", -1);
                String dayRemind=sharedPreferences.getString("dayRemind","");
                if(hourRemind==-1||dayRemind.equals("")){
                    Toast.makeText(getApplicationContext(), "Thiết lập giờ, ngày nhắc nhở.", Toast.LENGTH_SHORT).show();
                }else{

                    startAlarm();

                    editor.putBoolean("stateRemind", true);
                    editor.commit();
                    rd_img.setImageResource(R.drawable.switch_on);
                }


            }
        }
    };

    public View.OnClickListener setTime=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showTimeDialog();
        }
    };

    public void showTimeDialog(){
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);


        TimePickerDialog timePickerDialog = new TimePickerDialog(VocabularyRemind.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        editor.putInt("hourRemind", hourOfDay);
                        editor.putInt("minuteRemind", minute);
                        editor.commit();
                        rd_time.setText("Thời gian nhắc nhở: "+hourOfDay+":"+minute);

                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }

    public View.OnClickListener setDate=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            choseDay();
        }
    };

    public void choseDay(){
        final String[] day=new String[]{"Thứ hai", "Thứ ba", "Thứ tư", "Thứ năm", "Thứ sáu",
                "Thứ bảy", "Chủ nhật"};

        final boolean[] check={false, false, false,false, false, false, false};
        List<String> data= Arrays.asList(day);

        final AlertDialog.Builder builder=new AlertDialog.Builder(VocabularyRemind.this);
        builder.setTitle("Chọn ngày");
        builder.setMultiChoiceItems(day, check, new AlertDialog.OnMultiChoiceClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                List<String> list=new ArrayList<String>();
                for(int j=0;j<check.length;j++){
                    boolean checked=check[j];
                    if(checked){
                        list.add(day[j]);
                    }

                }
                saveDate(list);
                rd_date.setText(getDate(sharedPreferences.getString("dayRemind","")));
            }
        });

        AlertDialog dialog=builder.create();
        dialog.show();
    }

    public void saveDate(List<String> list){
        String str="";
        if(list.size()!=0){
            if(list.size()==1){
                str+=list.get(0);
            }else{
                for(int i=0;i<list.size();i++){
                    if(i==list.size()-1){
                        str+=list.get(i);
                    }else{
                        str+=list.get(i)+",";
                    }
                }
            }
        }
        editor.putString("dayRemind", str);
        editor.commit();
    }

    public void startAlarm(){
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        int hourRemind=sharedPreferences.getInt("hourRemind", 0);
        int minuteRemind=sharedPreferences.getInt("minuteRemind", 0);

        Calendar calNow = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourRemind );
        calendar.set(Calendar.MINUTE, minuteRemind);

        if(calendar.compareTo(calNow)>0){
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }
}
