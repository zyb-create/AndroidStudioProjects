package com.example.zyb15.broadcasttest;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SuppressLint("Registered")
@SuppressWarnings("deprecation")
public class ClipboardMonitorService extends Service {
    private ClipboardManager clipboardManager;
    public final static String ACTION_ID = "HAS_STUDENT_RECORD";

    public ClipboardMonitorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.addPrimaryClipChangedListener(new PrimaryClipChangedListener());
        super.onCreate();
    }

    private class PrimaryClipChangedListener implements ClipboardManager.OnPrimaryClipChangedListener{
        @Override
        public void onPrimaryClipChanged() {
            ClipData.Item item = clipboardManager.getPrimaryClip().getItemAt(0);
            String regex_id = "[sS][eE]\\d{6}$";
            Pattern pattern = Pattern.compile(regex_id);
            Matcher isId = pattern.matcher(item.getText().toString());
            if(isId.matches()) {
                Intent intent = new Intent();
                intent.setAction(ACTION_ID);
                intent.putExtra("id", item.getText().toString());
                sendBroadcast(intent);
            }

        }

    }
}
