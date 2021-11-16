package www.ahest.cn.base

import android.os.Bundle
import www.ahest.cn.base.base.BaseActivity

class BaseMainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_main)
    }
}