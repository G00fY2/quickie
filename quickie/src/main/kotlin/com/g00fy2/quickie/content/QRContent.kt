package com.g00fy2.quickie.content

@Suppress("unused")
sealed class QRContent(val rawValue: String) {

  override fun toString() = rawValue

  /**
   * Plain text or unknown content QR Code type.
   */
  data class Plain(private val _rawValue: String) : QRContent(_rawValue)

  /**
   * Wi-Fi access point details from a 'WIFI:' or similar QR Code type.
   */
  data class Wifi(private val _rawValue: String, val encryptionType: Int, val password: String, val ssid: String) :
    QRContent(_rawValue)

  /**
   * A URL or URL bookmark from a 'MEBKM:' or similar QR Code type.
   */
  data class Url(private val _rawValue: String, val title: String, val url: String) : QRContent(_rawValue)

  /**
   * An SMS message from an 'SMS:' or similar QR Code type.
   */
  data class Sms(private val _rawValue: String, val message: String, val phoneNumber: String) : QRContent(_rawValue)

  /**
   * GPS coordinates from a 'GEO:' or similar QR Code type.
   */
  data class GeoPoint(private val _rawValue: String, val lat: Double, val lng: Double) : QRContent(_rawValue)

  /**
   * An email message from a 'MAILTO:' or similar QR Code type.
   */
  data class Email(
    private val _rawValue: String,
    val address: String,
    val body: String,
    val subject: String,
    val type: EmailType
  ) :
    QRContent(_rawValue) {
    enum class EmailType {
      UNKNOWN, WORK, HOME
    }
  }

  /**
   * A phone number from a 'TEL:' or similar QR Code type.
   */
  data class Phone(private val _rawValue: String, val number: String, val type: PhoneType) : QRContent(_rawValue) {
    enum class PhoneType {
      UNKNOWN, WORK, HOME, FAX, MOBILE
    }
  }

  /**
   * A person's or organization's business card.
   */
  data class ContactInfo(
    private val _rawValue: String,
    val addresses: List<Address>,
    val emails: List<Email>,
    val name: PersonName,
    val organization: String,
    val phones: List<Phone>,
    val title: String,
    val urls: List<String>
  ) : QRContent(_rawValue) {

    data class Address(val addressLines: List<String>, val type: AddressType) {
      enum class AddressType {
        UNKNOWN, WORK, HOME
      }
    }

    data class PersonName(
      val first: String,
      val formattedName: String,
      val last: String,
      val middle: String,
      val prefix: String,
      val pronunciation: String,
      val suffix: String
    )
  }

  /**
   * A calendar event extracted from a QR Code.
   */
  data class CalendarEvent(
    private val _rawValue: String,
    val description: String,
    val end: CalendarDateTime,
    val location: String,
    val organizer: String,
    val start: CalendarDateTime,
    val status: String,
    val summary: String
  ) : QRContent(_rawValue) {

    data class CalendarDateTime(
      val day: Int,
      val hours: Int,
      val minutes: Int,
      val month: Int,
      val seconds: Int,
      val year: Int,
      val utc: Boolean
    )
  }
}
