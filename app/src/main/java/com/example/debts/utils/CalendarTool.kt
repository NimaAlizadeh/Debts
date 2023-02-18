package com.example.debts.utils

import java.util.*



class CalendarTool {

    constructor() {
        val calendar: Calendar = GregorianCalendar()
        setGregorianDate(
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH] + 1,
            calendar[Calendar.DAY_OF_MONTH]
        )
    }


    constructor(year: Int, month: Int, day: Int) {
        setGregorianDate(year, month, day)
    }


    val iranianDate: String
        get() = iranianYear.toString() + "/" + iranianMonth + "/" + iranianDay


    val gregorianDate: String
        get() = gregorianYear.toString() + "/" + gregorianMonth + "/" + gregorianDay


    val julianDate: String
        get() = julianYear.toString() + "/" + julianMonth + "/" + julianDay


    val weekDayStr: String
        get() {
            val weekDayStr = arrayOf(
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday",
                "Friday",
                "Saturday",
                "Sunday"
            )
            return weekDayStr[dayOfWeek]
        }


    override fun toString(): String {
        return weekDayStr +
                ", Gregorian:[" + gregorianDate +
                "], Julian:[" + julianDate +
                "], Iranian:[" + iranianDate + "]"
    }


    val dayOfWeek: Int
        get() = JDN % 7

    fun nextDay() {
        JDN++
        JDNToIranian()
        JDNToJulian()
        JDNToGregorian()
    }


    fun nextDay(days: Int) {
        JDN += days
        JDNToIranian()
        JDNToJulian()
        JDNToGregorian()
    }


    fun previousDay() {
        JDN--
        JDNToIranian()
        JDNToJulian()
        JDNToGregorian()
    }


    fun previousDay(days: Int) {
        JDN -= days
        JDNToIranian()
        JDNToJulian()
        JDNToGregorian()
    }


    fun setIranianDate(year: Int, month: Int, day: Int) {
        iranianYear = year
        iranianMonth = month
        iranianDay = day
        JDN = IranianDateToJDN()
        JDNToIranian()
        JDNToJulian()
        JDNToGregorian()
    }


    fun setGregorianDate(year: Int, month: Int, day: Int) {
        gregorianYear = year
        gregorianMonth = month
        gregorianDay = day
        JDN = gregorianDateToJDN(year, month, day)
        JDNToIranian()
        JDNToJulian()
        JDNToGregorian()
    }


    fun setJulianDate(year: Int, month: Int, day: Int) {
        julianYear = year
        julianMonth = month
        julianDay = day
        JDN = julianDateToJDN(year, month, day)
        JDNToIranian()
        JDNToJulian()
        JDNToGregorian()
    }


    private fun IranianCalendar() {
        val Breaks = intArrayOf(
            -61, 9, 38, 199, 426, 686, 756, 818, 1111, 1181,
            1210, 1635, 2060, 2097, 2192, 2262, 2324, 2394, 2456, 3178
        )
        var jm: Int
        var N: Int
        var leapJ: Int
        val leapG: Int
        var jp: Int
        var j: Int
        var jump: Int
        gregorianYear = iranianYear + 621
        leapJ = -14
        jp = Breaks[0]
        // Find the limiting years for the Iranian year 'irYear'
        j = 1
        do {
            jm = Breaks[j]
            jump = jm - jp
            if (iranianYear >= jm) {
                leapJ += jump / 33 * 8 + jump % 33 / 4
                jp = jm
            }
            j++
        } while (j < 20 && iranianYear >= jm)
        N = iranianYear - jp
        // Find the number of leap years from AD 621 to the begining of the current
// Iranian year in the Iranian (Jalali) calendar
        leapJ += N / 33 * 8 + (N % 33 + 3) / 4
        if (jump % 33 == 4 && jump - N == 4) leapJ++
        // And the same in the Gregorian date of Farvardin the first
        leapG = gregorianYear / 4 - (gregorianYear / 100 + 1) * 3 / 4 - 150
        march = 20 + leapJ - leapG
        // Find how many years have passed since the last leap year
        if (jump - N < 6) N = N - jump + (jump + 4) / 33 * 33
        leap = ((N + 1) % 33 - 1) % 4
        if (leap == -1) leap = 4
    }


    fun IsLeap(irYear1: Int): Boolean {
// Iranian years starting the 33-year rule
        val Breaks = intArrayOf(
            -61, 9, 38, 199, 426, 686, 756, 818, 1111, 1181,
            1210, 1635, 2060, 2097, 2192, 2262, 2324, 2394, 2456, 3178
        )
        var jm: Int
        var N: Int
        var leapJ: Int
        val leapG: Int
        var jp: Int
        var j: Int
        var jump: Int
        gregorianYear = irYear1 + 621
        leapJ = -14
        jp = Breaks[0]
        // Find the limiting years for the Iranian year 'irYear'
        j = 1
        do {
            jm = Breaks[j]
            jump = jm - jp
            if (irYear1 >= jm) {
                leapJ += jump / 33 * 8 + jump % 33 / 4
                jp = jm
            }
            j++
        } while (j < 20 && irYear1 >= jm)
        N = irYear1 - jp
        // Find the number of leap years from AD 621 to the begining of the current
// Iranian year in the Iranian (Jalali) calendar
        leapJ += N / 33 * 8 + (N % 33 + 3) / 4
        if (jump % 33 == 4 && jump - N == 4) leapJ++
        // And the same in the Gregorian date of Farvardin the first
        leapG = gregorianYear / 4 - (gregorianYear / 100 + 1) * 3 / 4 - 150
        march = 20 + leapJ - leapG
        // Find how many years have passed since the last leap year
        if (jump - N < 6) N = N - jump + (jump + 4) / 33 * 33
        leap = ((N + 1) % 33 - 1) % 4
        if (leap == -1) leap = 4
        return if (leap == 4 || leap == 0) true else false
    }


    private fun IranianDateToJDN(): Int {
        IranianCalendar()
        return gregorianDateToJDN(
            gregorianYear,
            3,
            march
        ) + (iranianMonth - 1) * 31 - iranianMonth / 7 * (iranianMonth - 7) + iranianDay - 1
    }


    private fun JDNToIranian() {
        JDNToGregorian()
        iranianYear = gregorianYear - 621
        IranianCalendar() // This invocation will update 'leap' and 'march'
        val JDN1F = gregorianDateToJDN(gregorianYear, 3, march)
        var k = JDN - JDN1F
        if (k >= 0) {
            if (k <= 185) {
                iranianMonth = 1 + k / 31
                iranianDay = k % 31 + 1
                return
            } else k -= 186
        } else {
            iranianYear--
            k += 179
            if (leap == 1) k++
        }
        iranianMonth = 7 + k / 30
        iranianDay = k % 30 + 1
    }


    private fun julianDateToJDN(year: Int, month: Int, day: Int): Int {
        return (year + (month - 8) / 6 + 100100) * 1461 / 4 + (153 * ((month + 9) % 12) + 2) / 5 + day - 34840408
    }


    private fun JDNToJulian() {
        val j = 4 * JDN + 139361631
        val i = j % 1461 / 4 * 5 + 308
        julianDay = i % 153 / 5 + 1
        julianMonth = i / 153 % 12 + 1
        julianYear = j / 1461 - 100100 + (8 - julianMonth) / 6
    }


    private fun gregorianDateToJDN(year: Int, month: Int, day: Int): Int {
        var jdn =
            (year + (month - 8) / 6 + 100100) * 1461 / 4 + (153 * ((month + 9) % 12) + 2) / 5 + day - 34840408
        jdn = jdn - (year + 100100 + (month - 8) / 6) / 100 * 3 / 4 + 752
        return jdn
    }


    private fun JDNToGregorian() {
        var j = 4 * JDN + 139361631
        j = j + ((4 * JDN + 183187720) / 146097 * 3 / 4 * 4 - 3908)
        val i = j % 1461 / 4 * 5 + 308
        gregorianDay = i % 153 / 5 + 1
        gregorianMonth = i / 153 % 12 + 1
        gregorianYear = j / 1461 - 100100 + (8 - gregorianMonth) / 6
    }


    var iranianYear // Year part of a Iranian date
            = 0
        private set


    var iranianMonth // Month part of a Iranian date
            = 0
        private set

    var iranianDay // Day part of a Iranian date
            = 0
        private set

    var gregorianYear // Year part of a Gregorian date
            = 0
        private set

    var gregorianMonth // Month part of a Gregorian date
            = 0
        private set

    var gregorianDay // Day part of a Gregorian date
            = 0
        private set

    var julianYear // Year part of a Julian date
            = 0
        private set

    var julianMonth // Month part of a Julian date
            = 0
        private set

    var julianDay // Day part of a Julian date
            = 0
        private set
    private var leap // Number of years since the last leap year (0 to 4)
            = 0
    private var JDN // Julian Day Number
            = 0
    private var march // The march day of Farvardin the first (First day of jaYear)
            = 0
}
