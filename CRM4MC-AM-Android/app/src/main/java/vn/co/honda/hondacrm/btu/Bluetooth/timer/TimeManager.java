package vn.co.honda.hondacrm.btu.Bluetooth.timer;


import java.util.Timer;
import java.util.TimerTask;

import vn.co.honda.hondacrm.btu.Utils.LogUtils;

public class TimeManager {
    private Timer mTimerTimeOut;
    private Timer mTimerSchedule;
    public void startTimer(IF_TimeOutListener timeOutListener, long time) {
        LogUtils.startEndMethodLog(true);
        if (mTimerTimeOut != null) {
            LogUtils.e("time out is running ==> stop old timer and start count again!");
            stopTimeOut();
        }
        mTimerTimeOut = new Timer();
        mTimerTimeOut.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mTimerTimeOut != null) {
                    mTimerTimeOut.cancel();
                    mTimerTimeOut = null;
                }
                if (timeOutListener != null) {
                    LogUtils.e(timeOutListener.getClass().getCanonicalName() + " TIMER OUT ERROR!");
                    timeOutListener.onTimeout();
                }
            }
        }, time);
        LogUtils.startEndMethodLog(false);
    }

    public void startTimer(IF_TimeScheduleListener timeScheduleListener, long time) {
        LogUtils.startEndMethodLog(true);
        if (mTimerSchedule != null) {
            LogUtils.e("time schedule is running ==> stop old timer and start count again!");
            stopTimeSchedule();
        }
        mTimerSchedule = new Timer();
        mTimerSchedule.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mTimerSchedule != null) {
                    mTimerSchedule.cancel();
                    mTimerSchedule = null;
                }
                if (timeScheduleListener != null) {
                    LogUtils.d(timeScheduleListener.getClass().getCanonicalName() + " TIMER END!");
                    timeScheduleListener.onTimeSchedule();
                }
            }
        }, time);
        LogUtils.startEndMethodLog(false);
    }

    public void stopAllTimer(){
        LogUtils.startEndMethodLog(true);
        if (mTimerTimeOut != null) {
            LogUtils.d(mTimerTimeOut.getClass().getCanonicalName() + " cancel count time out!");
            mTimerTimeOut.cancel();
            mTimerTimeOut = null;
        }
        if (mTimerSchedule != null) {
            LogUtils.d(mTimerSchedule.getClass().getCanonicalName() + " cancel count time Schedule!");
            mTimerSchedule.cancel();
            mTimerSchedule = null;
        }
        LogUtils.startEndMethodLog(false);
    }

    public void stopTimeOut(){
        LogUtils.startEndMethodLog(true);
        if (mTimerTimeOut != null) {
            LogUtils.d(mTimerTimeOut.getClass().getCanonicalName() + " cancel count time out!");
            mTimerTimeOut.cancel();
            mTimerTimeOut = null;
        }
        LogUtils.startEndMethodLog(false);
    }
    public void stopTimeSchedule(){
        LogUtils.startEndMethodLog(true);
        if (mTimerSchedule != null) {
            LogUtils.d(mTimerSchedule.getClass().getCanonicalName() + " cancel count time Schedule!");
            mTimerSchedule.cancel();
            mTimerSchedule = null;
        }
        LogUtils.startEndMethodLog(false);
    }
}
