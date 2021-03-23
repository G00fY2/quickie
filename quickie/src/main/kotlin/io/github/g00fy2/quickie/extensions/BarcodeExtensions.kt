package io.github.g00fy2.quickie.extensions

import android.os.Parcelable
import com.google.mlkit.vision.barcode.Barcode
import io.github.g00fy2.quickie.content.AddressParcelable
import io.github.g00fy2.quickie.content.CalendarDateTimeParcelable
import io.github.g00fy2.quickie.content.CalendarEventParcelable
import io.github.g00fy2.quickie.content.ContactInfoParcelable
import io.github.g00fy2.quickie.content.EmailParcelable
import io.github.g00fy2.quickie.content.GeoPointParcelable
import io.github.g00fy2.quickie.content.PersonNameParcelable
import io.github.g00fy2.quickie.content.PhoneParcelable
import io.github.g00fy2.quickie.content.SmsParcelable
import io.github.g00fy2.quickie.content.UrlBookmarkParcelable
import io.github.g00fy2.quickie.content.WifiParcelable

internal fun Barcode.toParcelableContentType(): Parcelable? {
  return when (valueType) {
    Barcode.TYPE_CONTACT_INFO -> {
      ContactInfoParcelable(
        addressParcelables = contactInfo?.addresses?.map { it.toParcelableAddress() } ?: emptyList(),
        emailParcelables = contactInfo?.emails?.map { it.toParcelableEmail() } ?: emptyList(),
        nameParcelable = contactInfo?.name.toParcelablePersonName(),
        organization = contactInfo?.organization ?: "",
        phoneParcelables = contactInfo?.phones?.map { it.toParcelablePhone() } ?: emptyList(),
        title = contactInfo?.title ?: "",
        urls = contactInfo?.urls?.mapNotNull { it } ?: emptyList()
      )
    }
    Barcode.TYPE_EMAIL -> {
      EmailParcelable(
        address = email?.address ?: "",
        body = email?.body ?: "",
        subject = email?.subject ?: "",
        type = email?.type ?: 0
      )
    }
    Barcode.TYPE_PHONE -> PhoneParcelable(number = phone?.number ?: "", type = phone?.type ?: 0)
    Barcode.TYPE_SMS -> SmsParcelable(message = sms?.message ?: "", phoneNumber = sms?.phoneNumber ?: "")
    Barcode.TYPE_URL -> UrlBookmarkParcelable(title = url?.title ?: "", url = url?.url ?: "")
    Barcode.TYPE_WIFI -> {
      WifiParcelable(
        encryptionType = wifi?.encryptionType ?: 0,
        password = wifi?.password ?: "",
        ssid = wifi?.ssid ?: ""
      )
    }
    Barcode.TYPE_GEO -> GeoPointParcelable(lat = geoPoint?.lat ?: 0.0, lng = geoPoint?.lng ?: 0.0)
    Barcode.TYPE_CALENDAR_EVENT -> {
      CalendarEventParcelable(
        description = calendarEvent?.description ?: "",
        end = calendarEvent?.end.toParcelableCalendarEvent(),
        location = calendarEvent?.location ?: "",
        organizer = calendarEvent?.organizer ?: "",
        start = calendarEvent?.start.toParcelableCalendarEvent(),
        status = calendarEvent?.status ?: "",
        summary = calendarEvent?.summary ?: ""
      )
    }
    else -> null // TYPE_TEXT, TYPE_ISBN, TYPE_PRODUCT, TYPE_DRIVER_LICENSE, TYPE_UNKNOWN
  }
}

private fun Barcode.Address?.toParcelableAddress() =
  AddressParcelable(
    addressLines = this?.addressLines?.toList()?.mapNotNull { it } ?: emptyList(),
    type = this?.type ?: 0
  )

private fun Barcode.Phone?.toParcelablePhone() = PhoneParcelable(number = this?.number ?: "", type = this?.type ?: 0)

private fun Barcode.PersonName?.toParcelablePersonName() =
  PersonNameParcelable(
    first = this?.first ?: "",
    formattedName = this?.formattedName ?: "",
    last = this?.last ?: "",
    middle = this?.middle ?: "",
    prefix = this?.prefix ?: "",
    pronunciation = this?.pronunciation ?: "",
    suffix = this?.suffix ?: ""
  )

private fun Barcode.Email?.toParcelableEmail() =
  EmailParcelable(
    address = this?.address ?: "",
    body = this?.body ?: "",
    subject = this?.subject ?: "",
    type = this?.type ?: 0
  )

private fun Barcode.CalendarDateTime?.toParcelableCalendarEvent() =
  CalendarDateTimeParcelable(
    day = this?.day ?: -1,
    hours = this?.hours ?: -1,
    minutes = this?.minutes ?: -1,
    month = this?.month ?: -1,
    seconds = this?.seconds ?: -1,
    year = this?.year ?: -1,
    utc = this?.isUtc ?: false
  )