package com.argando.parcersample.backgroundupdate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * User: argando
 * Date: 21.07.12
 * Time: 20:25
 */

/**
 * This receiver receive android boot load intent and start parsing service
 */
public class OnBootReceiver extends BroadcastReceiver {
    private final String mOnBootIntentAction = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mOnBootIntentAction.equalsIgnoreCase(intent.getAction())) {
            Intent updateService = new Intent(context, ParceService.class);
            context.startService(updateService);
        }
    }
}
