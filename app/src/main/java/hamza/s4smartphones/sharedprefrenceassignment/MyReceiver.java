package hamza.s4smartphones.sharedprefrenceassignment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Hamza on 12/7/2017.
 */

public class MyReceiver extends BroadcastReceiver {

    Boolean isAirplaneEnabled;
    WifiManager wifiManager;
    Boolean isWifiEnabled;
    String BatteryStatus;

    @Override
    public void onReceive(Context context, Intent intent) {

        //Toast.makeText(context, "OnReceive", Toast.LENGTH_SHORT).show();
        if (intent.getAction().equals(Intent.ACTION_BATTERY_LOW)) {
            BatteryStatus = "Low";
        }
        if (intent.getAction().equals(Intent.ACTION_BATTERY_OKAY)) {
            BatteryStatus = "Okay";
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            isAirplaneEnabled = Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) == 1;
        }
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        isWifiEnabled = wifiManager.isWifiEnabled();

        StateChanged stateChangedEvent = new StateChanged();
        stateChangedEvent.setbBoolean(isWifiEnabled);
        EventBus.getDefault().post(stateChangedEvent);
        stateChangedEvent.setaBoolean(isAirplaneEnabled);
        EventBus.getDefault().post(stateChangedEvent);
        stateChangedEvent.setS1(BatteryStatus);
        EventBus.getDefault().post(stateChangedEvent);
    }
}
