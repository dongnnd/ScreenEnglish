package recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;

import service.AlarmService;

import static android.content.Context.MODE_PRIVATE;

public class AlarmReceiver extends BroadcastReceiver {
    public SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreferences=context.getSharedPreferences("data", MODE_PRIVATE);
        boolean stateRemind=sharedPreferences.getBoolean("stateRemind", false);

        if(stateRemind==true){
            Calendar calendar=Calendar.getInstance();
            int day=calendar.get(Calendar.DAY_OF_WEEK);
            if(day==Calendar.TUESDAY){
                Log.d("tag",day+"");
                Intent service1 = new Intent(context, AlarmService.class);
                context.startService(service1);
            }
        }
    }
}
