package www.ahest.cn.base.utils

import www.ahest.cn.base.utils.StringBaseUtils.removeAnyTypeStr
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Utility DateTime class methods.
 *
 * @author BOANSEN
 * @version 1.0 11/15/2010
 */
object DateTimeUtil {
    const val PATTERN_DATETIME_Hour = "yyyy-MM-dd HH:mm"
    const val PATTERN_DATETIME_Month = "yyyy-MM"
    const val PATTERN_DATETIME_MS = "HH:mm:ss"
    const val PATTERN_DATETIME_FULL = "yyyy-MM-dd HH:mm:ss"
    const val PATTERN_DATETIME_ = "yyyyMMddHHmmss"
    const val PATTERN_DATETIME = "yyyy-MM-dd"
    const val PATTERN_DATETIME_sf = "yyyy-MM-dd HH:mm"
    val DATEFORMAT: DateFormat = SimpleDateFormat("yyyyMMdd")
    val DATEFORMAT_TYYYY_MM_DD: DateFormat = SimpleDateFormat(
        "yyyy-MM-dd"
    )
    val DATEFORMAT_TYYYY_MM_DD_ss: DateFormat = SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss"
    )
    val DATEFORMAT_MM_DD: DateFormat = SimpleDateFormat(
        "MM-dd"
    )
    val YEARFORMAT: DateFormat = SimpleDateFormat("yyyyMM")
    val YEAR_FORMAT: DateFormat = SimpleDateFormat("yyyy-MM")
    val CALENDAR = Calendar.getInstance()

    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    @Throws(ParseException::class)
    fun longToDate(currentTime: Long, formatType: String?): Date? {
        val dateOld = Date(currentTime) // 根据long类型的毫秒数生命一个date类型的时间
        val sDateTime = dateToString(dateOld, formatType) // 把date类型的时间转换为string
        return stringToDate(sDateTime, formatType)
    }

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    @Throws(ParseException::class)
    fun stringToDate(strTime: String?, formatType: String?): Date? {
        val formatter = SimpleDateFormat(formatType)
        var date: Date? = null
        date = formatter.parse(strTime)
        return date
    }

    // currentTime要转换的long类型的时间
    // formatType要转换的string类型的时间格式
    @Throws(ParseException::class)
    fun longToString(currentTime: Long, formatType: String?): String {
        val date = longToDate(currentTime, formatType) // long类型转成Date类型
        return dateToString(date, formatType)
    }

    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    fun dateToString(data: Date?, formatType: String?): String {
        return SimpleDateFormat(formatType).format(data)
    }

    @Throws(ParseException::class)
    fun parse(strDate: String?): Date {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        return sdf.parse(strDate)
    }

    //由出生日期获得年龄
    @Throws(Exception::class)
    fun getAge(strDate: String?): Int {
        if ("" == removeAnyTypeStr(strDate)) {
            return 0
        }
        val birthDay = parse(strDate)
        val cal = Calendar.getInstance()
        if (cal.before(birthDay)) {
            return -1
        }
        val yearNow = cal[Calendar.YEAR]
        val monthNow = cal[Calendar.MONTH]
        val dayOfMonthNow = cal[Calendar.DAY_OF_MONTH]
        cal.time = birthDay
        val yearBirth = cal[Calendar.YEAR]
        val monthBirth = cal[Calendar.MONTH]
        val dayOfMonthBirth = cal[Calendar.DAY_OF_MONTH]
        var age = yearNow - yearBirth
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--
            } else {
                age--
            }
        }
        return age
    }


    fun getCurrentDateTime(PATTERN: String?): String? {
        return getDateTime(Date(), PATTERN)
    }

    val currentDateTimeMS: String?
        get() = getDateTime(Date(), PATTERN_DATETIME_MS)

    /***
     *
     * @Description: 获取当前时间前n天
     * @Title: getCurrentDateTime_bei
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     * @author OP
     * @date 2014年11月19日 下午5:12:41
     */
    fun getCurrentDateTime(n: Int): String {
        val calendar = Calendar.getInstance() // 此时打印它获取的是系统当前时间
        calendar.add(Calendar.DATE, -n) // 得到前n天
        return SimpleDateFormat(PATTERN_DATETIME_FULL)
            .format(calendar.time)
    }

    fun getCurrentDateTime(): String? {
        return getDateTime(Date(), PATTERN_DATETIME_FULL)
    }

    //获取系统时间的10位的时间戳
    val time: String
        get() {
            val time = System.currentTimeMillis() / 1000 //获取系统时间的10位的时间戳
            return time.toString()
        }// 此时打印它获取的是系统当前时间
    // 得到前一天
    /***
     *
     * @Description: 获取当前时间前一天
     * @Title: getCurrentDateTime_bei
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     * @author OP
     * @date 2014年11月19日 下午5:12:41
     */
    val currentDateTime_bei: String
        get() {
            val calendar = Calendar.getInstance() // 此时打印它获取的是系统当前时间
            calendar.add(Calendar.DATE, -1) // 得到前一天
            return SimpleDateFormat(PATTERN_DATETIME_FULL)
                .format(calendar.time)
        }

    /**
     * Return current date string.
     *
     * @return current date, pattern: "yyyyMMdd".
     */
    val currentDate: String?
        get() = getDateTime(Date(), PATTERN_DATETIME)

    fun getCurrentDate(dateTime: Long?, PATTERN: String?): String? {
        // DateTime dateTime = new DateTime(dateTime);
        return if (dateTime == null || dateTime <= 0) {
            ""
        } else {
            getDateTime(Date(dateTime), PATTERN)
        }
    }

    val currentDateYearAndMonch: String?
        get() = getDateTime(Date(), "yyyy-MM")
    val currentDateYear: String?
        get() = getDateTime(Date(), "yyyy")
    val currentDateMonch: String?
        get() = getDateTime(Date(), "MM-dd")

    /**
     * Return current datetime string.
     *
     * @param pattern format pattern
     * @return current datetime return null if pattern is not right
     */
    fun getDateTime(date: Date?, pattern: String?): String? {
        if (pattern == null) {
            return null
        }
        var str: String? = null
        val format = SimpleDateFormat(pattern)
        str = format.format(date)
        return str
    }

    /**
     * Parse a datetime string.
     *
     * @param param datetime string, pattern: "yyyy-MM-dd HH:mm:ss".
     * @return java.util.Date return null if pattern or param is not right
     */
    fun parse(param: String?, pattern: String?): Date? {
        if (param == null || pattern == null) {
            return null
        }
        var date: Date? = null
        try {
            val format = SimpleDateFormat(pattern)
            date = format.parse(param)
        } catch (ex: ParseException) {
        }
        return date
    }

    /**
     * Monday's date for this week
     *
     * @return String the date string,Monday's date this week
     */
    val mondayOfWeek: String
        get() {
            val mondayPlus = mondayPlus
            val currentDate = GregorianCalendar()
            currentDate.add(GregorianCalendar.DATE, mondayPlus)
            val monday = currentDate.time
            return DATEFORMAT.format(monday)
        }

    /**
     * This week the date for Sunday
     *
     * @return String the date string,Sunday's date this week
     */
    val sundayOfWeek: String
        get() {
            val mondayPlus = mondayPlus
            val currentDate = GregorianCalendar()
            currentDate.add(GregorianCalendar.DATE, mondayPlus + 6)
            val monday = currentDate.time
            return DATEFORMAT.format(monday)
        }// So here Monday as
    // the first day
    // minus 1
// Today is the Day of the week, Sunday is the first day, Tuesday is the
    // day ......
    /**
     * Get the current date and the number of days difference between this
     * Sunday
     *
     * @return int To the amount of time this Sunday
     */
    private val mondayPlus: Int
        private get() {
            val cd = Calendar.getInstance()
            // Today is the Day of the week, Sunday is the first day, Tuesday is the
            // day ......
            val dayOfWeek = cd[Calendar.DAY_OF_WEEK] - 1 // So here Monday as
            // the first day
            // minus 1
            return if (dayOfWeek == 1) {
                0
            } else {
                1 - dayOfWeek
            }
        }

    /**
     * before month first day
     */
    val firstDayOfBeforeMonth: String?
        get() {
            var first: String? = null
            val firstDate = Calendar.getInstance()
            firstDate.add(Calendar.MONTH, -1)
            firstDate[Calendar.DATE] = 1
            first = DATEFORMAT.format(firstDate.time)
            return first
        }// Specified calendar field could have the maximum

    /**
     * before month last day
     */
    val lastDayOfBeforeMonth: String?
        get() {
            var last: String? = null
            val lastDate = Calendar.getInstance()
            lastDate.add(Calendar.MONTH, -1)
            // Specified calendar field could have the maximum
            lastDate[Calendar.DAY_OF_MONTH] = lastDate.getActualMaximum(Calendar.DAY_OF_MONTH)
            last = DATEFORMAT.format(lastDate.time)
            return last
        }

    /**
     * The date of the first day of the month
     *
     * @return String the date string,The date of the first day of the month
     */
    val firstDayOfMonth: String?
        get() {
            var fir: String? = null
            val firstDate = Calendar.getInstance()
            firstDate[Calendar.DATE] = 1
            fir = DATEFORMAT.format(firstDate.time)
            return fir
        }// Specified calendar field could have the maximum

    /**
     * The date of the last day of the month
     *
     * @return String the date string,The date of the last day of the month
     */
    val lastDayOfMonth: String
        get() {
            var str = ""
            val lastDate = Calendar.getInstance()
            // Specified calendar field could have the maximum
            lastDate[Calendar.DAY_OF_MONTH] = lastDate.getActualMaximum(Calendar.DAY_OF_MONTH)
            str = DATEFORMAT.format(lastDate.time)
            return str
        }

    /**
     * after month first day
     */
    val firstDayOfAfterMonth: String?
        get() {
            var first: String? = null
            val firstDate = Calendar.getInstance()
            firstDate.add(Calendar.MONTH, +1)
            firstDate[Calendar.DATE] = 1
            first = DATEFORMAT.format(firstDate.time)
            return first
        }// Specified calendar field could have the maximum

    /**
     * after month last day
     */
    val lastDayOfAfterMonth: String?
        get() {
            var last: String? = null
            val lastDate = Calendar.getInstance()
            lastDate.add(Calendar.MONTH, +1)
            // Specified calendar field could have the maximum
            lastDate[Calendar.DAY_OF_MONTH] = lastDate.getActualMaximum(Calendar.DAY_OF_MONTH)
            last = DATEFORMAT.format(lastDate.time)
            return last
        }

    /**
     * gaofeng add 11-09
     *
     * @param yyyy_mm_dd
     * @return
     */
    fun getEveryMonthOfLastDay(yyyy_mm_dd: String?): String? {
        try {
            val date = DATEFORMAT_TYYYY_MM_DD.parse(yyyy_mm_dd)
            val cc = Calendar.getInstance()
            cc.time = date
            cc[Calendar.DAY_OF_MONTH] = cc.getActualMaximum(Calendar.DAY_OF_MONTH)
            return DATEFORMAT.format(cc.time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * A month, first and last day. 查询某个月的的最后一天,日期
     *
     * @param fromDate '20110910'
     */
    fun getFirstAndLastDayOfSomeMonth(fromDate: String?): String {
        // ArrayList date = new ArrayList();
        var d: Date? = null
        try {
            d = DATEFORMAT.parse(fromDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        /*
         * Calendar c = Calendar.getInstance(); c.setTime(d);
         * c.add(Calendar.DATE, 1); String fir = DATEFORMAT.format(c.getTime());
         * date.add(fir);
         */
        val cc = Calendar.getInstance()
        cc.time = d
        cc[Calendar.DAY_OF_MONTH] = cc.getActualMaximum(Calendar.DAY_OF_MONTH)
        // date.add(cc);
        return DATEFORMAT.format(cc.time)
    }

    // 根据当前日期,查询前一个月的第一天.
    fun getFirsDayOfBeforeMonth(fromDate: String?): String? {
        var first: String? = null
        var d: Date? = null
        try {
            d = DATEFORMAT.parse(fromDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val firstDate = Calendar.getInstance()
        firstDate.time = d
        firstDate.add(Calendar.MONTH, -1)
        firstDate[Calendar.DATE] = 1
        first = DATEFORMAT.format(firstDate.time)
        return first
    }

    // 根据当前日期,查询后一个月的第一天.
    fun getFirsDayOfAfterMonth(fromDate: String?): String? {
        var first: String? = null
        var d: Date? = null
        try {
            d = DATEFORMAT.parse(fromDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val firstDate = Calendar.getInstance()
        firstDate.time = d
        firstDate.add(Calendar.MONTH, +1)
        firstDate[Calendar.DATE] = 1
        first = DATEFORMAT.format(firstDate.time)
        return first
    }

    /**
     * String date to convert long date
     *
     * @return long date
     */
    fun getTimeOfLong(fromDate: String?): Long {
        var time: Long = 0
        var d: Date? = null
        try {
            d = DATEFORMAT.parse(fromDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance()
        calendar.time = d
        time = calendar.timeInMillis
        return time
    }

    fun getTimeOfLong(fromDate: String?, PATTERN: String?): Long {
        var time: Long = 0
        var d: Date? = null
        val DATEF: DateFormat = SimpleDateFormat(PATTERN)
        try {
            d = DATEF.parse(fromDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance()
        calendar.time = d
        time = calendar.timeInMillis
        return time
    }// month start value 0

    /**
     * 当前年的每一个月如201101 当前年的当前的月如201110
     */
    val firstMonthOfCurrentYear: String?
        get() {
            var month: String? = null
            val calendar = Calendar.getInstance()
            calendar[Calendar.MONTH] = 0 // month start value 0
            month = YEARFORMAT.format(calendar.time)
            return month
        }

    /**
     * 获取今天的函数
     */
    val today: String?
        get() {
            var month: String? = null
            val calendar = Calendar.getInstance()
            month = DATEFORMAT.format(calendar.time)
            return month
        }

    /**
     * 获取今天的函数
     */
    val today_yy_mm_dd: String?
        get() {
            var month: String? = null
            val calendar = Calendar.getInstance()
            month = DATEFORMAT_TYYYY_MM_DD.format(calendar.time)
            return month
        }

    /**
     * 根据时间间隔（小时）获取时间
     *
     * @param date 日期
     * @param hour 小时
     * @return
     */
    fun getDateAddHour(date: Date, hour: Int): Date {
        val l = date.time
        val n = l + hour * 60 * 60 * 1000
        return Date(n)
    }

    /**
     * 获取下一天
     *
     * @param date
     * @return
     */
    fun getNextDate(date: Date): String {
        return DATEFORMAT_MM_DD.format(getDateAddHour(date, 24))
    }

    /**
     * 获取上一天
     *
     * @param date
     * @return
     */
    fun getPreviousDate(date: Date): String {
        return DATEFORMAT_TYYYY_MM_DD.format(getDateAddHour(date, -24))
    }

    /**
     * 获取前一个小时时间
     */
    fun getBeHourDate(n: Int): String {
        return DATEFORMAT_TYYYY_MM_DD_ss.format(getDateAddHour(Date(), n))
    }

    fun getPreviousDateMonch(date: Date): String {
        return YEAR_FORMAT.format(getDateAddHour(date, -24))
    }//        if ("1".equals(mWay)) {
//            mWay = "天";
//        } else if ("2".equals(mWay)) {
//            mWay = "一";
//        } else if ("3".equals(mWay)) {
//            mWay = "二";
//        } else if ("4".equals(mWay)) {
//            mWay = "三";
//        } else if ("5".equals(mWay)) {
//            mWay = "四";
//        } else if ("6".equals(mWay)) {
//            mWay = "五";
//        } else if ("7".equals(mWay)) {
//            mWay = "六";
//        }
    /***
     * 获取当前星期
     * @return
     */
    val nowXingQi: Int
        get() {
            val c = Calendar.getInstance()
            c.timeZone = TimeZone.getTimeZone("GMT+8:00")
            var mWay = c[Calendar.DAY_OF_WEEK] - 1
            if (mWay <= 0) {
                mWay = 7
            }
            //        if ("1".equals(mWay)) {
//            mWay = "天";
//        } else if ("2".equals(mWay)) {
//            mWay = "一";
//        } else if ("3".equals(mWay)) {
//            mWay = "二";
//        } else if ("4".equals(mWay)) {
//            mWay = "三";
//        } else if ("5".equals(mWay)) {
//            mWay = "四";
//        } else if ("6".equals(mWay)) {
//            mWay = "五";
//        } else if ("7".equals(mWay)) {
//            mWay = "六";
//        }
            return mWay
        }

    /**
     * 将"yyyy-mm-dd"格式字符串转换成java.sql.Date
     *
     * @param s "yyyy-mm-dd"格式字符串
     * @return java.sql.Date
     */
    fun convertStrToSQLDate(s: String?): java.sql.Date? {
        return if (removeAnyTypeStr(s) == "") null else java.sql.Date(convertStrToDate(s)!!.time)
    }
    /**
     * 将字符串转换成日期 (java.util.Date)
     *
     * @param s       需要转换成日期的字符串
     * @param pattern 字符串的格式
     * @return
     */
    /**
     * 将字符串转换成日期 (java.util.Date)
     *
     * @param s 需要转换成日期的字符串 格式为 "yyyy-MM-dd"
     * @return
     */
    fun convertStrToDate(s: String?, pattern: String? = "yyyy-MM-dd"): Date? {
        return try {
            val sdf = SimpleDateFormat(pattern)
            sdf.parse(s)
        } catch (pe: ParseException) {
            pe.printStackTrace()
            null
        }
    }

    /**
     * 时间戳格式转换
     */
    var dayNames = arrayOf("周日", "周一", "周二", "周三", "周四", "周五", "周六")

    fun getNewChatTime(timesamp: Long?): String {
        if (timesamp == null) {
            return ""
        }
        var result: String? = ""
        val todayCalendar = Calendar.getInstance()
        val otherCalendar = Calendar.getInstance()
        otherCalendar.timeInMillis = timesamp
        var timeFormat = "M月d日 HH:mm"
        var yearTimeFormat = "yyyy年M月d日 HH:mm"
        var am_pm = ""
        val hour = otherCalendar[Calendar.HOUR_OF_DAY]
        if (hour >= 0 && hour < 6) {
            am_pm = "凌晨"
        } else if (hour >= 6 && hour < 12) {
            am_pm = "早上"
        } else if (hour == 12) {
            am_pm = "中午"
        } else if (hour > 12 && hour < 18) {
            am_pm = "下午"
        } else if (hour >= 18) {
            am_pm = "晚上"
        }
        timeFormat = "M月d日 " + am_pm + "HH:mm"
        yearTimeFormat = "yyyy年M月d日 " + am_pm + "HH:mm"
        val yearTemp = todayCalendar[Calendar.YEAR] == otherCalendar[Calendar.YEAR]
        result = if (yearTemp) {
            val todayMonth = todayCalendar[Calendar.MONTH]
            val otherMonth = otherCalendar[Calendar.MONTH]
            if (todayMonth == otherMonth) { //表示是同一个月
                val temp = todayCalendar[Calendar.DATE] - otherCalendar[Calendar.DATE]
                when (temp) {
                    0 -> getHourAndMin(timesamp)
                    1 -> "昨天 " + getHourAndMin(timesamp)
                    2, 3, 4, 5, 6 -> {
                        val dayOfMonth = otherCalendar[Calendar.WEEK_OF_MONTH]
                        val todayOfMonth = todayCalendar[Calendar.WEEK_OF_MONTH]
                        if (dayOfMonth == todayOfMonth) { //表示是同一周
                            val dayOfWeek = otherCalendar[Calendar.DAY_OF_WEEK]
                            if (dayOfWeek != 1) { //判断当前是不是星期日     如想显示为：周日 12:09 可去掉此判断
                                dayNames[otherCalendar[Calendar.DAY_OF_WEEK] - 1] + getHourAndMin(
                                    timesamp
                                )
                            } else {
                                getTime(timesamp, timeFormat)
                            }
                        } else {
                            getTime(timesamp, timeFormat)
                        }
                    }
                    else -> getTime(timesamp, timeFormat)
                }
            } else {
                getTime(timesamp, timeFormat)
            }
        } else {
            getYearTime(timesamp, yearTimeFormat)
        }
        return StringBaseUtils.removeAnyTypeStr(result)
    }

    /**
     * 当天的显示时间格式
     *
     * @param time
     * @return
     */
    fun getHourAndMin(time: Long): String {
        val format = SimpleDateFormat("HH:mm")
        return format.format(Date(time))
    }

    /**
     * 不同一周的显示时间格式
     *
     * @param time
     * @param timeFormat
     * @return
     */
    fun getTime(time: Long, timeFormat: String?): String? {
        val format = SimpleDateFormat(timeFormat)
        return format.format(Date(time))
    }

    /**
     * 不同年的显示时间格式
     *
     * @param time
     * @param yearTimeFormat
     * @return
     */
    fun getYearTime(time: Long, yearTimeFormat: String?): String? {
        val format = SimpleDateFormat(yearTimeFormat)
        return format.format(Date(time))
    }
}