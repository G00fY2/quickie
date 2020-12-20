package com.g00fy2.quickie.extensions

import android.content.Intent
import com.g00fy2.quickie.QRScannerActivity
import com.g00fy2.quickie.content.AddressParcelable
import com.g00fy2.quickie.content.CalendarDateTimeParcelable
import com.g00fy2.quickie.content.CalendarEventParcelable
import com.g00fy2.quickie.content.ContactInfoParcelable
import com.g00fy2.quickie.content.EmailParcelable
import com.g00fy2.quickie.content.GeoPointParcelable
import com.g00fy2.quickie.content.PersonNameParcelable
import com.g00fy2.quickie.content.PhoneParcelable
import com.g00fy2.quickie.content.QRContent
import com.g00fy2.quickie.content.QRContent.CalendarEvent
import com.g00fy2.quickie.content.QRContent.CalendarEvent.CalendarDateTime
import com.g00fy2.quickie.content.QRContent.ContactInfo
import com.g00fy2.quickie.content.QRContent.ContactInfo.Address
import com.g00fy2.quickie.content.QRContent.ContactInfo.Address.AddressType
import com.g00fy2.quickie.content.QRContent.ContactInfo.PersonName
import com.g00fy2.quickie.content.QRContent.Email
import com.g00fy2.quickie.content.QRContent.Email.EmailType
import com.g00fy2.quickie.content.QRContent.GeoPoint
import com.g00fy2.quickie.content.QRContent.Phone
import com.g00fy2.quickie.content.QRContent.Phone.PhoneType
import com.g00fy2.quickie.content.QRContent.Plain
import com.g00fy2.quickie.content.QRContent.Sms
import com.g00fy2.quickie.content.QRContent.Url
import com.g00fy2.quickie.content.QRContent.Wifi
import com.g00fy2.quickie.content.SmsParcelable
import com.g00fy2.quickie.content.UrlBookmarkParcelable
import com.g00fy2.quickie.content.WifiParcelable
import com.google.mlkit.vision.barcode.Barcode

internal fun Intent?.toQuickieContentType(): QRContent {
  if (this == null) return Plain("")

  val rawValue = getStringExtra(QRScannerActivity.EXTRA_RESULT_VALUE) ?: ""

  return when (getIntExtra(QRScannerActivity.EXTRA_RESULT_TYPE, Barcode.TYPE_UNKNOWN)) {
    Barcode.TYPE_CONTACT_INFO -> {
      getParcelableExtra<ContactInfoParcelable>(QRScannerActivity.EXTRA_RESULT_PARCELABLE)?.run {
        ContactInfo(
          _rawValue = rawValue,
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
      getParcelableExtra<EmailParcelable>(QRScannerActivity.EXTRA_RESULT_PARCELABLE)?.run {
        Email(
          _rawValue = rawValue,
          address = address,
          body = body,
          subject = subject,
          type = EmailType.values().getOrElse(type) { EmailType.UNKNOWN }
        )
      }
    }
    Barcode.TYPE_PHONE -> {
      getParcelableExtra<PhoneParcelable>(QRScannerActivity.EXTRA_RESULT_PARCELABLE)?.run {
        Phone(_rawValue = rawValue, number = number, type = PhoneType.values().getOrElse(type) { PhoneType.UNKNOWN })
      }
    }
    Barcode.TYPE_SMS -> {
      getParcelableExtra<SmsParcelable>(QRScannerActivity.EXTRA_RESULT_PARCELABLE)?.run {
        Sms(_rawValue = rawValue, message = message, phoneNumber = phoneNumber)
      }
    }
    Barcode.TYPE_URL -> {
      getParcelableExtra<UrlBookmarkParcelable>(QRScannerActivity.EXTRA_RESULT_PARCELABLE)?.run {
        Url(_rawValue = rawValue, title = title, url = url)
      }
    }
    Barcode.TYPE_WIFI -> {
      getParcelableExtra<WifiParcelable>(QRScannerActivity.EXTRA_RESULT_PARCELABLE)?.run {
        Wifi(_rawValue = rawValue, encryptionType = encryptionType, password = password, ssid = ssid)
      }
    }
    Barcode.TYPE_GEO -> {
      getParcelableExtra<GeoPointParcelable>(QRScannerActivity.EXTRA_RESULT_PARCELABLE)?.run {
        GeoPoint(_rawValue = rawValue, lat = lat, lng = lng)
      }
    }
    Barcode.TYPE_CALENDAR_EVENT -> {
      getParcelableExtra<CalendarEventParcelable>(QRScannerActivity.EXTRA_RESULT_PARCELABLE)?.run {
        CalendarEvent(
          _rawValue = rawValue,
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
  } ?: Plain(rawValue)
}

internal fun Intent?.getRootException(): Exception {
  return try {
    this?.getSerializableExtra(QRScannerActivity.EXTRA_RESULT_EXCEPTION) as Exception
  } catch (e: Exception) {
    IllegalStateException("Could retrieve root exception")
  }
}

private fun PhoneParcelable.toPhone(rawValue: String) =
  Phone(_rawValue = rawValue, number = number, type = PhoneType.values().getOrElse(type) { PhoneType.UNKNOWN })

private fun EmailParcelable.toEmail(rawValue: String) =
  Email(
    _rawValue = rawValue,
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
