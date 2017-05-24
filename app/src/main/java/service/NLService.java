package service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.example.dongnd.screenenglish.LockScreenActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class NLService extends NotificationListenerService {
    private String TAG = this.getClass().getSimpleName();
    private NLServiceReceiver nlservicereciver;
    private LockScreenActivity lockScreenActivity;

    public static NLService nlService;


    @Override
    public void onCreate() {
        super.onCreate();

        nlService=this;
        lockScreenActivity=LockScreenActivity.getInstance();

        nlservicereciver = new NLServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("dong.NOTIFICATION_LISTENER_EXAMPLE");
        registerReceiver(nlservicereciver,filter);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nlservicereciver);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        Intent i1=new Intent("dong.NOTIFICATION_LISTENER_EXAMPLE");
        i1.putExtra("active", 2);
        sendBroadcast(i1);

        int i=1;
        for (StatusBarNotification sbn1 : NLService.this.getActiveNotifications()) {
            Bundle extras = sbn1.getNotification().extras;
            Intent i2 = new  Intent("dong.NOTIFICATION_LISTENER_EXAMPLE");

            Bitmap img2 = sbn1.getNotification().largeIcon;
            String pack=sbn1.getPackageName();
            String title=extras.getString("android.title");
            String content=extras.getCharSequence("android.text").toString();

            i2.putExtra("pack", pack);

            i2.putExtra("title", title);
            i2.putExtra("content", content);


            Drawable d;
            Bitmap bitmap;
            try
            {
                d = getPackageManager().getApplicationIcon(sbn1.getPackageName());
                bitmap = ((BitmapDrawable)d).getBitmap();

            }
            catch (PackageManager.NameNotFoundException e)
            {
                return;
            }

            if(bitmap != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                i2.putExtra("img", byteArray);
            }else{
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                img2.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                i2.putExtra("img", byteArray);
            }

            sendBroadcast(i2);
            i++;
        }


    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }

    class NLServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            int name=intent.getIntExtra("command", 0);
            if(name==1){
                lockScreenActivity.ls_List.removeAll(lockScreenActivity.ls_List);
                int i=1;
                for (StatusBarNotification sbn1 : NLService.this.getActiveNotifications()) {
                    Intent i2 = new  Intent("dong.NOTIFICATION_LISTENER_EXAMPLE");
                    Bundle extras = sbn1.getNotification().extras;
                    Bitmap img2 = sbn1.getNotification().largeIcon;


                    String pack=sbn1.getPackageName();
                    String title=extras.getString("android.title");
                    String content=extras.getCharSequence("android.text").toString();
                    i2.putExtra("pack", pack);
                    i2.putExtra("title", title);
                    i2.putExtra("content", content);

                    Drawable d;
                    Bitmap bitmap;
                    try
                    {
                        d = getPackageManager().getApplicationIcon(sbn1.getPackageName());
                        bitmap = ((BitmapDrawable)d).getBitmap();

                    }
                    catch (PackageManager.NameNotFoundException e)
                    {
                        return;
                    }

                    if(bitmap != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        i2.putExtra("img", byteArray);
                    }else{
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        img2.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        i2.putExtra("img", byteArray);
                    }


                    sendBroadcast(i2);
                    i++;
                }

            }

            if(intent.getIntExtra("command",0)==3){
                NLService.this.cancelAllNotifications();
            }
        }
    }

    public static NLService getInstance(){
        if(nlService==null){
            nlService=new NLService();
        }

        return nlService;
    }
}
