# Freedom

[![License](https://img.shields.io/badge/License-Apache%202.0-2196F3.svg?style=for-the-badge)](https://opensource.org/licenses/Apache-2.0)
[![language](https://img.shields.io/github/languages/top/BirjuVachhani/location-extension-android.svg?style=for-the-badge&colorB=f18e33)](https://kotlinlang.org/)
[![Platform](https://img.shields.io/badge/Platform-Android-green.svg?style=for-the-badge)](https://www.android.com/)
[![API](https://img.shields.io/badge/API-16%2B-F44336.svg?style=for-the-badge)](https://android-arsenal.com/api?level=16)

Freedom is an Android library to handle runtime permissions in easiest way. Freedom is fully written in Kotlin so it uses Kotlin features and dsl syntax extensively.

Please note that this library is still under development. Stable version of the library will be released soon. Stay tuned!

## Gradle Dependency

* Add the JitPack repository to your project's build.gradle file

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

* Add the dependency in your app's build.gradle file

```
dependencies {
    implementation 'com.github.BirjuVachhani:freedom:0.9.0'
}
```

## Usage

#### Set Permissions Event Listener

```kotlin
Freedom.setListener(this) whenGranted {
    // permission granted
    showToast("Granted")
} whenDenied {
    // permission denied
    showToast("Denied")
} whenPermanentlyDenied {
    // permission permanently denied, show open settings dialog
    showPermissionBlockedDialog()
} whenShouldShowRationale {listener->
    // show rationale dialog
    showRationaleDialog(listener)
}
```

#### Requesting Permission

```kotlin
Freedom.request(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
```

#### Handling Rationale Dialog

```kotlin
private fun showRationaleDialog(listener: RationaleInterface) {
    AlertDialog.Builder(this)
        .setTitle(getString(R.string.rationale_title))
        .setMessage(getString(R.string.rationale_msg)
        .setPositiveButton("Grant") { dialog, _ ->
            listener.request()
            dialog.dismiss()
        }
        .setNegativeButton("CANCEL") { dialog, _ ->
            dialog.dismiss()
        }.show()
}
```

### Example

```kotlin
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
        } whenShouldShowRationale {listener->
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
```

### Pull Request
To generate a pull request, please consider following [Pull Request Template](https://github.com/BirjuVachhani/freedom/blob/master/PULL_REQUEST_TEMPLATE.md).

### Issues
To submit an issue, please check the [Issue Template](https://github.com/BirjuVachhani/freedom/blob/master/ISSUE_TEMPLATE.md).

Code of Conduct
---
[Code of Conduct](https://github.com/BirjuVachhani/freedom/blob/master/CODE_OF_CONDUCT.md)

## Contribution

You are most welcome to contribute to this project!

Please have a look at [Contributing Guidelines](https://github.com/BirjuVachhani/freedom/blob/master/CONTRIBUTING.md), before contributing and proposing a change.

# License

```
   Copyright © 2019 BirjuVachhani

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
