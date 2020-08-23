package com.example.zyb15.otherapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class NetworkMonitorService extends Service {

    private Handler handler;
    private boolean status=true;
    private boolean quit=true;
    private boolean thread=true;
    private NetworkBind networkBind=new NetworkBind();
    private ConnectivityManager connectivityManager;

    public NetworkMonitorService() {
    }

    public class NetworkBind extends Binder {
        public boolean getNetworkStatus(){return status;}
        public void openCheck()
        {
            if(!thread)
            {
                new Thread()
                {
                    @Override
                    public void run() {
                        autoCheckNetwork();
                    }
                }.start();
            }

        }
        public void closeCheck()
        {
            synchronized (this){quit=false;}
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        new Thread()
        {
            @Override
            public void run() {
                autoCheckNetwork();
            }
        }.start();
    }

    private void autoCheckNetwork()
    {
        synchronized (this){thread=true;}
        while (quit)
        {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (this)
            {
                status=networkStatus();
                Log.d("TAG","检测ing");
            };
        }
        if(!quit)
        {
            synchronized (this){thread=false;quit=true;}
            Log.d("TAG","quit=true;thread=false;");
        }
    }

    private boolean networkStatus()
    {
        assert connectivityManager != null;
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null && info.isAvailable();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d("TAG","NetworkMonitorService--onBind--");

        return networkBind;
    }


}
