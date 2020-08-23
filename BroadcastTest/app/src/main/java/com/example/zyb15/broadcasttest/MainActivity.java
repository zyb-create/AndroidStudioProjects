package com.example.zyb15.broadcasttest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class MainActivity extends Activity {

    private EditText editText_receiver;
    public static StudentRecordBroadcastReceiver.StudentRecordEvent studentRecordEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(MainActivity.this, ClipboardMonitorService.class));
        editText_receiver = findViewById(R.id.editText_receiver);

        studentRecordEvent = new StudentRecordBroadcastReceiver.StudentRecordEvent() {
            @Override
            public void onReceive(String id) {
                editText_receiver.setText(id);
            }
        };
    }
}
