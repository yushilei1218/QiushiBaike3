package com.yushilei.qiushibaike3.receivers;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.yushilei.qiushibaike3.R;
import com.yushilei.qiushibaike3.Utils.UrlFormat;

public class NetStateReceiver extends BroadcastReceiver {
    public NetStateReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("NetStateReceiver", "onReceive");

        String action = intent.getAction();

        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

            ConnectivityManager service = (ConnectivityManager) context.getSystemService(
                    Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = service.getActiveNetworkInfo();

            String netWorkState = "无网络";
            if (networkInfo != null) {
                int type = networkInfo.getType();
                NetworkInfo.State state = networkInfo.getState();

                switch (type) {
                    case ConnectivityManager.TYPE_WIFI:
                        if (state == NetworkInfo.State.CONNECTED) {
                            Log.d("NetStateReceiver", "setmedium");
                            netWorkState = "使用至WIFI";
                            UrlFormat.setImageType("medium");
                        }
                        break;
                    case ConnectivityManager.TYPE_MOBILE:
                        if (state == NetworkInfo.State.CONNECTED) {
                            UrlFormat.setImageType("medium");
                            netWorkState = "使用2G/3G/4G网络";
                        }
                        break;
                    default:
                        if (state == NetworkInfo.State.CONNECTED) {
                            UrlFormat.setImageType("small");
                            netWorkState = "切换其他网络";
                        }
                        break;
                }
            }
            sendNotification(context, "网络变化", netWorkState);
        }
    }

    private static void sendNotification(Context context, String title, String content) {
        //1.准备notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        //2.设置内容
        builder.setSmallIcon(R.mipmap.notification).setContentTitle(title).setContentText(content);
        Notification n = builder.build();
        //3.发送通知
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);

        managerCompat.notify(998, n);
    }
}
