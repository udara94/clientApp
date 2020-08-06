package com.example.clientapp.utils;

import io.reactivex.Scheduler;

public interface IScheduler {

    Scheduler mainThread();

    Scheduler backgroundThread();
}
