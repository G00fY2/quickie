package io.github.g00fy2.quickie.extensions

import android.content.Intent
import android.os.Bundle
import androidx.core.os.BundleCompat
import com.google.mlkit.vision.barcode.common.Barcode
import io.github.g00fy2.quickie.QRScannerActivity
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
  val rawValue = this?.getStringExtra(QRScannerActivity.EXTRA_RESULT_VALUE).orEmpty()
  return this?.extras?.toQuickieContentType(rawValue) ?: Plain(rawValue)
}

@Suppress("LongMethod")
private fun Bundle.toQuickieContentType(rawValue: String): QRContent? {
  return when (getInt(QRScannerActivity.EXTRA_RESULT_TYPE, Barcode.TYPE_UNKNOWN)) {
    Barcode.TYPE_CONTACT_INFO -> {
      BundleCompat.getParcelable(this, QRScannerActivity.EXTRA_RESULT_PARCELABLE, ContactInfoParcelable::class.java)
        ?.run {
          ContactInfo(
            rawValue = rawValue,
            addresses = addressParcelables.map { it.toAddress() },
            emails = emailParcelables.map { it.toEmail(rawValue) },
            name = nameParcelable.toPersonName(),
            organization = organization,
            phones = phoneParcelables.map { it.toPhone(rawValue) },
            title = title,
            urls = urls
          )
        }
    }
    Barcode.TYPE_EMAIL -> {
      BundleCompat.getParcelable(this, QRScannerActivity.EXTRA_RESULT_PARCELABLE, EmailParcelable::class.java)?.run {
        Email(
          rawValue = rawValue,
          address = address,
          body = body,
          subject = subject,
          type = EmailType.values().getOrElse(type) { EmailType.UNKNOWN }
        )
      }
    }
    Barcode.TYPE_PHONE -> {
      BundleCompat.getParcelable(this, QRScannerActivity.EXTRA_RESULT_PARCELABLE, PhoneParcelable::class.java)?.run {
        Phone(rawValue = rawValue, number = number, type = PhoneType.values().getOrElse(type) { PhoneType.UNKNOWN })
      }
    }
    Barcode.TYPE_SMS -> {
      BundleCompat.getParcelable(this, QRScannerActivity.EXTRA_RESULT_PARCELABLE, SmsParcelable::class.java)?.run {
        Sms(rawValue = rawValue, message = message, phoneNumber = phoneNumber)
      }
    }
    Barcode.TYPE_URL -> {
      BundleCompat.getParcelable(this, QRScannerActivity.EXTRA_RESULT_PARCELABLE, UrlBookmarkParcelable::class.java)
        ?.run {
          Url(rawValue = rawValue, title = title, url = url)
        }
    }
    Barcode.TYPE_WIFI -> {
      BundleCompat.getParcelable(this, QRScannerActivity.EXTRA_RESULT_PARCELABLE, WifiParcelable::class.java)?.run {
        Wifi(rawValue = rawValue, encryptionType = encryptionType, password = password, ssid = ssid)
      }
    }
    Barcode.TYPE_GEO -> {
      BundleCompat.getParcelable(this, QRScannerActivity.EXTRA_RESULT_PARCELABLE, GeoPointParcelable::class.java)?.run {
        GeoPoint(rawValue = rawValue, lat = lat, lng = lng)
      }
    }
    Barcode.TYPE_CALENDAR_EVENT -> {
      BundleCompat.getParcelable(this, QRScannerActivity.EXTRA_RESULT_PARCELABLE, CalendarEventParcelable::class.java)
        ?.run {
          CalendarEvent(
            rawValue = rawValue,
            description = description,
            end = end.toCalendarEvent(),
            location = location,
            organizer = organizer,
            start = start.toCalendarEvent(),
            status = status,
            summary = summary
          )
        }
    }
    else -> null
  }
}

internal fun Intent?.getRootException(): Exception {
  return this?.extras?.let {
    BundleCompat.getParcelable(it, QRScannerActivity.EXTRA_RESULT_EXCEPTION, Exception::class.java)
  } ?: IllegalStateException("Could retrieve root exception")
}

private fun PhoneParcelable.toPhone(rawValue: String) =
  Phone(rawValue = rawValue, number = number, type = PhoneType.values().getOrElse(type) { PhoneType.UNKNOWN })

private fun EmailParcelable.toEmail(rawValue: String) =
  Email(
    rawValue = rawValue,
    address = address,
    body = body,
    subject = subject,
    type = EmailType.values().getOrElse(type) { EmailType.UNKNOWN }
  )

private fun AddressParcelable.toAddress() =
  Address(addressLines = addressLines, type = AddressType.values().getOrElse(type) { AddressType.UNKNOWN })

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