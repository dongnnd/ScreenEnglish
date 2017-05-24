package recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import service.LockScreenService;
import service.NLService;

public class StartApp extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent pushIntent = new Intent(context, LockScreenService.class);
            context.startService(pushIntent);
        }
    }
}
