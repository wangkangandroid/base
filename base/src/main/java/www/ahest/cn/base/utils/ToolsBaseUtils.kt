package www.ahest.cn.base.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import www.ahest.cn.base.R
import www.ahest.cn.base.view.sweet.SweetAlertDialog
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

object ToolsBaseUtils {

    fun getGson(): Gson {
        val gabon = GsonBuilder()//建造者模式设置不同的配置
            .serializeNulls()//序列化为null对象
            .disableHtmlEscaping()//防止对网址乱码 忽略对特殊字符的转换
            .excludeFieldsWithoutExposeAnnotation()
        return gabon.create()
    }

    fun getJsonByAny(obj: Any): String {
        return getGson().toJson(obj)
    }

    /**
     * 打开 APP 的详情设置
     */
    fun openAppDetails(context: Context) {
        SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
            .setTitleText(context.getString(R.string.app_name))
            .setContentText("需要相关权限，请到 “应用信息 -> 权限” 中授予！")
            .setConfirmText("去授权")
            .setConfirmClickListener {
                it.dismiss()
                PermissionPageUtils(context).jumpPermissionPage()
            }
            .show()
    }

//    /***
//     * 判断字段是否参与行政审批计数
//     */
//    fun xzspNeedCount(key: String): Boolean {
//        return when (key) {
//            "hy" -> false
//            "sw" -> false
//            "fw" -> false
//            "qb" -> false
//            "tzgg" -> false
//            "xf" -> false
//            "wcgl" -> false
//            "tzgg" -> false
//            else -> true
//        }
//    }

    fun createFilePath(context: Context) {
        var sdFile = context.getExternalFilesDir(null)
        if (sdFile == null || !sdFile.exists()) {
            sdFile = File(getAppPath(context))
            sdFile.mkdirs()
        }
    }

    private val mPath = "storage/emulated/0/Android/data/"
    fun getAppPath(context: Context): String {
        val sdFile = context.getExternalFilesDir(null)
        return if (sdFile == null || !sdFile.exists()) {
            mPath
        } else {
            sdFile.path + "/"
        }
    }

    fun getAppCachePath(context: Context): String {
        val sdFile = context.externalCacheDir
        return if (sdFile == null || !sdFile.exists()) {
            "storage/emulated/0/" + context.packageName + "/"
        } else {
            sdFile.path + "/"
        }
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    fun checkPermissionAllGranted(context: Context, permissions: MutableList<String>): Boolean {
        var isOk = true
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                isOk = false
                break
            }
        }
        return isOk
    }

    /**
     * 打开系统权限界面（通知）
     */
    fun openNotification(context: Context) {
        val intent = Intent()
        when {
            Build.VERSION.SDK_INT >= 26 -> {
                // android 8.0引导
                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                intent.putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
            }
            Build.VERSION.SDK_INT >= 21 -> {
                // android 5.0-7.0
                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                intent.putExtra("app_package", context.packageName)
                intent.putExtra("app_uid", context.applicationInfo.uid)
            }
            else -> {
                // 其他
                intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                intent.data = Uri.fromParts("package", context.packageName, null)
            }
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
//            intent.putExtra("app_package", pageName)
//            intent.putExtra("app_uid", context.applicationInfo.uid)
//            context.startActivity(intent)
//        } else if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
//            val intent = Intent()
//            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//            intent.addCategory(Intent.CATEGORY_DEFAULT)
//            intent.data = Uri.parse("package:$pageName")
//            context.startActivity(intent)
//        }
    }

    /**
     * 打开系统权限界面（全局dialog）
     */
    fun dialogOpenPower(context: Context, activity: Activity, sel: Int) {
        if (Build.VERSION.SDK_INT >= 23)
            if (!Settings.canDrawOverlays(context)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + context.packageName)
                )
                activity.startActivityForResult(intent, sel)
            }
    }

    /**
     * 重启appp
     */
    fun restartApplication(context: Context) {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context.startActivity(intent)
    }

    /**
     * * 清除本应用所有的数据 * *
     *
     * @param context
     * @param filepath
     */
    fun cleanApplicationData(context: Context) {
        cleanInternalCache(context)
        cleanExternalCache(context)
        cleanDatabases(context)
        cleanSharedPreference(context)
        cleanFiles(context)
    }

    /**
     * * 清除/data/data/com.xxx.xxx/files下的内容 * *
     *
     * @param context
     */
    fun cleanFiles(context: Context) {
        deleteAllFiles(context.filesDir)
    }

    /**
     * * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * *
     *
     * @param context
     */
    fun cleanDatabases(context: Context) {
        deleteAllFiles(File("/data/data/" + context.packageName + "/databases"))
    }

    /**
     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) *
     *
     * @param context
     */
    fun cleanSharedPreference(context: Context) {
        deleteAllFiles(File("/data/data/" + context.packageName + "/shared_prefs"))
    }

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     *
     * @param context
     */
    fun cleanExternalCache(context: Context) {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            deleteAllFiles(context.externalCacheDir)
        }
    }

    /**
     * * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * *
     *
     * @param context
     */
    fun cleanInternalCache(context: Context) {
        deleteAllFiles(context.cacheDir)
    }

    fun deleteAllFiles(root: File?) {
        val files = root?.listFiles()
        if (files != null)
            for (f in files) {
                if (f.isDirectory) { // 判断是否为文件夹
                    deleteAllFiles(f)
                    try {
                        f.delete()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f)
                        try {
                            f.delete()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }
                }
            }
    }

    /**
     * 是否可以调用wps打开
     */
    fun needWps(fileName: String): Boolean {
        return when (fileName.substring(fileName.lastIndexOf(".")).uppercase()) {
            ".DOC", ".TXT", ".DOCX", ".WPS", ".XLS", ".XLSX", ".PPT", ".PPTX" -> {
                true
            }
            ".PDF" -> {
                true
            }
            else -> {
                false
            }
        }
    }

    fun installApk(context: Context, apkFile: File) {

        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = Intent.ACTION_VIEW
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            FileProvider.getUriForFile(context, context.packageName + ".app.fileProvider", apkFile)
        } else {
            Uri.fromFile(apkFile)
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        context.startActivity(intent)
    }

    fun callPhone(context: Context, phoneNum: String) {
        if (isChinaPhone(phoneNum) || isTelephone(phoneNum)) {
            val intent = Intent(Intent.ACTION_DIAL)
            val data = Uri.parse("tel:$phoneNum")
            intent.data = data
            context.startActivity(intent)
        } else {
            showMsg(context,"号码格式错误！")
        }
    }

    private fun isTelephone(str: String): Boolean {
        val regex = "^0(10|2[0-5789]-|\\d{3})-?\\d{7,8}$"
        return Pattern.matches(regex, str)
    }

    @Throws(PatternSyntaxException::class)
    fun isChinaPhone(str: String): Boolean {
        val regExp = "^((13[0-9])|(15[^4])|(166)|(17[0-8])|(18[0-9])|(19[8-9])|(147,145))\\d{8}$"
        val p = Pattern.compile(regExp)
        val m = p.matcher(str)
        return m.matches()
    }

    /**
     * 检测是否有emoji表情 * @param source * @return
     */
    fun containsEmoji(source: String): Boolean {                          //两种方法限制emoji
        val len = source.length
        for (i in 0 until len) {
            val codePoint = source[i]
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true
            }
        }
        return false
    }

    /**
     * 判断是否是Emoji * @param codePoint 比较的单个字符 * @return
     */
    private fun isEmojiCharacter(codePoint: Char): Boolean {
        return (codePoint.code == 0x0 || codePoint.code == 0x9 || codePoint.code == 0xA || codePoint.code == 0xD
                || codePoint.code in 0x20..0xD7FF
                || codePoint.code in 0xE000..0xFFFD
                || codePoint.code in 0x10000..0x10FFFF)
    }

    /**
     * 控制版本隐藏内容
     * */
    fun isShowNewWrok(): Boolean {
        return true
    }

    fun openFile(context: Context, file: File) {
        try {
            val uri = if (Build.VERSION.SDK_INT >= 24) {
                FileProvider.getUriForFile(
                    context,
                    context.packageName + ".openfile.fileProvider",
                    file
                )
            } else {
                Uri.fromFile(file)
            }
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setDataAndType(uri, getMIMEType(file))
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 可选附件类型
     */
    val getselFileType = arrayOf(
        ".txt",
        ".pdf",
        ".zip",
        ".rar",
        ".wma",
        ".wav",
        ".rmvb",
        ".mp3",
        ".gif",
        ".3gp",
        ".png",
        ".jpg",
        ".doc",
        ".doc",
        ".wps",
        ".xls",
        ".xlsx",
        ".ppt",
        ".pptx"
    )

    private val MIME_MapTable = arrayOf(
        // {后缀名，MIME类型}
        arrayOf(".3gp", "video/3gpp"),
        arrayOf(".apk", "application/vnd.android.package-archive"),
        arrayOf(".asf", "video/x-ms-asf"),
        arrayOf(".avi", "video/x-msvideo"),
        arrayOf(".bin", "application/octet-stream"),
        arrayOf(".bmp", "image/bmp"),
        arrayOf(".c", "text/plain"),
        arrayOf(".class", "application/octet-stream"),
        arrayOf(".conf", "text/plain"),
        arrayOf(".cpp", "text/plain"),
        arrayOf(".doc", "application/msword"),
        arrayOf(".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
        arrayOf(".xls", "application/vnd.ms-excel"),
        arrayOf(".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
        arrayOf(".exe", "application/octet-stream"),
        arrayOf(".gif", "image/gif"),
        arrayOf(".gtar", "application/x-gtar"),
        arrayOf(".gz", "application/x-gzip"),
        arrayOf(".h", "text/plain"),
        arrayOf(".htm", "text/html"),
        arrayOf(".html", "text/html"),
        arrayOf(".jar", "application/java-archive"),
        arrayOf(".java", "text/plain"),
        arrayOf(".jpeg", "image/jpeg"),
        arrayOf(".jpg", "image/jpeg"),
        arrayOf(".js", "application/x-javascript"),
        arrayOf(".log", "text/plain"),
        arrayOf(".m3u", "audio/x-mpegurl"),
        arrayOf(".m4a", "audio/mp4a-latm"),
        arrayOf(".m4b", "audio/mp4a-latm"),
        arrayOf(".m4p", "audio/mp4a-latm"),
        arrayOf(".m4u", "video/vnd.mpegurl"),
        arrayOf(".m4v", "video/x-m4v"),
        arrayOf(".mov", "video/quicktime"),
        arrayOf(".mp2", "audio/x-mpeg"),
        arrayOf(".mp3", "audio/x-mpeg"),
        arrayOf(".mp4", "video/mp4"),
        arrayOf(".mpc", "application/vnd.mpohun.certificate"),
        arrayOf(".mpe", "video/mpeg"),
        arrayOf(".mpeg", "video/mpeg"),
        arrayOf(".mpg", "video/mpeg"),
        arrayOf(".mpg4", "video/mp4"),
        arrayOf(".mpga", "audio/mpeg"),
        arrayOf(".msg", "application/vnd.ms-outlook"),
        arrayOf(".ogg", "audio/ogg"),
        arrayOf(".pdf", "application/pdf"),
        arrayOf(".png", "image/png"),
        arrayOf(".pps", "application/vnd.ms-powerpoint"),
        arrayOf(".ppt", "application/vnd.ms-powerpoint"),
        arrayOf(
            ".pptx",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation"
        ),
        arrayOf(".prop", "text/plain"),
        arrayOf(".rc", "text/plain"),
        arrayOf(".rmvb", "audio/x-pn-realaudio"),
        arrayOf(".rtf", "application/rtf"),
        arrayOf(".show_pop", "text/plain"),
        arrayOf(".tar", "application/x-tar"),
        arrayOf(".tgz", "application/x-compressed"),
        arrayOf(".txt", "text/plain"),
        arrayOf(".wav", "audio/x-wav"),
        arrayOf(".wma", "audio/x-ms-wma"),
        arrayOf(".wmv", "audio/x-ms-wmv"),
        arrayOf(".wps", "application/vnd.ms-works"),
        arrayOf(".xml", "text/plain"),
        arrayOf(".z", "application/x-compress"),
        arrayOf(".zip", "application/x-zip-compressed"),
        arrayOf(".rar", "application/x-zip-compressed"),
        arrayOf("", "*/*")
    )

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    private fun getMIMEType(file: File): String {
        var type = "*/*"
        val fName = file.name
        // 获取后缀名前的分隔符"."在fName中的位置。
        val dotIndex = fName.lastIndexOf(".")
        if (dotIndex < 0) {
            return type
        }
        /* 获取文件的后缀名 */
        val end = fName.substring(dotIndex, fName.length).lowercase(Locale.getDefault())
        if (end == "") return type
        // 在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (i in MIME_MapTable.indices) { // MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if (end == MIME_MapTable[i][0]) type = MIME_MapTable[i][1]
        }
        return type
    }

    /**
     * @param @param context
     * @param @param msg 设定文件
     * @return void 返回类型
     * @throws
     * @Description: 吐司
     * @Title: showMsg
     * @author OP
     * @date 2014年11月11日 上午8:47:01
     */
    fun showMsg(context: Context,msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    /**
     * @param @param context
     * @param @param msg 设定文件
     * @return void 返回类型
     * @throws
     * @Description: 吐司
     * @Title: showMsg
     * @author OP
     * @date 2014年11月11日 上午8:47:01
     */
    fun showMsg(context: Context,id: Int) {
        Toast.makeText(context, context.getString(id), Toast.LENGTH_SHORT)
            .show()
    }


    /***
     * 判断email格式是否正确
     *
     * @param email
     * @return
     */
    fun isEmail(email: String): Boolean {
        val str =
            "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"
        val p = Pattern.compile(str)
        val m = p.matcher(email)

        return m.matches()
    }

    /***
     * 判断11位的手机号码是否正确
     *
     * @param mobiles
     * @return
     */
    fun isMobileNO(mobiles: String): Boolean {
        val p = Pattern.compile("[1][3456789]\\d{9}")
        val m = p.matcher(mobiles)
        return m.matches()
    }

    /***
     * 判断是否全是数字
     *
     * @param number
     * @return
     */
    fun isNumeric(number: String): Boolean {
        val pattern = Pattern.compile("[0-9]*")
        val isNum = pattern.matcher(number)
        return isNum.matches()
    }

    /***
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    fun isConnectInternet(context: Context): Boolean {
        return context.isNetWorkAvailable
    }

    /**
     * @Author:         kanghanbin
     * @Description:    NetworkUtil判断网络状态是否可用
     * @CreateDate:     2020/4/27 14:16
     */
    val Context.isNetWorkAvailable: Boolean
        get() {
            val connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val result: Boolean
            result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork
                val networkCapabilities: NetworkCapabilities? =
                    connectivityManager.getNetworkCapabilities(network)
                networkCapabilities != null
            } else {
                val networkInfo = connectivityManager.activeNetworkInfo
                networkInfo != null && networkInfo.isAvailable
            }
            return result

        }
    /***
     * 判断sdk是否存在
     *
     * @return
     */
    fun isSDK(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /****
     * 判断当前是上午还是下午
     *
     * @param context
     * @return
     */
    fun nowStatus(context: Context): String {
        val now: String
        val cv = context.contentResolver
        val strTimeFormat = android.provider.Settings.System.getString(
            cv,
            android.provider.Settings.System.TIME_12_24
        )
        if (strTimeFormat != null && strTimeFormat == "24") {
            val sdf = SimpleDateFormat("HH")
            val hour = sdf.format(Date())
            val h = Integer.parseInt(hour)
            now = if (h >= 12) {
                "下午"
            } else {
                "上午"
            }
            return now
        } else {
            // String amPmValues
            val c = Calendar.getInstance()
            now = if (c.get(Calendar.AM_PM) == 0) {
                "上午"
            } else {
                "下午"
            }
            return now
        }
    }
}