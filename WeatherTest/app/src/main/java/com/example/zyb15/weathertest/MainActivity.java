package com.example.zyb15.weathertest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private static final String namespace = "http://WebXml.com.cn/";
    private static final String transUrl = "http://ws.webxml.com.cn/WebServices/WeatherWS.asmx";
    private static final String method = "getWeather";
    final String userID = "0cb7eccfaf714bbb9212461b1ae43477";
    private ListView listView_weather;
    private List<Weather> weatherList;
    private TextView textView_local;
    private String cityName = "镇江";
    private EditText editText_dialog;
    private static final int TEMPERATURE_INDEX_TODAY = 8;   //今天  最低温度/最高温度
    private Thread weatherGetThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                weatherList = new ArrayList<>();
                weatherList.add(getWeatherToday(cityName));
                weatherList = getMoreWeather(cityName, weatherList);
                Message message = new Message();
                message.what = 0x11;
                message.obj = weatherList;
                handler.sendMessage(message);
            }catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            }
        }
    });
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0x11:
                    List<Weather> list = (List<Weather>)msg.obj;
                    Weather weather_today = list.get(0);    list.remove(0);
                    TextView textView_date_weather = findViewById(R.id.textView_date_weather);
                    textView_date_weather.setText(weather_today.getDate() + " - " + weather_today.getWeather());
                    TextView textView_temperature = findViewById(R.id.textView_temperature);
                    textView_temperature.setText(weather_today.getTemperature());
                    ImageView imageView_today = (ImageView)findViewById(R.id.imageView_today);
                    imageView_today.setImageResource(weather_today.getImg1());

                    WeatherAdapter adapter = new WeatherAdapter(MainActivity.this, R.layout.weather_item, list);
                    listView_weather.setAdapter(adapter);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + msg.what);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView_weather = findViewById(R.id.listView_weather);
        textView_local = findViewById(R.id.textView_local);
        textView_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("输入要查询的城市");
                builder.setView(editText_dialog = new EditText(MainActivity.this));
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cityName = editText_dialog.getText().toString();
                        if(cityName.length() == 0)
                            cityName = "镇江";
                        textView_local.setText(cityName);
                        updateWeather();
                    }
                });
                builder.show();
            }
        });
        updateWeather();
    }

    private int switchImageID(String data){
        int id = Integer.MIN_VALUE;
        switch (data){
            case "0.gif":
                id=R.drawable.i_0;
                break;
            case "1.gif":
                id=R.drawable.i_1;
                break;
            case "2.gif":
                id=R.drawable.i_2;
                break;
            case "3.gif":
                id=R.drawable.i_3;
                break;
            case "4.gif":
                id=R.drawable.i_4;
                break;
            case "5.gif":
                id=R.drawable.i_5;
                break;
            case "6.gif":
                id=R.drawable.i_6;
                break;
            case "7.gif":
                id=R.drawable.i_7;
                break;
            case "8.gif":
                id=R.drawable.i_8;
                break;
            case "9.gif":
                id=R.drawable.i_9;
                break;
            case "10.gif":
                id=R.drawable.i_10;
                break;
            case "11.gif":
                id=R.drawable.i_11;
                break;
            case "12.gif":
                id=R.drawable.i_12;
                break;
            case "13.gif":
                id=R.drawable.i_13;
                break;
            case "14.gif":
                id=R.drawable.i_14;
                break;
            case "15.gif":
                id=R.drawable.i_15;
                break;
            case "16.gif":
                id=R.drawable.i_16;
                break;
            case "17.gif":
                id=R.drawable.i_17;
                break;
            case "18.gif":
                id= R.drawable.i_18;
                break;
            case "19.gif":
                id=R.drawable.i_19;
                break;
            case "20.gif":
                id=R.drawable.i_20;
                break;
            case "21.gif":
                id=R.drawable.i_21;
                break;
            case "22.gif":
                id=R.drawable.i_22;
                break;
            case "23.gif":
                id=R.drawable.i_23;
                break;
            case "24.gif":
                id= R.drawable.i_24;
                break;
            case "25.gif":
                id=R.drawable.i_25;
                break;
            case "26.gif":
                id=R.drawable.i_26;
                break;
            case "27.gif":
                id= R.drawable.i_27;
                break;
            case "28.gif":
                id=R.drawable.i_28;
                break;
            case "29.gif":
                id= R.drawable.i_29;
                break;
            case "30.gif":
                id= R.drawable.i_30;
                break;
            case "31.gif":
                id=R.drawable.i_31;
                break;
            default:
                break;
        }
        return id;
    }

    private List<Weather> getMoreWeather(String cityName, List<Weather> weatherList) throws IOException, XmlPullParserException {
        final HttpTransportSE ht = new HttpTransportSE(transUrl);
        ht.debug = true;
        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        SoapObject soapObject = new SoapObject(namespace, method);
        soapObject.addProperty("theCityCode", cityName);
        soapObject.addProperty("theUserID", userID);
        envelope.bodyOut = soapObject;
        ht.call(namespace + method, envelope);
        if (envelope.getResponse() != null) {
            SoapObject result = (SoapObject) envelope.bodyIn;
            SoapObject detail=(SoapObject)result.getProperty(method+"Result");
            int image_index = 15;
            int weather_index = 12;
            int temprature_index = TEMPERATURE_INDEX_TODAY + 5;
            for(int i : new int[5]){
                Weather weather = new Weather();
                weather.setImg1(switchImageID(detail.getProperty(image_index).toString()));  image_index += 1;
                weather.setImg2(switchImageID(detail.getProperty(image_index).toString()));  image_index += 4;
                String date_weather = detail.getProperty(weather_index).toString();
                String[] s = date_weather.split(" ");
                weather.setDate(s[0]);
                weather.setWeather(s[1]);   weather_index += 5;
                weather.setTemperature(detail.getProperty(temprature_index).toString());    temprature_index += 5;
                weatherList.add(weather);
            }
        }
        return weatherList;
    }

    private Weather getWeatherToday(String cityName) throws IOException, XmlPullParserException {
        final HttpTransportSE ht = new HttpTransportSE(transUrl);
        ht.debug = true;
        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        SoapObject soapObject = new SoapObject(namespace, method);
        soapObject.addProperty("theCityCode", cityName);
        soapObject.addProperty("theUserID", userID);
        envelope.bodyOut = soapObject;
        ht.call(namespace + method, envelope);
        Weather weather = new Weather();
        if (envelope.getResponse() != null) {
            SoapObject result = (SoapObject) envelope.bodyIn;
            SoapObject detail = (SoapObject) result.getProperty(method + "Result");
            weather.setImg1(switchImageID(detail.getProperty(10).toString()));
            weather.setImg2(switchImageID(detail.getProperty(11).toString()));
            String[] s = detail.getProperty(7).toString().split(" ");
            weather.setDate(s[0]);
            weather.setWeather(s[1]);
            weather.setTemperature(detail.getProperty(TEMPERATURE_INDEX_TODAY).toString());
            for(int i=0;i<detail.getPropertyCount();i++){
                System.out.println("--- " + i + " : " + detail.getProperty(i).toString());
            }
        }
        return weather;
    }

    private void updateWeather(){
        weatherGetThread.start();
    }

}
