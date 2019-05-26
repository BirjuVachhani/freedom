package com.birjuvachhani.freedom

import android.util.Log

/*
 * Created by Birju Vachhani on 25 May 2019
 * Copyright © 2019 Freedom. All rights reserved.
 */

/**
 * Provides dsl methods to listen for permission request events.
 * @property grantedBlock Function0<Unit>? holds reference ot the lambda block which will be invoked
 * when requested permission is granted.
 * @property deniedBlock Function0<Unit>? holds reference ot the lambda block which will be invoked
 * when requested permission is denied by the user.
 * @property permanentlyDeniedBlock Function0<Unit>? holds reference ot the lambda block which will be invoked when requested permission is denied by user and never ask again option is checked.
 * @property shouldShowRationaleBlock Function1<[@kotlin.ParameterName] RationaleInterface, Unit>? holds reference ot the lambda block which will be invoked when requested permission should show rationale to provide the user information why the requested permission is needed.
 */
class FreedomResult {

    private var grantedBlock: (() -> Unit)? = null
    private var deniedBlock: (() -> Unit)? = null
    private var permanentlyDeniedBlock: (() -> Unit)? = null
    private var shouldShowRationaleBlock: ((listener: RationaleInterface) -> Unit)? = null

    /**
     * Setter for [grantedBlock] lambda. Lets us define what needs to be done when requested permission is granted.
     * @param granted () -> Unit contains code that needs to be executed when the requested permission is granted.
     * @return FreedomResult which is this instance itself as it follows builder pattern. It allows to create a chain call of these methods.
     */
    infix fun whenGranted(granted: () -> Unit) = apply {
        grantedBlock = granted
    }

    /**
     * Setter for [deniedBlock] lambda. Lets us define what needs to be done when requested permission is denied.
     * @param denied () -> Unit contains code that needs to be executed when the requested permission is denied.
     * @return FreedomResult which is this instance itself as it follows builder pattern. It allows to create a chain call of these methods.
     */
    infix fun whenDenied(denied: () -> Unit) = apply {
        deniedBlock = denied
    }

    /**
     * Setter for [deniedBlock] lambda. Lets us define what needs to be done when requested permission is permanently denied.
     * @param permanentlyDenied () -> Unit contains code that needs to be executed when the requested permission is permanently denied.
     * @return FreedomResult which is this instance itself as it follows builder pattern. It allows to create a chain call of these methods.
     */
    infix fun whenPermanentlyDenied(permanentlyDenied: () -> Unit) = apply {
        permanentlyDeniedBlock = permanentlyDenied
    }

    /**
     * Setter for [shouldShowRationaleBlock] lambda. Lets us define what needs to be done when requested permission should show a rationale explaining the user why this permission is needed.
     * @param showRationale (listener: RationaleInterface) -> Unit  contains code that needs to be executed when the requested permission should show a rationale. Generally it contains a dialog which explains user why this permission is needed and allows user to re-grant it. listener is an instance of [RationaleInterface] that provides [RationaleInterface.request] method which should be called on dialog action button if user selects to re-grant the requested permission.
     * @return FreedomResult which is this instance itself as it follows builder pattern. It allows to create a chain call of these methods.
     */
    infix fun whenShouldShowRationale(showRationale: (listener: RationaleInterface) -> Unit) = apply {
        shouldShowRationaleBlock = showRationale
    }

    /**
     * This method is called when a result is emitted on [Freedom.resultLiveData].
     *
     * It takes the data from [Freedom.resultLiveData] which is of type [PermissionState]. It determines which event needs to be called depending on the state passed to it.
     * @param state PermissionState is the state which will be used to determine which event to call.
     */
    internal fun callEventByState(state: PermissionState) {
        when (state) {
            is PermissionState.Granted -> grantedBlock?.invoke() ?: Log.e(
                this::class.java.simpleName,
                "granted block is not set."
            )
            is PermissionState.Denied -> deniedBlock?.invoke() ?: Log.e(
                this::class.java.simpleName,
                "Denied block is not set."
            )
            is PermissionState.PermanentlyDenied -> permanentlyDeniedBlock?.invoke() ?: Log.e(
                this::class.java.simpleName,
                "permanently denied block is not set."
            )
            is PermissionState.ShouldShowRationale -> shouldShowRationaleBlock?.invoke(state.listener) ?: Log.e(
                this::class.java.simpleName,
                "show rationale block is not set."
            )
        }
    }
}