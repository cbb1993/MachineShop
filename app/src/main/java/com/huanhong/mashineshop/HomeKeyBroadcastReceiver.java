package com.huanhong.mashineshop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/**
 * Created by 坎坎.
 * Date: 2019/7/15
 * Time: 12:14
 * describe:
 */
public class HomeKeyBroadcastReceiver extends BroadcastReceiver {
    String SYSTEM_REASON = "reason";
    String SYSTEM_HOME_KEY = "homekey";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra(SYSTEM_REASON);
            if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                //表示按了home键,程序到了后台
                System.exit(0);
            }
        }
    }
}
