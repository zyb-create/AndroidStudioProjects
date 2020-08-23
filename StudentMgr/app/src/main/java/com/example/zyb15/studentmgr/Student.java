package com.example.zyb15.studentmgr;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

public class Student implements Serializable{
    private String Stu_name;
    private int Stu_id;
    private String Stu_no;
    private String Stu_sex;
    private String Stu_dep;
    private  String Stu_zy;
    private ArrayList<Integer> Stu_hobby;
    private int []birth;
    private String Stu_introduce;
    private Bitmap bitmap;

    public Student(String stu_name,int stu_id,String stu_no,String stu_sex,String stu_dep,String stu_zy,ArrayList<Integer> stu_hobby,int []birth,String stu_introduce)
    {
        this.Stu_id=stu_id;
        this.Stu_name=stu_name;
        this.Stu_no=stu_no;
        this.Stu_sex=stu_sex;
        this.Stu_dep=stu_dep;
        this.Stu_zy=stu_zy;
        this.Stu_hobby=stu_hobby;
        this.birth=birth;
        this.Stu_introduce=stu_introduce;
    }

    public int getStu_id() {
        return Stu_id;
    }

    public String getStu_name() {
        return Stu_name;
    }

    public String getStu_no() {
        return Stu_no;
    }

    public String getStu_sex() {
        return Stu_sex;
    }

    public String getStu_dep() {
        return Stu_dep;
    }

    public String getStu_zy() {
        return Stu_zy;
    }

    public ArrayList<Integer> getStu_hobby() {
        return Stu_hobby;
    }

    public int[] getBirth() {
        return birth;
    }

    @Override
    public boolean equals(Object obj) {
        return this.getStu_name().equals(((Student)obj).getStu_name())&&this.getStu_no().equals(((Student)obj).getStu_no());
    }

    public String getStu_introduce() {
        return Stu_introduce;
    }

    public void setStu_introduce(String stu_introduce) {
        Stu_introduce = stu_introduce;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
