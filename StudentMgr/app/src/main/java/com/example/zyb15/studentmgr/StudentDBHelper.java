package com.example.zyb15.studentmgr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by zyb15 on 2020/4/19.
 */

public class StudentDBHelper extends SQLiteOpenHelper {

    public Context context;
    public String SqliteFileName = "Student.db";
    @SuppressLint("SdCardPath")
    private String DB_PATH = "/data/data/com.example.zyb15.studentmgr/databases/";
    private String DB_NAME="Student.db";
//    final String CREATE_TABLE_SQL = "create table student(stu_no varchar(10) primary key" +
//            ", stu_name not null,stu_birth,stu_gender,stu_dep,stu_zhanye,stu_hobby,stu_introduce)";
    private String STUDENT_TABLE="CREATE TABLE student (\n" +
            "    stu_no        VARCHAR (10) PRIMARY KEY,\n" +
            "    stu_message                   NOT NULL\n" +
            ");";
    private String CREATE_TEMP_TABle="alter table student rename to _temp_table";
    private String RESTORE_TABLE="insert into student select stu_no,stu_message from _temp_table";
    private String INSERT_DATA = "insert into student select stu_no,'' stu_image,stu_message from _temp_table";
    private String DROP_TABLE = "drop table _temp_table";
    private String NEW_STUDENT_TABLE="CREATE TABLE student (\n" +
            "    stu_no        VARCHAR (10) PRIMARY KEY,\n" +
            "    stu_image       blob,\n" +
            "    stu_message                   NOT NULL\n" +
            ");";


    public StudentDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NEW_STUDENT_TABLE);//db.execSQL(STUDENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion>oldVersion)
        {
            db.beginTransaction();
            try{
                db.execSQL(CREATE_TEMP_TABle);
                db.execSQL(NEW_STUDENT_TABLE);
                db.execSQL(INSERT_DATA);
                db.execSQL(DROP_TABLE);
                db.setTransactionSuccessful();
                Log.d("TAG","onUpgrade 成功");
            }catch (Exception e){Log.d("TAG",e.toString()+"失败");}
            finally {
                db.endTransaction();
            }
        }

    }

    private void copyDataBase() throws IOException {      //从外部导入数据库
        try {
            InputStream ip =  context.getAssets().open("Student");
            Log.d("TAG","Input Stream...."+ip);
            String op=  DB_PATH  +  DB_NAME ;
            OutputStream output = new FileOutputStream(op);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = ip.read(buffer))>0){
                output.write(buffer, 0, length);
                //Log.i("Content.... ",length+"");
            }
            Log.d("TAG","copyDataBase()成功");
            output.flush();
            output.close();
            ip.close();
        }
        catch (IOException e) {
            Log.d("TAGr", e.toString()+"失败");
        }

    }
}
