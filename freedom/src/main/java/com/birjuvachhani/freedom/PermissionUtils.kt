package com.birjuvachhani.freedom

import android.content.Context.MODE_PRIVATE
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/*
 * Created by Birju Vachhani on 25 May 2019
 * Copyright © 2019 Freedom. All rights reserved.
 */

/**
 * Checks whether the permission is defined in the manifest or not.
 * @receiver Fragment used to get context
 * @param permission String is the permission that needs to be checked in manifest.
 * @return Boolean true, if the permission is present in the manifest file, false otherwise.
 */
internal fun Fragment.hasPermissionDefinedInManifest(permission: String): Boolean =
    requireContext().packageManager.getPackageInfo(requireContext().packageName, PackageManager.GET_PERMISSIONS)
        ?.requestedPermissions?.contains(permission) ?: false

/**
 * Checks whether the app has the requested permission permission or not
 * @receiver Fragment is used for context
 * @param permission String is the permission that needs to be checked
 * @return Boolean true if the app has requested permission, false otherwise.
 * */
internal fun Fragment.hasPermission(permission: String): Boolean =
    ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED

/**
 * Checks whether the requested permission is asked for the first time or not.
 *
 * The value is stored in the shared preferences.
 * @receiver Fragment is used to get context.
 * @param permission String is the permission that needs to be checked.
 * @return Boolean true if the permission is asked for the first time, false otherwise.
 */
internal fun Fragment.isPermissionAskedFirstTime(permission: String): Boolean {
    return requireContext().getSharedPreferences("permissions_pref", MODE_PRIVATE).getBoolean(permission, true)
}

/**
 * Writes the false value into shared preferences which indicates that the [permission] has been requested previously.
 * @receiver Fragment is used to get context.
 * @param permission String is the permission that needs to be checked.
 */
internal fun Fragment.setPermissionAsked(permission: String) {
    requireContext()
        .getSharedPreferences("permissions_pref", MODE_PRIVATE)
        .edit()
        .putBoolean(permission, false)
        .apply()
}