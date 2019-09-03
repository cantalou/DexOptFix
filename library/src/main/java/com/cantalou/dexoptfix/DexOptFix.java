package com.cantalou.dexoptfix;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.util.Log;

/**
 * If we called DexFile.loadDex() or new DexClassLoader(path), android will fork a sub process to execute dexopt command.
 * In some devices , sub process will receive a signal "SIGALRM" two seconds after process start. Then dexopt will
 * fail with code 0x000e. After that, your app will crash with an exception: "IOException : unable to open DEX file" or "ClassNotFoundException" or "ClassNoDefinedError"
 *
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
        System.loadLibrary("dexoptfix");
    }

}
