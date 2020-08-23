package com.example.zyb15.weathertest;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.URL;

@SuppressLint("Registered")
public class WeatherService extends Service {
    private Weather weather;
    private URL url;
    private static final String namespace = "http://WebXml.com.cn/";
    private static final String transUrl = "http://ws.webxml.com.cn/WebServices/WeatherWS.asmx";
    private static final String method = "getWeather";
    private static final int envolopeVersion = SoapEnvelope.VER12;
    /*never use*/
    private String soapAction = "http://WebXml.com.cn/getWeatherbyCityName";

    public WeatherService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateWeather();
            }
        }).start();
    }

    public void updateWeather(){

    }

    private static SoapObject getWeatherByCity(String city){
        System.out.println("--- getWeatherByCity");
        HttpTransportSE httpTransportSE = new HttpTransportSE(transUrl);
        httpTransportSE.debug = true;
        SoapSerializationEnvelope mEnvelope = new SoapSerializationEnvelope(envolopeVersion);
        mEnvelope.dotNet = true;
        SoapObject soapObject = new SoapObject(namespace, method);
        soapObject.addProperty("theCityName", city);
        mEnvelope.bodyOut = soapObject;
        try {
            httpTransportSE.call(namespace+method, mEnvelope);
            if(mEnvelope.getResponse()!=null){
                SoapObject result = (SoapObject)mEnvelope.bodyIn;
                SoapObject detail = (SoapObject)result.getProperty(method + "Result");
                System.out.println("--- " + detail);
                return detail;
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
