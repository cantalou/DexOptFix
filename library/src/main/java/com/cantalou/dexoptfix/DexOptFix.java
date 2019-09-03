package com.cantalou.dexoptfix;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.util.Log;

/**
 * @author LinZhiWei
 */
public class DexOptFix {

    private static boolean isDebug(Context context) {
        ApplicationInfo info = context.getApplicationInfo();
        if (info == null) {
            return false;
        }
        return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    public static void fix(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            if (context != null && isDebug(context)) {
                Log.i("DexOptFix", "Just do nothing when system version larger than KITKAT");
            }
            return;
        }
        try {
            System.loadLibrary("dexoptfix");
        } catch (Throwable e) {
            Log.e("DexOptFix", "", e);
        }
    }

}
