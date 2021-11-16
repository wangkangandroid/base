package www.ahest.cn.base.base

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.disposables.CompositeDisposable
import www.ahest.cn.base.R
import www.ahest.cn.base.bean.DataErrException
import www.ahest.cn.base.impl.PermissionListener
import www.ahest.cn.base.utils.StringBaseUtils
import www.ahest.cn.base.utils.ToolsBaseUtils

/**
 * Created by Administrator on 2018/2/6 0006.
 */
abstract class BaseFragment : Fragment(), PermissionListener {

    val disposables = CompositeDisposable()

    protected open fun showErr(it: Throwable) {
        it.printStackTrace()
        if (it is DataErrException) {
            ToolsBaseUtils.showMsg(context!!, StringBaseUtils.removeAnyTypeStr(it.myMessage))
        } else {
            ToolsBaseUtils.showMsg(context!!, R.string.err_data)
        }
    }


    protected open fun init() {
    }

    protected open fun initView() {
    }

    protected open fun initData() {

    }

    protected open fun initListener() {
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    /**
     * 当前界面是否呈现给用户的状态标志
     */
    protected var isVisibleVieww: Boolean = false

    /**
     * 重写Fragment父类生命周期方法，在onCreate之前调用该方法，实现Fragment数据的缓加载.
     * @param isVisibleToUser 当前是否已将界面显示给用户的状态
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            isVisibleVieww = true
            onVisible()
        } else {
            isVisibleVieww = false
            onInvisible()
        }
    }

    /**
     * 当界面呈现给用户，即设置可见时执行，进行加载数据的方法
     * 在用户可见时加载数据，而不在用户不可见的时候加载数据，是为了防止控件对象出现空指针异常
     */
    protected open fun onVisible() {
        setlazyLoad()
    }

    /**
     * 当界面还没呈现给用户，即设置不可见时执行
     */
    protected open fun onInvisible() {}

    /**
     * 加载数据方法
     */
    protected open fun setlazyLoad() {}

    private val requestPermissionCode = 101
    protected open fun requestRunTimePermission(permissions: MutableList<String>) {
        //用于存放为授权的权限
        val permissionList = mutableListOf<String>()
        //遍历传递过来的权限集合
        for (permission in permissions) {
            //判断是否已经授权
            if (ContextCompat.checkSelfPermission(context!!, permission) != PackageManager.PERMISSION_GRANTED) {
                //未授权，则加入待授权的权限集合中
                permissionList.add(permission)
            }
        }
        //判断集合
        if (permissionList.isNotEmpty()) {  //如果集合不为空，则需要去授权
            ActivityCompat.requestPermissions(activity!!, permissionList.toTypedArray(), requestPermissionCode)
        } else {  //为空，则已经全部授权
            onGranted()
        }
    }

    //授权成功
    override fun onGranted() {
    }

    //授权部分
    override fun onGranted(grantedPermission: MutableList<String>) {
        ToolsBaseUtils.openAppDetails(context!!)
    }

    //拒绝授权
    override fun onDenied(deniedPermission: MutableList<String>) {
        ToolsBaseUtils.openAppDetails(context!!)
    }

    //授权失败
    override fun onDenied() {
        ToolsBaseUtils.openAppDetails(context!!)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
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