package com.example.zyb15.studentmgr;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class ActivityConfig extends PreferenceActivity {

    public static Context context;
    public float textSize=(float)1.15;
    public static boolean FLAG=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences!=null)
            textSize=Float.parseFloat(sharedPreferences.getString("fontsize",String.valueOf(1.15)));
        super.onCreate(savedInstanceState);
        context=ActivityConfig.this;
        //setContentView(R.layout.activity_config);
        //addPreferencesFromResource(R.xml.mypre);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(FLAG)
        {
            FLAG=false;
            Intent intent=new Intent(ActivityConfig.this,ActivityAdvertising.class);
            intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
            if(ActivityStudent.instance!=null)
                ActivityStudent.instance.finish();
            startActivity(intent);
        }
    }

    @Override
    public Resources getResources() {
        Resources resources=super.getResources();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            Configuration config = resources.getConfiguration();
            config.fontScale=textSize;//MyApplication.getTextSize();
            resources.updateConfiguration(config,resources.getDisplayMetrics());
        }
        return resources;
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pre_header,target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return true;
    }

    public static class PF1 extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        public EditTextPreference editTextPreference1;
        public EditTextPreference editTextPreference2;
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pf1);
            editTextPreference1=(EditTextPreference)findPreference("user_no");
            editTextPreference2=(EditTextPreference)findPreference("user_password");
            editTextPreference1.setOnPreferenceChangeListener(this);
            editTextPreference2.setOnPreferenceChangeListener(this);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if(preference==editTextPreference1)
            {
                editTextPreference1.setSummary((CharSequence)newValue);
            }
            else if(preference==editTextPreference2)
                editTextPreference2.setSummary((CharSequence)newValue);
            return true;
        }
    }
    public static class PF2 extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        public SharedPreferences settings;
       // public SwitchPreference switchPreference;
        public ListPreference listPreference;
        public static float textSize=(float)1.15;
        //public static boolean tag=false;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pf2);
            settings = PreferenceManager.getDefaultSharedPreferences(ActivityConfig.context);
            textSize= Float.parseFloat(settings.getString("fontsize",String.valueOf(1.15)));
            listPreference=(ListPreference)findPreference("fontsize");
            listPreference.setSummary((CharSequence)("当前字体大小为："+ getSize(String.valueOf(textSize))));
            listPreference.setOnPreferenceChangeListener(this);
        }


        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if(preference==listPreference)
            {
                listPreference.setSummary((CharSequence)("当前字体大小为："+ getSize((String) newValue)));
                ActivityConfig.FLAG=true;
                Toast toast= Toast.makeText(ActivityConfig.context,"按返回键更新字体大小",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
            return true;
        }

        public String getSize(String str)
        {
            switch (str.trim())
            {
                case "0.85":return "1";
                case "1":return "2";
                case "1.15":return "3";
                case "1.3":return "4";
                case "1.45":return "5";
                case "1.0":return "2";
                default:break;
            }
            return "";
        }


//        @Override
//        public boolean onPreferenceClick(Preference preference) {  //对于listPreference是点击之前触发，对于switchPreference是点击之后触发


    }
    public static class PF3 extends PreferenceFragment
    {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pf3);
        }

    }


}
