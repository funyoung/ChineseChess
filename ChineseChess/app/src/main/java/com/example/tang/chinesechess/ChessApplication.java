package com.example.tang.chinesechess;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/** LeakCanary检测内存溢出
 * @author yangfeng*/
public class ChessApplication extends Application {

    public static RefWatcher getRefWatcher(Context context) {
        ChessApplication application = (ChessApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;

    @Override public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
    }
}
