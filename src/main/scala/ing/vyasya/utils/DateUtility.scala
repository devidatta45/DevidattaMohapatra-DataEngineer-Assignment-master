package ing.vyasya.utils

import java.time.{Instant, LocalDateTime, ZoneOffset}

// Simple date utility class
object DateUtility {

  implicit class LocalDateTimeHelper(dateTime: LocalDateTime) {
    def toUTCInstant: Instant = dateTime.toInstant(ZoneOffset.UTC)
    def toEpochMilli: Long    = toUTCInstant.toEpochMilli
  }
}
