package com.birjuvachhani.freedom

import android.util.Log

/*
 * Created by Birju Vachhani on 25 May 2019
 * Copyright © 2019 Freedom. All rights reserved.
 */

class FreedomResult {

    internal var grantedBlock: (() -> Unit)? = null
    internal var deniedBlock: (() -> Unit)? = null
    internal var permanentlyDeniedBlock: (() -> Unit)? = null
    internal var shouldShowRationaleBlock: ((listener: RationaleInterface) -> Unit)? = null

    infix fun whenGranted(granted: () -> Unit) = apply {
        grantedBlock = granted
    }

    infix fun whenDenied(denied: () -> Unit) = apply {
        deniedBlock = denied
    }

    infix fun whenPermanentlyDenied(permanentlyDenied: () -> Unit) = apply {
        permanentlyDeniedBlock = permanentlyDenied
    }

    infix fun whenShouldShowRationale(showRationale: (listener: RationaleInterface) -> Unit) = apply {
        shouldShowRationaleBlock = showRationale
    }

    internal fun callEventByState(state: PermissionState) {
        when (state) {
            is PermissionState.Granted -> grantedBlock?.invoke() ?: Log.e(
                this::class.java.simpleName,
                "granted block is empty"
            )
            is PermissionState.Denied -> deniedBlock?.invoke() ?: Log.e(
                this::class.java.simpleName,
                "Denied block is empty"
            )
            is PermissionState.PermanentlyDenied -> permanentlyDeniedBlock?.invoke() ?: Log.e(
                this::class.java.simpleName,
                "permanently denied block is empty"
            )
            is PermissionState.ShouldShowRationale -> shouldShowRationaleBlock?.invoke(state.listener) ?: Log.e(
                this::class.java.simpleName,
                "show rationale block is empty"
            )
        }
    }
}