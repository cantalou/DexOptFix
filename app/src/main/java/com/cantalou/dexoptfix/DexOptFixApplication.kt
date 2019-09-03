package com.cantalou.dexoptfix

import android.app.Application
import android.content.Context

/**
 *
 * @author  LinZhiWei
 * @date    2019年08月30日 12:03
 *
 * Copyright (c) 2019年, 4399 Network CO.ltd. All Rights Reserved.
 */
class DexOptFixApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        System.loadLibrary("dexoptfix")
    }
}