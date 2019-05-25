package com.birjuvachhani.freedom

import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/*
 * Created by Birju Vachhani on 25 May 2019
 * Copyright © 2019 Freedom. All rights reserved.
 */

object Freedom {

    private val resultLiveData = MutableLiveData<Permission>()
    private val requestResult = FreedomResult()

    fun request(activity: FragmentActivity, permission: String): FreedomResult {
        return requestPermission(getOrInitializeHelper(activity.supportFragmentManager), activity, permission)
    }

    fun request(fragment: Fragment, permission: String): FreedomResult {
        return requestPermission(getOrInitializeHelper(fragment.childFragmentManager), fragment, permission)
    }

    private fun requestPermission(
        mPermissionHelper: PermissionHelper,
        owner: LifecycleOwner,
        permission: String
    ): FreedomResult {

        resultLiveData.removeObservers(owner)
        resultLiveData.observe(owner, Observer { result ->
            result?.let { permission ->
                requestResult.callEventByState(permission.state)
                resultLiveData.value = null
            }
        })
        Handler().post { mPermissionHelper.executePermissionRequest(permission, resultLiveData) }
        return requestResult
    }

    fun setListener(owner: LifecycleOwner): FreedomResult {
        resultLiveData.observe(owner, Observer { result ->
            result?.let { permission ->
                requestResult.callEventByState(permission.state)
                resultLiveData.value = null
            }
        })
        return requestResult
    }

    /**
     * Getter method to get an Instance of [PermissionHelper]. It always returns an existing instance if there's any,
     * otherwise creates a new instance.
     * @return Instance of [PermissionHelper] class which can be used to initiate permission request process.
     * */
    private fun getOrInitializeHelper(fragmentManager: FragmentManager): PermissionHelper {
        var permissionHelper = fragmentManager.findFragmentByTag(PermissionHelper.TAG) as? PermissionHelper
        if (permissionHelper == null) {
            Log.e("LazyPermissionHelper", "Initializing new permission helper instance")
            permissionHelper = PermissionHelper.newInstance()
            fragmentManager.beginTransaction().add(permissionHelper, PermissionHelper.TAG)
                .commitNow()
        } else {
            Log.e("LazyPermissionHelper", "already have permission helper instance")
        }
        return permissionHelper
    }
}