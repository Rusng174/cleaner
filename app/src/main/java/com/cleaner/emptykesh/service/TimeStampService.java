package com.cleaner.emptykesh.service;

public class TimeStampService {
    public static boolean isLessThen30Sec(Long time1, Long time2) {
        return Math.abs(time1 / 1000 - time2 / 1000) < 30;
    }
}