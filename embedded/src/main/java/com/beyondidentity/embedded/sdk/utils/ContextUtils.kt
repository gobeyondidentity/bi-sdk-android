package com.beyondidentity.embedded.sdk.utils

import android.content.Context
import android.content.pm.PackageManager.NameNotFoundException

fun Context.appVersionName(): String = try {
    packageManager.getPackageInfo(packageName, 0).versionName
} catch (t: NameNotFoundException) {
    "unknown"
}
