package adapter;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.provider.AlarmClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.dongnd.screenenglish.LockScreenActivity;
import com.example.dongnd.screenenglish.MainActivity;
import com.example.dongnd.screenenglish.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import battery.Battery;
import battery.MobileNetWork;
import blur.BlurBuilder;
import verticalpager.VerticalPager;

/**
 * Created by DongND on 3/1/2017.
 */

public class PagerAdapter extends android.support.v4.view.PagerAdapter {
    MultiDirectionSlidingDrawer drawer;

    LockScreenActivity lockScreenActivity = LockScreenActivity.getInstance();
    MainActivity mainActivity=MainActivity.getInstance();
    Battery battery = new Battery(lockScreenActivity);

    public static PagerAdapter pagerAdapter;

    // Khai bao layout main
    private TextClock se_clock;
    private TextView se_date;

    // Khai bao layout notification
    private TextClock ns_clock;
    private TextView ns_date;
    private TextView ns_clear;

    public ListView ns_listview;
    public NotificationAdapter ns_adapter;
    public ArrayList<NotificationItem> ns_List=new ArrayList<>();
   // public NotificationListener listener;

    // Khai bao layout PassScreen
    private ImageView ps_pass1, ps_pass2, ps_pass3, ps_pass4;
    private ImageView number0, number1, number2, number3, number4, number5, number6,
            number7, number8, number9;
    private ImageView ps_battery_img;
    private TextView ps_battery_text;
    private BottomSheetBehavior behavior;
    public TextView ps_cancel, ps_del;

    // Khai bao sharepresent
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    private ArrayList<Integer> userpass = new ArrayList<>();

    LayoutInflater layoutInflater;


    public final String[] TITLE = {"Tab01", "Tab02", "Tab03"};
    public Activity context;

    public PagerAdapter(Activity context) {
        this.context = context;

        ns_List=new ArrayList<>();

        sharedPreferences=context.getSharedPreferences("data", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();


       /* listener = new NotificationListener();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.kpbird.nlsexample.NOTIFICATION_LISTENER_EXAMPLE");
        context.registerReceiver(listener,filter);*/
    }



    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLE[position];
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View locationView = new View(this.context);
        if (position == 0) {
            locationView = layoutInflater.inflate(R.layout.notification_screen, container, false);
            container.addView(locationView, 0);
            showNotification(locationView);
        }
        if (position == 1) {
            locationView = layoutInflater.inflate(R.layout.english_screen, container, false);
            container.addView(locationView, 0);
            showMain(locationView);
        }
        if (position == 2) {
            locationView = layoutInflater.inflate(R.layout.pass_screen, container, false);
            initPassScreen(locationView);
            container.addView(locationView, 0);

        }

        return locationView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /*public void loadBottomSheet(View view){
        View botoom_sheet=view.findViewById(R.id.es_bottom_sheet);
        behavior=BottomSheetBehavior.from(botoom_sheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback(){
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }*/

    public Bitmap getBitmap(int id){
        Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(),
                id);
        BlurBuilder builder=new BlurBuilder();
        Bitmap bitmap1=builder.blur(context, bitmap);
        return bitmap1;
    }

    public int getBackground(){
        int background=sharedPreferences.getInt("background", 0);
        if(background!=0){
            return background;
        }

        return R.drawable.a28;
    }


    public void initPassScreen(View view) {

        ps_pass1=(ImageView)view.findViewById(R.id.ps_pass1);
        ps_pass1.setOnClickListener(passOnClick);
        ps_pass2=(ImageView)view.findViewById(R.id.ps_pass2);
        ps_pass2.setOnClickListener(passOnClick);
        ps_pass3=(ImageView)view.findViewById(R.id.ps_pass3);
        ps_pass3.setOnClickListener(passOnClick);
        ps_pass4=(ImageView)view.findViewById(R.id.ps_pass4);
        ps_pass4.setOnClickListener(passOnClick);


        number0 = (ImageView) view.findViewById(R.id.ps_0);
        number0.setOnClickListener(passOnClick);
        number1 = (ImageView) view.findViewById(R.id.ps_1);
        number1.setOnClickListener(passOnClick);
        number2 = (ImageView) view.findViewById(R.id.ps_2);
        number2.setOnClickListener(passOnClick);
        number3 = (ImageView) view.findViewById(R.id.ps_3);
        number3.setOnClickListener(passOnClick);
        number4 = (ImageView) view.findViewById(R.id.ps_4);
        number4.setOnClickListener(passOnClick);
        number5 = (ImageView) view.findViewById(R.id.ps_5);
        number5.setOnClickListener(passOnClick);
        number6 = (ImageView) view.findViewById(R.id.ps_6);
        number6.setOnClickListener(passOnClick);
        number7 = (ImageView) view.findViewById(R.id.ps_7);
        number7.setOnClickListener(passOnClick);
        number8 = (ImageView) view.findViewById(R.id.ps_8);
        number8.setOnClickListener(passOnClick);
        number9 = (ImageView) view.findViewById(R.id.ps_9);
        number9.setOnClickListener(passOnClick);

        ps_cancel=(TextView)view.findViewById(R.id.ps_cancel);
        ps_cancel.setOnClickListener(passOnClick);
        ps_del=(TextView)view.findViewById(R.id.ps_del);
        ps_del.setOnClickListener(passOnClick);

    }

    public View.OnClickListener passOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.ps_0:
                    userpass.add(0);
                    lenghPass();
                    break;
                case R.id.ps_1:
                    userpass.add(1);
                    lenghPass();
                    break;
                case R.id.ps_2:
                    userpass.add(2);
                    lenghPass();
                    break;
                case R.id.ps_3:
                    userpass.add(3);
                    lenghPass();
                    break;
                case R.id.ps_4:
                    userpass.add(4);
                    lenghPass();
                    break;
                case R.id.ps_5:
                    userpass.add(5);
                    lenghPass();
                    break;
                case R.id.ps_6:
                    userpass.add(6);
                    lenghPass();
                    break;
                case R.id.ps_7:
                    userpass.add(7);
                    lenghPass();
                    break;
                case R.id.ps_8:
                    userpass.add(8);
                    lenghPass();
                    break;

                case R.id.ps_9:
                    userpass.add(9);
                    lenghPass();
                    break;
                case R.id.ps_del:
                    deletePass();
                    lenghPass();
                    break;
                case R.id.ps_cancel:
                    lockScreenActivity.setPager(1);
                    break;
            }
        }
    };

    public void deletePass(){
        if(userpass.size()>0){
            userpass.remove(userpass.size()-1);
        }
    }

    public void lenghPass(){
        int size_pass=userpass.size();
        switch (size_pass){
            case 0:
                ps_pass1.setImageResource(R.drawable.open_dot);
                ps_pass2.setImageResource(R.drawable.open_dot);
                ps_pass3.setImageResource(R.drawable.open_dot);
                ps_pass4.setImageResource(R.drawable.open_dot);
                break;
            case 1:
                ps_pass1.setImageResource(R.drawable.close_dot);
                ps_pass2.setImageResource(R.drawable.open_dot);
                ps_pass3.setImageResource(R.drawable.open_dot);
                ps_pass4.setImageResource(R.drawable.open_dot);
                break;
            case 2:
                ps_pass1.setImageResource(R.drawable.close_dot);
                ps_pass2.setImageResource(R.drawable.close_dot);
                ps_pass3.setImageResource(R.drawable.open_dot);
                ps_pass4.setImageResource(R.drawable.open_dot);
                break;
            case 3:
                ps_pass1.setImageResource(R.drawable.close_dot);
                ps_pass2.setImageResource(R.drawable.close_dot);
                ps_pass3.setImageResource(R.drawable.close_dot);
                ps_pass4.setImageResource(R.drawable.open_dot);
                break;
            case 4:
                ps_pass1.setImageResource(R.drawable.close_dot);
                ps_pass2.setImageResource(R.drawable.close_dot);
                ps_pass3.setImageResource(R.drawable.close_dot);
                ps_pass4.setImageResource(R.drawable.close_dot);
                checkPass();
                break;
        }
    }

    public void checkPass() {
        Log.d("tab", "Check");
        String user="";
        for(int i=0;i<userpass.size();i++){
            user+=userpass.get(i);
        }
        if(user.equals(getPass())){
            boolean time=sharedPreferences.getBoolean("time", false);
            boolean setting=sharedPreferences.getBoolean("setting", false);
            String app=sharedPreferences.getString("app", null);
            if(time==true){
                editor.putBoolean("time", false);
                editor.commit();
                startTime();
                userpass.removeAll(userpass);
                lockScreenActivity.wm.removeView(lockScreenActivity.myView);
                lockScreenActivity.myView=null;
                lockScreenActivity.finish();

            }else if(setting==true){
                editor.putBoolean("setting", false);
                editor.commit();
                startSetting();
                userpass.removeAll(userpass);
                lockScreenActivity.wm.removeView(lockScreenActivity.myView);
                lockScreenActivity.myView=null;
                lockScreenActivity.finish();
            }else  if(app!=null){
                Intent intent = lockScreenActivity.getPackageManager().getLaunchIntentForPackage(app);
                editor.remove("app");
                editor.commit();
                lockScreenActivity.startActivity(intent);
                lockScreenActivity.wm.removeView(lockScreenActivity.myView);
                lockScreenActivity.myView=null;
                lockScreenActivity.finish();
            }else{
                userpass.removeAll(userpass);
                lockScreenActivity.wm.removeView(lockScreenActivity.myView);
                lockScreenActivity.myView=null;
                lockScreenActivity.finish();
            }


        }else{
            userpass.removeAll(userpass);
            lenghPass();
        }
    }

    public String getPass(){
        String password=sharedPreferences.getString("password", null);

        return password;
    }

    public void showBattery(ImageView img_battery) {
        int battery_status = battery.getBattery();
        if (battery_status >= 90) {
            img_battery.setImageResource(R.drawable.battery4);
        } else if (battery_status >= 60) {
            img_battery.setImageResource(R.drawable.battery3);
        } else if (battery_status > 30) {
            img_battery.setImageResource(R.drawable.battery2);
        } else {
            img_battery.setImageResource(R.drawable.battery1);
        }
    }

    public void showNetwork(ImageView image_view, TextView text_view){
        text_view.setText(mainActivity.operatorName);
    }

    public void showMain(View view){

        se_clock=(TextClock)view.findViewById(R.id.se_clock);
        se_clock.setFormat24Hour("HH:mm");
        se_clock.setTypeface(lockScreenActivity.type);

        se_date=(TextView)view.findViewById(R.id.se_date);
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yy");
        Calendar c=Calendar.getInstance();
        se_date.setText(format.format(c.getTime())+"  "+new SimpleDateFormat("EE", Locale.ENGLISH).format(c.getTime()));
        se_date.setTypeface(lockScreenActivity.type);

        VerticalPager verticalPager=(VerticalPager)view.findViewById(R.id.sub_pager);
        verticalPager.setAdapter(new EnglishAdapter(context));


    }


    public void showNotification(View view){

        ns_clock=(TextClock)view.findViewById(R.id.ns_clock);
        ns_clock.setFormat24Hour("HH:mm");
        ns_clock.setTypeface(lockScreenActivity.type);

        ns_date=(TextView)view.findViewById(R.id.ns_date);
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yy");
        Calendar c=Calendar.getInstance();
        ns_date.setText(format.format(c.getTime())+"  "+new SimpleDateFormat("EE", Locale.ENGLISH).format(c.getTime()));
        ns_date.setTypeface(lockScreenActivity.type);

        ns_clear=(TextView)view.findViewById(R.id.ns_clear);
        ns_clear.setOnClickListener(nsClear);

        ns_List=lockScreenActivity.ls_List;
        Log.d("tag","ABC: "+ns_List.size());
        ns_listview=(ListView)view.findViewById(R.id.ns_listview);
        ns_adapter=new NotificationAdapter(lockScreenActivity, ns_List);
        ns_listview.setAdapter(ns_adapter);
        ns_listview.setOnItemClickListener(ns_listener);
    }

    public View.OnClickListener nsClear=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent("dong.NOTIFICATION_LISTENER_EXAMPLE");
            intent.putExtra("command", 3);
            lockScreenActivity.sendBroadcast(intent);
            Log.d("tag", "OLE");
        }
    };



    public void startTime(){
        Intent intent=new Intent(AlarmClock.ACTION_SET_ALARM);
        context.startActivity(intent);
    }

    public void startSetting(){
        Intent intent=new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public AdapterView.OnItemClickListener ns_listener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent LaunchIntent = lockScreenActivity.getPackageManager().getLaunchIntentForPackage(ns_List.get(position).getPack());
            if(LaunchIntent!=null){
                openItent(ns_List.get(position).getPack());
            }else{
                boolean statePin=sharedPreferences.getBoolean("statePin", false);
                if(statePin==false){
                    lockScreenActivity.finish();
                }
            }

        }
    };

    public void openItent(String pack){
        boolean statePin=sharedPreferences.getBoolean("statePin", false);
        if(statePin==false){
            Intent intent = lockScreenActivity.getPackageManager().getLaunchIntentForPackage(pack);
            lockScreenActivity.startActivity(intent);
            lockScreenActivity.finish();
        }else{
            editor.putString("app", pack);
            editor.commit();
            lockScreenActivity.loadPassSetting();
        }
    }


    /*class NotificationListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String pack = intent.getStringExtra("pack");
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");


            byte[] byteArray =intent.getByteArrayExtra("img");
            Bitmap bmp = null;
            if(byteArray !=null) {
                bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            }

            NotificationItem model = new NotificationItem();
            model.setTitle(title);
            model.setPack(pack);
            model.setContent(content);
            model.setImg(bmp);



            if(ns_List !=null) {
                if(model.getPack()!=null){
                    ns_List.add(model);
                    ns_adapter.notifyDataSetChanged();
                }

            }else {

                if(model.getPack()!=null){
                    ns_List = new ArrayList<NotificationItem>();
                    ns_List.add(model);
                    ns_adapter.notifyDataSetChanged();
                }

            }

        }
    }*/

}
