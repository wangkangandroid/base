package www.ahest.cn.base.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.kinggrid.commonrequestauthority.AppRegister;
import com.kinggrid.iappoffice.Constant;
import com.kinggrid.iappoffice.file.FileUtil;
import com.kinggrid.iappofficeserver.iAppOfficeServer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.wps.moffice.client.ActionType;
import cn.wps.moffice.client.ViewType;
import cn.wps.moffice.demo.util.SettingPreference;
import cn.wps.moffice.demo.util.Util;
import cn.wps.moffice.service.OfficeService;
import cn.wps.moffice.service.OfficeService.Stub;
import cn.wps.moffice.service.Variant;
import cn.wps.moffice.service.base.print.PrintProgress;
import cn.wps.moffice.service.doc.Document;
import cn.wps.moffice.service.doc.MsoTriState;
import cn.wps.moffice.service.doc.PictureFormat;
import cn.wps.moffice.service.doc.SaveFormat;
import cn.wps.moffice.service.doc.WdInformation;
import cn.wps.moffice.service.doc.WdProtectionType;
import cn.wps.moffice.service.doc.WrapType;
import cn.wps.moffice.service.pdf.PDFReader;
import cn.wps.moffice.service.presentation.Presentation;
import cn.wps.moffice.service.spreadsheet.Workbook;
import cn.wps.moffice.service.spreadsheet.Workbooks;
import cn.wps.moffice.service.spreadsheet.Worksheet;

public class WPSUtils implements Constant {
    public static boolean DEBUG = true;
    private static String tag = "iappoffice";
    private OfficeService officeService;
    private Intent editservice;
    private Intent hideService;
    private static Document document;
    private static Workbook workBook;
    private static Presentation presentation;
    private static PDFReader pdfReader = null;
    private static int initBool = 0;
    private static int initService = 0;
    private Activity activity;
    private static Context context;
    private static Context signContext;
    public static Context dialogContext;
    private static String webUrl;
    private static String fileName;
    private static String fileType = ".doc";
    private static String userName;
    private static String setText;
    private static URL url;
    private static String filepath;
    private static int filesize;
    protected SaveReceiver saveRec = null;
    private static String directory = Environment.getExternalStorageDirectory().getPath().toString() + "/localfiles/";
    private static String imgDir;
    private static String pgfFilePath;
    private static Boolean localfile = false;
    private static int netTransMode = 0;
    private static String resultValue = "";
    public static String copyRight = "";
    private static boolean isDisplay = true;
    private static String recordId = "";
    private static String charset = "GBK";
    private static FileUtil fileUtil;
    private static boolean isSaved = false;
    private static boolean isSavedAs = false;
    private static boolean isGif = false;
    private static boolean isPGF = false;
    private boolean isUsed2015 = false;
    private String parmsFor2015;
    private static int currentPage;
    private static Bitmap bmp;
    private static boolean isBind = false;
    private static ProgressDialog progressDialog;
    private static String progressInfo;
    private boolean isReviseMode = false;
    private boolean isReadOnly = false;
    private boolean isClearTrace = false;
    private boolean isClearFile = false;
    private boolean isDocShowView = true;
    private boolean isEditMode = false;
    private boolean isShowReviewingPaneRightDefault = true;
    private boolean isScreenshotForbid = false;
    private boolean isDefaultUseFinger = false;
    private boolean isForbiddenInk = false;
    private boolean isNeedUnProtect = false;
    private boolean isNeedProtect = false;
    private String unProtectSecret = "";
    private String protectSecret = "";
    private WdProtectionType protectType;
    private String fileProviderAuthor = "";
    private String serialNumber = "";
    private static int editType = 0;
    private static ArrayList<ActionType> menuHiddenList = new ArrayList();
    private static ArrayList<ViewType> viewHiddenList = new ArrayList();
    private static String result;
    private static boolean hasRegistered = false;
    private static String appPackageName = "com.seeyon.mobile.android";
    private static SettingPreference settingPreference;
    protected static String companyName;
    private static int typeFlag = 0;
    private String moduleType = "";
    public static boolean uploadResult = false;
    private static boolean useWPSPer = false;
    private boolean useMethod2 = false;
    private String registUrl = "";
    private static iAppOfficeServer officeServer;
    public static boolean usedFileProvider = false;
    private boolean kinggrid_pro = false;
    private boolean kingsoft_pro = false;
    private ServiceConnection mOfficeConn = new MyServiceConnection();
    private boolean useWPSPer_read = false;
    private boolean isSaveAsPDF = false;
    private String waterMaskText = "";
    private int waterMaskColor = 0;
    public static String agentMessageServiceAction = "cn.wps.moffice.agent.OfficeServiceAgent2";
    public static String mOfficeClientServiceAction = "cn.wps.moffice.client.OfficeServiceClient2";
    private static boolean documentNotLanding = false;
    private static byte[] saveFileDate = null;
    private static byte[] fileDate = null;
    private Thread webThread;
    List<String> fieldList = new ArrayList();
    List<String> valueList = new ArrayList();
    static boolean isCancelled = false;
    private Thread uploadThread;
    private boolean isClose = false;
    private boolean isUploading = false;
    private static final Handler myHandler = new Handler() {
        @SuppressLint({"NewApi"})
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case -1:
                    toastShow("产品未注册！");
                    break;
                case 0:
                    if (msg.arg1 == 0) {
                        fileUtil.setIsPageInfoDecoded(true);
                        toastShow("加载页面截图失败");
                    } else {
                        fileUtil.setIsPageInfoDecoded(false);
                    }
                    break;
                case 1:
                    Log.d(tag, "progressDialog show");
                    if (progressDialog != null && progressDialog.isShowing()) {
                        return;
                    }

                    progressDialog = new ProgressDialog(context);
                    progressDialog.setTitle("提示");
                    progressDialog.setMessage(progressInfo);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setButton(-1, "取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.e("zxg", "isCancelled onclick");
                            isCancelled = true;
                            iAppOfficeServer.isCancellLoadFile = true;
                            progressDialog.dismiss();
                        }
                    });
                    progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        public void onCancel(DialogInterface dialog) {
                            Log.e("zxg", "isCancelled oncancell");
                            isCancelled = true;
                            iAppOfficeServer.isCancellLoadFile = true;
                        }
                    });
                    progressDialog.show();
                    break;
                case 2:
                    Log.d(tag, "progressDialog gone, result = " + result);
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    break;
                case 3:
                    Log.d(tag, "signature Dialog show");
                case 4:
                case 5:
                case 9:
                case 12:
                default:
                    break;
                case 6:
                    toastShow("文档处于保护状态，请先解保护！");
                    break;
                case 7:
                    toastShow("验证成功！");
                    break;
                case 8:
                    toastShow("验证失败！");
                    break;
                case 10:
                    toastShow("WPS未安装！");
                    break;
                case 11:
                    toastShow("WPS启动失败，请检查WPS版本！");
                    break;
                case 13:
                    toastShow("打开失败，文件不存在！");
            }

            super.handleMessage(msg);
        }
    };
    public static boolean isShowToast = true;
    private static int toastGravity = 17;
    private static final String[] IMAGE_FORMAT = new String[]{"PNG", "JPEG", "24 位位图", "24 色灰度图", "256色灰度图", "单色位图"};
    private final Map<String, PictureFormat> saveImgTypeList = new HashMap<String, PictureFormat>() {
        private static final long serialVersionUID = 1L;

        {
            this.put("PNG", PictureFormat.PNG);
            this.put("JPEG", PictureFormat.JPEG);
            this.put("24 位位图", PictureFormat.BMP24);
            this.put("24 色灰度图", PictureFormat.BMP24GRAY);
            this.put("256色灰度图", PictureFormat.BMP8GRAY);
            this.put("单色位图", PictureFormat.BMP1);
        }
    };
    public OnDownLoadStateListener mOnDownLoadStateListener;
    public OnUpLoadStateListener mOnUpLoadStateListener;
    public OnRegisterResultListener mOnRegisterResultListener;
    public OnOpenResultListener mOnOpenResultListener;

    public WPSUtils(Activity activity) {
        context = activity;
        this.activity = activity;
    }

    public static String getiAppOfficeVersion() {
        return "V3.2.0.286_20210910";
    }

    public static String getSupportWPSVersion() {
        return "moffice_13.6_default_ProCn00731_multidex_52aedd80f6";
    }

    public void unInit() {
        Log.v(tag, "unInit()");
        fileUtil = null;
        document = null;
        if (initService == 1 && context != null) {
            if (!useWPSPer) {
                try {
                    context.unbindService(this.mOfficeConn);
                    if (this.editservice != null) {
                        context.stopService(this.editservice);
                    }
                } catch (Exception var2) {
                    var2.printStackTrace();
                }
            }

            initService = 0;
        }

        isBind = false;
        if (this.saveRec != null) {
            Log.e(tag, "call unregisterReceiver");

            try {
                context.unregisterReceiver(this.saveRec);
                this.saveRec = null;
            } catch (IllegalArgumentException var3) {
                if (!var3.getMessage().contains("Receiver not registered")) {
                    throw var3;
                }
            }
        }

        this.officeService = null;
        hasRegistered = false;
    }

    public void init(OnRegisterResultListener l) {
        this.mOnRegisterResultListener = l;
        this.init();
    }

    public int init() {
        hasRegistered = true;
//        if ("".equals(copyRight)) {
//            this.useWPSPer_read = true;
//        } else {
//            this.initAuthority();
//        }
        initBool = 1;
        this.initFileDir();
        return 1;
    }

    private void initRegisterReceiver() {
        if (this.saveRec == null) {
            IntentFilter backFilter = new IntentFilter();
            backFilter.addAction("com.kingsoft.back.key.down");
            IntentFilter homeFilter = new IntentFilter();
            homeFilter.addAction("com.kingsoft.home.key.down");
            IntentFilter saveFilter = new IntentFilter();
            saveFilter.addAction("cn.wps.moffice.file.save");
            IntentFilter saveAsFilter = new IntentFilter();
            saveAsFilter.addAction("cn.wps.moffice.broadcast.AfterSaved");
            IntentFilter closeFilter = new IntentFilter();
            closeFilter.addAction("cn.wps.moffice.file.close");
            IntentFilter signFilter = new IntentFilter();
            signFilter.addAction("com.kinggrid.iappoffice.fullsignature.save");
            IntentFilter systemHomeFilter = new IntentFilter();
            systemHomeFilter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
            this.saveRec = new SaveReceiver((SaveReceiver)null);
            Log.e(tag, "registerReceiver");
            context.registerReceiver(this.saveRec, backFilter);
            context.registerReceiver(this.saveRec, homeFilter);
            context.registerReceiver(this.saveRec, saveFilter);
            context.registerReceiver(this.saveRec, saveAsFilter);
            context.registerReceiver(this.saveRec, closeFilter);
            context.registerReceiver(this.saveRec, signFilter);
            context.registerReceiver(this.saveRec, systemHomeFilter);
        }

    }

    private void initFileDir() {
        imgDir = directory + "sign/";
        pgfFilePath = imgDir + "test.pgf";
        File file1 = new File(directory);
        if (!file1.exists()) {
            file1.mkdirs();
        }

        File file = new File(imgDir);
        if (!file.isDirectory() && !file.exists()) {
            file.mkdirs();
        }

    }

    public void setRegisterUrl(String registerIp) {
        if (registerIp.contains("WebAppServer")) {
            this.registUrl = registerIp;
        } else {
            this.registUrl = "http://" + registerIp + "/WebAppServer/server.do";
        }

    }

    private void initAuthority() {
        String extsInfo = "用户名：" + userName;
        final AppRegister appRegister = new AppRegister(context, copyRight, "2", "", extsInfo, this.registUrl) {
            protected void onPostExecute(SparseArray<String> result) {
                super.onPostExecute(result);
                int register_result_key = result.keyAt(0);
                int SUCCESS_VALIDTIMEANDCOMPANY = 200;
                int SUCCESS_COMPANY = 201;
                int SUCCESS_UNLIMIT_TYPE = 202;
                int SUCCESS_OFFLINE_TYPE = 203;
                initBool = 1;
                if (mOnRegisterResultListener != null) {
                    mOnRegisterResultListener.registerSuccess();
                }
            }
        };
        appRegister.setDialogListener(new AppRegister.ProgressDialogListener() {
            public void onExitDialog() {
                Log.e(tag, "context:" + context);
                appRegister.closeDialog(activity);
                Log.e(tag, "onExitDialog content:" + context);
                hasRegistered = false;
                initBool = 0;
                closeDocument();
                unInitOfficeServer();
                if (mOnRegisterResultListener != null) {
                    mOnRegisterResultListener.registerFail();
                }

            }
        });
    }

    private boolean initOfficeService() {
        isDisplay = true;
        isSaved = false;
        if (useWPSPer) {
            this.editservice = new Intent("cn.wps.moffice.service.OfficeService");
        } else {
            String version = getVersion();
            String[] wpsversion = version.split("[.]");
            if (Integer.parseInt(wpsversion[0]) >= 10) {
                this.editservice = new Intent("cn.wps.moffice.service.OfficeService");
            } else {
                this.editservice = new Intent("cn.wps.moffice.service.ProOfficeService");
            }

            this.editservice.setPackage(getCurPackageName());
        }

        this.editservice.putExtra("DisplayView", true);
        return this.bindOfficeService(this.editservice);
    }

    public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
        if (resolveInfo != null && resolveInfo.size() == 1) {
            ResolveInfo serviceInfo = (ResolveInfo)resolveInfo.get(0);
            String packageName = serviceInfo.serviceInfo.packageName;
            String className = serviceInfo.serviceInfo.name;
            ComponentName component = new ComponentName(packageName, className);
            Intent explicitIntent = new Intent(implicitIntent);
            explicitIntent.setComponent(component);
            return explicitIntent;
        } else {
            return null;
        }
    }

    private boolean bindHideUIService() {
        if (isBind && context != null) {
            Log.d(tag, "savePic isBind----" + isBind);
            isBind = false;
            context.unbindService(this.mOfficeConn);
            context.stopService(this.editservice);
            this.officeService = null;
        }

        this.hideService = new Intent("cn.wps.moffice.service.ProOfficeService");
        this.hideService.putExtra("DisplayView", false);
        isDisplay = false;
        return this.bindOfficeService(this.hideService);
    }

    @SuppressLint("WrongConstant")
    private synchronized boolean bindOfficeService(Intent intent) {
        if (this.officeService != null) {
            context.unbindService(this.mOfficeConn);
        }

        try {
            Intent startAppIntent = new Intent();
            startAppIntent.setClassName(getCurPackageName(), "cn.wps.moffice.second_dev.StartAppActivity");
            startAppIntent.setAction("cn.wps.moffice.second_dev.StartApp");
            startAppIntent.addFlags(268435456);
            this.activity.startActivity(startAppIntent);
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        String version = getVersion();
        String[] wpsversion = version.split("[.]");
        if (Integer.parseInt(wpsversion[0]) > 9) {
            this.wakeWPSApp();
        }

        boolean bindOk = false;

        for(int i = 0; i < 5; ++i) {
            bindOk = context.bindService(intent, this.mOfficeConn, 1);
            if (bindOk) {
                break;
            }

            try {
                Thread.sleep(500L);
            } catch (InterruptedException var8) {
                var8.printStackTrace();
                break;
            }
        }

        if (!bindOk) {
            context.unbindService(this.mOfficeConn);
            return false;
        } else {
            return true;
        }
    }

    @SuppressLint("WrongConstant")
    private void wakeWPSApp() {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if ("cn.wps.moffice.service.startup".equals(intent.getAction())) {
                    synchronized(this) {
                        this.notifyAll();
                    }
                }

            }
        };
        context.registerReceiver(receiver, new IntentFilter("cn.wps.moffice.service.startup"));
        Intent wakeAppIntent = new Intent();
        wakeAppIntent.setClassName(getCurPackageName(), "cn.wps.moffice.service.MOfficeWakeActivity");
        wakeAppIntent.addFlags(268435456);

        try {
            context.startActivity(wakeAppIntent);
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        synchronized(this) {
            try {
                this.wait(500L);
            } catch (InterruptedException var5) {
                var5.printStackTrace();
            }
        }

        context.unregisterReceiver(receiver);
    }

    private void saveToPic() {
        try {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putBoolean("SendSaveBroad", true);
            bundle.putBoolean("SendCloseBroad", true);
            if (typeFlag != 1 && typeFlag != 2) {
                bundle.putBoolean("DisplayView", false);
            } else {
                bundle.putBoolean("DisplayView", true);
            }

            intent.putExtras(bundle);
            if (this.isReadOnly) {
                intent.putExtra("OpenMode", "ReadOnly");
            }

            Log.d(tag, "saveToPic officeService=" + this.officeService);

            while(this.officeService == null) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException var9) {
                    var9.printStackTrace();
                }
            }

            if (typeFlag == 1) {
                Workbooks workbooks = this.officeService.getWorkbooks();
                workBook = workbooks.openBookEx(directory + fileName, "", intent);
                Worksheet workSheet = workBook.getActiveSheet();
                workSheet.saveToImage(imgDir, (PictureFormat)this.saveImgTypeList.get(IMAGE_FORMAT[0]), 100, 1.0F);
                Log.d(tag, "save excel imgs");
            } else if (typeFlag == 2) {
                presentation = this.officeService.openPresentation(directory + fileName, "", intent);
                List<String> pathResult = presentation.exportImage(imgDir, (PictureFormat)this.saveImgTypeList.get(IMAGE_FORMAT[0]), new PrintProgress() {
                    public IBinder asBinder() {
                        return null;
                    }

                    public boolean isCanceled() throws RemoteException {
                        return false;
                    }

                    public void exportProgress(int progress) throws RemoteException {
                    }
                });
                StringBuffer buffer = new StringBuffer();
                int i = 0;

                for(int size = pathResult.size(); i < size; ++i) {
                    buffer.append((String)pathResult.get(i));
                    buffer.append('\n');
                }

                Log.d(tag, "save ppt imgs");
            } else {
                document = this.officeService.openWordDocument(directory + fileName, "", intent);
                Log.d(tag, "save doc imgs==" + document);
                PictureFormat picFormat = PictureFormat.PNG;
                int pages = document.getPageCount();
                Log.d(tag, "saveToPic pages==" + pages);
                float w = 794.0F;
                float h = 1123.0F;

                for(int i = 1; i <= pages; ++i) {
                    Log.d(tag, "saveToPic doc.getName==" + document.getName());
                    String pagePath = imgDir + i + ".png";
                    if (!isGif) {
                        pagePath = imgDir + i + ".gif";
                    }

                    document.getPage(i - 1).saveToImage(pagePath, picFormat, 100, w, h, 72, 1);
                }

                if (pages == 0) {
                    isDisplay = true;
                } else {
                    isDisplay = false;
                    document.close();
                }
            }

            context.unbindService(this.mOfficeConn);
            this.officeService = null;
            initService = 0;
            isBind = false;
            document = null;
        } catch (Exception var10) {
            var10.printStackTrace();
            document = null;
            workBook = null;
            presentation = null;
        }

    }

    @SuppressLint("WrongConstant")
    private boolean startWPSPer(String fileName) {
        this.isClose = false;
        Intent intent = new Intent();
        intent.addFlags(268435456);
        intent.setAction("android.intent.action.VIEW");
        intent.setClassName(this.getWPSPrePageName(), "cn.wps.moffice.documentmanager.PreStartActivity2");
        intent.putExtra("UserName", userName);
        if (this.isReadOnly) {
            intent.putExtra("OpenMode", "ReadOnly");
        } else if (this.isEditMode) {
            intent.putExtra("OpenMode", "EditMode");
        } else {
            intent.putExtra("OpenMode", "Normal");
        }

        if (this.isReviseMode) {
            intent.putExtra("ShowReviewingPaneRightDefault", true);
        }

        intent.getExtras();
        File file = new File(fileName);
        if (!file.exists()) {
            sendMsgToHandler(13);
            return false;
        } else {
            Uri uri = null;
            if (VERSION.SDK_INT >= 24) {
                if ("".equals(this.fileProviderAuthor)) {
                    this.fileProviderAuthor = context.getPackageName() + ".fileProvider";
                }

                uri = FileProvider.getUriForFile(context, this.fileProviderAuthor, file);
                intent.addFlags(1);
                intent.addFlags(2);
                intent.setDataAndType(uri, "application/*");
            } else {
                uri = Uri.fromFile(file);
                intent.setData(uri);
            }

            try {
                context.startActivity(intent);
                return true;
            } catch (ActivityNotFoundException var6) {
                var6.printStackTrace();
                return false;
            }
        }
    }

    @SuppressLint("WrongConstant")
    private boolean startWpsOffice2(String filepath) throws Exception {
        if (DEBUG) {
            Log.v(tag, "use startWpsOffice2 start wps!");
        }

        this.isClose = false;
        Intent intent = new Intent();
        Bundle bundle = this.getWpsStartBundle();
        intent.addFlags(268435456);
        intent.setAction("android.intent.action.VIEW");
        if (this.kingsoft_pro) {
            intent.setClassName("com.kingsoft.moffice_pro", "cn.wps.moffice.documentmanager.PreStartActivity2");
        } else {
            intent.setClassName("com.kingsoft.moffice_kinggrid", "cn.wps.moffice.documentmanager.PreStartActivity2");
        }

        File file = new File(filepath);
        if (file != null && file.exists()) {
            Uri uri = Uri.fromFile(file);
            if (VERSION.SDK_INT >= 24) {
                intent.setData((Uri)null);
                bundle.putString("FILEPATH", filepath);
            } else {
                intent.setData(uri);
            }

            intent.putExtras(bundle);
            this.activity.startActivity(intent);
            if (this.saveRec == null) {
                this.initRegisterReceiver();
            }

            return true;
        } else {
            return false;
        }
    }

    private Bundle getWpsStartBundle() {
        Bundle bundle = new Bundle();
        if (this.isReadOnly) {
            bundle.putString("OpenMode", "ReadOnly");
        } else if (this.isEditMode) {
            bundle.putString("OpenMode", "EditMode");
        } else {
            bundle.putString("OpenMode", "Normal");
        }

        bundle.putBoolean("isScreenshotForbid", this.isScreenshotForbid);
        bundle.putBoolean("SendCloseBroad", true);
        bundle.putBoolean("SendSaveBroad", true);
        bundle.putString("ThirdPackage", context.getPackageName());
        bundle.putBoolean("ClearTrace", this.isClearTrace);
        bundle.putBoolean("ClearFile", this.isClearFile);
        bundle.putBoolean("EnterReviseMode", this.isReviseMode);
        bundle.putString("UserName", userName);
        if (!"".equals(this.waterMaskText)) {
            bundle.putString("WaterMaskText", this.waterMaskText);
        }

        if (!"".equals(this.waterMaskColor)) {
            bundle.putInt("WaterMaskColor", this.waterMaskColor);
        }

        if (!TextUtils.isEmpty(this.serialNumber)) {
            if (this.serialNumber.length() > 12) {
                bundle.putString("SerialNumberOtherPc", this.serialNumber);
            } else {
                bundle.putString("SerialNumberOther", this.serialNumber);
            }
        }

        bundle.putBoolean("ShowReviewingPaneRightDefault", this.isShowReviewingPaneRightDefault);
        return bundle;
    }

    private boolean startWpsOffice(String filepath) throws Exception {
        if (DEBUG) {
            Log.v(tag, "use startWpsOffice start wps!");
        }

        OpenFileByWPS openFile = new OpenFileByWPS(filepath);
        openFile.start();
        return document != null;
    }

    public void setSaveAsPDF(boolean saveAsPDF) {
        this.isSaveAsPDF = saveAsPDF;
    }

    public void setUseMethod2(boolean userMethod2) {
        this.useMethod2 = userMethod2;
    }

    public void setServiceAction(String agentAction, String clientAction) {
        agentMessageServiceAction = agentAction;
        mOfficeClientServiceAction = clientAction;
    }

    public void addWaterMask(String waterMaskText, int waterMaskColor) {
        this.waterMaskText = waterMaskText;
        this.waterMaskColor = waterMaskColor;
    }

    /** @deprecated */
    @Deprecated
    public void showReviewingPane(boolean isShow) {
    }

    public void showRevisionsAndComments(boolean isShow) {
        if (document != null) {
            try {
                document.getActiveWindow().getView().putShowRevisionsAndComments(isShow);
            } catch (Exception var3) {
                var3.printStackTrace();
            }
        } else {
            toastShow("文档没有打开！");
        }

    }

    public void appOpen(boolean localFlag) {
        Log.e(tag, "appOpen enter!");
        if (!this.isAvailable()) {
            if (this.mOnOpenResultListener != null) {
                this.mOnOpenResultListener.openFail();
            }

        } else {
            if (this.officeService != null && !useWPSPer) {
                context.unbindService(this.mOfficeConn);
                this.mOfficeConn = new MyServiceConnection();
                this.officeService = null;
            }

            localfile = localFlag;
            this.initFileFlags();
            if (localFlag) {
                if (!useWPSPer && this.officeService == null && !this.useMethod2) {
                    this.initOfficeService();
                    Log.v(tag, "officeService = null");
                }

                fileUtil = null;
                File file = new File(fileName);
                if (!documentNotLanding && !file.exists()) {
                    toastShow("文件不存在！");
                    return;
                }

                try {
                    if (useWPSPer) {
                        this.startWPSPer(fileName);
                    } else if (this.useMethod2) {
                        this.startWpsOffice2(fileName);
                    } else {
                        this.startWpsOffice(fileName);
                    }
                } catch (Exception var4) {
                    var4.printStackTrace();
                }
            } else {
                this.initFileUtil();
                officeServer = new iAppOfficeServer(webUrl, recordId, fileType, directory, userName, fileName);
                officeServer.setIsSaveFile(!documentNotLanding);
                this.downloadOpenFile();
            }

        }
    }

    private void initFileUtil() {
        fileUtil = new FileUtil(context);
        fileUtil.setDir(directory, imgDir);
        fileUtil.setIsPageInfoDecoded(true);
        this.setFileNameByFileType();
    }

    private boolean isAvailable() {
        if (!hasRegistered) {
            sendMsgToHandler(-1);
            return false;
        } else {
            if (this.useWPSPer_read) {
                useWPSPer = true;
                this.isReadOnly = true;
            }

            if (!this.isWPSInstalled() && !useWPSPer) {
                sendMsgToHandler(10);
                return false;
            } else {
                if (useWPSPer) {
                    if (!this.isWPSPerInstalled()) {
                        sendMsgToHandler(10);
                        return false;
                    }
                } else {
                    String version = getVersion();
                    String[] wpsversion = version.split("[.]");
                    if (DEBUG) {
                        Log.v(tag, "wps version:" + version);
                    }

                    int[] minversion = new int[]{9, 2, 9};

                    for(int i = 0; i < wpsversion.length && Integer.parseInt(wpsversion[i]) <= minversion[i]; ++i) {
                        if (Integer.parseInt(wpsversion[i]) != minversion[i] && Integer.parseInt(wpsversion[i]) < minversion[i]) {
                            if (DEBUG) {
                                Log.v(tag, "wps version is lower!");
                            }

                            sendMsgToHandler(11);
                            return false;
                        }
                    }
                }

                return true;
            }
        }
    }

    @SuppressLint("WrongConstant")
    public boolean isWPSPerInstalled() {
        try {
            context.getPackageManager().getApplicationInfo("cn.wps.moffice_eng", 8192);
            return true;
        } catch (NameNotFoundException var3) {
            try {
                context.getPackageManager().getApplicationInfo("cn.wps.moffice_cn", 8192);
                return true;
            } catch (NameNotFoundException var2) {
                return false;
            }
        }
    }

    @SuppressLint("WrongConstant")
    private String getWPSPrePageName() {
        String pagename = "cn.wps.moffice_eng";

        try {
            context.getPackageManager().getApplicationInfo("cn.wps.moffice_eng", 8192);
            pagename = "cn.wps.moffice_eng";
            return pagename;
        } catch (NameNotFoundException var4) {
            try {
                context.getPackageManager().getApplicationInfo("cn.wps.moffice_cn", 8192);
                pagename = "cn.wps.moffice_cn";
            } catch (NameNotFoundException var3) {
            }

            return pagename;
        }
    }

    @SuppressLint("WrongConstant")
    public boolean isWPSInstalled() {
        try {
            context.getPackageManager().getApplicationInfo("com.kingsoft.moffice_kinggrid", 8192);
            this.kinggrid_pro = true;
            return true;
        } catch (NameNotFoundException var3) {
            this.kinggrid_pro = false;

            try {
                context.getPackageManager().getApplicationInfo("com.kingsoft.moffice_pro", 8192);
                this.kingsoft_pro = true;
            } catch (NameNotFoundException var2) {
                this.kingsoft_pro = false;
            }

            return this.kingsoft_pro;
        }
    }

    public static void saveDocumentAs(String filePath, String fileFormat, String password, String writePassword) {
        if (document != null) {
            try {
                if (fileFormat.equals("DOC")) {
                    if (TextUtils.isEmpty(filePath)) {
                        if (localfile) {
                            filePath = directory + "saveas" + fileName.substring(fileName.lastIndexOf("/") + 1, fileName.lastIndexOf(".")) + ".doc";
                        } else {
                            filePath = directory + "saveas" + fileName;
                        }
                    }

                    document.saveAs(filePath, SaveFormat.DOC, password, writePassword);
                } else if (fileFormat.equals("TXT")) {
                    document.saveAs(filePath, SaveFormat.TXT, password, writePassword);
                } else if (fileFormat.equals("DOCX")) {
                    document.saveAs(filePath, SaveFormat.DOCX, password, writePassword);
                } else if (fileFormat.equals("PDF")) {
                    if (filePath == null) {
                        document.saveAs(directory + recordId + ".pdf", SaveFormat.PDF, password, writePassword);
                    } else if (filePath.equals("")) {
                        if (localfile) {
                            filePath = fileName.substring(0, fileName.lastIndexOf(".")) + ".pdf";
                        } else {
                            filePath = directory + recordId + ".pdf";
                        }
                    } else {
                        document.saveAs(filePath, SaveFormat.PDF, password, writePassword);
                    }
                }
            } catch (RemoteException var5) {
                var5.printStackTrace();
            }
        }

    }

    public static String getWPSVersion() {
        if (document != null) {
            try {
                return document.getApplication().getVersion();
            } catch (Exception var1) {
                var1.printStackTrace();
            }
        }

        return null;
    }

    @SuppressLint("WrongConstant")
    public static String getCurPackageName() {
        try {
            context.getPackageManager().getApplicationInfo("com.kingsoft.moffice_kinggrid", 8192);
            return "com.kingsoft.moffice_kinggrid";
        } catch (NameNotFoundException var2) {
            try {
                context.getPackageManager().getApplicationInfo("com.kingsoft.moffice_pro", 8192);
                return "com.kingsoft.moffice_pro";
            } catch (NameNotFoundException var1) {
                return "";
            }
        }
    }

    public static void saveDocument() {
        if (document != null) {
            try {
                document.save(true);
            } catch (RemoteException var1) {
                var1.printStackTrace();
            }
        }

    }

    public static void saveAndExit(boolean noPrompt) {
        Log.d(tag, "saveAndExit," + document);
        if (document != null) {
            try {
                Log.d(tag, "close document" + document.getProtectionType());
                document.save(true);
                document.close();
            } catch (RemoteException var4) {
                var4.printStackTrace();
            }
        }

        if (workBook != null) {
            try {
                Log.d(tag, "close workBook");
                workBook.close();
            } catch (RemoteException var3) {
                var3.printStackTrace();
            }
        }

        if (presentation != null) {
            try {
                Log.d(tag, "close presentation");
                presentation.close();
            } catch (RemoteException var2) {
                var2.printStackTrace();
            }
        }

    }

    public static void insertImage(String path) throws RemoteException {
        if (document != null) {
            Log.d(tag, "picPath: begin");
            new BitmapFactory();
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            float height = (float)(bitmap.getHeight() / 2);
            float width = (float)(bitmap.getWidth() / 2);
            Variant mTopVirant = document.getApplication().getSelection().getInformation(WdInformation.wdVerticalPositionRelativeToPage);
            Variant mLeftVirant = document.getApplication().getSelection().getInformation(WdInformation.wdHorizontalPositionRelativeToPage);
            float left = (Float)mLeftVirant.value;
            float top = (Float)mTopVirant.value;
            document.getShapes().addPicture(path, false, false, left, top, width, height, document.getSelection().getStart(), WrapType.TopOfText);
            Log.d(tag, "add picPath end: " + path);
        }

    }

    public void insertText(String textstring) throws RemoteException {
        Log.d(tag, "insertText," + document);
        if (document != null) {
            document.getSelection().typeText(textstring);
        }

    }

    public static void closeDocument() {
        Log.d(tag, "close file, " + document);
        if (document != null) {
            try {
                Log.d(tag, "close document");
                document.close();
            } catch (RemoteException var3) {
                var3.printStackTrace();
            }
        }

        if (workBook != null) {
            try {
                Log.d(tag, "close workBook");
                workBook.close();
            } catch (RemoteException var2) {
                var2.printStackTrace();
            }
        }

        if (presentation != null) {
            try {
                Log.d(tag, "close presentation");
                presentation.close();
            } catch (RemoteException var1) {
                var1.printStackTrace();
            }
        }

    }

    private void protectDocumentAfterEdit() {
        Log.e(tag, "document:" + document);

        try {
            String path = "";
            if (localfile) {
                path = fileName;
            } else {
                path = directory + fileName;
            }

            Intent openIntent = Util.getOpenIntent(context, path, true, this.isReviseMode, this.isClearTrace, this.fileProviderAuthor);
            document = this.officeService.openWordDocument(fileName, "", openIntent);
            if (document != null) {
                this.protectDocument(1);
                document.save(true);
            } else {
                toastShow("文档保护失败！");
            }
        } catch (RemoteException var3) {
            var3.printStackTrace();
        }

    }

    public void protectDocument(int protectionType) {
        if (document != null) {
            try {
                Log.e(tag, "call: Thread.sleep(1000)");
                Thread.sleep(500L);
                document.protect2(WdProtectionType.wdAllowOnlyReading, true, TextUtils.isEmpty(this.protectSecret) ? "GoldGridDBstep" : this.protectSecret, false, true);
            } catch (RemoteException var3) {
                var3.printStackTrace();
            } catch (InterruptedException var4) {
                var4.printStackTrace();
            }
        }

    }

    public static void acceptAllRevision() {
        Log.d(tag, "acceptAllRevision, " + document);
        if (document != null) {
            try {
                document.acceptAllRevisions();
            } catch (RemoteException var1) {
                var1.printStackTrace();
            }
        }

    }

    public static void enterReviseMode() {
        Log.d(tag, "enterReviseMode, " + document);
        if (document != null) {
            try {
                document.enterReviseMode();
            } catch (RemoteException var1) {
                var1.printStackTrace();
            }
        }

    }

    public static void exitReviseMode() {
        Log.d(tag, "exitReviseMode, " + document);
        if (document != null) {
            try {
                document.exitReviseMode();
            } catch (RemoteException var1) {
                var1.printStackTrace();
            }
        }

    }

    public static void denyAllRevision() {
        if (document != null) {
            try {
                document.denyAllRevision();
            } catch (RemoteException var1) {
                var1.printStackTrace();
            }
        }

    }

    public static void clearAllHandwrite() {
        if (document != null) {
            try {
                int shapeCount = (int)document.getShapes().getCount();
                int sum = 0;
                Log.d(tag, "shapes num : " + shapeCount);
                int noInk = 0;

                for(int i = 1; shapeCount > 0; ++i) {
                    MsoTriState state = document.getShapes().item(i).hasInk();
                    if (state == MsoTriState.msoTrue) {
                        document.getShapes().item(i).delete();
                        shapeCount = (int)document.getShapes().getCount();
                        --i;
                        ++sum;
                    } else {
                        ++noInk;
                    }

                    if (shapeCount == noInk) {
                        return;
                    }
                }
            } catch (RemoteException var5) {
                var5.printStackTrace();
            }
        }

    }

    public static void clearAllComments() {
        if (document != null) {
            try {
                document.clearAllComments();
            } catch (RemoteException var1) {
                var1.printStackTrace();
            }
        }

    }

    public void undo() {
        if (document != null) {
            try {
                document.undo();
            } catch (RemoteException var2) {
                var2.printStackTrace();
            }
        }

    }

    public void newAndOpen() {
        localfile = true;
        this.initFileFlags();

        try {
            if (this.officeService == null) {
                this.initOfficeService();
            }

            fileUtil = null;
            File file = new File(fileName);
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                file.createNewFile();
            }

            this.StartNewWpsOffice(fileName);
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public String getAuthorziedInfo() {
        return resultValue;
    }

    public void setCopyRight(String copyRight) {
        this.copyRight = copyRight;
    }

    public long checkCopyRight() {
        return 0L;
    }

    public void setPackageName(String packageName) {
        appPackageName = packageName;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
        this.initFileDir();
    }

    public void setIsReviseMode(boolean isReviseMode) {
        this.isReviseMode = isReviseMode;
    }

    public void setNeedUnProtectDocument(boolean isNeed, String secret) {
        this.isNeedUnProtect = isNeed;
        this.unProtectSecret = secret;
    }

    public void setNeedProtectDocumentAfterEdit(boolean isNeed, String secret) {
        this.isNeedProtect = isNeed;
        this.protectSecret = secret;
    }

    public void setFileProviderAuthor(String fileProviderAuthor) {
        this.fileProviderAuthor = fileProviderAuthor;
    }

    public void setIsScreenshotForbid(boolean isScreenshotForbid) {
        this.isScreenshotForbid = isScreenshotForbid;
    }

    public void setIsDefaultUseFinger(boolean isDefaultUseFinger) {
        this.isDefaultUseFinger = isDefaultUseFinger;
    }

    public void setToggleForbiddenInk(boolean isForbiddenInk) {
        this.isForbiddenInk = isForbiddenInk;
    }

    public void setIsClearTrace(boolean isClearTrace) {
        this.isClearTrace = isClearTrace;
    }

    public void setIsClearFile(boolean isClear) {
        this.isClearFile = isClear;
    }

    public void setIsDocShowView(boolean isDocShowView) {
        this.isDocShowView = isDocShowView;
    }

    public void setShowReviewingPaneRightDefault(boolean isShow) {
        this.isShowReviewingPaneRightDefault = isShow;
    }

    public void setReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }

    public void setMenuHiddenList(ArrayList<ActionType> list) {
        menuHiddenList = list;
    }

    public void setViewHiddenList(ArrayList<ViewType> list) {
        viewHiddenList = list;
    }

    public static ArrayList<ActionType> getMenuHiddenList() {
        return menuHiddenList;
    }

    public static ArrayList<ViewType> getViewHiddenList() {
        return viewHiddenList;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setText(String setText) {
        this.setText = setText;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    /** @deprecated */
    @Deprecated
    public void setPGFType(boolean isPGF) {
    }

    public void setEditType(int editType) {
        this.editType = editType;
    }

    public void setUseWPSPersonalEdition(boolean useWPSPer) {
        this.useWPSPer = useWPSPer;
    }

    public static int getCurrentPageNum() {
        try {
            if (document != null) {
                return document.getCurrentPageNum(1);
            }

            if (workBook != null) {
                return workBook.getActiveSheet().getCurrentPageIndex() + 1;
            }

            if (presentation != null) {
                return presentation.getCurrentPageIndex() + 1;
            }
        } catch (RemoteException var1) {
            var1.printStackTrace();
        } catch (NullPointerException var2) {
            var2.printStackTrace();
        }

        return 1;
    }

    public static synchronized void showSignature(Context context, String userName, int num) {
        Log.d(tag, "showSignature page=" + num);
        if (fileUtil == null) {
            Log.d(tag, "showSignature fileUtil= null" + num);
        } else {
            Log.d(tag, "nnnnnnnnnnnnnnnnnnnnnn===" + num);
            getCompoundBitmap(num);
        }
    }

    public void setGifCreated(boolean ischecked) {
        isGif = ischecked;
    }

    public void setNetTransMode(int netTransMode) {
        this.netTransMode = netTransMode;
    }

    public static void showHandwrite(Context context) {
        Intent intent = new Intent();
        intent.setAction("com.kinggrid.iappoffice.showHandwrite");
        if (document != null) {
            try {
                if (document.getProtectionType2() != WdProtectionType.wdNoProtection) {
                    sendMsgToHandler(6);
                    return;
                }

                dialogContext = context;
                sendMsgToHandler(4);
            } catch (Exception var3) {
                var3.printStackTrace();
            }

        }
    }

    public static void showSignature(Context context) {
        dialogContext = context;
        sendMsgToHandler(5);
    }

    public static void insertISignature(Context context) {
        signContext = context;
        if (document != null) {
            try {
                if (document.getProtectionType2() != WdProtectionType.wdNoProtection) {
                    sendMsgToHandler(6);
                    return;
                }

                sendMsgToHandler(9);
            } catch (Exception var2) {
                var2.printStackTrace();
            }

        }
    }

    public static void verifySignature() {
        if (document != null) {
            ;
        }
    }

    private boolean StartNewWpsOffice(String filepath) throws Exception {
        NewDocThread newFile = new NewDocThread(filepath);
        newFile.start();
        return document != null;
    }

    public void setDocumentNotLanding(boolean notLanding) {
        documentNotLanding = notLanding;
    }

    public void setFileData(byte[] fileDate) {
        this.fileDate = fileDate;
    }

    public static void setSaveFileData(byte[] fileDate) {
        saveFileDate = fileDate;
    }

    public static boolean isSaveFileMode() {
        return !documentNotLanding;
    }

    public static byte[] getNewInput() {
        return documentNotLanding ? fileDate : null;
    }

    private byte[] readStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        boolean var4 = false;

        int len;
        while((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }

        inputStream.read(buffer);
        inputStream.close();
        return outputStream.toByteArray();
    }

    private void setFileNameByFileType() {
        if (TextUtils.isEmpty(fileName)) {
            if (!TextUtils.isEmpty(recordId)) {
                fileName = recordId + fileType;
            }

        } else if (!fileName.contains(".")) {
            fileName = fileName + fileType;
        } else {
            fileName = fileName.replace(fileName.substring(fileName.lastIndexOf(".")), fileType);
        }
    }

    private void initFileFlags() {
        Log.v(tag, "initFileFlags");
        typeFlag = 0;
        if (fileType.equals(".xls") || fileType.equals(".xlsx")) {
            typeFlag = 1;
        }

        if (fileType.equals(".pptx") || fileType.equals(".ppt")) {
            typeFlag = 2;
        }

        if (fileType.equals(".pdf") || fileType.equals(".PDF")) {
            typeFlag = 3;
        }

        Log.v(tag, "typeFlag = " + typeFlag);
        document = null;
        workBook = null;
        presentation = null;
    }

    private static void sendMsgToHandler(int what) {
        Message msg = Message.obtain();
        msg.what = what;
        myHandler.sendMessage(msg);
    }

    private static void sendMsgToHandler(int what, String message) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = message;
        myHandler.sendMessage(msg);
    }

    private void downloadOpenFile() {
        Runnable downloadRun = new Runnable() {
            public void run() {
                isCancelled = false;
                iAppOfficeServer.isCancellLoadFile = false;
                switch(netTransMode) {
                    case 0:
                        progressInfo = "正在打开文档，请稍后...";
                        sendMsgToHandler(1);
                        if (!useWPSPer &&  officeService == null && !useMethod2) {
                            initOfficeService();
                        }

                        Log.v(tag, "download isUsed2015 = " + isUsed2015);
                        boolean state = false;

                        try {
                            try {
                                if (isUsed2015) {
                                    if (officeServer != null) {
                                        state = officeServer.downloadOpen2015(parmsFor2015);
                                    }
                                } else if (officeServer != null) {
                                    state = officeServer.downloadOpen(fieldList, valueList);
                                }
                            } catch (Exception var8) {
                                result = "下载失败";
                                var8.printStackTrace();
                                if (mOnDownLoadStateListener != null) {
                                    mOnDownLoadStateListener.error();
                                }
                            }
                            break;
                        } finally {
                            if (state) {
                                if (officeServer.isPGFFile()) {
                                    decodeFile(pgfFilePath);
                                }

                                fileDate = officeServer.getFileData();
                                Log.e(tag, "fileDate:" + fileDate);
                                result = "下载成功";
                            } else {
                                result = "下载失败";
                            }

                            sendMsgToHandler(2);
                        }
                    case 1:
                        try {
                            if (officeServer != null) {
                                officeServer.download();
                            }
                        } catch (Exception var7) {
                            var7.printStackTrace();
                        }
                }

            }
        };
        this.webThread = new Thread(downloadRun);
        this.webThread.start();

        try {
            this.webThread.join();
        } catch (InterruptedException var8) {
            var8.printStackTrace();
        } finally {
            sendMsgToHandler(2);
        }

        this.webThread = null;
        if (!isCancelled) {
            toastShow(result, isShowToast);
            if (result.equals("下载成功")) {
                if (editType != 1) {
                    if (!useWPSPer) {
                        try {
                            if (this.useMethod2) {
                                this.startWpsOffice2(directory + fileName);
                            } else {
                                this.startWpsOffice(directory + fileName);
                            }
                        } catch (Exception var7) {
                            var7.printStackTrace();
                        }

                        if (isPGF) {
                            this.decodeFile(pgfFilePath);
                        }
                    } else {
                        this.startWPSPer(directory + fileName);
                        if (isPGF) {
                            this.decodeFile(pgfFilePath);
                        }
                    }
                } else {
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction("com.kinggrid.iappoffice.fullsignature.show");
                    context.sendBroadcast(broadcastIntent);
                }

                if (this.mOnDownLoadStateListener != null) {
                    this.mOnDownLoadStateListener.success();
                }
            } else {
                if (this.mOnDownLoadStateListener != null) {
                    this.mOnDownLoadStateListener.error();
                }

                Log.d(tag, "download file fail , file cannot be opened");
            }

        }
    }

    public void stopLoadFile() {
        isCancelled = true;
        iAppOfficeServer.isCancellLoadFile = true;
        if (this.webThread != null && !this.webThread.isAlive()) {
            this.webThread.interrupt();
            toastShow("下载已取消！");
        }

        if (this.uploadThread != null && this.uploadThread.isAlive()) {
            this.uploadThread.interrupt();
            toastShow("上传已取消！");
        }

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

    public void setUsediWebOffice2015(boolean isUsed) {
        isPGF = false;
        this.isUsed2015 = isUsed;
    }

    public void setParmsFor2015(String parms) {
        this.parmsFor2015 = parms;
    }

    private boolean uploadFile(String pathfrom) throws Exception {
        Log.v(tag, "upload isUsed2015 = " + this.isUsed2015);
        filepath = pathfrom;
        boolean state = false;
        officeServer.setSaveFileData(saveFileDate);
        Log.e(tag, "saveFileDate:" + saveFileDate);
        switch(netTransMode) {
            case 0:
                if (this.isUsed2015) {
                    if (officeServer != null) {
                        state = officeServer.upload2015(this.parmsFor2015);
                    }
                } else {
                    if (officeServer != null) {
                        state = officeServer.upload(this.fieldList, this.valueList);
                    }

                    if (!this.moduleType.equals("")) {
                        officeServer.uploadDocFileCustom(this.moduleType);
                    }
                }
                break;
            case 1:
                if (officeServer != null) {
                    officeServer.doUpload();
                    state = true;
                }
        }

        return state;
    }

    public void setURLRequestProperty(String field, String value) {
        for(int i = 0; i < this.fieldList.size(); ++i) {
            if (((String)this.fieldList.get(i)).equals(field)) {
                this.fieldList.remove(i);
                this.valueList.remove(i);
                break;
            }
        }

        this.fieldList.add(field);
        this.valueList.add(value);
    }

    private static void exportCurrentPageImage(String path) {
        Log.d(tag, "exportCurPageToImage: " + path);

        try {
            if (document != null) {
                document.saveCurrentPageToImage(path, PictureFormat.PNG, 0, 1, 1, 0, 794, 1123);
            }

            if (workBook != null) {
                Worksheet var1 = workBook.getActiveSheet();
            }

            if (presentation != null) {
                Log.d(tag, "ppt exportCurPageToImage");
                String ss = presentation.exportCurPageToImage(path, PictureFormat.PNG);
                Log.d(tag, "ppt exportCurPageToImage: " + ss);
            }
        } catch (RemoteException var2) {
            var2.printStackTrace();
        } catch (NullPointerException var3) {
            var3.printStackTrace();
        }

    }

    private static synchronized Bitmap getCompoundBitmap(int num) {
        currentPage = num;
        Thread thread = new Thread() {
            public void run() {
                String pagePath = imgDir + currentPage + ".png";
                if (editType != 1) {
                    exportCurrentPageImage(pagePath);
                }

                bmp = fileUtil.getCompoundBitmap(currentPage, pagePath, isGif, typeFlag);
                sendMsgToHandler(3);
            }
        };
        thread.start();
        return bmp;
    }

    private void decodeFile(String filePath) {
        fileUtil.decodeFile(filePath);
    }

    private void encodeFile(String docPath, String pgfPath) throws Exception {
        fileUtil.encodeFile(docPath, pgfPath, typeFlag);
    }

    private boolean checkTime() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(1);
        int mMonth = c.get(2);
        int mDay = c.get(5);
        Log.d("Kevin", "mYear + mMonth : " + mYear + "," + mMonth);
        if (mYear < 2014) {
            resultValue = "成功";
            Log.d("Kevin", "mYear + mMonth : " + mYear + "," + mMonth + resultValue);
            return true;
        } else if (mYear == 2014 && mMonth + 1 <= 12) {
            resultValue = "成功";
            Log.d("Kevin", "mYear + mMonth : " + mYear + "," + mMonth + resultValue);
            return true;
        } else {
            resultValue = "试用期已过";
            Log.d("Kevin", "mYear + mMonth : " + mYear + "," + mMonth + resultValue);
            return false;
        }
    }

    private void unInitOfficeServer() {
        if (this.officeService != null && !useWPSPer) {
            Log.d(tag, "unInitOfficeServer();");

            try {
                context.unbindService(this.mOfficeConn);
            } catch (Exception var2) {
                var2.printStackTrace();
                Log.v("zxg", "unBindservice exception");
            }

            if (this.editservice != null) {
                context.stopService(this.editservice);
            }
        }

        isBind = false;
        this.officeService = null;
        isBind = false;
        initService = 0;
        document = null;
        workBook = null;
        presentation = null;
    }

    private void clearFile() {
        if (this.isClearFile) {
            File file;
            if (localfile) {
                file = new File(fileName);
            } else {
                file = new File(directory + fileName);
            }

            if (file.exists()) {
                file.delete();
            }
        }

    }

    private void uploadFile() {
        this.uploadThread = new Thread(new Runnable() {
            public void run() {
                isUploading = true;
                progressInfo = "正在保存文档，请稍后...";
                sendMsgToHandler(1);
                Log.v(tag, "officeServer:" + officeServer);
                boolean state = false;

                try {
                    if (officeServer != null) {
                        Log.v(tag, "isPGFFile:" + officeServer.isPGFFile());
                        if (officeServer.isPGFFile()) {
                            encodeFile(directory + recordId + fileType, pgfFilePath);
                            state = officeServer.uploadPGFFile(fieldList, valueList);
                            if (!moduleType.equals("")) {
                                officeServer.uploadDocFileCustom(moduleType);
                            }
                        } else {
                            if (DEBUG) {
                                Log.v(tag, "uploadFile:filePath=" + directory + fileName);
                                Log.v(tag, "file is exit:" + (new File(directory + fileName)).exists());
                            }

                            state = uploadFile(directory + fileName);
                        }

                        if (state) {
                            result = "上传成功";
                        } else {
                            result = "上传失败";
                        }
                    } else {
                        result = "上传失败";
                    }
                } catch (Exception var7) {
                    result = "上传失败";
                    var7.printStackTrace();
                } finally {
                    if (isClose) {
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction("com.kinggrid.iappoffice.close");
                        context.sendBroadcast(broadcastIntent);
                        unInitOfficeServer();
                        unRegisterReceiver();
                        clearFile();
                    }

                    sendMsgToHandler(2);
                    if (result.equals("上传成功")) {
                        toastShow("上传成功！", isShowToast);
                        if (mOnUpLoadStateListener != null) {
                            mOnUpLoadStateListener.success(filepath);
                        }
                    } else {
                        toastShow("上传失败！" + officeServer.getErrorMessage(), isShowToast);
                        if (mOnUpLoadStateListener != null) {
                            mOnUpLoadStateListener.error();
                        }
                    }

                    isUploading = false;
                }

            }
        });
        this.uploadThread.start();
    }

    public void unRegisterReceiver() {
        Log.e(tag, "unRegisterReceiver");
        if (this.saveRec != null && context != null) {
            try {
                context.unregisterReceiver(this.saveRec);
                this.saveRec = null;
            } catch (IllegalArgumentException var2) {
                if (!var2.getMessage().contains("Receiver not registered")) {
                    throw var2;
                }
            }
        }

    }

    public void setToastGravity(int gravity) {
        toastGravity = gravity;
    }

    private static void toastShow(String msg) {
        if (isShowToast) {
            Util.showToast(context, msg, toastGravity);
        }

    }

    private static void toastShow(String msg, boolean isShow) {
        if (isShow) {
            Util.showToast(context, msg, toastGravity);
        }

    }

    public void setOnDownLoadStateListener(OnDownLoadStateListener l) {
        this.mOnDownLoadStateListener = l;
    }

    public void setOnUpnLoadStateListener(OnUpLoadStateListener l) {
        this.mOnUpLoadStateListener = l;
    }

    public void setOnRegisterResultListener(OnRegisterResultListener l) {
        this.mOnRegisterResultListener = l;
    }

    public void setOnOpenResultListener(OnOpenResultListener l) {
        this.mOnOpenResultListener = l;
    }

    public static String getVersion() {
        if (context == null) {
            return "";
        } else {
            PackageManager manager = context.getPackageManager();
            String version = "";

            PackageInfo info;
            try {
                info = manager.getPackageInfo("com.kingsoft.moffice_kinggrid", 0);
                version = info.versionName;
            } catch (Exception var5) {
                version = "";
            }

            if (!TextUtils.isEmpty(version)) {
                return version;
            } else {
                try {
                    info = manager.getPackageInfo("com.kingsoft.moffice_pro", 0);
                    version = info.versionName;
                } catch (Exception var4) {
                    version = "";
                }

                return version;
            }
        }
    }

    private boolean isRegisterReceiver(LocalBroadcastManager manager, String action) {
        boolean isRegister = false;

        try {
            Field mReceiversField = manager.getClass().getDeclaredField("mReceivers");
            mReceiversField.setAccessible(true);
            HashMap<BroadcastReceiver, ArrayList<IntentFilter>> mReceivers = (HashMap)mReceiversField.get(manager);
            Iterator var7 = mReceivers.keySet().iterator();

            while(var7.hasNext()) {
                BroadcastReceiver key = (BroadcastReceiver)var7.next();
                ArrayList<IntentFilter> intentFilters = (ArrayList)mReceivers.get(key);

                for(int i = 0; i < intentFilters.size(); ++i) {
                    IntentFilter intentFilter = (IntentFilter)intentFilters.get(i);
                    Field mActionsField = intentFilter.getClass().getDeclaredField("mActions");
                    mActionsField.setAccessible(true);
                    ArrayList<String> mActions = (ArrayList)mActionsField.get(intentFilter);

                    for(int j = 0; j < mActions.size(); ++j) {
                        if (((String)mActions.get(j)).equals(action)) {
                            isRegister = true;
                            break;
                        }
                    }
                }
            }
        } catch (NoSuchFieldException var14) {
            var14.printStackTrace();
        } catch (IllegalAccessException var15) {
            var15.printStackTrace();
        }

        return isRegister;
    }

    public static String getJSON_DATA_CONTENT() {
        return "[{ \"name\" : \"" + mOfficeClientServiceAction + "\"," + " \"type\" : \"Package-ID\",\"id\" : \"cn.wps.moffice.client\", " + "\"Security-Level\" : \"Full-access\", \"Authorization\"  : \"abxxdsewrwsds3232ss\" }," + "]";
    }

    public class MyServiceConnection implements ServiceConnection {
        public MyServiceConnection() {
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            officeService = Stub.asInterface(service);
            initService = 1;
            Log.d(tag, "onServiceConnected officeService=" + officeService);
            isBind = true;
        }

        public void onServiceDisconnected(ComponentName name) {
            Log.e("zxg", "onServiceDisconnected");
            officeService = null;
            isBind = false;
            clearFile();
        }
    }

    class NewDocThread extends Thread {
        private String path;

        public NewDocThread(String path) {
            this.path = path;
        }

        public void run() {
            while(true) {
                try {
                    if (officeService == null) {
                        Thread.sleep(100L);
                        continue;
                    }

                    Intent newDocIntent = Util.getOpenIntent(context, this.path, true, isReviseMode, isClearTrace, fileProviderAuthor);
                    newDocIntent.putExtra("UserName", userName);
                    if (isClearTrace) {
                        newDocIntent.putExtra("IsClearTrace", isClearTrace);
                    }

                    newDocIntent.getExtras();
                    if (typeFlag == 0) {
                        document = officeService.newDocument(this.path, newDocIntent);
                        Log.d(tag, "new word");
                    } else if (typeFlag == 1) {
                        workBook = officeService.newWorkbook(this.path, newDocIntent);
                        Log.d(tag, "new excel");
                    } else if (typeFlag == 2) {
                        Log.d(tag, "new ppt");
                        presentation = officeService.newPresentation(this.path, newDocIntent);
                    }
                } catch (RemoteException var2) {
                    var2.printStackTrace();
                    document = null;
                    workBook = null;
                    presentation = null;
                } catch (InterruptedException var3) {
                    var3.printStackTrace();
                }

                return;
            }
        }
    }

    public interface OnDownLoadStateListener {
        void success();

        void error();
    }

    public interface OnOpenResultListener {
        void openSuccess();

        void openFail();
    }

    public interface OnRegisterResultListener {
        void registerSuccess();

        void registerFail();
    }

    public interface OnUpLoadStateListener {
        void success(String var1);

        void error();
    }

    @SuppressLint("WrongConstant")
    class OpenFileByWPS extends Thread {
        private String path;

        public OpenFileByWPS(String filepath) {
            this.path = filepath;
        }

        public void run() {
            Log.d(tag, "StartWpsOffice Begin officeService: " + officeService);

            while(officeService == null) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException var10) {
                    var10.printStackTrace();
                }
            }

            Looper.prepare();
            Intent openIntent = Util.getOpenIntent(context, this.path, true, isReviseMode, isClearTrace, fileProviderAuthor);
            if (openIntent != null) {
                openIntent.putExtra("UserName", userName);
                openIntent.putExtra("DisplayView", isDocShowView);
                Bundle bundle = new Bundle();
                bundle.putString("WaterMaskText", waterMaskText);
                bundle.putInt("WaterMaskColor", waterMaskColor);
                bundle.putBoolean("ShowReviewingPaneRightDefault", isShowReviewingPaneRightDefault);
                bundle.putBoolean("SendCloseBroad", true);
                bundle.putBoolean("SendSaveBroad", true);
                bundle.putBoolean("isScreenshotForbid", isScreenshotForbid);
                bundle.putBoolean("ClearFile", isClearFile);
                if (!TextUtils.isEmpty(serialNumber)) {
                    if (serialNumber.length() > 12) {
                        bundle.putString("SerialNumberOtherPc", serialNumber);
                    } else {
                        bundle.putString("SerialNumberOther", serialNumber);
                    }
                }

                openIntent.putExtras(bundle);
                AppRegister.DEBUG = true;
                if (isReadOnly) {
                    openIntent.putExtra("OpenMode", "ReadOnly");
                } else if (isEditMode) {
                    openIntent.putExtra("OpenMode", "EditMode");
                } else {
                    openIntent.putExtra("OpenMode", "Normal");
                }

                openIntent.putExtra("SendCloseBroad", true);
                openIntent.putExtra("SendSaveBroad", true);

                try {
                    Log.d(tag, "mDocment begin: " + document);
                    Log.d(tag, "typeFlag: " + typeFlag);
                    if (typeFlag != 0) {
                        if (typeFlag == 1) {
                            Workbooks workbooks = officeService.getWorkbooks();
                            workBook = workbooks.openBookEx(this.path, "", openIntent);
                            if (workBook != null) {
                                context.sendBroadcast(new Intent("com.kinggrid.file.open.end"));
                            }

                            Log.e(tag, "StartWpsOffice workBook:" + workBook);
                        } else if (typeFlag == 2) {
                            presentation = officeService.openPresentation(this.path, "", openIntent);
                            if (presentation != null) {
                                context.sendBroadcast(new Intent("com.kinggrid.file.open.end"));
                            }
                        } else if (typeFlag == 3) {
                            Log.v(tag, "start typeFlag == FILE_TYPE_PDF");
                            pdfReader = officeService.openPDFReader(this.path, "", openIntent);
                            if (pdfReader != null) {
                                context.sendBroadcast(new Intent("com.kinggrid.file.open.end"));
                            }
                        }
                    } else {
                        String version = getVersion();
                        String[] wpsversion = version.split("[.]");
                        if (Integer.parseInt(wpsversion[0]) > 9) {
                            Intent i = officeService.getDocuments().getDocumentIntent(this.path, "", openIntent);
                            Log.v(tag, "i:" + i);
                            Log.v(tag, "document:" + document);
                            if (i != null) {
                                i.addFlags(268435456);
                                if (VERSION.SDK_INT >= 24) {
                                    i.setData((Uri)null);
                                    Bundle bundle_t = new Bundle();
                                    bundle_t.putString("FILEPATH", this.path);
                                    i.putExtras(bundle_t);
                                }

                                context.startActivity(i);
                                document = officeService.getDocuments().waitForDocument(this.path);
                            } else {
                                document = officeService.openWordDocument(this.path, "", openIntent);
                            }
                        } else {
                            Log.e(tag, "call openWordDocument:" + System.currentTimeMillis());
                            document = officeService.openWordDocument(this.path, "", openIntent);
                            Log.e(tag, "openWordDocument end" + System.currentTimeMillis());
                            Log.e(tag, "document:" + document);
                        }

                        Log.v(tag, "document text:" + document);
                        if (document != null) {
                            boolean isOk = false;

                            for(int k = 0; k < 10; ++k) {
                                if (document.isLoadOK()) {
                                    Log.d(tag, "文档已就绪，可以开始进行AIDL调用了");
                                    isOk = true;
                                    break;
                                }

                                try {
                                    Thread.sleep(500L);
                                } catch (InterruptedException var9) {
                                    var9.printStackTrace();
                                }
                            }

                            if (isOk) {
                                isClose = false;
                                Log.e("zxg", "isReviseMode:" + isReviseMode);
                                if (!isReviseMode) {
                                    document.exitReviseMode();
                                }

                                if (isSaveAsPDF) {
                                    String saveasfilename = fileName.substring(0, fileName.lastIndexOf(".")) + ".pdf";
                                    boolean result = document.saveAs(saveasfilename, SaveFormat.PDF, "", "");
                                    Intent resultintent = new Intent();
                                    resultintent.putExtra("result", result);
                                    resultintent.putExtra("filename", saveasfilename);
                                    resultintent.setAction("com.kinggrid.file.saveas.end");
                                    context.sendBroadcast(resultintent);
                                }

                                if (isDefaultUseFinger) {
                                    document.toggleInkFinger();
                                }

                                document.toggleForbiddenInk(isForbiddenInk);
                                boolean isProtectOn = document.isProtectOn();
                                Log.v("iappoffice", "document.isProtectOn():" + isProtectOn);
                                if (isNeedUnProtect && isProtectOn) {
                                    protectType = document.getProtectionType2();
                                    document.unprotect(TextUtils.isEmpty(unProtectSecret) ? "GoldGridDBstep" : unProtectSecret);
                                }

                                if (isNeedProtect) {
                                    document.protect2(WdProtectionType.wdAllowOnlyReading, true, "123465", false, true);
                                    Log.d(tag, "doc open protect2 end:");
                                    protectDocument(1);
                                }

                                context.sendBroadcast(new Intent("com.kinggrid.file.open.end"));
                                if (mOnOpenResultListener != null) {
                                    mOnOpenResultListener.openSuccess();
                                }
                            }
                        }
                    }

                    if (saveRec == null) {
                        Log.e(tag, "call initRegisterReceiver:" + System.currentTimeMillis());
                        initRegisterReceiver();
                        Log.e(tag, "initRegisterReceiver:end" + System.currentTimeMillis());
                    }

                    Log.d(tag, "StartWpsOffice openDocument=" + this.path);
                    Log.d(tag, "StartWpsOffice mDocment=" + document);
                    isDisplay = true;
                } catch (Exception var11) {
                    if (mOnOpenResultListener != null) {
                        mOnOpenResultListener.openFail();
                    }

                    var11.printStackTrace();
                    Log.d(tag, "StartWpsOffice: Begin" + var11);
                }

                Log.d(tag, "mDocment end: " + document);
            }
            Looper.loop();
        }
    }

    private class SaveReceiver extends BroadcastReceiver {
        private SaveReceiver(SaveReceiver saveReceiver) {
        }

        public void onReceive(Context context, Intent intent) {
            Intent broadcastIntent = new Intent();
            if ("com.kingsoft.back.key.down".equals(intent.getAction())) {
                Log.d(tag, "back");
                broadcastIntent.setAction("com.kinggrid.iappoffice.back");
                context.sendBroadcast(broadcastIntent);
                unInitOfficeServer();
            } else if ("com.kingsoft.home.key.down".equals(intent.getAction())) {
                Log.d(tag, "home");
                broadcastIntent.setAction("com.kinggrid.iappoffice.home");
                context.sendBroadcast(broadcastIntent);
                unInitOfficeServer();
            } else if ("cn.wps.moffice.file.save".equals(intent.getAction())) {
                Log.d(tag, "save");
                if (!isDisplay) {
                    isDisplay = true;
                    return;
                }

                broadcastIntent.setAction("com.kinggrid.iappoffice.save");
                context.sendBroadcast(broadcastIntent);
                isSaved = true;
                if (!localfile) {
                    if (initBool == 0) {
                        sendMsgToHandler(-1);
                        return;
                    }

                    File file = new File(directory + fileName);
                    if (file.exists()) {
                        uploadFile();
                    }
                }
            } else if ("cn.wps.moffice.file.close".equals(intent.getAction())) {
                Log.v(tag, "wps close, document=" + document);
                Log.d(tag, "close");
                if (!isDisplay) {
                    isDisplay = true;
                    return;
                }

                isClose = true;
                if (!isUploading) {
                    broadcastIntent.setAction("com.kinggrid.iappoffice.close");
                    context.sendBroadcast(broadcastIntent);
                    unInitOfficeServer();
                    unRegisterReceiver();
                    clearFile();
                }

                isSaved = false;
            } else if (!"com.kinggrid.iappoffice.fullsignature.save".equals(intent.getAction()) && "cn.wps.moffice.broadcast.AfterSaved".equals(intent.getAction())) {
                boolean isSaveAs = intent.getBooleanExtra("SaveAs", false);
                isSavedAs = isSaveAs;
                broadcastIntent.setAction("com.kinggrid.iappoffice.saveas");
                context.sendBroadcast(broadcastIntent);
                Log.v(tag, "isSaveAs BroadCast:" + isSaveAs);
            }

        }
    }
}
