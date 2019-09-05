package com.mustafa.foodApp;


import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.LogRecord;

public class AppExecutors {

    private static AppExecutors instance;

    public static AppExecutors getInstance(){
        if(instance == null){
            instance = new AppExecutors();
        }
        return instance;
    }

//    private final ScheduledExecutorService mNetworkIO = Executors.newScheduledThreadPool(3);
//
//    public ScheduledExecutorService networkIO(){
//        return mNetworkIO;
//    }

    // To do things on the background threat
    private final Executor mDiskIO = Executors.newSingleThreadExecutor();

    // if you are in the background thread to do things { posting value } to the main thread
    private final Executor mMainThreadExecutor  = new MainThreadExecutor();

    private static class MainThreadExecutor implements Executor {

        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mainThreadHandler.post(command);
        }
    }

    public Executor getMainThreadExecutor() {
       return mMainThreadExecutor;
    }

    public Executor getDiskIO() {
        return mDiskIO;
    }
}
