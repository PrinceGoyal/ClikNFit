package com.cliknfit.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Prince on 23/06/17.
 */

public class AppUtil {

    @SuppressWarnings("deprecation")
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();

        return (int) d.getHeight();
    }

    public static int getVersion(Activity mContext) throws Exception {
        PackageInfo packageInfo = mContext.getPackageManager()
                .getPackageInfo(mContext.getPackageName(), 0);
        int versionCode = packageInfo.versionCode;
        return versionCode;
    }

}
