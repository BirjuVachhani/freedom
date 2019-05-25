package com.birjuvachhani.freedom

import android.content.Context.MODE_PRIVATE
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/*
 * Created by Birju Vachhani on 25 May 2019
 * Copyright © 2019 Freedom. All rights reserved.
 */

internal fun Fragment.hasPermissionDefinedInManifest(permission: String): Boolean =
    requireContext().packageManager.getPackageInfo(requireContext().packageName, PackageManager.GET_PERMISSIONS)
        ?.requestedPermissions?.contains(permission) ?: false

/**
 * Checks whether the app has location permission or not
 * @return true is the app has location permission, false otherwise.
 * */
internal fun Fragment.hasPermission(permission: String): Boolean =
    ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED

internal fun Fragment.isPermissionAskedFirstTime(permission: String): Boolean {
    return requireContext().getSharedPreferences("permissions_pref", MODE_PRIVATE).getBoolean(permission, true)
}

internal fun Fragment.setPermissionAsked(permission: String) {
    requireContext()
        .getSharedPreferences("permissions_pref", MODE_PRIVATE)
        .edit()
        .putBoolean(permission, false)
        .apply()
}