package com.example.zyb15.studentmgr;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zyb15 on 2020/4/19.
 */

public class StudentDAL {

    private StudentDBHelper helper=null;
    private SQLiteDatabase database=null;
    private Gson gson=null;

    public StudentDAL(Context context,String name)
    {
        helper=new StudentDBHelper(context,name,null,1);  //目前数据库版本是1
        database=helper.getReadableDatabase();
        gson=new Gson();
    }

    public List<Student> findAllStudents()
    {
        List<Student> list=new ArrayList<>();
        String sql="select * from student";
        @SuppressLint("Recycle") Cursor cursor=database.rawQuery(sql,null);//new String[]{});
        //Log.d("TAG",cursor.getCount()+"");
        try{
            while (cursor.moveToNext())
            {
                String string=cursor.getString(cursor.getColumnIndex("stu_message"));
                Student student=gson.fromJson(string,Student.class);
                list.add(student);
                //Log.d("TAG",student.getStu_name());
            }
        }catch (Exception e){Log.d("TAG","bad");}

        return list;
    }

    public List<Map<String,Object>>findAllStudentsWithBitmap()
    {
        List<Map<String,Object>> list=new ArrayList<>();
        String sql="select * from student";
        @SuppressLint("Recycle") Cursor cursor=database.rawQuery(sql,null);//new String[]{});
        //Log.d("TAG",cursor.getCount()+"");
        try{
            while (cursor.moveToNext())
            {
                String string=cursor.getString(cursor.getColumnIndex("stu_message"));
                Student student=gson.fromJson(string,Student.class);
                Map<String,Object> map=new HashMap<>();
                map.put("student",student);
                byte[] photo=cursor.getBlob(cursor.getColumnIndex("stu_image"));
                if (photo!=null)
                {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(photo, 0,photo.length, null);
                    map.put("photo",bitmap);
                }else
                    map.put("photo",null);
                list.add(map);
                //Log.d("TAG",student.getStu_name());
            }
        }catch (Exception e){Log.d("TAG","bad");}

        return list;
    }

    public Student findStudentByStuNo(String no)
    {
        Student student=null;
        String sql="select * from student where stu_no = ?";
        @SuppressLint("Recycle") Cursor cursor=database.rawQuery(sql,new String[]{no});
        if(cursor.moveToNext())
            student=gson.fromJson(cursor.getString(cursor.getColumnIndex("stu_message")),Student.class);
        return student;
    }

    public boolean updateStudent(Student student)
    {
//        String sql="update student\n" +
//                "set stu_message=? \n" +
//                "where stu_no=? ";
        String str=gson.toJson(student);
        ContentValues values=new ContentValues();
        values.put("stu_message",str);
        int result=database.update("student",values,"stu_no=?",new String[]{student.getStu_no()});
        return result >= 1;
    }

    public boolean updateStudentWithBitmap(Student student, Bitmap bitmap)
    {
        String str=gson.toJson(student);
        ContentValues values=new ContentValues();
        values.put("stu_message",str);
        values.put("stu_image",getByte(bitmap));
        int result=database.update("student",values,"stu_no=?",new String[]{student.getStu_no()});
        return result >= 1;
    }

    public boolean insertStudent(Student student)
    {
//        String sql="insert into student(stu_no,stu_message) values(?,?)";
        String message=gson.toJson(student);
        ContentValues values=new ContentValues();
        values.put("stu_message",message);
        values.put("stu_no",student.getStu_no());
        long result=database.insert("student",null,values);
        return result!=-1;
    }

    public boolean insertStudentWithBitmap(Student student, Bitmap bitmap)
    {
        String message=gson.toJson(student);
        ContentValues values=new ContentValues();
        values.put("stu_message",message);
        values.put("stu_no",student.getStu_no());
        values.put("stu_image",getByte(bitmap));
        long result=database.insert("student",null,values);
        return result!=-1;
    }

    public boolean deleteStudent(String no)
    {
        int result=database.delete("student","stu_no=?",new String[]{no});
        return result>=1;
    }

    public void close()
    {
        if(database!=null)
            database.close();
        if(helper!=null)
            helper.close();
    }

    private byte[] getByte(Bitmap bitmap)
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        byte[] photo=os.toByteArray();
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return photo;
    }

}
