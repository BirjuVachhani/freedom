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

/**
 * Headless fragment that handles permission model, permission requests and results
 * @property resultLiveData MutableLiveData<Permission> on which results of the permission request will be set.
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

    /**
     * Sets [MutableLiveData] instance on which events will be posted
     * @param liveData MutableLiveData<Permission>
     */
    internal fun setResultLiveData(liveData: MutableLiveData<Permission>) {
        resultLiveData = liveData
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    /**
     * Entry point method to initiate permission request execution
     * @param permission String is the requested permission
     * @param liveData MutableLiveData<Permission> on which execution result will be emitted.
     */
    internal fun executePermissionRequest(permission: String, liveData: MutableLiveData<Permission>) {
        if (!hasPermissionDefinedInManifest(permission)) {
            Throwable("$permission is not defined in the manifest")
            return
        }
        resultLiveData = liveData
        initPermissionModel(permission)
    }

    /**
     * Initiates permission model to request [permission].
     *
     * It follows google's recommendations for permission model.
     *
     * 1. Check whether the permission is already granted or not.
     * 2. If not, then check if rationale should be displayed or not.
     * 3. If not, check if the permission is requested for the first time or not.
     * 4. If yes, save that in preferences and request permission.
     * 5. If not, then the permission is permanently denied.
     * @param permission String is the permission that needs to be requested.
     */
    private fun initPermissionModel(permission: String) {
        Log.e(this::class.java.simpleName, "Initializing permission model")
        if (!hasPermission(permission)) {
            //doesn't have permission, checking if user has been asked for permission earlier
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission)) {
                // should show rationale
                resultLiveData.value =
                    Permission(
                        permission,
                        PermissionState.ShouldShowRationale(RationaleInterface { requestPermission(permission) })
                    )
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
     * Actual request for the permission
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