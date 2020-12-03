<p align="center">
  <img width="345" height="120" src="https://raw.githubusercontent.com/G00fY2/Quickie/gh-pages/media/logo.png">
</p>

**quickie** is a Quick Response (QR) Code scanning library for Android that is based on CameraX and ML Kit on-device barcode detection. It's an alternative to ZXing and written in Kotlin. quickie features:
- Easy API for launching the QR scanner and receiving results by using the AndroidX Activity Result API
- Modern design, edge-to-edge scanning view with multilingual user hint
- Android Jetpack CameraX for communicating with the camera and showing the preview
- Firebase ML Kit on-device barcode recognition and decoding (no network connection required)

## Download [![Download](https://img.shields.io/maven-metadata/v?label=quickie-bundled&metadataUrl=https%3A%2F%2Fbintray.com%2Fg00fy2%2Fmaven%2Fdownload_file%3Ffile_path%3Dcom%252Fg00fy2%252Fquickie%252Fquickie-unbundled%252Fmaven-metadata.xml)](https://bintray.com/g00fy2/maven/quickie-bundled) [![Download](https://img.shields.io/maven-metadata/v?label=quickie-unbundled&metadataUrl=https%3A%2F%2Fbintray.com%2Fg00fy2%2Fmaven%2Fdownload_file%3Ffile_path%3Dcom%252Fg00fy2%252Fquickie%252Fquickie-bundled%252Fmaven-metadata.xml)](https://bintray.com/g00fy2/maven/quickie-unbundled)
There are two different flavors available on `jcenter()`:

| Bundled                             | Unbundled                                         |
| ----------------------------------- | ------------------------------------------------- |
| ML Kit model is bundled inside app (independed of Google Services) | ML Kit model will be automatically downloaded via Play Services (after app install) |
| additional 1.1 MB size per ABI (you should use AAB or ABI splitting) | smaller app size |
| V2 barcode model is used (possibly faster, more accurate) | currently V1 will be downloaded
```kotlin
// bundled:  
implementation("com.g00fy2.quickie:quickie-bundled:0.1.0")

// unbundled:
implementation("com.g00fy2.quickie:quickie-unbundled:0.1.0")
```

## Quick Start
To use the QR scanner simply register the `ScanQRCode()` ActivityResultContract together with a callback during `initialization` or in the `onCreate()` lifecycle of your Activity/Fragment and call `launch(null)` anywhere to start it:
```kotlin
private val scanQrCode = registerForActivityResult(ScanQRCode()) { handleResult(it) }

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    ...

    binding.buttonQrScanner.setOnClickListener { scanQrCode.launch(null) }
}
```
⚠️ **You can't register the ActivityResultContract inside of the OnClickListener. This will fail since the code get's executed after the onCreate lifecycle!**

Check out the [samples](https://github.com/G00fY2/quickie/tree/master/sample) inside this repo or visit the [Activity Result API documentation](https://developer.android.com/training/basics/intents/result) for more information.

#### Responses
The callback you add to the `registerForActivityResult` will receive an instance of the sealed `QRResult` class: 

1. `QRSuccess` when ML Kit successfully detected a QR code
   1. wraps a `QRContent` object
1. `QRUserCanceled` when the activity got canceled by the user
1. `QRMissingPermission` when the user didn't accept the camera permission
1. `QRError` when CameraX or ML kit threw an exception
   1. wraps the `exception`

#### Content
**quickie** wraps the content type of the QR code detected by ML Kit inside an sealed `QRContent` class. All of them provide the `rawValue`.

Currently supported types are:
`Plain`, `Wifi`, `Url`, `Sms`, `GeoPoint`, `Email`, `Phone`, `ContactInfo`, `CalendarEvent`

See the ML Kit [Barcode documentation](https://developers.google.com/android/reference/com/google/mlkit/vision/barcode/Barcode#nested-class-summary) for further details.

### Customization
The library is designed to behave and look as generic as possible. Currently it's not possible to change the UI, but there are plans to add customizations in future releases.

### Screenshots
![Image](https://raw.githubusercontent.com/G00fY2/Quickie/gh-pages/media/quickie-device-demo.png)

## Release state
**quickie** relies on Google Jetpack libraries which are in pre-release state. CameraX has no stable release version yet and the Activity Result API is part of the latest AndroidX Activity and Fragment beta releases. Here is what Google says about this release state:
* Beta releases are functionally stable and have a feature-complete API surface.
* They are ready for production use but may contain bugs.

You should consider **quickie** to be in beta state too. I will raise the version to 1.0 once all dependent libraries hit stable.

## Requirements
* AndroidX
* Min SDK 21+
* (Google Play Services available on the end device if using `quickie-unbundled`)

## License
     The MIT License (MIT)

    Copyright (C) 2020 Thomas Wirth

    Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
    associated documentation files (the "Software"), to deal in the Software without restriction,
    including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
    and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
    subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all copies or substantial
    portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
    LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
    NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
    DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT
    OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

