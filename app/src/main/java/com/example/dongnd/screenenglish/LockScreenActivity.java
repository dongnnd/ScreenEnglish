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
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
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

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

import adapter.EnglishAdapter;
import adapter.MultiDirectionSlidingDrawer;
import adapter.NotificationItem;
import adapter.PagerAdapter;
import battery.Battery;
import databases.DbAdapter;
import databases.Question;
import databases.Vocabulary;
import service.NLService;
import weather.Common;
import weather.Helper;
import weather.OpenWeather;


public class LockScreenActivity extends Activity implements LocationListener{

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

        // load Weather
        if(sharedPreferences.getBoolean("stateWeather", false)){
            showWeather();
        }

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

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(LockScreenActivity.this, new String[]{
                    android.Manifest.permission.INTERNET,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_NETWORK_STATE,
                    android.Manifest.permission.SYSTEM_ALERT_WINDOW,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 0);

            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        lat=location.getLatitude();
        lon=location.getLongitude();
        new GetWeather().execute(Common.apiRequest(String.valueOf(lat), String.valueOf(lon)));
    }


    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lat=location.getLatitude();
        lon=location.getLongitude();

        new GetWeather().execute(Common.apiRequest(String.valueOf(lat), String.valueOf(lon)));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class GetWeather extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog=new ProgressDialog(LockScreenActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Please wait...");
            progressDialog.show();
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

            Gson gson=new Gson();
            Type mType=new TypeToken<OpenWeather>(){}.getType();
            openWeather=gson.fromJson(s, mType);
            progressDialog.dismiss();

            wt_city=String.format("%s,%s", openWeather.getName(), openWeather.getSys().getCountry());
            wt_date=String.format("Last update: %s", Common.getDate());
            wt_celsius=String.format(String.format("%.2f °C", openWeather.getMain().getTemp()));
            wt_description=String.format("%s", openWeather.getWeathers().get(0).getMain());
            wt_icon=String.format("%s", openWeather.getWeathers().get(0).getIcon());


            Log.d("tag", wt_city);
            Log.d("tag", wt_description);
            Log.d("tag", wt_celsius);
            Log.d("tag", wt_icon);
            adapter.ns_vitri.setText(wt_city);
            Picasso.with(LockScreenActivity.this)
                    .load(Common.getImage(openWeather.getWeathers().get(0).getIcon()))
                    .into(adapter.ns_img);

           /* text_city.setText(String.format("%s,%s", openWeather.getName(), openWeather.getSys().getCountry()));
            text_update.setText(String.format("Last update: %s", Common.getDate()));
            text_description.setText(String.format("%s", openWeather.getWeathers().get(0).getMain()));
            text_celsius.setText(String.format("%.2f °C", openWeather.getMain().getTemp()));
            Picasso.with(LockScreenActivity.this)
                    .load(Common.getImage(openWeather.getWeathers().get(0).getIcon()))
                    .into(image_view);*/
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }


}
