package com.example.zyb15.studentmgr;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityPhonePlace extends Activity {

    private Handler handler;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_place);

        //////////////////////////////////////////////////////////////////////////////
        phoneAddress phoneAddress=new phoneAddress((EditText)findViewById(R.id.telNumber),(TextView)findViewById(R.id.GuiShuDi),(Button)findViewById(R.id.search_Place),ActivityPhonePlace.this);
        //////////////////////////////////////////////////////////////////////////////
        ((Button)findViewById(R.id.copy_message)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message="";
                if(((TextView)findViewById(R.id.GuiShuDi)).getText().toString().trim().equals(""))
                    message="复制失败";
                else
                {
                    ClipboardManager clipboardManager=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                    ClipData clipData=ClipData.newPlainText("address",((TextView)findViewById(R.id.GuiShuDi)).getText().toString().trim());
                    assert clipboardManager != null;
                    clipboardManager.setPrimaryClip(clipData);
                    message="复制成功";
                }
                Toast toast= Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });
        ////////////////////////////////////////////////////////////////////////////////
        findViewById(R.id.GuiShuDi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getExtras()==null)
                    return;
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putString("PhoneAddress",((TextView)findViewById(R.id.GuiShuDi)).getText().toString().trim());
                try{
                    intent.putExtras(bundle);
                    setResult(Activity.RESULT_OK,intent);
                    ActivityPhonePlace.this.finish();
                }catch (Exception ignored){}

            }
        });
        /////////////////////////////////////////////////////////////////////////////////
    }
}
