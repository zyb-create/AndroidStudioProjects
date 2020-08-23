package com.example.zyb15.studentmgr;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;

import static android.R.layout.simple_spinner_item;
public class ActivityStudent extends Activity {

    Button button;
    //String s;
    Spinner spinner;
    ArrayAdapter adapter1;
    ArrayAdapter adapter2;
    public Bundle bundle;
    private int year=2000;
    private int month=0;
    private int days=1;
    public int tag=0;  //判断字体是否已设置的标志
    public boolean TAG=false;  //判断是否将该页面销毁
    public static ActivityStudent instance=null;
    public GestureDetector detector;
    public String FileNamePath="/storage/emulated/legacy/Download/flagCache/introduce.txt";
    public EditText editText;
//    public StudentDBHelper helper;
//    public SQLiteDatabase database;
//    public Gson gson;
    public StudentDAL studentDAL;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        getActionBar().hide();
        instance=this;
        detector=new GestureDetector(this,new FlowLR(ActivityStudent.this,ActivityMain.class));
        SharedPreferences sharedPreferences=getSharedPreferences("com.example.zyb15.studentmgr_preferences",MODE_PRIVATE);
        if(sharedPreferences!=null)
            if(sharedPreferences.getBoolean("turnonoffscreen",false))
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        studentDAL=new StudentDAL(this,"Student");
        new HandleStudentPhoto((ImageView) findViewById(R.id.stu_photo),this);

        editText= findViewById(R.id.editText_introduce);


        ((Button)findViewById(R.id.button)).setText("提交");
       // ((RadioButton) findViewById(R.id.radioButton)).setChecked(true);
        ////////////////////////////////////////////////////////////////////////////
        String []strings1={"软件工程","信息安全","物联网"};
        String []strings2={"电气工程","电机工程","自动化"};
        adapter1 = new ArrayAdapter(this,simple_spinner_item, strings1);
        adapter2 = new ArrayAdapter(this,simple_spinner_item, strings2);
        ((Spinner)findViewById(R.id.spinner2)).setAdapter(adapter1);
        spinner=(Spinner)findViewById(R.id.spinner1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position==0)
                    ((Spinner)findViewById(R.id.spinner2)).setAdapter(adapter1);
                else if(position==1)
                    ((Spinner)findViewById(R.id.spinner2)).setAdapter(adapter2);
                else
                    return;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Init();
        ///////////////////////////////////////////////////
        button= findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(((Button)findViewById(R.id.button)).getText().toString().equals("修改"))
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(ActivityStudent.this);
                    builder.setMessage("确认要修改吗？");
                    setDialodButton(builder);
                    builder.show();
                }
                else if (((Button)findViewById(R.id.button)).getText().toString().equals("提交")) {
                    TAG=true;
                    ImageView imageView=findViewById(R.id.stu_photo);
                    Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                    if(!((EditText)findViewById(R.id.editText2)).getText().toString().trim().equals(""))
                    {
                        if(studentDAL.insertStudentWithBitmap(upLode(),bitmap))
                        {
                            ActivityMain.playRecord.doPlay("Music","add");
                            ActivityMain.setToast(R.mipmap.right,"添加成功",ActivityStudent.this);
                        }
                        else
                            ActivityMain.setToast(R.mipmap.wrong,"添加失败",ActivityStudent.this);
                    }else
                        ActivityMain.setToast(R.mipmap.right,"学号不能为空",ActivityStudent.this);
                    tiaoZhuan();
                }
                else
                    return;

            }
        });
////////////////////////////////////////////////////
        ((EditText)findViewById(R.id.birth)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               setEditTextOnClick();
            }
        });
//////////////////////////////////////////////////////
        ((ImageView)findViewById(R.id.homepage)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tiaoZhuan();
//                Intent intent=new Intent(ActivityStudent.this,ActivityMain.class);
//                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
            }
        });
        //////////////////////////////////////////////
        ((Button)findViewById(R.id.getExternalFile)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.LAUNCHER");
                intent.addCategory("zyb.getIntroduce");
                startActivityForResult(intent,1001);
                //((EditText)findViewById(R.id.editText_introduce)).setText(read());
            }
        });
        //////////////////////////////////////////////
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(database!=null)
//            database.close();
//        if(helper!=null)
//            helper.close();
        studentDAL.close();
        //Log.d("TAG","onDestroy();");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId())
        {
            case 0: break;
            case 1:
                Intent intent=new Intent(ActivityStudent.this,ActivityDraw.class);
                startActivityForResult(intent,1002);
                break;
            default:return false;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case 1001:
                if(resultCode==1002)
                {
                    editText.setText(read(data.getExtras().getString("filepath","")));
                }
                break;
            case 1002:
//                if(data.getParcelableExtra("stu_photo")!=null)
//                    ((ImageView) findViewById(R.id.stu_photo)).setImageBitmap((Bitmap) data.getParcelableExtra("stu_photo"));
                if(ActivityDraw.bitmap!=null&&ActivityDraw.TAG)
                    ((ImageView) findViewById(R.id.stu_photo)).setImageBitmap(ActivityDraw.bitmap);
                break;
                default:break;
        }
    }

    public String read(String filepath)
    {
        @SuppressLint("SdCardPath") File root = new File("/mnt/sdcard/");
        // 如果 SD卡存在
        if (root.exists())
        {
            try {
                //Log.d("TAG",root.getCanonicalPath());
                FileInputStream fileInputStream=new FileInputStream(filepath);
                Log.d("TAG","FileInputStream fileInputStream=new FileInputStream(root.getCanonicalPath()+FileNamePath)打开成功");
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(fileInputStream));
                Log.d("TAG","BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(fileInputStream));包装成功");
                StringBuilder stringBuffer=new StringBuilder("");
                String line=null;
                while ((line=bufferedReader.readLine())!=null)
                {
                    stringBuffer.append(line);
                }
                bufferedReader.close();
                fileInputStream.close();
                return stringBuffer.toString();
            } catch (IOException e) {
                Log.d("TAG"," FileInputStream fileInputStream=new FileInputStream(root.getCanonicalPath()+FileNamePath);打开失败");
            }
        }
        return "读取失败";
    }

    public void setEditTextOnClick()  //显示日期
    {
        DatePickerDialog dialog=new DatePickerDialog(ActivityStudent.this, new OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                ActivityStudent.this.year=year;
                ActivityStudent.this.month=month;
                ActivityStudent.this.days=dayOfMonth;
                showTime();
            }
        },year,month,days);
        dialog.show();
    }

    public synchronized void showTime()
    {
        EditText editText=(EditText)findViewById(R.id.birth);
        if(tag==0)
            setTextStyle(R.id.birth);
        Calendar calendar=Calendar.getInstance();  //获取当前时间
        calendar.set(year,month,days);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日");
        editText.setText(format.format(calendar.getTime()));
    }

    public void setTextStyle(int id)  //设置字体样式
    {
        EditText editText=(EditText)findViewById(id);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/楷体.ttf");
        editText.setTypeface(typeface);
        editText.setTextSize(18);
        tag=1;
    }

    public void setDialodButton(AlertDialog.Builder builder)   //对话框中的两个按钮设置
    {
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TAG=true;
                ImageView imageView=findViewById(R.id.stu_photo);
                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                if(studentDAL.updateStudentWithBitmap(upLode(),bitmap))
                    ActivityMain.setToast(R.mipmap.right,"修改成功",ActivityStudent.this);
                else
                    ActivityMain.setToast(R.mipmap.wrong,"修改失败",ActivityStudent.this);
                tiaoZhuan();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tiaoZhuan();
//                ActivityMain.list.add((Student) bundle.getSerializable("stu"));
//                ActivityMain.list=ActivityMain.getSortList(ActivityMain.list);
//                ActivityMain.adapter.notifyDataSetChanged();
//                ActivityStudent.this.finish();
//                //ActivityStudent.this.onDestroy();
            }
        });
    }

    private void tiaoZhuan()
    {
        Intent intent=new Intent(ActivityStudent.this,ActivityMain.class);
        startActivity(intent);
    }

    private Student upLode()     //页面传值
    {
        String s1=((EditText)findViewById(R.id.editText)).getText().toString();  //name
        String s2=((EditText)findViewById(R.id.editText2)).getText().toString(); //no
        String s3=((Spinner)findViewById(R.id.spinner1)).getSelectedItem().toString();  //学院
        String s4=((Spinner)findViewById(R.id.spinner2)).getSelectedItem().toString();  //专业
        String s5=((EditText)findViewById(R.id.editText_introduce)).getText().toString();  //好友介绍
        int id;
        if(Objects.equals(getSex(), "男"))
            id=R.mipmap.man;
        else
            id=R.mipmap.lady;

        return new Student(s1,id,s2,getSex(),s3,s4,getHobby(),new int[]{year,month,days},s5);
    }

    public void Init()  //页面初始化
    {
        bundle= getIntent().getExtras();
        if(bundle==null||bundle.getSerializable("student")==null)
            return;
        ((Button)findViewById(R.id.button)).setText("修改");
        findViewById(R.id.homepage).setVisibility(View.INVISIBLE);
//        Map<String,Object> map=(Map<String, Object>)bundle.getSerializable("stu");
//        assert map != null;
//        Student student=(Student) map.get("student");
//        Bitmap bitmap=(Bitmap)map.get("photo");
        Student student=(Student)bundle.getSerializable("student");
        //Bitmap bitmap=ActivityMain.bitmap;
        assert student != null;
        if(ActivityMain.bitmap!=null)
        {
            ((ImageView)findViewById(R.id.stu_photo)).setImageBitmap(ActivityMain.bitmap);
            ActivityMain.bitmap=null;
        }

        ((EditText)findViewById(R.id.editText)).setText(student.getStu_name());
        ((EditText)findViewById(R.id.editText2)).setText(student.getStu_no());
        findViewById(R.id.editText2).setEnabled(false);
        ((EditText)findViewById(R.id.editText_introduce)).setText(student.getStu_introduce());
        if(student.getStu_sex().equals("男"))
            ((RadioButton) findViewById(R.id.radioButton)).setChecked(true);
        else if(student.getStu_sex().equals("女"))
            ((RadioButton) findViewById(R.id.radioButton2)).setChecked(true);
        else
            Toast.makeText(getApplicationContext(), "出现错误", Toast.LENGTH_SHORT).show();
        ((Spinner)findViewById(R.id.spinner1)).setSelection(getPosition(student.getStu_dep(),(Spinner)findViewById(R.id.spinner1)),true);
        //if(((Spinner)findViewById(R.id.spinner1)).performClick())
        ((Spinner)findViewById(R.id.spinner2)).setSelection(getPosition(student.getStu_zy(),(Spinner)findViewById(R.id.spinner2)),true);
        //Log.d("TAG",student.getStu_zy());
        setCheckBoxSelccted(student.getStu_hobby());
        year=student.getBirth()[0];
        month=student.getBirth()[1];
        days=student.getBirth()[2];
        showTime();
    }

    public void setCheckBoxSelccted(ArrayList<Integer> arrayList)  //界面初始设置多选框
    {
        try
        {
            for(int i=0;i<arrayList.size();i++)
                ((CheckBox)findViewById(arrayList.get(i))).setChecked(true);
        }catch (Exception e){}

    }

    public int getPosition(String s,Spinner spinner)       //获得下拉链表的项目匹配
    {
        SpinnerAdapter adapter=spinner.getAdapter();
        for(int i=0;i<adapter.getCount();i++)
        {
            if(s.equals(adapter.getItem(i).toString().trim()))
                return i;
        }
        return 0;
    }

    public String getSex()
    {
        return ((RadioButton)(findViewById(((RadioGroup)findViewById(R.id.radioGroup)).getCheckedRadioButtonId()))).getText().toString();
    }

    public ArrayList<Integer> getHobby()
    {
        ArrayList<Integer> list=new ArrayList<Integer>();
        if(((CheckBox)findViewById(R.id.checkBox)).isChecked())
        list.add(R.id.checkBox);
        if(((CheckBox)findViewById(R.id.checkBox2)).isChecked())
            list.add(R.id.checkBox2);
        if(((CheckBox)findViewById(R.id.checkBox3)).isChecked())
            list.add(R.id.checkBox3);
        if(((CheckBox)findViewById(R.id.checkBox4)).isChecked())
            list.add(R.id.checkBox4);
        return list;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(TAG)
            ActivityStudent.this.finish();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(detector.onTouchEvent(ev))
            return true;
        return super.dispatchTouchEvent(ev);
    }
}
