package www.ahest.cn.base.view

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import www.ahest.cn.base.R

/**
 * Created by Administrator on 2018/3/5 0005.
 */
class MyLoadingView(context: Context) : AlertDialog(context, R.style.loadingDialogStyle) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading)
        this.setCanceledOnTouchOutside(false)
    }
}