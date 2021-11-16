package www.ahest.cn.base.utils

import android.text.Spanned
import androidx.core.text.HtmlCompat
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.regex.Pattern

object StringBaseUtils {

    fun fromHtml(source: String): Spanned {
        return HtmlCompat.fromHtml(source, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    fun removeNullToLong(o: Any?): Long {
        return if (o == null || o == "" || !isNumeric(o.toString())) {
            -1L
        } else
            removeNullToLong(o, -1)
    }

    fun removeNullToLong(o: Any?, s: Long): Long {
        return if (o == null || o == "" || !isNumeric(o.toString())) {
            s
        } else
            o.toString().toLong()
//            Integer.parseInt(o.toString())
    }

    /***
     * 转换url中的中文字符
     * @param s
     * @return
     */
    fun toURLString(s: String): String {
        val sb = StringBuffer()
        for (i in 0 until s.length) {
            val c = s[i]
            if (c.code in 0..255) {
                sb.append(c)
            } else {
                try {
                    sb.append(URLEncoder.encode(c.toString(), "utf-8"))
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }

            }
        }
        return sb.toString()
    }

    fun removeNullToFloat(o: Any?, f: Float): Float {
        return if (o == null || o == ""
            || !isDecimal(o.toString()) && !isNumeric(o.toString())
        ) {
            f
        } else
            java.lang.Float.parseFloat(o.toString())
    }

    /***
     * 判断字符串是否是小数 *
     *
     * @param str
     * @return
     */
    fun isDecimal(str: String): Boolean {
        return Pattern.compile("([1-9]+[0-9]*|0)(\\.[\\d]+)?").matcher(str)
            .matches()
    }

    fun removeNullToDouble(o: Any?): Double {
        return if (o == null || o == ""
            || !isDecimal(o.toString()) && !isNumeric(o.toString())
        ) {
            -1.0
        } else
            java.lang.Double.parseDouble(o.toString())
    }

    fun removeNullToInt(o: Any?): Int {
        /**
         * if (o == null) { return ""; }
         *
         * return o.toString().trim();
         */
        return removeNullToInt(o, -1)
    }

    /***
     * 判断是否位字符串数字
     *
     * @param str
     * @return
     */
    fun isNumeric(str: String): Boolean {
        val pattern = Pattern.compile("[0-9]*")
        return pattern.matcher(str).matches()
    }

    fun removeNullToInt(o: Any?, s: Int): Int {
        return if (o == null || o == "" || !isNumeric(o.toString())) {
            s
        } else
            Integer.parseInt(o.toString())
    }

    /**
     * 判断字符串是否为null或者空
     */
    fun checkStrIsNullOrEmpty(s: String?): Boolean {
        var isNull = false
        if (s.isNullOrEmpty()) {
            isNull = true
        }
        if (s.isNullOrBlank()) {
            isNull = true
        }
        return isNull
    }

    fun removeAnyTypeStr(s: String?): String {
        return if (checkStrIsNullOrEmpty(s)) {
            ""
        } else {
            s!!
        }
    }

    fun removeAnyTypeStr(s: String?, c: String): String {
        return if (checkStrIsNullOrEmpty(s)) {
            c
        } else {
            s!!
        }
    }

}