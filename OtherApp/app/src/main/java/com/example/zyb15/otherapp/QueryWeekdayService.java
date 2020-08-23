package com.example.zyb15.otherapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.Calendar;

public class QueryWeekdayService extends Service {

    private MyBinder myBinder=new MyBinder();
    public Context context;
    public QueryWeekdayService weekdayService;

    public class MyBinder extends IBindlInterface.Stub
    {
        @Override
        public String getWeekDay(int year, int month, int day) throws RemoteException {
            return getWeekday(year, month, day);
        }
    }

    public QueryWeekdayService() {
        Log.d("TAG","QueryWeekdayService我被创建了");
        //weekdayService=this;
    }

    public String getWeekday(int year, int month, int day)
    {
        Calendar calendar=Calendar.getInstance();
        calendar.set(year, month, day);
       // Log.d("TAG",calendar.getTime().toString());
        return judgeWeekday(calendar);
    }

    private String judgeWeekday(Calendar c)
    {
        String Week="星期";
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += "天";
        }
         else if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += "一";
        }
        else if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += "二";
        }
        else if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += "三";
        }
        else if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += "四";
        }
        else if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += "五";
        }
        else if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += "六";
        }
        else
            Week=null;
        return Week;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TAG","Service--onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        Log.d("TAG","QueryWeekdayService--onBind--");
        return myBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("TAg","--onUnbind--");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("TAg","QueryWeekdayService--onDestroy--");
    }
}
