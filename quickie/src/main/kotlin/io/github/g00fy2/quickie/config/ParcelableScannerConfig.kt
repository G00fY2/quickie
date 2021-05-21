package io.github.g00fy2.quickie.config

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal class ParcelableScannerConfig(val formats: IntArray, val text: Int, val icon: Int) : Parcelable