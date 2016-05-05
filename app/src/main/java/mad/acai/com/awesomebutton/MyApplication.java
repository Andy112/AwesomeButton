package mad.acai.com.awesomebutton;

import android.app.Application;
import android.content.Context;

/**
 * Created by Andy on 21/04/2016.
 */
public class MyApplication extends Application {
    private static MyApplication sInstance;

    public static final String API_KEY = "anything";
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static MyApplication getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }
}
