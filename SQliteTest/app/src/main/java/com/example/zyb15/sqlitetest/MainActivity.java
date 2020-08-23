package com.example.zyb15.sqlitetest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.example.zyb15.sqlitetest.StudentDBHelper.DB_NAME;
import static com.example.zyb15.sqlitetest.StudentDBHelper.DB_PATH;

public class MainActivity extends Activity {

    StudentDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper=new StudentDBHelper(this,"Student.db",null,1);
        SQLiteDatabase db=helper.getReadableDatabase();

        //SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(this.getFilesDir()+"/"+DB_NAME,null);
        ArrayList<String> arrayList=new ArrayList<>();
        arrayList.add("100");
        arrayList.add("200");
        St st=new St();
        Gson gson = new Gson();
        //String inputString= gson.toJson(arrayList);
        String inputString= gson.toJson(st);

        try
        {
            String s="update student\n" +
                    "set stu_hobby=? \n" +
                    "where stu_no='3170212039' ";
            db.execSQL(s,new String[]{inputString});
            Log.d("TAG","db.execSQL(s,new String[]{inputString});");
            //db.execSQL("insert into student(stu_no,stu_name,stu_gender) values('3170212039','张颜兵','男')");
        }catch (Exception e){}

        String sql="select * from student where stu_no = ?";
        @SuppressLint("Recycle") Cursor cursor=db.rawQuery(sql,new String[]{"3170212039"});
        StringBuilder message= new StringBuilder();
        String out="";
        while (cursor.moveToNext())
        {
            message.append(cursor.getString(0)).append(cursor.getString(2)).append(cursor.getString(3));
            out=cursor.getString(6);
        }
        //Type type = new TypeToken<St>() {}.getType();
        St finalOutputString = gson.fromJson(out, St.class);
        ((TextView)findViewById(R.id.test)).setText(finalOutputString.age);
//        Type type = new TypeToken<ArrayList<String>>() {}.getType();
//        ArrayList<String> finalOutputString = gson.fromJson(out, type);
//        ((TextView)findViewById(R.id.test)).setText(finalOutputString.get(0));
        db.close();
        helper.close();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
