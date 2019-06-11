package com.birjuvachhani.freedomdemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.birjuvachhani.freedom.Freedom
import com.birjuvachhani.freedom.RationaleInterface

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Freedom.setListener(this) whenGranted {
            // permission granted
            showToast("Granted")
        } whenDenied {
            // permission denied
            showToast("Denied")
        } whenPermanentlyDenied {
            // permission permanently denied, show open settings dialog
            showPermissionBlockedDialog()
        } whenShouldShowRationale { listener ->
            // show rationale dialog
            showRationaleDialog(listener)
        }
    }

    private fun showPermissionBlockedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission Blocked")
            .setMessage("This feature requires location permission to function. Please grant location permission for settings.")
            .setPositiveButton("OPEN SETTINGS") { dialog, _ ->
                openSettings()
                dialog.dismiss()
            }
            .setNegativeButton("CANCEL") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun showRationaleDialog(listener: RationaleInterface) {
        AlertDialog.Builder(this)
            .setTitle("Permission Required")
            .setMessage("This feature requires location permission to function. Please grant location permission.")
            .setPositiveButton("Grant") { dialog, _ ->
                listener.request()
                dialog.dismiss()
            }
            .setNegativeButton("CANCEL") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    /**
     * Opens app settings screen
     * */
    private fun openSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    /**
     * method is invoked on button click which initiates permission request.
     */
    fun requestPermission(view: View) {
        Freedom.request(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

