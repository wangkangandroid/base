package www.ahest.cn.base.base

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import www.ahest.cn.base.R
import www.ahest.cn.base.bean.DataErrException
import www.ahest.cn.base.impl.PermissionListener
import www.ahest.cn.base.utils.StringBaseUtils
import www.ahest.cn.base.utils.ToolsBaseUtils
import io.reactivex.rxjava3.disposables.CompositeDisposable

/**
 * Created by Administrator on 2018/2/5 0005.
 */
abstract class BaseActivity : AppCompatActivity(), PermissionListener {

    val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected open fun init() {
    }

    protected open fun initView() {
    }

    protected open fun initData() {

    }

    protected open fun initListener() {
    }

    protected open fun showErr(it: Throwable) {
        it.printStackTrace()
        if (it is DataErrException) {
            ToolsBaseUtils.showMsg(this,StringBaseUtils.removeAnyTypeStr(it.myMessage))
        } else {
            ToolsBaseUtils.showMsg(this,R.string.err_data)
        }
    }

    private var isResumed = false

    override fun onResume() {
        super.onResume()
        isResumed = true
    }

    override fun onPause() {
        super.onPause()
        isResumed = false
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    private val requestPermissionCode = 101
    protected open fun requestRunTimePermission(permissions: MutableList<String>) {
        //用于存放为授权的权限
        val permissionList = mutableListOf<String>()
        //遍历传递过来的权限集合
        for (permission in permissions) {
            //判断是否已经授权
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                //未授权，则加入待授权的权限集合中
                permissionList.add(permission)
            }
        }
        //判断集合
        if (permissionList.isNotEmpty()) {  //如果集合不为空，则需要去授权
            ActivityCompat.requestPermissions(
                this,
                permissionList.toTypedArray(),
                requestPermissionCode
            )
        } else {  //为空，则已经全部授权
            onGranted()
        }
    }

    //授权成功
    override fun onGranted() {
    }

    //授权部分
    override fun onGranted(grantedPermission: MutableList<String>) {
        ToolsBaseUtils.openAppDetails(this)
    }

    //拒绝授权
    override fun onDenied(deniedPermission: MutableList<String>) {
        ToolsBaseUtils.openAppDetails(this)
    }

    //授权失败
    override fun onDenied() {
        ToolsBaseUtils.openAppDetails(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            requestPermissionCode -> {
                if (grantResults.isNotEmpty()) {
                    //被用户拒绝的权限集合
                    val deniedPermissions = mutableListOf<String>()
                    //用户通过的权限集合
                    val grantedPermissions = mutableListOf<String>()
                    for (i in grantResults.indices) {
                        //获取授权结果，这是一个int类型的值
                        val grantResult = grantResults[i]

                        if (grantResult != PackageManager.PERMISSION_GRANTED) { //用户拒绝授权的权限
                            deniedPermissions.add(permissions[i])
                        } else {  //用户同意的权限
                            grantedPermissions.add(permissions[i])
                        }
                    }

                    if (deniedPermissions.isNullOrEmpty()) {  //用户拒绝权限为空
                        onGranted()
                    } else {  //不为空
                        //回调授权成功的接口
                        onDenied(deniedPermissions)
                        //回调授权失败的接口
                        onGranted(grantedPermissions)
                        onDenied()
                    }
                }
            }
        }
    }
}