package com.mustafa.foodapp;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Executors are responsible for executing Runnable tasks, those tasks are used to grab data from the server
 */
public class AppExecutors {
    private static AppExecutors instance;

    public static AppExecutors getInstance() {
        if (instance == null){
            instance = new AppExecutors();
        }
        return instance;
    }
    // Schedule thread or fixed or signal thread which are
    //
    //
    private final ScheduledExecutorService scheduledExecutor= Executors.newScheduledThreadPool(3);

    public ScheduledExecutorService getScheduledExecutor(){
        return scheduledExecutor;
    }

}
