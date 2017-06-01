package com.example.dongnd.screenenglish;

import android.*;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

import adapter.EnglishAdapter;
import adapter.MultiDirectionSlidingDrawer;
import adapter.NotificationItem;
import adapter.PagerAdapter;
import battery.Battery;
import databases.DbAdapter;
import databases.Question;
import databases.Vocabulary;
import service.NLService;
import service.TrackGPS;
import weather.Common;
import weather.Helper;
import weather.OpenWeather;


public class LockScreenActivity extends Activity {

    public WindowManager.LayoutParams params;
    public WindowManager wm;
    public View myView;
    public MultiDirectionSlidingDrawer drawer;
    TextView textView;

    public DbAdapter dbAdapter;
    public Vocabulary vc = null;
    public Question qs = null;


    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    public EnglishAdapter englishAdapter;



    public static LockScreenActivity lockScreenActivity;
    MainActivity mainActivity = MainActivity.getInstance();

    Battery battery;
    ImageView img_singal_1, img_singal_2, img_singal_3, img_singal_4, img_singal_5;
    ImageView img_battery, background, background_demo;
    TextView text_singal, text_battery;

    public PagerAdapter adapter;
    public ViewPager pager;
    public Bitmap bitmap;


    public WebView ex_webview;
    public TextView ex_word, ex_spelling;

    public Typeface type;

    public ArrayList<NotificationItem> ls_List = new ArrayList<>();
    public NotificationReceiver nReceiver;

    public NLService notificationService;

    public boolean allowNotifi=false;

    public LocationManager locationManager;
    public String provider;
    public static double lat, lon;
    public OpenWeather openWeather;
    public String wt_city="A";
    public String wt_date;
    public String wt_celsius;
    public String wt_description;
    public String wt_icon;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    private boolean isNetworkEnable=false;
    private boolean isGpsEnable=false;
    private Bitmap weather;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        nReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("dong.NOTIFICATION_LISTENER_EXAMPLE");
        registerReceiver(nReceiver, filter);


        notificationService = NLService.getInstance();
        allowNotifi=chechAllowNotification();
        if(allowNotifi){
            getNotification();
        }

        lockScreenActivity = this;

        englishAdapter = EnglishAdapter.getInstance();


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lock_screen);

        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        editor = sharedPreferences.edit();


        type = Typeface.createFromAsset(getAssets(), "lightfont.otf");

        dbAdapter = new DbAdapter(this);
        getTypeEnglish();


        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION,
                PixelFormat.TRANSLUCENT);




        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        myView = inflater.inflate(R.layout.activity_lock_screen, null);


        // Add layout to window manager
        wm.addView(myView, params);

        // setup viewpager
        pager = (ViewPager) myView.findViewById(R.id.pager);
        adapter = new adapter.PagerAdapter(lockScreenActivity);
        pager.setAdapter(adapter);

        //pager.setCurrentItem(0);

        // load Weather


        if(chechAllowNotification()){
            if (notificationService.getActiveNotifications().length > 0) {
                pager.setCurrentItem(0);
            } else {
                pager.setCurrentItem(1);
            }

        }else{
            pager.setCurrentItem(1);
        }
        pager.setOnPageChangeListener(pageChange);

        //load battery, singal
        initBattery(myView);
        loadBattery();
        loadSingal();

        // load slidingdrawer
        initSidingDrawer();

        if(sharedPreferences.getBoolean("stateWeather", false)){
            showWeather();

        }



    }

    public void getTypeEnglish() {
        boolean stateVc = sharedPreferences.getBoolean("stateVc", false);
        if (stateVc) {
            vc = dbAdapter.getWordRandom();
        } else {
            qs = dbAdapter.getRandomQuestion();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nReceiver);
    }



/*    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.kpbird.nlsexample.NOTIFICATION_LISTENER_EXAMPLE");
        registerReceiver(nReceiver,filter);
    }*/

    public ViewPager.OnPageChangeListener pageChange = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            if (position == 0) {

                if(allowNotifi){
                    getNotification();
                }
            } else if (position == 1) {

                editor.remove("app");
                editor.remove("time");
                editor.remove("setting");
                editor.commit();
            } else {

                if (checkPassState() == false || getStatePin() == false) {
                    wm.removeView(myView);
                    finish();
                }
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public boolean openImmediate(){
        if(chechAllowNotification()){
            if(notificationService.getActiveNotifications().length>0){
                return true;
            }
        }
        return false;
    }

    public void getNotification() {
        ls_List.removeAll(ls_List);
        Intent i = new Intent("dong.NOTIFICATION_LISTENER_EXAMPLE");

        i.putExtra("command", 1);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendBroadcast(i);
    }

    public boolean checkPassState() {
        String pass = sharedPreferences.getString("password", null);
        if (pass == null) {
            return false;
        }
        return true;
    }

    public boolean getStatePin() {
        boolean statePin = sharedPreferences.getBoolean("statePin", false);
        return statePin;
    }

    public void loadBattery() {
        battery = new Battery(getApplicationContext());

        text_battery.setText(battery.getBattery() + "%");
        if (battery.getStatus()) {
            img_battery.setImageResource(R.drawable.battery_charging);
        } else {
            int current_battery = battery.getBattery();
            if (current_battery > 80) {
                img_battery.setImageResource(R.drawable.battery4);
            } else if (current_battery > 50) {
                img_battery.setImageResource(R.drawable.battery3);
            } else if (current_battery > 20) {
                img_battery.setImageResource(R.drawable.battery2);
            } else {
                img_battery.setImageResource(R.drawable.battery1);
            }
        }
    }

    public void loadSingal() {
        int current_network = mainActivity.singalNetwork;
        if (current_network >= 12) {
            loadImgSingal(1, 1, 1, 1, 1);
        } else if (current_network > 8) {
            loadImgSingal(1, 1, 1, 0, 0);
        } else if (current_network > 5) {
            loadImgSingal(1, 1, 0, 0, 0);
        } else {
            loadImgSingal(1, 0, 0, 0, 0);
        }

        text_singal.setText(mainActivity.operatorName.toUpperCase());
    }

    public void loadImgSingal(int s1, int s2, int s3, int s4, int s5) {
        if (s1 == 1) {
            img_singal_1.setImageResource(R.drawable.network1);
        } else {
            img_singal_1.setImageResource(R.drawable.network2);
        }
        if (s2 == 1) {
            img_singal_2.setImageResource(R.drawable.network1);
        } else {
            img_singal_2.setImageResource(R.drawable.network2);
        }
        if (s3 == 1) {
            img_singal_3.setImageResource(R.drawable.network1);
        } else {
            img_singal_3.setImageResource(R.drawable.network2);
        }
        if (s4 == 1) {
            img_singal_4.setImageResource(R.drawable.network1);
        } else {
            img_singal_4.setImageResource(R.drawable.network2);
        }
        if (s5 == 1) {
            img_singal_5.setImageResource(R.drawable.network1);
        } else {
            img_singal_5.setImageResource(R.drawable.network2);
        }
    }

    public void initBattery(View view) {
        img_singal_1 = (ImageView) view.findViewById(R.id.ls_singal_1);
        img_singal_2 = (ImageView) view.findViewById(R.id.ls_singal_2);
        img_singal_3 = (ImageView) view.findViewById(R.id.ls_singal_3);
        img_singal_4 = (ImageView) view.findViewById(R.id.ls_singal_4);
        img_singal_5 = (ImageView) view.findViewById(R.id.ls_singal_5);

        text_singal = (TextView) view.findViewById(R.id.ls_singal_text);

        text_battery = (TextView) view.findViewById(R.id.ls_battery_text);
        img_battery = (ImageView) view.findViewById(R.id.ls_battery_img);


        background = (ImageView) view.findViewById(R.id.background);
        getBackground();


    }

    public void getBackground() {
        String strBackground=sharedPreferences.getString("background", null);
        if(strBackground==null){
            background.setImageResource(R.drawable.a28);
        }else{
            if(strBackground.contains("/")){
                File file=new File(strBackground);
                Bitmap bitmap=BitmapFactory.decodeFile(file.getAbsolutePath());
                background.setImageBitmap(bitmap);
            }else{
                background.setImageResource(Integer.parseInt(strBackground));
            }
        }



    }

    public void initSidingDrawer() {
        drawer = (MultiDirectionSlidingDrawer) myView.findViewById(R.id.drawer);
        final ImageView imageView = (ImageView) myView.findViewById(R.id.handle);
        MultiDirectionSlidingDrawer.OnDrawerCloseListener ac = new MultiDirectionSlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                imageView.setImageDrawable(null);
            }
        };
        MultiDirectionSlidingDrawer.OnDrawerOpenListener ap = new MultiDirectionSlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                imageView.setImageResource(R.drawable.bg_bottom);
            }
        };
        drawer.setOnDrawerCloseListener(ac);
        drawer.setOnDrawerOpenListener(ap);

        loadSiding();

    }

    public void loadSiding() {


        ex_webview = (WebView) myView.findViewById(R.id.ex_webview);
        ex_word = (TextView) myView.findViewById(R.id.ex_word);
        ex_spelling = (TextView) myView.findViewById(R.id.ex_spelling);


        if (vc == null) {
            String grammar = dbAdapter.getExGrammar(qs.getQs_ref());
            ex_webview.loadData(grammar, "text/html; charset=utf-8", "UTF-8");
        } else {
            String content = dbAdapter.getEEMean(vc.getVc_id());
            String spelling = "BrE " + vc.getSpellingaa() + " ;" + " NAmE " + vc.getSpellingam();
            ex_word.setText(vc.getWord());
            ex_spelling.setText(spelling);
            ex_webview.loadData(content, "text/html; charset=utf-8", "UTF-8");
        }


    }


    public void loadPassSetting() {
        Log.d("tag", "number");
        pager.setCurrentItem(2);
    }

    public void setPager(int page) {
        pager.setCurrentItem(page);
    }

    public static LockScreenActivity getInstance() {
        if (lockScreenActivity == null) {
            return new LockScreenActivity();
        }
        return lockScreenActivity;
    }


    class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (allowNotifi) {
                if (intent.getIntExtra("command", 0) == 3) {
                    ls_List.removeAll(ls_List);
                    adapter.ns_List = ls_List;
                    adapter.ns_adapter.notifyDataSetChanged();
                    pager.setCurrentItem(1);
                }

                if (intent.getIntExtra("active", 0) == 2) {
                    ls_List.removeAll(ls_List);
                }

                String pack = intent.getStringExtra("pack");
                String title = intent.getStringExtra("title");
                String content = intent.getStringExtra("content");


                byte[] byteArray = intent.getByteArrayExtra("img");
                Bitmap bmp = null;
                if (byteArray != null) {
                    bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                }

                NotificationItem model = new NotificationItem();
                model.setTitle(title);
                model.setPack(pack);
                model.setContent(content);
                model.setImg(bmp);


                if (model.getPack() != null) {
                    ls_List.add(model);


                    if (adapter.ns_adapter != null) {
                        adapter.ns_List = ls_List;
                        adapter.ns_adapter.notifyDataSetChanged();
                    }


                }


            }else{
                NotificationItem model=new NotificationItem();
                model.setPack(null);
                model.setTitle("Hiện tại bạn không có thông báo nào");
                model.setContent(null);
                model.setImg(null);


                ls_List.add(model);

                if (adapter.ns_adapter != null) {
                    adapter.ns_List = ls_List;
                    adapter.ns_adapter.notifyDataSetChanged();
                }
            }
        }
    }
            // Kiểm tra hệ thống cho phép lấy notification hay không
        public boolean chechAllowNotification() {
            String enabledListeners = Settings.Secure.getString(getContentResolver(),
                    "enabled_notification_listeners");
            if (enabledListeners == null || !enabledListeners.contains(getPackageName())) {
                return false;
            }
            return true;
        }


    public void showWeather(){
        openWeather=new OpenWeather();

        TrackGPS trackGPS=new TrackGPS(this);
        lat=trackGPS.getLatitude();
        lon=trackGPS.getLongitude();
        Log.d("tag", lat+"");
        Log.d("tag", lon+"");
        if(Double.compare(lat, 0.0d)==0){

        }else{
            new GetWeather().execute(Common.apiRequest(String.valueOf(lat), String.valueOf(lon)));
        }


    }




    private class GetWeather extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String stream=null;
            String urlString=params[0];

            Helper http=new Helper();
            stream=http.getHttpData(urlString);
            return stream;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("tag", "S "+s);
            if(s==null){

                Log.d("tag", "BREAK");
                Log.d("tag", sharedPreferences.getString("wt_city", null));
                adapter.ns_vitri.setText(sharedPreferences.getString("wt_city", null));
                adapter.ns_update.setText(sharedPreferences.getString("wt_date", null));
            }else{
                Gson gson=new Gson();
                Type mType=new TypeToken<OpenWeather>(){}.getType();
                openWeather=gson.fromJson(s, mType);

                Log.d("tag", "NOT NULL");
                wt_city=String.format("%s,%s", openWeather.getName(), openWeather.getSys().getCountry());
                wt_date=String.format("Last update: %s", Common.getDate());
                if(sharedPreferences.getString("temperate", null).equals("C")){
                    wt_celsius=String.format(String.format("%s",(int)openWeather.getMain().getTemp()));
                }else{
                    wt_celsius=String.format(String.format("%s",convertCF((int)openWeather.getMain().getTemp())));
                }

                wt_description=String.format("%s", openWeather.getWeathers().get(0).getDescription());
                wt_icon=String.format("%s", openWeather.getWeathers().get(0).getIcon());


                Log.d("tag", wt_city);
                Log.d("tag", wt_description);
                Log.d("tag", wt_celsius);
                Log.d("tag", wt_icon);
                editor.putString("wt_city", wt_city);
                editor.putString("wt_date", wt_date);
                editor.putString("wt_description", wt_description);
                editor.putString("wt_celsius", wt_celsius);
                editor.commit();
                adapter.ns_vitri.setText(wt_city);
                adapter.ns_weather.setText(wt_celsius+", "+wt_description);
                adapter.ns_update.setText(wt_date);

                Picasso.with(LockScreenActivity.this)
                        .load(Common.getImage(openWeather.getWeathers().get(0).getIcon()))
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                adapter.ns_img.setImageBitmap(bitmap);
                                String root = Environment.getExternalStorageDirectory().toString();
                                File myDir = new File(root + "/Download/");
                                String fname = "ic_weather.jpg";
                                File file = new File (myDir, fname);
                                if (file.exists ()) file.delete ();
                                try {
                                    FileOutputStream out = new FileOutputStream(file);
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                    out.flush();
                                    out.close();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                String icon=Environment.getExternalStorageDirectory().toString()+"/Download/ic_weather.jpg";


                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });

            }

        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    public int convertCF(int C){
        int valueF=(int)(C*1.8 +32);

        return valueF;
    }


}
