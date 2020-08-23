package com.example.zyb15.studentmgr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Objects;

/**
 * Created by zyb15 on 2020/4/6.
 */

public class phoneAddress implements TextWatcher, View.OnClickListener {

    private static final String namespaceAddress="http://WebXml.com.cn/";
    private static final String urlAddress="http://ws.webxml.com.cn/WebServices/MobileCodeWS.asmx";
    private static final String methodName="getMobileCodeInfo";
    private static final String soapAction="http://WebXml.com.cn/getMobileCodeInfo";
    public EditText editText;
    public TextView textView;
    public Button button;
    public Context context;
    private Handler handler;

    @SuppressLint("HandlerLeak")
    public phoneAddress(EditText editText, final TextView textView, Button button, Context context)
    {
        this.editText=editText;
        this.textView=textView;
        this.button=button;
        this.context=context;
        button.setOnClickListener(this);
        editText.addTextChangedListener(this);
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                //super.handleMessage(msg);
                switch (msg.what)
                {
                    case 0x11:
                        Bundle bundle=msg.getData();
                        if(bundle==null)
                            break;
                        textView.setText(bundle.getString("message"));
                        break;
                    default:break;
                }
            }
        };
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if(s.length()>4&&s.length()<12)
            jiSuan();
        else if(s.length()>=12)
            textView.setText("号码格式不对，无法获取信息！");
        else
            textView.setText("");
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @SuppressLint("ShowToast")
    @Override
    public void onClick(View v) {
        if(editText.getText().toString().trim().length()<7||editText.getText().toString().trim().length()>11)
        {
            Toast.makeText(context.getApplicationContext(),"号码格式不对，无法获取信息！",Toast.LENGTH_SHORT).show();
            editText.setText("");
        }
        else if(Objects.equals(editText.getText().toString().trim(), ""))
        {
            Toast.makeText(context.getApplicationContext(),"不能为空！",Toast.LENGTH_SHORT).show();
            editText.setText("");
        }
        else
            jiSuan();
    }

    private String getTelINfo(String message)
    {
        ///////////////////////相当于把信息封装成XML文件
        SoapObject soapObject=new SoapObject(namespaceAddress,methodName);
        soapObject.addProperty("mobileCode",message);
        soapObject.addProperty("useID","");

        ///////////////////////创建文件袋并把XML文件装进去
        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut=soapAction;
        envelope.dotNet=true;
        envelope.setOutputSoapObject(soapObject);

        /////////////////////通过HTTP协议进行数据交换
        HttpTransportSE se=new HttpTransportSE(urlAddress);
        try {
            se.call(soapAction,envelope);
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }

        //////////////////////从文件袋中取出信息
        return ((SoapObject)envelope.bodyIn).getProperty(0).toString();
    }

    private void jiSuan()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message=new Message();
                message.what=0x11;
                Bundle bundle=new Bundle();
                bundle.putString("message",getTelINfo(editText.getText().toString().trim()));
                message.setData(bundle);
                handler.sendMessage(message);
            }
        }).start();
    }
}
