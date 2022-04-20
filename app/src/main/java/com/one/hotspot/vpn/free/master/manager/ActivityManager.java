package com.one.hotspot.vpn.free.master.manager;



public class ActivityManager {
    private static final ActivityManager ourInstance = new ActivityManager();
    private ActivityStateCallback stateCallback;
    private ActivityManager() {
    }

    public static ActivityManager getInstance() {
        return ourInstance;
    }




    public void currentState(ActivityStateCallback callback) {
        this.stateCallback = callback;
    }




    public void onCreate() {
        if (stateCallback != null)
            stateCallback.onCreate();
    }

    public void onDestroy() {
        if (stateCallback != null)
            stateCallback.onDestroy();
    }


    public interface ActivityStateCallback {
        void onCreate();

        void onDestroy();

    }

}
