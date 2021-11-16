package www.ahest.cn.base.impl

interface PermissionListener {
    //授权成功
    fun onGranted()

    //授权部分
    fun onGranted(grantedPermission: MutableList<String>)

    //拒绝授权
    fun onDenied(deniedPermission: MutableList<String>)

    //授权失败
    fun onDenied()
}