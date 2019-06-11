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

/**
 * Main class managing subscriptions and permissions.
 *
 * It is the entry point of the library which provides methods to initiate permission request process.
 * At this, it currently support permission requests from [FragmentActivity] and [Fragment]. It uses [MutableLiveData] to pass permission updates to the requester to support lifecycle methods.
 */
object Freedom {

    private val resultLiveData = MutableLiveData<Permission>()
    private val requestResult = FreedomResult()

    private val permissionObserver = Observer<Permission> { result ->
        result?.let { permission ->
            requestResult.callEventByState(permission.state)
            resultLiveData.value = null
        }
    }

    /**
     * Requests given permission.
     *
     * Allows to request permission from an [FragmentActivity]. Responsible for initializing [PermissionHelper].
     * @param activity FragmentActivity is the activity from which the permission is requested
     * @param permission String is the permission which will be requested
     * @return FreedomResult which can be used to listen for different permission events like Granted,
     * Denied, Permanently Denied or ShouldShowRationale
     */
    fun request(activity: FragmentActivity, permission: String): FreedomResult =
        requestPermission(getOrInitializeHelper(activity.supportFragmentManager), activity, permission)

    /**
     * Requests given permission.
     *
     * Allows to request permission from a [Fragment]. Responsible for initializing [PermissionHelper].
     * @param fragment Fragment is the fragment from which the permission is requested
     * @param permission String is the permission which will be requested
     * @return FreedomResult which can be used to listen for different permission events like Granted,
     * Denied, Permanently Denied or ShouldShowRationale
     */
    fun request(fragment: Fragment, permission: String): FreedomResult =
        requestPermission(getOrInitializeHelper(fragment.childFragmentManager), fragment, permission)

    /**
     * Requests given permission to [PermissionHelper] which is responsible for handling
     * permission model and emitting results in [resultLiveData].
     *
     * It removes previously set observers on [resultLiveData] and sets new one.
     * Once an event/result is emitted from [resultLiveData], it is reset to null so that configuration changes doesn't trigger already executed events.
     * It uses [Handler] to post permission request execution event on [PermissionHelper] to keep
     * everything in sync and eliminate unset/null listener state. It gives [FreedomResult] required time to be set before it starts executing permission request and emitting results.
     * @param mPermissionHelper PermissionHelper is the helper fragment which will execute permission requests.
     * @param owner LifecycleOwner will be used to observe on [resultLiveData] in life cycle aware method.
     * @param permission String is the requested permission string
     * @return FreedomResult which can be used to listen for different permission events like Granted,
     * Denied, Permanently Denied or ShouldShowRationale
     */
    private fun requestPermission(
        mPermissionHelper: PermissionHelper,
        owner: LifecycleOwner,
        permission: String
    ): FreedomResult {
        resultLiveData.removeObservers(owner)
        resultLiveData.removeObserver(permissionObserver)
        resultLiveData.observe(owner, permissionObserver)

        Handler().post { mPermissionHelper.executePermissionRequest(permission, resultLiveData) }
        return requestResult
    }

    /**
     * Sets an observer on [resultLiveData]. It is supposed to be called on lifecycle methods like [FragmentActivity.onCreate] and [Fragment.onViewCreated].
     *
     * It is suggested to use this method to register event listener rather than using [request] method's returned result.
     * The benefit of using this is that when activity or fragment forced to be restarted it will still receive last update that occurred before the restart.
     * @param owner LifecycleOwner will be used to observe on [resultLiveData] in life cycle aware method.
     * @return FreedomResult which can be used to listen for different permission events like Granted,
     * Denied, Permanently Denied or ShouldShowRationale.
     */
    fun setListener(owner: LifecycleOwner): FreedomResult {
        resultLiveData.removeObservers(owner)
        resultLiveData.removeObserver(permissionObserver)
        resultLiveData.observe(owner, permissionObserver)
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
        }
        return permissionHelper
    }
}