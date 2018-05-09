package robbyturnip333.gmail.com.service;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {
    private TextView mBatteryLevelText, batteryStatus, batteryCharge, networkStatus;
    private ProgressBar mBatteryLevelProgress;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBatteryLevelText = (TextView) findViewById(R.id.t1);
        batteryStatus = findViewById(R.id.status);
        batteryCharge = findViewById(R.id.plug);
        networkStatus = findViewById(R.id.network);
        mBatteryLevelProgress = (ProgressBar) findViewById(R.id.pb1);
        mReceiver = new BatteryBroadcastReceiver();
    }

    public void playAudio(View view) {
        Intent baru = new Intent(this, PlayAudio.class);
        startService(baru);
    }

    public void stopAudio(View view) {
        Intent stopbaru = new Intent(this, PlayAudio.class);
        stopService(stopbaru);
    }


    @Override
    protected void onStart() {
        registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(mReceiver);
        super.onStop();
    }

    private class BatteryBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent baru = context.registerReceiver(null, ifilter);

            int status = baru.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;

            int chargePlug = baru.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
            boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;



            mBatteryLevelText.setText(getString(R.string.battery_level) + " " + level + "%");
            if (level >= 80) {
                batteryStatus.setText("Kuat");
            } else if (level >= 30) {
                batteryStatus.setText("Sedang");
            } else if (level < 30) {
                batteryStatus.setText("Lemah");
            }
            mBatteryLevelProgress.setProgress(level);

            if (status == 2) {
                if (chargePlug == 2)
                    batteryCharge.setText("Battery Charging with USB Charging");
                else
                    batteryCharge.setText("Battery Charging with AC Charging");
            } else {
                batteryCharge.setText("Battery Not Charging");
            }
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info=cm.getActiveNetworkInfo();
            boolean isConnected= info != null && info.isConnectedOrConnecting();
            boolean isWifi= info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI;
            boolean isData=info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE;
            if(isWifi==true){
                networkStatus.setText("Connected to Internet Using WiFi");
            }else if(isData==true){
                networkStatus.setText("Connected to Internet Using Mobile Data "+ isConnectionFast(info.getSubtype()));
            }
            else {
                networkStatus.setText("Not Connected to Internet");
            }

        }
    }




    public static String isConnectionFast(int subType) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return "1xRTT"; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return "CDMA"; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return "EDGE"; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return "EVDO_O"; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return "EVDO_A"; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return "GPRS"; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return "HSDPA"; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return "HSPA"; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return "HSUPA"; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return "UMTS"; // ~ 400-7000 kbps
            /*
             * Above API level 7, make sure to set android:targetSdkVersion
             * to appropriate level to use these
             */
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return "EHRPD"; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return "EVDO_B"; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return "HSPAP"; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return "IDEN"; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return "LTE"; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return "UNKNOWN";
            }
    }
}