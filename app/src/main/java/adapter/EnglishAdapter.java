package adapter;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.MediaPlayer;

import android.net.wifi.WifiManager;
import android.os.Vibrator;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dongnd.screenenglish.LockScreenActivity;
import com.example.dongnd.screenenglish.MainActivity;
import com.example.dongnd.screenenglish.PassSetting;
import com.example.dongnd.screenenglish.R;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import databases.DbAdapter;
import databases.Question;
import databases.Vocabulary;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by DongND on 3/17/2017.
 */

public class EnglishAdapter extends android.support.v4.view.PagerAdapter{

    private Activity context;
    private static EnglishAdapter englishAdapter;
    private LockScreenActivity lockScreenActivity=LockScreenActivity.getInstance();
    private DbAdapter db;


    //Khai bao man hinh screen english question
    private TextView eq_word, eq_spelling, eq_eg;
    private Button eq_btn1, eq_btn2, eq_btn3;
    private ImageView eq_speak;



    //Khai bao man hinh screen english setting
    private ImageView setting_wifi, setting_bluetooth, setting_brightless, setting_orientation;
    private ImageView setting_flash, setting_time, setting_calculator, setting_camera;
    private boolean flashState=false;
    private Camera camera;
    private Camera.Parameters parameters;
    private SeekBar setting_light;


    // Khai báo biến chung cho vocabulary và question
    private int id_question=-1;
    private String question=null;
    private String tr_answear=null;
    private String err1_answear=null;
    private String err2_answear=null;
    private String spelling=null;
    private byte[] sound=null;

    // Lay vocabulary
    public Vocabulary vc;
    public Question qs;


    // Khai bao sharepresetn
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    // Khai bao rung
    Vibrator vibrator;

    public EnglishAdapter(Activity context){
        this.context=context;
        englishAdapter=this;


        db=new DbAdapter(context);
        getQuestion();

        sharedPreferences=context.getSharedPreferences("data", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        vibrator= (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void getQuestion(){
        if(lockScreenActivity.vc==null){
            id_question=lockScreenActivity.qs.getQs_id();
            question=lockScreenActivity.qs.getQuestion();
            tr_answear=lockScreenActivity.qs.getTr_answer();
            err1_answear=lockScreenActivity.qs.getErr1_answer();
            err2_answear=lockScreenActivity.qs.getErr2_answer();
        }else{
            id_question=lockScreenActivity.vc.getVc_id();
            question=lockScreenActivity.vc.getWord();
            tr_answear=lockScreenActivity.vc.getTr_mean();
            err1_answear=lockScreenActivity.vc.getErr1_mean();
            err2_answear=lockScreenActivity.vc.getErr2_mean();
            spelling=lockScreenActivity.vc.getSpellingaa();
            sound=lockScreenActivity.vc.getSound();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;

    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        Object locationObject = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View locationView = new View(this.context);
        if (position == 0) {
            locationObject = ((LayoutInflater)locationObject).inflate(R.layout.english_question, null);
            container.addView((View)locationObject, 0);
            initEQuestion((View)locationObject);
            return locationObject;
        }
        if (position == 1) {
            locationObject = ((LayoutInflater)locationObject).inflate(R.layout.english_setting, null);
            container.addView((View)locationObject, 0);
            initSetting((View)locationObject);
            return locationObject;

        }


        return locationView;
    }

    @Override
    public int getCount() {
        return 2;
    }


    public void initSetting(View view){
        setting_wifi=(ImageView)view.findViewById(R.id.setting_wifi);
        setting_wifi.setOnClickListener(wifi);

        setting_bluetooth=(ImageView)view.findViewById(R.id.setting_bluetooth);
        //setting_bluetooth.setOnClickListener(bluetooth);

        setting_brightless=(ImageView)view.findViewById(R.id.setting_brightless);
        setting_brightless.setOnClickListener(brighless);

        setting_orientation=(ImageView)view.findViewById(R.id.setting_orientation);
        setting_orientation.setOnClickListener(rotate);

        setting_flash=(ImageView)view.findViewById(R.id.setting_flash);
        setting_flash.setOnClickListener(flash);

        setting_time=(ImageView)view.findViewById(R.id.setting_time);
        setting_time.setOnClickListener(time);

        setting_calculator=(ImageView)view.findViewById(R.id.setting_calculator);
        setting_calculator.setOnClickListener(settingEnglish);

        setting_camera=(ImageView)view.findViewById(R.id.setting_camera);
        setting_camera.setOnClickListener(cameralisnter);

        setting_light=(SeekBar)view.findViewById(R.id.setting_light);
        setting_light.setOnSeekBarChangeListener(lightChange);

        // Khởi tạo trạng thái của các cài đặt nhanh
        loadState();
    }


    public void initEQuestion(View view){
        eq_word=(TextView)view.findViewById(R.id.eq_word);

        eq_eg=(TextView)view.findViewById(R.id.eq_eg);



        eq_btn1=(Button)view.findViewById(R.id.eq_btn1);
        eq_btn1.setOnClickListener(answer);
        eq_btn2=(Button)view.findViewById(R.id.eq_btn2);
        eq_btn2.setOnClickListener(answer);
        eq_btn3=(Button)view.findViewById(R.id.eq_btn3);
        eq_btn3.setOnClickListener(answer);



        if(sound!=null){
            eq_speak=(ImageView)view.findViewById(R.id.eq_speak);
            eq_speak.setImageResource(R.drawable.speaker);
            eq_speak.setOnClickListener(speak);

            if(getEg(db.getStatement(id_question),question)!=""){
                eq_eg.setText(Html.fromHtml(getEg("Eg: "+db.getStatement(id_question),question)));
            }
        }
        if(question.length()>15){
            eq_word.setTextSize(18);
            eq_word.setText(question);
        }else{
            eq_word.setText(question);
        }


        if(spelling!=null){
            eq_spelling=(TextView)view.findViewById(R.id.eq_spelling);
            eq_spelling.setText(spelling);
        }else{
            eq_spelling=(TextView)view.findViewById(R.id.eq_spelling);
            eq_spelling.setTextSize(18);
            eq_spelling.setText(question);
            eq_word.setText("");
        }

        getRandom(1,3);
    }

    public String getEg(String str, String word){
        String strLowerCase=str.toLowerCase();

        if(!strLowerCase.contains(word)){
            return str;
        }else if(str==""){
            return str;
        }else{
            int first=strLowerCase.indexOf(word);
            if(first==0){
                str=str.replace(getWordFirst(str), "<u><b>"+getWordFirst(str)+"</b></u>");

            }else if(first==-1){
                return str;
            }else{
                String subStr=str.substring(first, getIndex(str, first));
                str=str.replace(subStr, "<u><b>"+subStr+"</u></b>");

            }
        }



        return str;

    }
    public String getWordFirst(String str){
        int end=-1;
        for(int i=0;i<str.length();i++){
            if(str.charAt(i)==' '){
                end=i;
                break;
            }
        }
        if(end==-1){
            return str;
        }else{
            return str.substring(0, end);
        }
    }
    public int getIndex(String str, int start){
        int index=-1;
        if(start==-1){
            return index;
        }else{
            for(int i=start;i<str.length();i++){
                if(str.charAt(i)==' '){
                    index=i;
                    break;
                }
            }

        }

        if(index==-1){
            return str.length();
        }
        return index;
    }

    public static EnglishAdapter getInstance(){
        return englishAdapter;
    }

    public View.OnClickListener abc=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            context.finish();
        }
    };

    public View.OnClickListener speak=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

                try {
                    File Mytemp = File.createTempFile("TCL", "mp3");
                    Mytemp.deleteOnExit();
                    FileOutputStream fos = new FileOutputStream(Mytemp);
                    fos.write(sound);
                    fos.close();

                    MediaPlayer mediaPlayer = new MediaPlayer();

                    FileInputStream MyFile = new FileInputStream(Mytemp);
                    mediaPlayer.setDataSource(MyFile.getFD());

                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException ex) {
                    String s = ex.toString();
                    ex.printStackTrace();
                }

        }
    };

    public View.OnClickListener answer=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id=v.getId();
            String answer="";
            switch (id){
                case R.id.eq_btn1:
                    answer=String.valueOf(eq_btn1.getText());
                    break;
                case R.id.eq_btn2:
                    answer=String.valueOf(eq_btn2.getText());
                    break;
                case R.id.eq_btn3:
                    answer=String.valueOf(eq_btn3.getText());
                    break;
            }
            String result1=answer.toLowerCase();
            if(result1.equals(tr_answear.toLowerCase())){
                if(spelling==null){
                    String subjectGm=sharedPreferences.getString("subjectGm", null);
                    String subjectID=sharedPreferences.getString(subjectGm, null);
                    String[] arrId=subjectID.split(",");
                    if(arrId.length==1){
                        String newId=db.getIdSubjectGm(subjectGm);
                        editor.putString(subjectGm, newId);
                        editor.commit();
                    }else{
                        Log.d("tag",id_question+" ");
                        subjectID=subjectID.replace(","+id_question, "");
                        Log.d("tag", subjectID);
                        editor.putString(subjectGm, subjectID);
                        editor.commit();
                    }
                }else{
                    String lap=sharedPreferences.getString("lap", null);
                    if(lap.equals("lap1")){
                        unlockVcLap1();
                    }else{
                        unlockVcLap2();
                    }
                }

                if(sharedPreferences.getString("stateUnlock", null).equals("state1")){
                    context.finish();
                }else{
                    if(sharedPreferences.getBoolean("stateLockSound", false)){
                        MediaPlayer player=MediaPlayer.create(context, R.raw.tr_answer);
                        player.start();
                    }
                    lockScreenActivity.loadPassSetting();
                }

            }else{
                if(sharedPreferences.getString("lap", null).equals("lap1")){
                    lockVcLap1();
                }else {
                    lockVcLap2();
                }
                errorAnswer(answer);
                eq_btn1.setEnabled(false);
                eq_btn2.setEnabled(false);
                eq_btn3.setEnabled(false);
            }

        }
    };

    public void unlockVcLap1(){
        String subjectselect=sharedPreferences.getString("subjectselect", null);
        String subjectId=sharedPreferences.getString(subjectselect, null);
        String[] arrId=subjectId.split(",");

        if(arrId.length==0){
            subjectId=id_question+"";
            editor.putString(subjectselect, subjectId);
        }else {
            if (!arrId[0].equals(id_question+"")) {
                Log.d("tag", subjectId);
                subjectId = subjectId.replace("," + id_question, "");
                subjectId = id_question + "," + subjectId;
                Log.d("tag", subjectId);
                editor.putString(subjectselect, subjectId);
                editor.commit();
            }
        }
        if(sharedPreferences.getBoolean("stateLockSound", false)){
            MediaPlayer player=MediaPlayer.create(context, R.raw.unlock);
            player.start();
        }
    }

    public void unlockVcLap2(){
        String subjectselect=sharedPreferences.getString("subjectselect", null);
        String subjectId=sharedPreferences.getString(subjectselect, null);
        String[] arrId=subjectId.split(",");

        if(arrId.length==0){
            subjectId=id_question+"";
            editor.putString(subjectselect, subjectId);
            editor.commit();
        }else{
            Log.d("tag", subjectId);
            subjectId=subjectId.replace(","+id_question,"");

            subjectId=id_question+","+subjectId;
            Log.d("tag", subjectId);
            editor.putString(subjectselect, subjectId);
            editor.commit();

            if(sharedPreferences.getBoolean("stateLockSound", false)){
                MediaPlayer player=MediaPlayer.create(context, R.raw.unlock);
                player.start();
            }
        }
    }

    public void lockVcLap1(){
        String subjectselect=sharedPreferences.getString("subjectselect", null);
        String subjectId=sharedPreferences.getString(subjectselect, null);
        String[] arrId=subjectId.split(",");

        if(arrId.length==1){
            editor.putString("subjectselect", "");
            addListError(id_question);
        }else{
            if(arrId[0].equals(id_question)){
                subjectId=subjectId.replace(id_question+",", "");
                editor.putString(subjectselect, subjectId);
                addListError(id_question);
            }else{
                subjectId=subjectId.replace(","+id_question,"");
                editor.putString(subjectselect, subjectId);
                addListError(id_question);
            }
        }
        if(sharedPreferences.getBoolean("stateVibration", false)){
            vibrator.vibrate(800);
        }
    }

    public void lockVcLap2(){
        String subjectselect=sharedPreferences.getString("subjectselect", null);
        String subjectId=sharedPreferences.getString(subjectselect, null);
        String[] arrId=subjectId.split(",");

        if(arrId.length==1){
            editor.putString(subjectselect, "");
            editor.commit();
            addListError(id_question);
        }else{
            subjectId=subjectId.replace(","+id_question, "");
            editor.putString(subjectselect, subjectId);
            editor.commit();
            addListError(id_question);
        }
        if(sharedPreferences.getBoolean("stateVibration", false)){
            vibrator.vibrate(800);
        }

    }

    public void addListError(int id){
        String list_error=sharedPreferences.getString("list_error", null);
        if(list_error==""||list_error==null){
            list_error=id+"";
            editor.putString("list_error", list_error);
            editor.commit();
        }else {
            list_error=list_error+","+id;
            editor.putString("list_error", list_error);
            editor.commit();
        }
        Log.d("tag", "ERROR:"+list_error);
    }


    public void errorAnswer(String answer){
        String lowAndswer=answer.toLowerCase();
        String ls1=eq_btn1.getText().toString().toLowerCase();
        String ls2=eq_btn2.getText().toString().toLowerCase();
        String ls3=eq_btn3.getText().toString().toLowerCase();

        if(lowAndswer.equals(ls1)){
            eq_btn1.setBackgroundColor(Color.RED);
        }else if(lowAndswer.equals(ls2)){
            eq_btn2.setBackgroundColor(Color.RED);
        }else{
            eq_btn3.setBackgroundColor(Color.RED);
        }
    }

    // Tao random dap an tu vung
    public void getRandom(int min, int max){
        Random R=new Random();
        int rd1=min + R.nextInt(max- min +1);
        int rd2=0;
        do{
            rd2=min + R.nextInt(max- min +1);
        }while(rd2==rd1);
        int rd3=0;
        do{
            rd3=min + R.nextInt(max -min +1);
        }while(rd3==rd1 || rd3==rd2);

        switch (rd1){
            case 1:
                eq_btn1.setText(tr_answear);
                break;
            case 2:
                eq_btn1.setText(err1_answear);
                break;
            case 3:
                eq_btn1.setText(err2_answear);
                break;
        }
        switch (rd2){
            case 1:
                eq_btn2.setText(tr_answear);
                break;
            case 2:
                eq_btn2.setText(err1_answear);
                break;
            case 3:
                eq_btn2.setText(err2_answear);
                break;
        }
        switch (rd3){
            case 1:
                eq_btn3.setText(tr_answear);
                break;
            case 2:
                eq_btn3.setText(err1_answear);
                break;
            case 3:
                eq_btn3.setText(err2_answear);
                break;
        }
    }

    // Load trang thai cua cai dat nhanh
    public void loadState(){
        if(stateWifi()){
            setting_wifi.setImageResource(R.drawable.ic_wifi_checked);
        }else{
            setting_wifi.setImageResource(R.drawable.ic_wifi_normal);
        }

        /*if(stateBluetooth()){
            setting_bluetooth.setImageResource(R.drawable.ic_bluetooth_checked);
        }else{
            setting_bluetooth.setImageResource(R.drawable.ic_bluetooth_normal);
        }*/

        if(stateBrightless()){
            setting_brightless.setImageResource(R.drawable.ic_bright_checked);
        }else{
            setting_brightless.setImageResource(R.drawable.ic_brightless_normal);
        }

        if(stateRotate()){
            setting_orientation.setImageResource(R.drawable.ic_orientation_checked);
        }else{
            setting_orientation.setImageResource(R.drawable.ic_orientation_normal);
        }

        setting_light.setProgress(Settings.System.getInt(lockScreenActivity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0));
    }


    // Bắt sự kiện tắt bật wifi
    private View.OnClickListener wifi=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WifiManager wifiManager=(WifiManager)context.getApplicationContext().getSystemService(WIFI_SERVICE);
            if(wifiManager.isWifiEnabled()){
                wifiManager.setWifiEnabled(false);
                setting_wifi.setImageResource(R.drawable.ic_wifi_normal);
            }else{
                wifiManager.setWifiEnabled(true);
                setting_wifi.setImageResource(R.drawable.ic_wifi_checked);
            }
        }
    };

    // Bắt sự kiện tắt bật bluetooth
   /* private View.OnClickListener bluetooth=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
            if(bluetoothAdapter.isEnabled()){
                bluetoothAdapter.disable();
                setting_bluetooth.setImageResource(R.drawable.ic_bluetooth_normal);
            }else{
                bluetoothAdapter.enable();
                setting_bluetooth.setImageResource(R.drawable.ic_bluetooth_checked);
            }
        }
    };*/

    // Bắt sự kiện tắt bật điều chỉnh ánh sáng
    private View.OnClickListener brighless=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(stateBrightless()){
                Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, 0);
                setting_brightless.setImageResource(R.drawable.ic_brightless_normal);
            }else{
                Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, 1);
                setting_brightless.setImageResource(R.drawable.ic_bright_checked);
            }
        }
    };

    // Bắt sự kiện tắt bật xoay màn hình
    private View.OnClickListener rotate=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(stateRotate()){
                Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
                setting_orientation.setImageResource(R.drawable.ic_orientation_normal);
            }else{
                Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1);
                setting_orientation.setImageResource(R.drawable.ic_orientation_checked);
            }
        }
    };


    // Lấy trạng thái Wifi
    private boolean stateWifi(){
        WifiManager wifiManager=(WifiManager)context.getApplicationContext().getSystemService(WIFI_SERVICE);
        if(wifiManager.isWifiEnabled()){
            return true;
        }

        return false;
    }

    // Lấy trạng thái bluetooth
    /*private boolean stateBluetooth(){
        BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter.isEnabled()){
            return true;
        }

        return false;
    }*/

    // Lấy trạng thái điều chỉnh độ sáng màn hình
    private boolean stateBrightless(){
        return Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, 0)==1;
    }

    // Lấy trạng thái xoay màn hình
    private boolean stateRotate(){
        return Settings.System.getInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0)==1;
    }


    // Bắt sự kiện bật tắt flash
    private View.OnClickListener flash=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getCamera();
            if(flashState){
                flashOff();
            }else{
                flashOn();
            }
        }
    };

    // Bật flash
    private void flashOn(){
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
        camera.startPreview();

        flashState=true;
    }

    // Tắt flash
    private void flashOff(){
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameters);
        camera.startPreview();

        flashState=false;
    }

    // getCamera
    private void getCamera(){
        if(camera==null){
            camera=Camera.open();
            parameters=camera.getParameters();
        }
    }

    private View.OnClickListener time=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean statePin=sharedPreferences.getBoolean("statePin", false);
            if(statePin==false){
                startTime();
                context.finish();
            }else{
                editor.putBoolean("time", true);
                editor.commit();
                lockScreenActivity.loadPassSetting();
            }
        }
    };

    private void startTime(){
        Intent intent=new Intent(AlarmClock.ACTION_SET_ALARM);
        context.startActivity(intent);
    }

    private View.OnClickListener settingEnglish=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean statePin=sharedPreferences.getBoolean("statePin", false);
            if(statePin){
                editor.putBoolean("setting", true);
                editor.commit();
                lockScreenActivity.loadPassSetting();
            }else{
                startSetting();
            }
        }
    };

    private void startSetting(){
        context.finish();
        Intent intent=new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    private View.OnClickListener cameralisnter=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            context.finish();
            startCamera();
        }
    };

    private void startCamera(){
        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        context.startActivity(intent);
    }

    private SeekBar.OnSeekBarChangeListener lightChange=new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            lockScreenActivity.params=lockScreenActivity.getWindow().getAttributes();
            lockScreenActivity.params.screenBrightness=(float)progress/255;
            lockScreenActivity.getWindow().setAttributes(lockScreenActivity.params);
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

}
