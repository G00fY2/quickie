package com.g00fy2.quickie.content

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
object EmptyParcelable : Parcelable

@Parcelize
data class WifiParcelable(val encryptionType: Int, val password: String, val ssid: String) : Parcelable

@Parcelize
data class UrlBookmarkParcelable(val title: String, val url: String) : Parcelable

@Parcelize
data class SmsParcelable(val message: String, val phoneNumber: String) : Parcelable

@Parcelize
data class GeoPointParcelable(val lat: Double, val lng: Double) : Parcelable

@Parcelize
data class ContactInfoParcelable(
  val addressParcelables: List<AddressParcelable>,
  val emailParcelables: List<EmailParcelable>,
  val nameParcelable: PersonNameParcelable,
  val organization: String,
  val phoneParcelables: List<PhoneParcelable>,
  val title: String,
  val urls: List<String>
) : Parcelable

@Parcelize
data class EmailParcelable(val address: String, val body: String, val subject: String, val type: Int) : Parcelable

@Parcelize
data class PhoneParcelable(val number: String, val type: Int) : Parcelable

@Parcelize
data class PersonNameParcelable(
  val first: String,
  val formattedName: String,
  val last: String,
  val middle: String,
  val prefix: String,
  val pronunciation: String,
  val suffix: String
) : Parcelable

@Parcelize
data class CalendarEventParcelable(
  val description: String,
  val end: CalendarDateTimeParcelable,
  val location: String,
  val organizer: String,
  val start: CalendarDateTimeParcelable,
  val status: String,
  val summary: String
) : Parcelable

@Parcelize
data class CalendarDateTimeParcelable(
  val day: Int,
  val hours: Int,
  val minutes: Int,
  val month: Int,
  val seconds: Int,
  val year: Int,
  val utc: Boolean
) : Parcelable

@Parcelize
data class AddressParcelable(val addressLines: List<String>, val type: Int) : Parcelable