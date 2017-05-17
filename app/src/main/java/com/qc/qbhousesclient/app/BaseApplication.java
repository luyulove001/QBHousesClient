package com.qc.qbhousesclient.app;

import android.app.Activity;
import android.app.Application;

import com.qc.qbhousesclient.utils.Constant;
import com.qc.qbhousesclient.utils.ToastUtils;

import org.xutils.x;

import java.util.LinkedList;
import java.util.List;

import talex.zsw.baselibrary.util.klog.KLog;

/**
 * 作者：xnn
 * 描述:
 */
public class BaseApplication extends Application {

    private List<Activity> activityList = new LinkedList<>();
    private static final String TAG = "BaseApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化KLog
        KLog.init(true);//初始化KLog
//		KLog.init(BuildConfig.DEBUG);
        x.Ext.init(this);
        x.Ext.setDebug(Constant.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
        //程序异常捕获
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        crashHandler.setApplication(this);

        ToastUtils.init(this);

    }


    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public void exit() {
        exitApp();
        System.exit(0);
    }

    public void exitApp() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        // 杀死该应用进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
