package com.example.zyb15.sqlitetest;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by zyb15 on 2020/4/18.
 */

public class StudentDBHelper extends SQLiteOpenHelper {

    public Context context;
    public String SqliteFileName = "Student.db";
    @SuppressLint("SdCardPath")
    public static String DB_PATH = "/data/data/com.example.zyb15.sqlitetest/databases/";
    public static String DB_NAME="Student.db";
    final String CREATE_TABLE_SQL = "create table student(stu_no varchar(10) primary key" +
            ", stu_name not null,stu_birth,stu_gender,stu_dep,stu_zhanye,stu_hobby,stu_introduce)";

    public StudentDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
//        try {
//            copyDataBase();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void copyDataBase() throws IOException {
//        if (!(new File(DB_PATH+DB_NAME).exists()))
//        {
//
//        }
        try {
            InputStream ip =  context.getAssets().open("Student.db");
            Log.d("TAG","Input Stream...."+ip);
            String op=  DB_PATH  +  DB_NAME ;
            OutputStream output = new FileOutputStream(op);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = ip.read(buffer))>0){
                output.write(buffer, 0, length);
                //Log.i("Content.... ",length+"");
            }
            Log.d("TAG","good");
            output.flush();
            output.close();
            ip.close();
        }
        catch (IOException e) {
            Log.d("error", e.toString());
        }

    }




}
