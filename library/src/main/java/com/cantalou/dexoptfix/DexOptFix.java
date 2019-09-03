package com.cantalou.dexoptfix;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

/**
 * @author LinZhiWei
 */
public class DexOptFix {

    public static void fix(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            Log.i("DexOptFix", "Just do nothing when system version larger than KITKAT");
            return;
        }

        if (!TextUtils.isEmpty(Build.CPU_ABI) && Build.CPU_ABI.contains("x86")) {
            Log.w("DexOptFix", "ignore if it was x86 devices");
            return;
        }

        try {
            System.loadLibrary("dexoptfix");
        } catch (Throwable e) {
            Log.e("DexOptFix", "", e);
        }
    }

}
