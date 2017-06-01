package com.example.dongnd.screenenglish;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.DialogAdapter;
import adapter.LockAdapter;
import adapter.LockItem;
import databases.DbAdapter;
import databases.Subject;
import databases.SubjectGm;
import service.LockScreenService;

public class MainActivity extends AppCompatActivity {

    public ImageView se_img, pw_img;

    // Khai bao view mật khẩu
    public TextView pw_datmk, pw_resetmk;

    // Khai báo view ảnh nền
    public TextView wp_gallery, wp_album;

    public TextView vc_caidat;

    // Khai báo view học ngữ pháp
    public TextView gm_setting, gm_kichhoat, gm_practice;
    public ImageView gm_img;


    // Khai báo view học từ vựng
    public TextView vc_kichhoat, vc_practice;
    public ImageView vc_img;

    // Khai báo view cài đặt chung
    public ImageView notification_img;

    // Khai báo rung
    public TextView vibration_kichhoat;
    public ImageView vibration_img;

    // Âm thanh khóa
    public TextView locksound_kichhoat;
    public ImageView locksound_img;

    // Tùy chọn khóa màn hình
    public TextView lock_kichhoat;
    private AlertDialog alertDialog1;
    private CharSequence[] values = {"Chọn đúng đáp án để mở khóa "," Bắt buộc nhập mã PIN để mở khóa"};

    // Thời tiết
    private TextView weather_kichhoat;

    public int singalNetwork = 0;
    public String operatorName = "";
    public TelephonyManager telephonyManager;
    public MyPhoneStateListener MyListener;

    public static final int REQUEST_CODE_SETPIN = 1;
    public static final int REQUEST_CODE_OLDPIN=3;

    public static MainActivity mainActivity;
    public LockScreenService lockScreenService;

    public SharedPreferences sharedPreferences;

    public SharedPreferences.Editor editor;

    public DbAdapter db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mainActivity = this;
        lockScreenService = LockScreenService.getInstance();
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        editor=sharedPreferences.edit();
        db=new DbAdapter(this);


        // Find id view
        init();


        // Set các giá trị mặc định cho lần đầu mở app
        setDataDefault();

        // Load trạng thái cài đặt
        loadState();

        // Lấy thông tin mạng
        getInfoNetwork();

        // Khởi chạy service
        start();

        editor.putString("list_error","1,2,3,4");
        editor.commit();
    }

    public void start(){
        startService(new Intent(this, LockScreenService.class));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // Lấy thông tin mạng
    public void getInfoNetwork() {
        MyListener = new MyPhoneStateListener();
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(MyListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        operatorName = telephonyManager.getSimOperatorName();
    }


    // Load trạng thái lấy từ sharepresent
    public void loadState() {
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);

        boolean state = sharedPreferences.getBoolean("state", false);
        boolean statePin=sharedPreferences.getBoolean("statePin", false);

        boolean stataVc=sharedPreferences.getBoolean("stateVc", false);
        boolean stateGm=sharedPreferences.getBoolean("stateGm", false);

        boolean notification=sharedPreferences.getBoolean("notification", false);

        boolean stateVibration=sharedPreferences.getBoolean("stateVibration", false);

        boolean stateLockSound=sharedPreferences.getBoolean("stateLockSound", false);

        if (state) {
            se_img.setImageResource(R.drawable.switch_on);
        } else {
            se_img.setImageResource(R.drawable.switch_off);
        }

        if(statePin){
            pw_img.setImageResource(R.drawable.switch_on);
        }else{
            pw_img.setImageResource(R.drawable.switch_off);
        }

        if(stataVc){
            vc_img.setImageResource(R.drawable.switch_on);
        }else{
            vc_img.setImageResource(R.drawable.switch_off);
        }

        if(stateGm){
            gm_img.setImageResource(R.drawable.switch_on);
        }else{
            gm_img.setImageResource(R.drawable.switch_off);
        }

        if(notification){
            notification_img.setImageResource(R.drawable.switch_on);
        }else{
            notification_img.setImageResource(R.drawable.switch_off);
        }

        if(stateVibration){
            vibration_img.setImageResource(R.drawable.switch_on);
        }else{
            vibration_img.setImageResource(R.drawable.switch_off);
        }

        if(stateLockSound){
            locksound_img.setImageResource(R.drawable.switch_on);
        }else{
            locksound_img.setImageResource(R.drawable.switch_off);
        }
    }

    // Set các giá trị mặc định khi lần đầu mở app
    public void setDataDefault(){
        List<Subject> subjects=db.getAllSubject();
        List<SubjectGm> subjectGms=db.getAllSubjecGm();

        String subjectselect=sharedPreferences.getString("subjectselect", null);
        String phienam=sharedPreferences.getString("phienam", null);
        String lap=sharedPreferences.getString("lap", null);
        String subjectGm=sharedPreferences.getString("subjectGm", null);
        boolean notification=sharedPreferences.getBoolean("notification", false);

        boolean stateVc=sharedPreferences.getBoolean("stateVc", false);
        boolean stateGm=sharedPreferences.getBoolean("stateGm", false);

        boolean stateRemind=sharedPreferences.getBoolean("stateRemind", false);

        boolean stateVibration=sharedPreferences.getBoolean("stateVibration", false);

        boolean stateLockSound=sharedPreferences.getBoolean("stateLockSound", false);

        String stateUnlock=sharedPreferences.getString("stateUnlock", null);

        boolean stateWeather=sharedPreferences.getBoolean("stateWeather", false);

        String temperate=sharedPreferences.getString("temperate", null);

        if(subjectselect==null){
            editor.putString("subjectselect", subjects.get(0).getSb_title());
            editor.putString(subjects.get(0).getSb_title(), db.getIdSubject(subjects.get(0).getSb_title()));
            editor.commit();
        }

        if(subjectGm==null){
            editor.putString("subjectGm", subjectGms.get(0).getSb_name());
            editor.putString(subjectGms.get(0).getSb_name(), db.getIdSubjectGm(subjectGms.get(0).getSb_name()));
            editor.commit();
        }

        if (phienam==null){
            editor.putString("phienam", "anh-anh");
            editor.commit();
        }
        if(lap==null){
            editor.putString("lap","lap1");
            editor.commit();
        }

        if(stateVc==false && stateGm==false){
            editor.putBoolean("stateVc", true);
            editor.putBoolean("stateGm", false);
            editor.commit();
        }

        if(notification==false){
            editor.putBoolean("notification", false);
            editor.commit();
        }
        if(stateRemind==false){
            editor.putBoolean("stateRemind", false);
            editor.commit();
        }

        if(stateVibration==false){
            editor.putBoolean("stateVibration", false);
            editor.commit();
        }

        if(stateLockSound==false){
            editor.putBoolean("stateLockSound", false);
            editor.commit();
        }

        if(stateUnlock==null){
            editor.putString("stateUnlock", "state1");
            editor.commit();
        }

        if(stateWeather==false){
            editor.putBoolean("stateWeather", false);
            editor.commit();
        }

        if(temperate==null){
            editor.putString("temperate", "C");
            editor.commit();
        }

    }

    // find Id
    public void init() {
        se_img = (ImageView) findViewById(R.id.se_img);
        se_img.setOnClickListener(startLock);

        pw_img = (ImageView) findViewById(R.id.pw_img);
        pw_img.setOnClickListener(startPin);
        pw_datmk=(TextView)findViewById(R.id.pw_datmk);
        if(getStateSetPin()){
            pw_datmk.setEnabled(false);
        }
        pw_datmk.setOnClickListener(datMk);

        pw_resetmk=(TextView)findViewById(R.id.pw_resetmk);
        if(getStateSetPin()==false) {
            pw_resetmk.setEnabled(false);
        }
        pw_resetmk.setOnClickListener(resetMk);

        vc_kichhoat=(TextView)findViewById(R.id.vc_kichhoat);
        vc_kichhoat.setOnClickListener(vcKichoat);
        vc_img=(ImageView)findViewById(R.id.vc_img);
        vc_caidat=(TextView)findViewById(R.id.vc_caidat);
        vc_caidat.setOnClickListener(vcSetting);

        vc_practice=(TextView)findViewById(R.id.vc_practice);
        vc_practice.setOnClickListener(vcPractice);

        wp_gallery=(TextView)findViewById(R.id.wp_gallery);
        wp_gallery.setOnClickListener(gallery);
        wp_album=(TextView)findViewById(R.id.wp_album);
        wp_album.setOnClickListener(album);

        gm_kichhoat=(TextView)findViewById(R.id.gm_kichhoat) ;
        gm_kichhoat.setOnClickListener(gmKichhoat);
        gm_img=(ImageView)findViewById(R.id.gm_img);
        gm_setting=(TextView)findViewById(R.id.gm_setting);
        gm_setting.setOnClickListener(gmSetting);

        gm_practice=(TextView)findViewById(R.id.gm_pratice);
        gm_practice.setOnClickListener(gmPractice);

        notification_img=(ImageView)findViewById(R.id.notification_img);
        notification_img.setOnClickListener(notification);

        vibration_kichhoat=(TextView)findViewById(R.id.vibration_kichhoat);
        vibration_kichhoat.setOnClickListener(vibration);
        vibration_img=(ImageView)findViewById(R.id.vibration_img);

        locksound_kichhoat=(TextView)findViewById(R.id.locksound_kichhoat);
        locksound_kichhoat.setOnClickListener(locksound);
        locksound_img=(ImageView)findViewById(R.id.locksound_img);

        lock_kichhoat=(TextView)findViewById(R.id.lock_kichhoat);
        lock_kichhoat.setOnClickListener(lockKichhoat);

        weather_kichhoat=(TextView)findViewById(R.id.weather_kichhoat);
        weather_kichhoat.setOnClickListener(weather);


    }


    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("LockScreenService".equals(service.service.getClassName())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        /* Get the Signal strength from the provider, each tiome there is an update */
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);

            if (null != signalStrength && signalStrength.getGsmSignalStrength() != 99) {
                singalNetwork = signalStrength.getGsmSignalStrength();

            }
        }
    }

    public boolean getState() {
        boolean state = sharedPreferences.getBoolean("state", false);

        return state;
    }

    public View.OnClickListener startLock = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
            boolean state = sharedPreferences.getBoolean("state", false);
            if (state == false) {
                se_img.setImageResource(R.drawable.switch_on);
                startApp();
            } else {
                se_img.setImageResource(R.drawable.switch_off);
                stopApp();
            }
        }
    };

    public void startApp() {

        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        editor.putBoolean("state", true);
        editor.commit();


    }

    public void stopApp() {

        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        editor.putBoolean("state", false);
        editor.commit();

    }

    public View.OnClickListener startPin = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
            boolean statePin = sharedPreferences.getBoolean("statePin", false);

            if (statePin == false) {
                checkSetPin();
                String pass_word = sharedPreferences.getString("password", null);
                if(pass_word!=null){
                    pw_img.setImageResource(R.drawable.switch_on);
                    editor.putBoolean("statePin", true);
                    editor.commit();
                }
            } else {
                pw_img.setImageResource(R.drawable.switch_off);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean("statePin", false);
                editor.commit();
            }
        }
    };

    public void checkSetPin() {
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String pass_word = sharedPreferences.getString("password", null);
        if (pass_word == null) {
            setPassword();
        }
    }

    public boolean getStateSetPin(){
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String pass_word = sharedPreferences.getString("password", null);

        if(pass_word==null){
            return false;
        }

        return true;
    }

    public void setPassword() {
        Intent setPass = new Intent(this, SetPassActivity.class);
        startActivityForResult(setPass, REQUEST_CODE_SETPIN);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null && requestCode==4) {
            Log.d("tag","Found");
            if(chechAllowNotification()){
                notification_img.setImageResource(R.drawable.switch_on);
                editor.putBoolean("notification", true);
                editor.commit();
            }else {
                notification_img.setImageResource(R.drawable.switch_off);
                editor.putBoolean("notification", false);
                editor.commit();
            }
        }else{
            return;
        }


        if (requestCode == REQUEST_CODE_SETPIN) {
            Bundle bundle = data.getBundleExtra("result");
            boolean result = bundle.getBoolean("result");

            if (result == true) {
                pw_img.setImageResource(R.drawable.switch_on);
                editor.putBoolean("statePin", true);
                editor.commit();
                pw_datmk.setEnabled(false);
                pw_resetmk.setEnabled(true);
            } else {
                checkSetPin();
            }
        }

        if(requestCode==REQUEST_CODE_OLDPIN){
            Bundle bundle=data.getBundleExtra("old_pass");
            String old_pass=bundle.getString("old_pass");

            String pass_word=sharedPreferences.getString("password", null);
            if(old_pass.equals(pass_word)){
                setPassword();
            }else{
                Toast.makeText(getApplicationContext(),"Mật khẩu không chính xác", Toast.LENGTH_LONG).show();
            }
        }


    }

    public View.OnClickListener datMk=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setPassword();
        }
    };


    public View.OnClickListener resetMk=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            resetPass();

        }
    };

    public void resetPass(){
        Intent intent=new Intent(this, ResetPass.class);
        startActivityForResult(intent, REQUEST_CODE_OLDPIN);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    // Bắt sự kiện cài đặt từ vựng
    public View.OnClickListener vcSetting=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startSetingVc();
        }
    };

    public void startSetingVc(){
        Intent intent=new Intent(this, VocabularySetting.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    // Bắt sự kiện chọn ảnh nên từ gallery
    public View.OnClickListener gallery=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            choseGallery();
        }
    };

    public View.OnClickListener album=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(getApplicationContext(), ChoseImgAlbum.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    };

    // Chọn ảnh nền từ Gallery
    public void choseGallery(){
        Intent intent=new Intent(this, ChoseImgGallery.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    // Bắt sự kiện kích hoạt tính năng học từ vựng
    public View.OnClickListener vcKichoat=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean stateVc=sharedPreferences.getBoolean("stateVc", false);
            if(stateVc){
                vc_img.setImageResource(R.drawable.switch_off);
                gm_img.setImageResource(R.drawable.switch_on);
                editor.putBoolean("stateVc", false);
                editor.putBoolean("stateGm", true);
                editor.commit();
            }else{
                vc_img.setImageResource(R.drawable.switch_on);
                gm_img.setImageResource(R.drawable.switch_off);
                editor.putBoolean("stateVc", true);
                editor.putBoolean("stateGm", false);
                editor.commit();
            }
        }
    };

    // Bắt sự kiện kích hoạt tính năng học ngữ pháp
    public View.OnClickListener gmKichhoat=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean stateGm=sharedPreferences.getBoolean("stateGm", false);
            if(stateGm){
                vc_img.setImageResource(R.drawable.switch_on);
                gm_img.setImageResource(R.drawable.switch_off);
                editor.putBoolean("stateVc", true);
                editor.putBoolean("stateGm", false);
                editor.commit();
            }else{
                vc_img.setImageResource(R.drawable.switch_off);
                gm_img.setImageResource(R.drawable.switch_on);
                editor.putBoolean("stateVc", false);
                editor.putBoolean("stateGm", true);
                editor.commit();
            }
        }
    };

    // Bắt sự kiện thiết lập grammar
    public View.OnClickListener gmSetting=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startGrammar();
        }
    };

    public void startGrammar(){
        Intent intent=new Intent(this, GrammarSetting.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    // Bắt sự kiện luyện tập ngữ pháp
    public View.OnClickListener gmPractice=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(MainActivity.this, GrammarPractice.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    };


    public View.OnClickListener notification=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startNotification();
        }
    };

    public void startNotification(){
        Intent intent=new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivityForResult(intent, 4);
    }


    public boolean chechAllowNotification(){
        String enabledListeners = Settings.Secure.getString(getContentResolver(),
                "enabled_notification_listeners");
        if (enabledListeners == null || !enabledListeners.contains(getPackageName())) {
            return false;
        }
        return true;
    }


    public View.OnClickListener vcPractice=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(getApplicationContext(), VocabularyPractice.class);
            startActivity(intent);
        }
    };

    public View.OnClickListener vibration=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean stateVibration=sharedPreferences.getBoolean("stateVibration", false);
            if(stateVibration){
                editor.putBoolean("stateVibration", false);
                editor.commit();
                vibration_img.setImageResource(R.drawable.switch_off);
            }else{
                editor.putBoolean("stateVibration", true);
                editor.commit();
                vibration_img.setImageResource(R.drawable.switch_on) ;
            }
        }
    };

    public View.OnClickListener locksound=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean stateLockSound=sharedPreferences.getBoolean("stateLockSound", false);
            if(stateLockSound){
                editor.putBoolean("stateLockSound", false);
                editor.commit();
                locksound_img.setImageResource(R.drawable.switch_off);
            }else{
                editor.putBoolean("stateLockSound", true);
                editor.commit();
                locksound_img.setImageResource(R.drawable.switch_on);
            }
        }
    };

    public View.OnClickListener lockKichhoat=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showOptionLock();
        }
    };

    public void showOptionLock(){
    android.support.v7.app.AlertDialog.Builder builder=new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Hình thức mở khóa");
        builder.setIcon(R.drawable.ic_select);

        List<LockItem> data=new ArrayList<>();
        data.add(new LockItem(R.drawable.ic_lock_01, "Chọn đáp án chính xác để mở khóa"));
        data.add(new LockItem(R.drawable.ic_lock_02, "Bắt buộc nhập mã PIN để mở khóa"));

        LockAdapter adapter=new LockAdapter(this, R.layout.item_lock, data);


        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();

    }

    public View.OnClickListener weather=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(getApplicationContext(), Weather.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    };

    public static MainActivity getInstance() {
        if(mainActivity==null){
            mainActivity=new MainActivity();
        }

        return mainActivity;
    }

}
