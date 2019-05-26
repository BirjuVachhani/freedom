package com.birjuvachhani.freedom

/*
 * Created by Birju Vachhani on 25 May 2019
 * Copyright © 2019 Freedom. All rights reserved.
 */

/**
 * Model class for holding information related to one permission.
 * @property permission String defines the actual permission string
 * @property state PermissionState represents the current state of the permission
 */
internal data class Permission(
    val permission: String,
    var state: PermissionState
) {
    override fun equals(other: Any?): Boolean = when (other) {
        null -> false
        !is Permission -> false
        else -> permission == other.permission
    }

    override fun hashCode(): Int {
        var result = permission.hashCode()
        result = 31 * result + state.hashCode()
        return result
    }
}