package service;

import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.example.dongnd.screenenglish.R;

import recevier.BootReceiver;

/**
 * Created by Yaakov Shahak on 14/12/2016.
 */

public class LockScreenService extends Service{

    BootReceiver bootReceiver;
    static LockScreenService lockScreenService;
    public boolean check=true;

    @Override
    public IBinder onBind(Intent intent) {
        // Not used
        return null;
    }

    public static LockScreenService getInstance(){
        if(lockScreenService==null){
            return new LockScreenService();
        }
        return lockScreenService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        lockScreenService=this;
        ((KeyguardManager)getSystemService(KEYGUARD_SERVICE)).newKeyguardLock("IN").disableKeyguard();
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        this.bootReceiver = new BootReceiver();
        registerReceiver(this.bootReceiver, localIntentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onTaskRemoved(intent);
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent intent=new Intent(getApplicationContext(), this.getClass());
        intent.setPackage(getPackageName());
        startService(intent);
        super.onTaskRemoved(rootIntent);
    }


    public void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(this.bootReceiver);
    }


}
