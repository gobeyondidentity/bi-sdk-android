package com.beyondidentity.embedded.sdk.utils

import android.content.Context
import android.content.pm.PackageManager.NameNotFoundException

fun Context.appVersionName(): String = try {
    // todo instrument tests are grabbing the test package and it's failing
    // "com.beyondidentity.authenticator.sdk.embedded"
    packageManager.getPackageInfo(packageName, 0).versionName
} catch (t: NameNotFoundException) {
    "unknown"
}
