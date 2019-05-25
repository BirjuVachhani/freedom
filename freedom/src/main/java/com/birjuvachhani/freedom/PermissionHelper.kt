package com.birjuvachhani.freedom

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData

/*
 * Created by Birju Vachhani on 25 May 2019
 * Copyright © 2019 Freedom. All rights reserved.
 */


internal class PermissionHelper : Fragment() {

    companion object {
        const val TAG = "PermissionHelper"
        private const val REQUEST_CODE = 7

        /**
         * Creates a new instance o this class and returns it.
         * */
        internal fun newInstance(): PermissionHelper = PermissionHelper()
    }

    private var resultLiveData = MutableLiveData<Permission>()

    internal fun setResultLiveData(liveData: MutableLiveData<Permission>) {
        resultLiveData = liveData
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    fun executePermissionRequest(permission: String, liveData: MutableLiveData<Permission>) {
        if (!hasPermissionDefinedInManifest(permission)) {
            Throwable(
                """No location permission is defined in manifest.
                            Please make sure that location permission is added to the manifest"""
            )
            return
        }
        resultLiveData = liveData
        initPermissionModel(permission)
    }

    /**
     * Initiates permission model to request location permission in order to retrieve location successfully.=
     * */
    private fun initPermissionModel(permission: String) {
        Log.e(this::class.java.simpleName, "Initializing permission model")
        if (!hasPermission(permission)) {
            //doesn't have permission, checking if user has been asked for permission earlier
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission)) {
                // should show rationale
                resultLiveData.value =
                    Permission(permission, PermissionState.ShouldShowRationale(object : RationaleInterface {
                        override fun request() {
                            requestPermission(permission)
                        }
                    }))
            } else {
                if (isPermissionAskedFirstTime(permission)) {
                    // request permission
                    setPermissionAsked(permission)
                    requestPermission(permission)
                } else {
                    // permanently denied
                    resultLiveData.value = Permission(permission, PermissionState.PermanentlyDenied)
                }
            }
        } else {
            // permission is already granted
            resultLiveData.value = Permission(permission, PermissionState.Granted)
        }
    }

    /**
     * Requests user for location permission
     * */
    private fun requestPermission(permission: String) = requestPermissions(arrayOf(permission), REQUEST_CODE)

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE) {
            val permission = permissions.firstOrNull() ?: return
            resultLiveData.value = Permission(
                permission,
                if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED)
                    PermissionState.Granted
                else
                    PermissionState.Denied
            )
        }
    }
}