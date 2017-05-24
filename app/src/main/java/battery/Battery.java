package battery;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * Created by DongND on 3/13/2017.
 */

public class Battery {

    public IntentFilter filter;
    public Intent batteryStatus;

    public Battery(Context context) {
        filter=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryStatus=context.registerReceiver(null, filter);


    }

    public int getBattery(){
        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

        float batteryPct = level / (float) scale;

        return (int)(batteryPct*100);
    }

    public boolean getStatus(){
        int status=batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        if(status==BatteryManager.BATTERY_STATUS_CHARGING){
            return true;
        }

        return false;
    }

}
