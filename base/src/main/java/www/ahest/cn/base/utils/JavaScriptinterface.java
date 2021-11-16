package www.ahest.cn.base.utils;

import android.content.Context;
import android.webkit.JavascriptInterface;

public class JavaScriptinterface {

    Context context;

    public JavaScriptinterface(Context c) {
        context = c;
    }

    /**
     * 与js交互时用到的方法，在js里直接调用的
     */
    @JavascriptInterface
    public void showToast(String ssss) {

    }
}
