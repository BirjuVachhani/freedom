package com.birjuvachhani.freedom

/*
 * Created by Birju Vachhani on 25 May 2019
 * Copyright © 2019 FreedomDemo. All rights reserved.
 */

/**
 * Represents state of the requested permission.
 *
 * The state is used to determine which event to fire.
 */
internal sealed class PermissionState {

    /**
     * Indicates that the requested permission is granted successfully.
     */
    object Granted : PermissionState()

    /**
     * Represents that the requested permission should show rationale because it has been denied previously so it is important to let the user know why this permission is needed.
     * @property listener RationaleInterface provides a way to restart permission request from rationale dialog.
     */
    data class ShouldShowRationale(val listener: RationaleInterface) : PermissionState()

    /**
     * Indicates that the requested permission is permanently denied by the user.
     */
    object PermanentlyDenied : PermissionState()

    /**
     * Indicates that the requested permission is denied by the user.
     */
    object Denied : PermissionState()
}