package recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.dongnd.screenenglish.LockScreenActivity;
import com.example.dongnd.screenenglish.MainActivity;

import service.LockScreenService;

import static android.content.Context.MODE_PRIVATE;

public class BootReceiver extends BroadcastReceiver {

    MainActivity mainActivity=MainActivity.getInstance();

    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            if(mainActivity.getState()){
                BootReceiver.this.startUnlockScreen(context);
            }
        }
    }

    public void startUnlockScreen(Context context){
        Intent lockactivity= new Intent(context, LockScreenActivity.class);
        lockactivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(lockactivity);
    }
}
