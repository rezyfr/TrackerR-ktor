package trackerr.rezyfr.dev.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

const val DATE_SERVER_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS"
fun getStartOfMonth(month: Int): Date = Calendar.getInstance().also {
    it.set(Calendar.MONTH, month)
    it.set(Calendar.DAY_OF_MONTH, 1)
}.time

fun getEndOfMonth(month: Int): Date = Calendar.getInstance().also {
    it.set(Calendar.MONTH, month)
    it.set(Calendar.DATE, 1)
    it.add(Calendar.MONTH, 1)
    it.add(Calendar.MILLISECOND, -1)
}.time

fun String.formatToLocalDateTime() : LocalDateTime {
    return try {
        val dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_SERVER_FORMAT)
        LocalDateTime.parse(this, dateTimeFormatter)
    } catch (e: Exception) {
        LocalDateTime.now()
    }
}
