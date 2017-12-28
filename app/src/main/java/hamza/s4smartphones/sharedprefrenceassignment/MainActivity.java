package hamza.s4smartphones.sharedprefrenceassignment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class MainActivity extends AppCompatActivity {

    Switch airplane_mode;
    Switch wifi_mode;
    Boolean aBoolean;
    Boolean bBoolean;
    WifiManager wifiManager;
    EditText myname;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    TextView battery_mode;
    NotificationManager notificationManager;
    String Id = "chanel 1";
    Intent intent;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);
        ButterKnife.bind(this);

        myname = findViewById(R.id.etName);
        battery_mode = findViewById(R.id.battery_status);
        airplane_mode = findViewById(R.id.airplane_switch);
        wifi_mode = findViewById(R.id.wifi_switch);

        sp = getSharedPreferences("Info", MODE_PRIVATE);
        myname.setText(sp.getString("myName", "Not Found"));

        intent = new Intent(MainActivity.this, Settings.class);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        wifiManager = (WifiManager) this.getApplicationContext().getSystemService(WIFI_SERVICE);
        wifi_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked && !wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(true);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, Id)
                            .setContentTitle("WIFI Notification")
                            .setSmallIcon(R.drawable.wifi)
                            .setContentText("WIFI On")
                            .setContentIntent(pendingIntent);
                    Notification notification = builder.build();
                    notificationManager.notify(1, notification);


                } else if (!isChecked && wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(false);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, Id)
                            .setContentTitle("WIFI Notification")
                            .setSmallIcon(R.drawable.nowifi)
                            .setContentText("WIFI Off")
                            .setContentIntent(pendingIntent);
                    Notification notification = builder.build();
                    notificationManager.notify(0, notification);
                }
            }
        });
    }



    @Subscribe
    public void onStateChanged(StateChanged state) {
        aBoolean = state.getaBoolean();
        airplane_mode.setChecked(aBoolean);
        bBoolean = state.getbBoolean();
        wifi_mode.setChecked(bBoolean);
        battery_mode.setText(state.getS1().toString());

        if (state.getS1() == "Low") {
            battery_mode.setTextColor(Color.RED);
        } else {
            battery_mode.setTextColor(Color.GREEN);
        }
    }
    @OnTextChanged(value = R.id.etName, callback = OnTextChanged.Callback.TEXT_CHANGED)
    void TextChanged(CharSequence text) {

        editor = sp.edit();
        editor.putString("myName", text.toString());
        editor.apply();
    }
}
