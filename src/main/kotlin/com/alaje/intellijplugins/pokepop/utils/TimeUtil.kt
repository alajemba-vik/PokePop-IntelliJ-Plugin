package com.alaje.intellijplugins.pokepop.utils

import com.alaje.intellijplugins.pokepop.utils.StringUtil.substringOrNull
import com.alaje.intellijplugins.pokepop.utils.ValidationUtil.isValidTimeFormat
import java.util.*
import java.util.concurrent.TimeUnit

object TimeUtil {
    /**
     * @return current time only (no date part) in milliseconds
     * */
    val onlyCurrentTimeInMillis: Long get() {
        val currentDate = Date()
        val calendar = Calendar.getInstance()
        calendar.time = currentDate

        return calendar.let {
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY).toLong()
            val currentMinute = calendar.get(Calendar.MINUTE).toLong()
            val currentSecond = calendar.get(Calendar.SECOND).toLong()

            TimeUnit.HOURS.toMillis(currentHour) +
                    TimeUnit.MINUTES.toMillis(currentMinute) +
                    TimeUnit.SECONDS.toMillis(currentSecond)
        }
    }

    /**
     * String must be in HH:mm format
     * @return time in milliseconds
     * */
    fun String.toMilliseconds(): Long? {
        return if (isValidTimeFormat) {
            val hours = substringOrNull(0, 2)?.toLongOrNull()
            val minutes = substringOrNull(3, 5)?.toLongOrNull() ?: 0
            if (hours != null) {
                TimeUnit.HOURS.toMillis(hours) + TimeUnit.MINUTES.toMillis(minutes)
            } else {
                null
            }
        } else {
            null
        }
    }

    /**
     * @return time in HH:mm format
     * */
    fun Long.toTimeString(): String {
        val hours = TimeUnit.MILLISECONDS.toHours(this)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(this) - TimeUnit.HOURS.toMinutes(hours)

        return String.format("%02d:%02d", hours, minutes)
    }
}
