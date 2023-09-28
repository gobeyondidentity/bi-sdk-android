package com.beyondidentity.embedded.sdk.utils

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.os.Build

@Suppress("DEPRECATION")
fun Context.appVersionName(): String = try {
    // todo instrument tests are grabbing the test package and it's failing
    // "com.beyondidentity.authenticator.sdk.embedded"
    val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
    } else {
        packageManager.getPackageInfo(packageName, 0)
    }
    packageInfo.versionName
} catch (t: NameNotFoundException) {
    "unknown"
} catch (t: NullPointerException) {
    "unknown"
}
