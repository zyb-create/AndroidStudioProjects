package com.example.zyb15.studentmgr;

import android.app.AlertDialog;
import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;

/**
 * Created by zyb15 on 2020/4/15.
 */

public class MyListPreference extends ListPreference {
    private Context context;
    public MyListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        ListPreferenceAdapter adapter=new ListPreferenceAdapter(this.context,R.layout.stu_item);
        builder.setAdapter(adapter,null);
        super.onPrepareDialogBuilder(builder);/////调用父类的这个方法，点击列表项会关闭对话框
    }
}
