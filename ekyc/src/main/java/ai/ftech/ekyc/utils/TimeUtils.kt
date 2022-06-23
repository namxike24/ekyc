package ai.ftech.ekyc.utils

import android.text.format.DateUtils
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

object TimeUtils {
    const val ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
    const val ISO_SHORT_DATE_TIME_FORMAT = "yyyy/MM/dd, HH:mm"
    const val ISO_SHORT_DATE_FOMAT = "dd/MM/yyyy"
    const val MONTH_YEAR_FORMAT = "MM/YY"
    private const val ISO_DATE_ONLY_FORMAT = "yyyy-MM-dd"
    private const val DASHBOARD_TIME_ZONE = "GMT+7"
    private const val GMT_TIME_ZONE = "GMT"
    private const val UTC_TIME_ZONE = "UTC"
    private const val ISO_8601SSSZ_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    private const val ISO_MONTH_FOMAT = "MM/dd/yyyy"
    private const val TIME = "HH:mm"
    private const val STRING_DAY_MONTH = "d MMMM"
    private const val STRING_DAY_MONTH_YEAR = "d MMMM yyyy"
    private const val STRING_MINUTES_SECOND = "mm:ss"
    private const val STRING_DATE_AND_TIME = "dd/MM/yyyy, HH:mm"

    fun millisToDate(millis: Long, format: String = ISO_DATE_ONLY_FORMAT): String {
        val format = SimpleDateFormat(format, Locale.getDefault())
        return format.format(millis)
    }

    fun dateToISODateString(date: Date): String {
        val format = SimpleDateFormat(ISO_DATE_ONLY_FORMAT, Locale.getDefault())
        return format.format(date)
    }

    fun changeToGMTZoneISODateTime(date: Date): String {
        val format = SimpleDateFormat(ISO_DATE_TIME_FORMAT)
        format.timeZone = TimeZone.getTimeZone(GMT_TIME_ZONE)
        return format.format(date)
    }

    fun dateToISODateTimeString(date: Date): String {
        val format = SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.getDefault())
        return format.format(date)
    }

    fun dateStringToMillis(dateStr: String): Long {
        val formatter = SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.getDefault())
        val date = formatter.parse(dateStr)
        return date.time
    }

    fun dateStringToDate(dateStr: String): Date {
        val formatter = SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.getDefault())
        val date = formatter.parse(dateStr)
        return date
    }

    fun dateStringUTCToDate(dateStr: String): Date {
        val formatter = SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone(UTC_TIME_ZONE)
        return formatter.parse(dateStr)
    }

    fun dateToDateString(date: Date, format: String): String {
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        return formatter.format(date)
    }

    fun dateToDateString(calendar: Calendar, format: String): String {
        return dateToDateString(calendar.time, format)
    }

    fun changeDateStringFormat(dateStr: String, fromFormat: String, toFormat: String): String {
        val formatter = SimpleDateFormat(fromFormat, Locale.getDefault())
        val date = formatter.parse(dateStr)
        val toFormatter = SimpleDateFormat(toFormat, Locale.getDefault())
        return toFormatter.format(date)
    }

    fun getToday(): Calendar {
        return Calendar.getInstance(TimeZone.getTimeZone(DASHBOARD_TIME_ZONE))
    }

    fun getMidNight(): Calendar {
        val cal = Calendar.getInstance(TimeZone.getTimeZone(DASHBOARD_TIME_ZONE))
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal
    }

    fun getStartOfDay(calendar: Calendar = Calendar.getInstance()): Calendar {
        val cal = calendar.clone() as Calendar
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal
    }

    fun getEndOfDay(calendar: Calendar = Calendar.getInstance()): Calendar {
        val cal = calendar.clone() as Calendar
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        return cal
    }

    fun getCalendarFromIsoString(isoDateString: String?, timeZone: TimeZone? = null): Calendar? {
        return if (isoDateString.isNullOrBlank()) null else {
            try {
                val dateFormat = SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.getDefault())
                if (timeZone != null) {
                    dateFormat.timeZone = timeZone
                }
                val date = dateFormat.parse(isoDateString) ?: return null
                Calendar.getInstance().apply { time = date }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    fun getCalendarFromString(dateString: String?, timeFormat: String): Calendar? {
        return if (dateString.isNullOrBlank()) null else {
            try {
                val dateFormat = SimpleDateFormat(timeFormat, Locale.getDefault())
                val date = dateFormat.parse(dateString)
                Calendar.getInstance().apply { time = date }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    fun before(calendar: Calendar, offset: Int): Calendar {
        val before = calendar.clone() as Calendar
        before.add(Calendar.DATE, offset * (-1))
        return before
    }

    fun after(startDate: Calendar, offset: Int): Calendar {
        val after = startDate.clone() as Calendar
        after.add(Calendar.DATE, offset)
        return after
    }

    fun daysBetween(startDate: Calendar, endDate: Calendar): Long {
        val newStart = Calendar.getInstance()
        newStart.timeInMillis = startDate.timeInMillis
        newStart.set(Calendar.HOUR_OF_DAY, 0)
        newStart.set(Calendar.MINUTE, 0)
        newStart.set(Calendar.SECOND, 0)
        newStart.set(Calendar.MILLISECOND, 0)

        val newEnd = Calendar.getInstance()
        newEnd.timeInMillis = endDate.timeInMillis
        newEnd.set(Calendar.HOUR_OF_DAY, 0)
        newEnd.set(Calendar.MINUTE, 0)
        newEnd.set(Calendar.SECOND, 0)
        newEnd.set(Calendar.MILLISECOND, 0)

        return TimeUnit.MILLISECONDS.toDays(abs(newEnd.timeInMillis - newStart.timeInMillis))
    }

    fun daysBetweenIncludePeak(startDate: Calendar, endDate: Calendar): Long {
        return daysBetween(startDate, endDate) + 1
    }

    fun yearsBetween(startDate: Calendar, endDate: Calendar): Int{
        var diff: Int = endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR)
        if (startDate.get(Calendar.MONTH) > endDate.get(Calendar.MONTH) ||
            startDate.get(Calendar.MONTH) == endDate.get(Calendar.MONTH) && startDate.get(Calendar.DATE) > endDate.get(Calendar.DATE)
        ) {
            diff--
        }
        return diff
    }

    fun formatIsoToVNFormat(date: Date?): String? {
        val format = SimpleDateFormat("d 'thg' M", Locale.getDefault())
        return format.format(date)
    }

    fun changeDateToISOFormat(dateStr: String, format: String): String {
        val orginFormat = SimpleDateFormat(format, Locale.getDefault())
        val date = orginFormat.parse(dateStr)
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        val isoDateFormat = SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.getDefault())
        return isoDateFormat.format(calendar.time)
    }
    fun changeDateToISO(dateStr: String): String {
        val orginFormat = SimpleDateFormat(ISO_DATE_ONLY_FORMAT, Locale.getDefault())
        val date = orginFormat.parse(dateStr)
        val calendar = Calendar.getInstance()
        calendar.time = date
        val isoDateFormat = SimpleDateFormat(ISO_SHORT_DATE_FOMAT, Locale.getDefault())
        return isoDateFormat.format(calendar.time)
    }
    fun changeDateToISOShotFormat(dateStr: String): String {
        val orginFormat = SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.getDefault())
        val date = orginFormat.parse(dateStr)
        val calendar = Calendar.getInstance()
        calendar.time = date
        val isoDateFormat = SimpleDateFormat(ISO_SHORT_DATE_FOMAT, Locale.getDefault())
        return isoDateFormat.format(calendar.time)
    }
    fun changeDateToISOFormat(dateStr: String): String {
        val orginFormat = SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.getDefault())
        val date = orginFormat.parse(dateStr)
        val calendar = Calendar.getInstance()
        calendar.time = date
        val isoDateFormat = SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.getDefault())
        isoDateFormat.timeZone = TimeZone.getTimeZone(UTC_TIME_ZONE)
        return isoDateFormat.format(calendar.time)
    }
    fun changeDateToISOFormatOnMin(dateStr: String, format: String): String {
        val orginFormat = SimpleDateFormat(format, Locale.getDefault())
        val date = orginFormat.parse(dateStr)
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        val isoDateFormat = SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.getDefault())
        return isoDateFormat.format(calendar.time)
    }
    fun changeDateToISOFormatOnMin1(dateStr: String, format: String): Long {
        val orginFormat = SimpleDateFormat(format, Locale.getDefault())
        val date = orginFormat.parse(dateStr)
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        return calendar.timeInMillis
    }
    fun changeDateToISOFormatOnMax(dateStr: String, format: String): String {
        val orginFormat = SimpleDateFormat(format, Locale.getDefault())
        val date = orginFormat.parse(dateStr)
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar[Calendar.HOUR_OF_DAY] = 23
        calendar[Calendar.MINUTE] = 59
        calendar[Calendar.SECOND] = 59
        val isoDateFormat = SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.getDefault())
        return isoDateFormat.format(calendar.time)
    }

    fun formatToToday(dateStr: String?): String? {
        if (dateStr == null) {
            return null
        }
        val isoDateFormat = SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.getDefault())
        isoDateFormat.timeZone = TimeZone.getTimeZone(UTC_TIME_ZONE)
        val date = isoDateFormat.parse(dateStr)
        val format = if (DateUtils.isToday(date.time)) {
            SimpleDateFormat("HH:mm", Locale.getDefault())
        } else {
            val cal = Calendar.getInstance()
            val thisYear = cal.get(Calendar.YEAR)
            cal.time = date
            val year = cal.get(Calendar.YEAR)
            if (thisYear == year) {
                SimpleDateFormat("d 'Thg' M", Locale.getDefault())
            } else {
                SimpleDateFormat("d/M/yyyy", Locale.getDefault())
            }
        }

        return format.format(date)
    }

    fun formatDate(date: Date, formatPattern: String): String? {
        val format = SimpleDateFormat(formatPattern, Locale.getDefault())
        return format.format(date)
    }

    fun isoFormatToOtherFormat(isoDate: String, format: String): String {
        val isoDateFormat = SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.getDefault())
        val date = isoDateFormat.parse(isoDate)
        val otherFormat = SimpleDateFormat(format, Locale.getDefault())
        return otherFormat.format(date)
    }

    fun isoFormatToOtherFormatUTC(isoDate: String, format: String): String {
        val isoDateFormat = SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.getDefault())
        isoDateFormat.timeZone = TimeZone.getTimeZone(UTC_TIME_ZONE)
        val date = isoDateFormat.parse(isoDate)
        val otherFormat = SimpleDateFormat(format, Locale.getDefault())
        return otherFormat.format(date)
    }

    fun formatStartDateUTC(startDate: Calendar): String {
        val copyStartDate = startDate.clone() as Calendar
        copyStartDate[Calendar.HOUR_OF_DAY] = 0
        copyStartDate[Calendar.MINUTE] = 0
        copyStartDate[Calendar.SECOND] = 0
        copyStartDate[Calendar.MILLISECOND] = 0
        val formatterUTC = SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.ENGLISH)
        formatterUTC.timeZone = TimeZone.getTimeZone(GMT_TIME_ZONE)
        return formatterUTC.format(copyStartDate.time)
    }

    fun formatEndDateUTC(endDate: Calendar): String {
        val copyEndDate = endDate.clone() as Calendar
        copyEndDate[Calendar.HOUR_OF_DAY] = 23
        copyEndDate[Calendar.MINUTE] = 59
        copyEndDate[Calendar.SECOND] = 59
        copyEndDate[Calendar.MILLISECOND] = 999
        val formatterUTC = SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.ENGLISH)
        formatterUTC.timeZone = TimeZone.getTimeZone(GMT_TIME_ZONE)
        return formatterUTC.format(copyEndDate.time)
    }

    fun formatStartDateUTCFilter(startDate: Calendar): String {
        val copyStartDate = startDate.clone() as Calendar
        val formatterUTC = SimpleDateFormat(ISO_DATE_ONLY_FORMAT, Locale.ENGLISH)
        formatterUTC.timeZone = TimeZone.getTimeZone(GMT_TIME_ZONE)
        return formatterUTC.format(copyStartDate.time)
    }


//    fun formatDateRangeShort(startDate: Calendar, endDate: Calendar, format: String = ISO_SHORT_DATE_FOMAT): String {
//        val today = getToday()
//        val beforDay = before(today, 1)
//        if (daysBetween(startDate, today).toInt() == 0
//                && daysBetween(endDate, today).toInt() == 0) {
//            return F365Application.getApplication().getString(R.string.today)
//        }
//        if (daysBetween(startDate, beforDay).toInt() == 0
//                && daysBetween(endDate, beforDay).toInt() == 0) {
//            return F365Application.getApplication().getString(R.string.yesterday)
//        }
//        if (daysBetween(startDate, endDate).toInt() == 0) {
//            return dateToDateString(startDate, format)
//        }
//        return "%1s - %2s".format(
//            dateToDateString(startDate, format),
//                dateToDateString(endDate, format)
//        )
//    }

    fun format8601SSSZToToday(dateStr: String, format: String): String {
        val isoDateFormat = SimpleDateFormat(ISO_8601SSSZ_TIME_FORMAT, Locale.getDefault())
        val date = isoDateFormat.parse(dateStr)
        return if (getCalendarFromIso8601SSSZString(dateStr)!!.time == getToday()) {
            formatDate(date, "HH:ss") ?: ""
        } else {
            formatDate(date, format) ?: ""
        }
    }

    fun convertTimeToDateAndTime(time: Long): String {
        val date = Date(time)
        val format: Format = SimpleDateFormat(STRING_DATE_AND_TIME)
        return format.format(date)
    }

//    fun getDisplayNotificationTime(dateStr: String, format: String): String? {
//        val isoDateFormat = SimpleDateFormat(ISO_8601SSSZ_TIME_FORMAT, Locale.getDefault())
//        val date = isoDateFormat.parse(dateStr)
//        var difference: Long = 0
//        val mDate = System.currentTimeMillis()
//        if (mDate > date.time) {
//            difference = mDate - date.time
//            val seconds = difference / 1000
//            val minutes = seconds / 60
//            val hours = minutes / 60
//            val days = hours / 24
//            return if (minutes <= 1) {
//                String.format(SAApplication.saApplication().getString(R.string.min_ago), "1")
//            } else if (minutes < 60) // 45 * 60
//            {
//                String.format(SAApplication.saApplication().getString(R.string.min_ago), "$minutes")
//            } else if (hours < 24) // 24 * 60 * 60
//            {
//                String.format(SAApplication.saApplication().getString(R.string.hour_ago), "$hours")
//            } else if (days < 2) // 30 * 24 * 60 * 60
//            {
//                String.format(SAApplication.saApplication().getString(R.string.day_ago), "$days")
//            } else {
//                formatDate(date, format) ?: ""
//            }
//        }
//        return ""
//    }

    fun getCalendarFromIso8601SSSZString(isoDateString: String?): Calendar? {
        return if (isoDateString.isNullOrBlank()) null else {
            try {
                val dateFormat = SimpleDateFormat(ISO_8601SSSZ_TIME_FORMAT, Locale.getDefault())
                val date = dateFormat.parse(isoDateString)
                Calendar.getInstance().apply { time = date }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    fun formatDateToUTCDate(date: Date): String {
        val isoDateFormat = SimpleDateFormat(ISO_DATE_TIME_FORMAT, Locale.getDefault())
        isoDateFormat.timeZone = TimeZone.getTimeZone(UTC_TIME_ZONE)
        return isoDateFormat.format(date)
    }

    fun formatDateToIso8601SSSZString(date: Date): String {
        val isoDateFormat = SimpleDateFormat(ISO_8601SSSZ_TIME_FORMAT, Locale.getDefault())
        isoDateFormat.timeZone = TimeZone.getTimeZone(UTC_TIME_ZONE)
        return isoDateFormat.format(date)
    }

    fun checkOverDate(checkTime: Calendar?, timeMax: Calendar): Boolean {
        if (checkTime == null) {
            return false
        }
        if (checkTime.get(Calendar.YEAR) >= timeMax.get(Calendar.YEAR)
                && checkTime.get(Calendar.MONTH) >= timeMax.get(Calendar.MONTH)
                && checkTime.get(Calendar.DAY_OF_MONTH) >= timeMax.get(Calendar.DAY_OF_MONTH)) {
            return true
        }
        return false
    }

    fun getCalendarFromDateString(dateString: String, dateFormatStr: String = ISO_DATE_TIME_FORMAT): Calendar{
        val formatter = SimpleDateFormat(dateFormatStr, Locale.getDefault())
        val date = formatter.parse(dateString)
        val cal = Calendar.getInstance()
        cal.time = date
        return cal
    }

    fun getStartOfMonth(iosDateStr: String, dateFormatStr: String): Calendar {
        val formatter = SimpleDateFormat(dateFormatStr, Locale.getDefault())
        val date = formatter.parse(iosDateStr)
        val cal = Calendar.getInstance()
        cal.time = date
        val endDateOfMonth = cal.getActualMinimum(Calendar.DAY_OF_MONTH)
        cal.set(Calendar.DAY_OF_MONTH, endDateOfMonth)
        return cal
    }

    fun getEndOfMonth(iosDateStr: String, dateFormatStr: String): Calendar {
        val formatter = SimpleDateFormat(dateFormatStr, Locale.getDefault())
        val date = formatter.parse(iosDateStr)
        val cal = Calendar.getInstance()
        cal.time = date
        val endDateOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        cal.set(Calendar.DAY_OF_MONTH, endDateOfMonth)
        val currentCal = getToday()
        return cal
    }

    fun getStartOfYear(iosDateStr: String, dateFormatStr: String): Calendar {
        val formatter = SimpleDateFormat(dateFormatStr, Locale.getDefault())
        val date = formatter.parse(iosDateStr)
        val cal = Calendar.getInstance()
        cal.time = date
        val endDateOfYear = cal.getActualMinimum(Calendar.DAY_OF_YEAR);
        cal.set(Calendar.DAY_OF_YEAR, endDateOfYear)
        return cal
    }

    fun getEndOfYear(iosDateStr: String, dateFormatStr: String): Calendar {
        val formatter = SimpleDateFormat(dateFormatStr, Locale.getDefault())
        val date = formatter.parse(iosDateStr)
        val cal = Calendar.getInstance()
        cal.time = date
        val endDateOfYear = cal.getActualMaximum(Calendar.DAY_OF_YEAR);
        cal.set(Calendar.DAY_OF_YEAR, endDateOfYear)
        return cal
    }


    fun getMaxDate(dateFilter: Date, dateCompare: Date): Date {
        return if (dateFilter.time > dateCompare.time) {
            dateFilter
        } else {
            dateCompare
        }
    }

    fun getMinDate(dateFilter: Date, dateCompare: Date): Date {
        return if (dateFilter.time < dateCompare.time) {
            dateFilter
        } else {
            dateCompare
        }
    }

//    fun formatDateFromMillis(millis: Long, format: String = ISO_DATE_ONLY_FORMAT): String {
//        val today = getToday()
//        val date = Calendar.getInstance()
//        date.timeInMillis = millis
//        if (daysBetween(date, today).toInt() == 0) {
//            return SAApplication.saApplication().getString(R.string.today)
//        }
//        val formatDate = SimpleDateFormat(format, Locale.getDefault())
//        return formatDate.format(millis)
//    }

    fun checkIsSameDate(millis1: Long, millis2: Long): Boolean {
        return millisToDate(millis1) == millisToDate(millis2)
    }

    fun checkIsSameMinute(millis1: Long, millis2: Long): Boolean {
        val cal1 = Calendar.getInstance()
        cal1.timeInMillis = millis1
        val cal2 = Calendar.getInstance()
        cal2.timeInMillis = millis2
        return checkIsSameDate(millis1, millis2)
                && cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY)
                && cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE)
    }

    fun dateToISOShortDateTimeString(date: Date): String {
        val format = SimpleDateFormat(ISO_SHORT_DATE_FOMAT, Locale.getDefault())
        return format.format(date)
    }

    fun isToday(date: Date?): Boolean {
        return isSameDay(date, Calendar.getInstance().time)
    }

    fun isYesterday(date: Date?): Boolean {
        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DAY_OF_MONTH, -1)
        return isSameDay(date, yesterday.time)
    }

    fun isCurrentYear(date: Date?): Boolean {
        return isSameYear(date, Calendar.getInstance().time)
    }

    fun dateToISOMonthString(date: Date): String {
        val format = SimpleDateFormat(ISO_MONTH_FOMAT, Locale.getDefault())
        return format.format(date)
    }

    fun formatConversationTime(millis: Long): String? {
        val date = Date(millis)
        return when {
            isToday(date) -> {
                val format = SimpleDateFormat(TIME, Locale.getDefault())
                format.format(date)
            }
            isYesterday(date) ->{
                val format = SimpleDateFormat(STRING_DAY_MONTH, Locale.getDefault())
                format.format(date)
            }
            isCurrentYear(date) -> {
                val format = SimpleDateFormat(STRING_DAY_MONTH, Locale.getDefault())
                format.format(date)
            }
            else -> {
                val format = SimpleDateFormat(STRING_DAY_MONTH_YEAR, Locale.getDefault())
                format.format(date)
            }
        }
    }

    private fun isSameYear(date1: Date?, date2: Date?): Boolean {
        require(!(date1 == null || date2 == null)) { "Dates must not be null" }
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        return isSameYear(cal1, cal2)
    }

    private fun isSameYear(cal1: Calendar?, cal2: Calendar?): Boolean {
        require(!(cal1 == null || cal2 == null)) { "Dates must not be null" }
        return cal1[Calendar.ERA] == cal2[Calendar.ERA] &&
                cal1[Calendar.YEAR] == cal2[Calendar.YEAR]
    }

    private fun isSameDay(date1: Date?, date2: Date?): Boolean {
        require(!(date1 == null || date2 == null)) { "Dates must not be null" }
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        return isSameDay(cal1, cal2)
    }

    private fun isSameDay(cal1: Calendar?, cal2: Calendar?): Boolean {
        require(!(cal1 == null || cal2 == null)) { "Dates must not be null" }
        return cal1[Calendar.ERA] == cal2[Calendar.ERA] && cal1[Calendar.YEAR] == cal2[Calendar.YEAR] && cal1[Calendar.DAY_OF_YEAR] == cal2[Calendar.DAY_OF_YEAR]
    }

    fun changeDateToISOShortFormat(dateStr: String, format: String): String {
        val orginFormat = SimpleDateFormat(format, Locale.getDefault())
        val date = orginFormat.parse(dateStr)
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        val isoDateFormat = SimpleDateFormat(ISO_SHORT_DATE_FOMAT, Locale.getDefault())
        return isoDateFormat.format(calendar.time)
    }

    fun dateStringUTCToMillis(dateStr: String): Long {
        val formatter = SimpleDateFormat(ISO_DATE_TIME_FORMAT)
        formatter.timeZone = TimeZone.getTimeZone(UTC_TIME_ZONE)
        val date = formatter.parse(dateStr)
        val toFormatter = SimpleDateFormat(ISO_SHORT_DATE_FOMAT)
        toFormatter.timeZone = TimeZone.getDefault()
        val dateString = toFormatter.format(date)
        return toFormatter.parse(dateString).time
    }

    fun getTodayDefaultTimeZoneToMillis(): Long {
        val toFormatter = SimpleDateFormat(ISO_SHORT_DATE_FOMAT)
        toFormatter.timeZone = TimeZone.getDefault()
        val dateString = toFormatter.format(Calendar.getInstance().time)
        return toFormatter.parse(dateString).time
    }

    fun formatSecondDateTime(second: Long): String {
        val hours = second / 3600;
        val minutes = (second % 3600) / 60;
        val seconds = second % 60;

        if (hours>0)  return String.format("%02d:%02d:%02d", hours, minutes, seconds)
        else return String.format("%02d:%02d", minutes, seconds)
    }
}
