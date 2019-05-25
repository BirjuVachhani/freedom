package com.birjuvachhani.freedom

/*
 * Created by Birju Vachhani on 25 May 2019
 * Copyright © 2019 FreedomDemo. All rights reserved.
 */

internal sealed class PermissionState {
    object Granted : PermissionState()
    data class ShouldShowRationale(val listener: RationaleInterface) : PermissionState()
    object PermanentlyDenied : PermissionState()
    object Denied : PermissionState()
}