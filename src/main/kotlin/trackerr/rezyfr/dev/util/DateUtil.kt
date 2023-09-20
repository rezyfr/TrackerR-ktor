@file:OptIn(InternalAPI::class)

package trackerr.rezyfr.dev.util

import io.ktor.server.util.*
import io.ktor.util.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

const val DATE_SERVER_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS"
fun getStartOfMonth(month: Int? = null): LocalDateTime = Calendar.getInstance().also {
    month?.let { month ->
        it.set(Calendar.MONTH, month)
    }
    it.set(Calendar.DAY_OF_MONTH, 1)
}.time.toLocalDateTime()

fun getEndOfMonth(month: Int? = null): LocalDateTime = Calendar.getInstance().also {
    month?.let { month ->
        it.set(Calendar.MONTH, month)
    }
    it.set(Calendar.DAY_OF_MONTH, it.getActualMaximum(Calendar.DAY_OF_MONTH))
    it.add(Calendar.MILLISECOND, -1)
}.time.toLocalDateTime()

@OptIn(InternalAPI::class)
fun getStartOfWeek(): LocalDateTime = Calendar.getInstance().also {
    it.set(Calendar.DAY_OF_WEEK, it.firstDayOfWeek)
}.time.toLocalDateTime()

fun getEndOfWeek(): LocalDateTime = Calendar.getInstance().also {
    it.set(Calendar.DAY_OF_WEEK, it.firstDayOfWeek + 6)
    it.add(Calendar.MILLISECOND, -1)
}.time.toLocalDateTime()

fun getStartOfYear() : LocalDateTime = getStartOfMonth(1)
@OptIn(InternalAPI::class)
fun getEndOfYear() : LocalDateTime = getEndOfMonth(12)

fun String.formatToLocalDateTime() : LocalDateTime {
    return try {
        val dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_SERVER_FORMAT)
        LocalDateTime.parse(this, dateTimeFormatter)
    } catch (e: Exception) {
        LocalDateTime.now()
    }
}
