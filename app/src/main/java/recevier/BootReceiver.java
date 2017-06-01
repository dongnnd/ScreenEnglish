package recevier;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.dongnd.screenenglish.LockScreenActivity;
import com.example.dongnd.screenenglish.MainActivity;

import java.util.Calendar;

import service.AlarmService;
import service.LockScreenService;

import static android.content.Context.MODE_PRIVATE;

public class BootReceiver extends BroadcastReceiver {

    MainActivity mainActivity=MainActivity.getInstance();
    public SharedPreferences sharedPreferences;

    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreferences=context.getSharedPreferences("data", MODE_PRIVATE);

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            if(LockScreenActivity.getInstance().wm!=null){
                LockScreenActivity.getInstance().finish();
            }else{
                Log.d("tag","Khong ton tai");
            }

            if(sharedPreferences.getBoolean("state", false)){
                BootReceiver.this.startUnlockScreen(context);
            }
        }else if("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())){
            Intent main=new Intent(context, MainActivity.class);
            main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
            context.startActivity(main);
           // ((MainActivity)context).finish();
            Intent locscreen=new Intent(context, LockScreenActivity.class);
            locscreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(locscreen);
        }


    }

    public void startUnlockScreen(Context context){
        Intent lockactivity= new Intent(context, LockScreenActivity.class);
        lockactivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(lockactivity);
    }


}
