package io.github.g00fy2.quickie.extensions

import android.content.Intent
import androidx.core.content.IntentCompat
import com.google.mlkit.vision.barcode.common.Barcode
import io.github.g00fy2.quickie.QRScannerActivity
import io.github.g00fy2.quickie.QRScannerActivity.Companion.EXTRA_RESULT_BYTES
import io.github.g00fy2.quickie.QRScannerActivity.Companion.EXTRA_RESULT_EXCEPTION
import io.github.g00fy2.quickie.QRScannerActivity.Companion.EXTRA_RESULT_PARCELABLE
import io.github.g00fy2.quickie.QRScannerActivity.Companion.EXTRA_RESULT_VALUE
import io.github.g00fy2.quickie.content.AddressParcelable
import io.github.g00fy2.quickie.content.CalendarDateTimeParcelable
import io.github.g00fy2.quickie.content.CalendarEventParcelable
import io.github.g00fy2.quickie.content.ContactInfoParcelable
import io.github.g00fy2.quickie.content.EmailParcelable
import io.github.g00fy2.quickie.content.GeoPointParcelable
import io.github.g00fy2.quickie.content.PersonNameParcelable
import io.github.g00fy2.quickie.content.PhoneParcelable
import io.github.g00fy2.quickie.content.QRContent
import io.github.g00fy2.quickie.content.QRContent.CalendarEvent
import io.github.g00fy2.quickie.content.QRContent.CalendarEvent.CalendarDateTime
import io.github.g00fy2.quickie.content.QRContent.ContactInfo
import io.github.g00fy2.quickie.content.QRContent.ContactInfo.Address
import io.github.g00fy2.quickie.content.QRContent.ContactInfo.Address.AddressType
import io.github.g00fy2.quickie.content.QRContent.ContactInfo.PersonName
import io.github.g00fy2.quickie.content.QRContent.Email
import io.github.g00fy2.quickie.content.QRContent.Email.EmailType
import io.github.g00fy2.quickie.content.QRContent.GeoPoint
import io.github.g00fy2.quickie.content.QRContent.Phone
import io.github.g00fy2.quickie.content.QRContent.Phone.PhoneType
import io.github.g00fy2.quickie.content.QRContent.Plain
import io.github.g00fy2.quickie.content.QRContent.Sms
import io.github.g00fy2.quickie.content.QRContent.Url
import io.github.g00fy2.quickie.content.QRContent.Wifi
import io.github.g00fy2.quickie.content.SmsParcelable
import io.github.g00fy2.quickie.content.UrlBookmarkParcelable
import io.github.g00fy2.quickie.content.WifiParcelable

internal fun Intent?.toQuickieContentType(): QRContent {
  val rawBytes = this?.getByteArrayExtra(EXTRA_RESULT_BYTES)
  val rawValue = this?.getStringExtra(EXTRA_RESULT_VALUE)
  return this?.toQuickieContentType(rawBytes, rawValue) ?: Plain(rawBytes, rawValue)
}

@Suppress("LongMethod")
private fun Intent.toQuickieContentType(rawBytes: ByteArray?, rawValue: String?): QRContent? {
  return when (extras?.getInt(QRScannerActivity.EXTRA_RESULT_TYPE, Barcode.TYPE_UNKNOWN)) {
    Barcode.TYPE_CONTACT_INFO -> {
      IntentCompat.getParcelableExtra(this, EXTRA_RESULT_PARCELABLE, ContactInfoParcelable::class.java)?.let {
        ContactInfo(
          rawBytes = rawBytes,
          rawValue = rawValue,
          addresses = it.addressParcelables.map { address -> address.toAddress() },
          emails = it.emailParcelables.map { mail -> mail.toEmail(rawBytes, rawValue) },
          name = it.nameParcelable.toPersonName(),
          organization = it.organization,
          phones = it.phoneParcelables.map { phone -> phone.toPhone(rawBytes, rawValue) },
          title = it.title,
          urls = it.urls
        )
      }
    }
    Barcode.TYPE_EMAIL -> {
      IntentCompat.getParcelableExtra(this, EXTRA_RESULT_PARCELABLE, EmailParcelable::class.java)?.let {
        Email(
          rawBytes = rawBytes,
          rawValue = rawValue,
          address = it.address,
          body = it.body,
          subject = it.subject,
          type = EmailType.entries.getOrElse(it.type) { EmailType.UNKNOWN }
        )
      }
    }
    Barcode.TYPE_PHONE -> {
      IntentCompat.getParcelableExtra(this, EXTRA_RESULT_PARCELABLE, PhoneParcelable::class.java)?.let {
        Phone(
          rawBytes = rawBytes,
          rawValue = rawValue,
          number = it.number,
          type = PhoneType.entries.getOrElse(it.type) { PhoneType.UNKNOWN }
        )
      }
    }
    Barcode.TYPE_SMS -> {
      IntentCompat.getParcelableExtra(this, EXTRA_RESULT_PARCELABLE, SmsParcelable::class.java)?.let {
        Sms(
          rawBytes = rawBytes,
          rawValue = rawValue,
          message = it.message,
          phoneNumber = it.phoneNumber
        )
      }
    }
    Barcode.TYPE_URL -> {
      IntentCompat.getParcelableExtra(this, EXTRA_RESULT_PARCELABLE, UrlBookmarkParcelable::class.java)?.let {
        Url(
          rawBytes = rawBytes,
          rawValue = rawValue,
          title = it.title,
          url = it.url
        )
      }
    }
    Barcode.TYPE_WIFI -> {
      IntentCompat.getParcelableExtra(this, EXTRA_RESULT_PARCELABLE, WifiParcelable::class.java)?.let {
        Wifi(
          rawBytes = rawBytes,
          rawValue = rawValue,
          encryptionType = it.encryptionType,
          password = it.password,
          ssid = it.ssid
        )
      }
    }
    Barcode.TYPE_GEO -> {
      IntentCompat.getParcelableExtra(this, EXTRA_RESULT_PARCELABLE, GeoPointParcelable::class.java)?.let {
        GeoPoint(
          rawBytes = rawBytes,
          rawValue = rawValue,
          lat = it.lat,
          lng = it.lng
        )
      }
    }
    Barcode.TYPE_CALENDAR_EVENT -> {
      IntentCompat.getParcelableExtra(this, EXTRA_RESULT_PARCELABLE, CalendarEventParcelable::class.java)?.let {
        CalendarEvent(
          rawBytes = rawBytes,
          rawValue = rawValue,
          description = it.description,
          end = it.end.toCalendarEvent(),
          location = it.location,
          organizer = it.organizer,
          start = it.start.toCalendarEvent(),
          status = it.status,
          summary = it.summary
        )
      }
    }
    else -> null
  }
}

internal fun Intent?.getRootException(): Exception {
  return this?.let { IntentCompat.getParcelableExtra(it, EXTRA_RESULT_EXCEPTION, Exception::class.java) }
    ?: IllegalStateException("Could retrieve root exception")
}

private fun PhoneParcelable.toPhone(rawBytes: ByteArray?, rawValue: String?) =
  Phone(
    rawBytes = rawBytes,
    rawValue = rawValue,
    number = number,
    type = PhoneType.entries.getOrElse(type) { PhoneType.UNKNOWN }
  )

private fun EmailParcelable.toEmail(rawBytes: ByteArray?, rawValue: String?) =
  Email(
    rawBytes = rawBytes,
    rawValue = rawValue,
    address = address,
    body = body,
    subject = subject,
    type = EmailType.entries.getOrElse(type) { EmailType.UNKNOWN }
  )

private fun AddressParcelable.toAddress() =
  Address(
    addressLines = addressLines,
    type = AddressType.entries.getOrElse(type) { AddressType.UNKNOWN }
  )

private fun PersonNameParcelable.toPersonName() =
  PersonName(
    first = first,
    formattedName = formattedName,
    last = last,
    middle = middle,
    prefix = prefix,
    pronunciation = pronunciation,
    suffix = suffix
  )

private fun CalendarDateTimeParcelable.toCalendarEvent() =
  CalendarDateTime(
    day = day,
    hours = hours,
    minutes = minutes,
    month = month,
    seconds = seconds,
    year = year,
    utc = utc
  )