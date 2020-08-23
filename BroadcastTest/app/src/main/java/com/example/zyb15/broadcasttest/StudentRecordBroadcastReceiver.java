package com.example.zyb15.broadcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class StudentRecordBroadcastReceiver extends BroadcastReceiver {
    public StudentRecordEvent studentRecordEvent = MainActivity.studentRecordEvent;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action == ClipboardMonitorService.ACTION_ID){
           LayoutInflater factory =  (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           final View activity_main = factory.inflate(R.layout.activity_main, null);
           EditText editText_receiver = activity_main.findViewById(R.id.editText_receiver);
           editText_receiver.setText(intent.getStringExtra("id"));
           studentRecordEvent.onReceive(intent.getStringExtra("id"));
        }
    }
    public interface StudentRecordEvent{
        void onReceive(String id);
    }
}
