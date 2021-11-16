package www.ahest.cn.base.bean

/**
 * Created by Administrator on 2018/3/8 0008.
 */
class DataErrException : Exception() {
    var myMessage: String? = null
    var errCode = 0


    /**
     * 约定异常
     */
    object ERROR {
        /**
         * 未知错误
         */
        const val UNKNOWN = 1000

        /**
         * 解析错误
         */
        const val PARSE_ERROR = UNKNOWN + 1

        /**
         * 网络错误
         */
        const val NETWORD_ERROR = PARSE_ERROR + 1

        /**
         * 协议出错
         */
        const val HTTP_ERROR = NETWORD_ERROR + 1

        /**
         * 证书出错
         */
        const val SSL_ERROR = HTTP_ERROR + 1

        /**
         * 连接超时
         */
        const val TIMEOUT_ERROR = SSL_ERROR + 1

        /**
         * 调用错误
         */
        const val INVOKE_ERROR = TIMEOUT_ERROR + 1

        /**
         * 类转换错误
         */
        const val CAST_ERROR = INVOKE_ERROR + 1

        /**
         * 请求取消
         */
        const val REQUEST_CANCEL = CAST_ERROR + 1

        /**
         * 未知主机错误
         */
        const val UNKNOWNHOST_ERROR = REQUEST_CANCEL + 1

        /**
         * 空指针错误
         */
        const val NULLPOINTER_EXCEPTION = UNKNOWNHOST_ERROR + 1
    }
}