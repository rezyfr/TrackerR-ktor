package trackerr.rezyfr.dev.util

import java.util.*

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
