package battery;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by DongND on 3/14/2017.
 */

public class MobileNetWork {

    public int singalNetwork=0;
     String operatorName="";
    TelephonyManager telephonyManager;
    MyPhoneStateListener MyListener;



    public MobileNetWork(Context context){
         telephonyManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
        operatorName = telephonyManager.getNetworkOperatorName();


        MyListener   = new MyPhoneStateListener();
        telephonyManager.listen(MyListener ,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

    }

    private class MyPhoneStateListener extends PhoneStateListener {
        /* Get the Signal strength from the provider, each tiome there is an update */
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);

            if (null != signalStrength && signalStrength.getGsmSignalStrength() != 99) {
                singalNetwork = signalStrength.getGsmSignalStrength();

            }
        }
    }

}
