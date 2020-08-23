package com.example.zyb15.studentmgr;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.lang.Thread.sleep;

public class ActivityMain extends Activity {

    public static List<Map<String,Object>> list=new ArrayList<>();
    private static int tag=1;
    public static StudentAdapter adapter;
    public GestureDetector detector;
    public static StudentDAL studentDAL=null;
    public static Bitmap bitmap;
    public static PlayRecord playRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playRecord=new PlayRecord(this);
        studentDAL=new StudentDAL(this,"Student");

        detector=new GestureDetector(this,new FlowLR(ActivityMain.this,ActivityStudent.class));
        detector.setIsLongpressEnabled(false);

//        if(tag==1) {
//            Init2();
//            tag=0;
//        }
        //Init();
        //getMessage();
        Init2();
        //list=getSortList(list);
        adapter=new StudentAdapter(this,R.layout.stu_item,list);
        ListView listView=(ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);

        ////////设置ListView点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                @SuppressLint("InflateParams") View v =  getLayoutInflater().inflate(R.layout.dialog_one,null);  /////////若自定义某界面，需先获得其View
                @SuppressLint("InflateParams") View v1 =  getLayoutInflater().inflate(R.layout.activity_student,null);  /////////若自定义某界面，需先获得其View
                showStudent(v,v1,ActivityMain.this,position);
            }
        });
        //////////长按弹出菜单
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {        //this.registerForContextMenu(listView);
                menu.add(0, 0, 0, "修改");
                menu.add(0, 1, 0, "删除");
            }
        });
        /////////////////////////////////////////////////////////////////////
    }

    //////////////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {     //创建菜单
        //getMenuInflater().inflate(R.menu.menu_one,menu);
        //new MenuInflater(getApplication()).inflate(R.menu.menu_one,menu);
        getMenuInflater().inflate(R.menu.menu_one,menu);
        return super.onCreateOptionsMenu(menu);
    }

    ///////////////////////////////////////////////////

    @SuppressLint("SetTextI18n")
    public static void showStudent(View v, View v1, Context context, int position)
    {

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        Student student=(Student) list.get(position).get("student");
        builder.setTitle(student.getStu_name());
        ((TextView)v.findViewById(R.id.textView)).setText("学号："+ student.getStu_no());
        ((TextView)v.findViewById(R.id.textView8)).setText("性别："+ student.getStu_sex());
        ((TextView)v.findViewById(R.id.textView9)).setText("学院："+ student.getStu_dep());
        ((TextView)v.findViewById(R.id.textView10)).setText("专业："+ student.getStu_zy());
        String string="";
        for(int i=0;i<student.getStu_hobby().size();i++)
        {
            string+=((CheckBox)v1.findViewById(student.getStu_hobby().get(i))).getText().toString();
            if(i==student.getStu_hobby().size()-1)
                break;
            string+="、";
        }
        ((TextView)v.findViewById(R.id.textView11)).setText("爱好："+string );
        ((TextView)v.findViewById(R.id.textView12)).setText("生日："+ getTime(student.getBirth()[0],student.getBirth()[1],student.getBirth()[2]));
        ((TextView)v.findViewById(R.id.text_introduce)).setText("介绍："+ student.getStu_introduce());
        //builder.setMessage("please choose yes or no?");
        //setDialodButton(builder);
        builder.setView(v);   //可以把按钮设置成透明，只留出来汉字
        builder.show();
    }

    public static String getTime(int year,int month,int days)
    {
        Calendar calendar=Calendar.getInstance();  //获取当前时间对象
        calendar.set(year,month,days);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(calendar.getTime());
    }

    @Override////////////上下文菜单被点击
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId())
        {
            case 1:deleteStu(menuInfo.position,ActivityMain.this); break;
            case 0: updateStu(menuInfo.position,ActivityMain.this);break;
            default:return false;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {       //菜单被点击
        Intent intent;
        switch (item.getItemId())
        {
            case R.id.actionbar_search:
                intent=new Intent(ActivityMain.this,ActivityGetSearch.class);
                startActivity(intent);
                break;
            case R.id.settings:
                intent=new Intent(ActivityMain.this,ActivityConfig.class);
                startActivity(intent);
                break;
            case R.id.actionbar_refresh: break;
            case R.id.actionbar_add:
                if(ActivityStudent.instance!=null)
                    ActivityStudent.instance.finish();
                intent = new Intent(ActivityMain.this, ActivityStudent.class);
                startActivity(intent);
                break;
            case R.id.actionbar_telSearch:
                intent = new Intent(ActivityMain.this, ActivityPhonePlace.class);
                startActivity(intent);
                break;
            case R.id.record:
                intent = new Intent(ActivityMain.this, ActivityRecord.class);
                startActivity(intent);
                break;
            default:return false;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void deleteStu(int position , Context context)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage("确认要删除吗？");
        setDialodButton(builder,position,context);
        //builder.setView(v);   //可以把按钮设置成透明，只留出来汉字
        builder.show();
    }

//    ///////////////////////////////////////////////////////////////
//    @Override
//    public Resources getResources() {
//        Resources resources=super.getResources();
//        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
//            Configuration config = resources.getConfiguration();
//            config.fontScale=MyApplication.getTextSize();
//            resources.updateConfiguration(config,resources.getDisplayMetrics());
//        }
//        return resources;
//    }
//
//    ////////////////////////////////////////////////////////////////
    public void updateStu(int position, Context context)
    {
        if(ActivityStudent.instance!=null)
            ActivityStudent.instance.finish();
        Intent intent=new Intent(context,ActivityStudent.class);
        //Student student=(Student)(list.remove(position).get("student"));
        Map<String,Object> map=list.remove(position);
        //adapter.notifyDataSetChanged();
        //adapter.notifyDataSetChanged(); //更新listView
        Bundle bundle=new Bundle();
        Student student=(Student)map.get("student");
        //student.setBitmap((Bitmap)map.get("photo"));
        bundle.putSerializable("student",student);
        ActivityMain.bitmap=(Bitmap)map.get("photo");
        //bundle.putParcelable("photo",(Bitmap)map.get("photo"));
        //bundle.putSerializable("stu",(Serializable) map);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void Init()  //链表初始化
    {
//        ArrayList<Integer> arrayList=new ArrayList<Integer>();
//        arrayList.add(R.id.checkBox2);
//        arrayList.add(R.id.checkBox4);
//        Student s=new Student("张小凡",R.mipmap.man,"3170212001","男","计算机学院","软件工程",arrayList, new int[]{1998, 1, 23},"我很笨");
//        list.add(s);
//        ArrayList<Integer> arrayList2=new ArrayList<Integer>();
//        arrayList2.add(R.id.checkBox);
//        arrayList2.add(R.id.checkBox3);
//        Student s1=new Student("宁缺",R.mipmap.man,"3170212010","男","电气学院","电机工程",arrayList2, new int[]{1999, 3, 11},"我很好");
//        list.add(s1);
//        ArrayList<Integer> arrayList3=new ArrayList<Integer>();
//        arrayList3.add(R.id.checkBox);
//        arrayList3.add(R.id.checkBox4);
//        Student s2=new Student("范若若",R.mipmap.lady,"3170212026","女","计算机学院","通信工程",arrayList3,new int[]{1998, 9, 14},"我很坏");
//        list.add(s2);
//
//        for(Student student : list)
//            studentDAL.insertStudent(student);
        //helper.close();
    }
    public void Init2()  //数据库链表初始化
    {
        list.clear();
//        String sql="select * from student";
//        @SuppressLint("Recycle") Cursor cursor=database.rawQuery(sql,null);//new String[]{});
//        while (cursor.moveToNext())
//        {
//            String string=cursor.getString(1);
//            Student student=gson.fromJson(string,Student.class);
//            list.add(student);
//        }
        list.addAll(studentDAL.findAllStudentsWithBitmap());
        //adapter.notifyDataSetChanged();
    }

    public static void setDialodButton(AlertDialog.Builder builder, final int position, final Context context)
    {
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(ActivityGetSearch.studentList==null||ActivityGetSearch.studentList.isEmpty()||ActivityGetSearch.adapter==null)
                    ;
                else
                {
                    try{
//                        ActivityGetSearch.studentList.remove(ActivityGetSearch.getPosition(ActivityGetSearch.studentList,list,position));
//                        ActivityGetSearch.adapter.notifyDataSetChanged();
                    }catch (Exception e){System.out.println("发生错误");}

                }
                studentDAL.deleteStudent(((Student)list.remove(position).get("student")).getStu_no());
               // list=getSortList(list);
                adapter.notifyDataSetChanged();
                playRecord.doPlay("Music","delete");
                setToast(R.mipmap.right,"删除成功",context);

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
    }

    public static List<Student> getSortList(List<Student> list)
    {
        Collections.sort(list, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return o1.getStu_name().compareTo(o2.getStu_name());
            }
        });
        return list;
    }

    public static void setToast(int id,String s,Context context)
    {
        Toast toast=new Toast(context);
        toast.setGravity(Gravity.CENTER,0,0);
        //@SuppressLint("InflateParams") View view=getLayoutInflater().inflate(R.layout.toast_one,null);  //getLayoutInflater()不能是static
        View view= LayoutInflater.from(context).inflate(R.layout.toast_one,null);
        ((ImageView)view.findViewById(R.id.toast_imageview)).setImageResource(id);
        ((TextView)view.findViewById(R.id.toast_textview)).setText(s);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /*@Override
    public boolean onTouchEvent(MotionEvent event) {      //将点击事件传入手势分析器
        Log.d("TAG","onTouchEvent");
        return detector.onTouchEvent(event);
    }*/

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //Log.d("TAG","dispatchTouchEvent");
        if(detector.onTouchEvent(ev))
            return detector.onTouchEvent(ev);
        //detector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
        //return onTouchEvent(ev);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        studentDAL.close();
//        if(database!=null)
//            database.close();
//        if(helper!=null)
//            helper.close();
    }
}
