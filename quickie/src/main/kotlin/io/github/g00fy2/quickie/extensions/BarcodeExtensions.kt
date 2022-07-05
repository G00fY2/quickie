package io.github.g00fy2.quickie.extensions

import android.os.Parcelable
import com.google.mlkit.vision.barcode.common.Barcode
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
        addressParcelables = contactInfo?.addresses?.map { it.toParcelableAddress() }.orEmpty(),
        emailParcelables = contactInfo?.emails?.map { it.toParcelableEmail() }.orEmpty(),
        nameParcelable = contactInfo?.name.toParcelablePersonName(),
        organization = contactInfo?.organization.orEmpty(),
        phoneParcelables = contactInfo?.phones?.map { it.toParcelablePhone() }.orEmpty(),
        title = contactInfo?.title.orEmpty(),
        urls = contactInfo?.urls?.mapNotNull { it }.orEmpty()
      )
    }
    Barcode.TYPE_EMAIL -> {
      EmailParcelable(
        address = email?.address.orEmpty(),
        body = email?.body.orEmpty(),
        subject = email?.subject.orEmpty(),
        type = email?.type ?: 0
      )
    }
    Barcode.TYPE_PHONE -> PhoneParcelable(number = phone?.number.orEmpty(), type = phone?.type ?: 0)
    Barcode.TYPE_SMS -> SmsParcelable(message = sms?.message.orEmpty(), phoneNumber = sms?.phoneNumber.orEmpty())
    Barcode.TYPE_URL -> UrlBookmarkParcelable(title = url?.title.orEmpty(), url = url?.url.orEmpty())
    Barcode.TYPE_WIFI -> {
      WifiParcelable(
        encryptionType = wifi?.encryptionType ?: 0,
        password = wifi?.password.orEmpty(),
        ssid = wifi?.ssid.orEmpty()
      )
    }
    Barcode.TYPE_GEO -> GeoPointParcelable(lat = geoPoint?.lat ?: 0.0, lng = geoPoint?.lng ?: 0.0)
    Barcode.TYPE_CALENDAR_EVENT -> {
      CalendarEventParcelable(
        description = calendarEvent?.description.orEmpty(),
        end = calendarEvent?.end.toParcelableCalendarEvent(),
        location = calendarEvent?.location.orEmpty(),
        organizer = calendarEvent?.organizer.orEmpty(),
        start = calendarEvent?.start.toParcelableCalendarEvent(),
        status = calendarEvent?.status.orEmpty(),
        summary = calendarEvent?.summary.orEmpty()
      )
    }
    else -> null // TYPE_TEXT, TYPE_ISBN, TYPE_PRODUCT, TYPE_DRIVER_LICENSE, TYPE_UNKNOWN
  }
}

private fun Barcode.Address?.toParcelableAddress() =
  AddressParcelable(
    addressLines = this?.addressLines?.toList()?.mapNotNull { it }.orEmpty(),
    type = this?.type ?: 0
  )

private fun Barcode.Phone?.toParcelablePhone() =
  PhoneParcelable(number = this?.number.orEmpty(), type = this?.type ?: 0)

private fun Barcode.PersonName?.toParcelablePersonName() =
  PersonNameParcelable(
    first = this?.first.orEmpty(),
    formattedName = this?.formattedName.orEmpty(),
    last = this?.last.orEmpty(),
    middle = this?.middle.orEmpty(),
    prefix = this?.prefix.orEmpty(),
    pronunciation = this?.pronunciation.orEmpty(),
    suffix = this?.suffix.orEmpty()
  )

private fun Barcode.Email?.toParcelableEmail() =
  EmailParcelable(
    address = this?.address.orEmpty(),
    body = this?.body.orEmpty(),
    subject = this?.subject.orEmpty(),
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