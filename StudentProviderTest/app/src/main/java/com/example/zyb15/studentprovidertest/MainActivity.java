package com.example.zyb15.studentprovidertest;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.preference.ListPreference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TextWatcher {

    public ContentResolver contentResolver;
    public EditText editText;
    public Handler handler;
    public ListView listView;
    public static List<Student> studentList=new ArrayList<>();
    public static StudentAdapter adapter;
    public static final Uri uriALL=Uri.parse("content://zyb.student.provider/students");
    public static final Uri uriSingle=Uri.parse("content://zyb.student.provider/student");


    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentResolver=getContentResolver();
        editText=findViewById(R.id.search_text);
        listView=findViewById(R.id.listView);
        editText.addTextChangedListener(this);

        adapter=new StudentAdapter(this,R.layout.stu_item,studentList);
        listView.setAdapter(adapter);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 0x11:
                        Bundle bundle=msg.getData();
                        if(bundle==null)
                            break;
                        updateList(bundle);
                        break;
                    default:break;
                }
            }
        };

    }

    public void updateList(Bundle bundle )   //动态更新ListView
    {
        studentList.clear();
        List<Student> list=(List<Student>)bundle.getSerializable("StudentList") ;
        assert list != null;
        studentList.addAll(list);
//        assert studentList != null;
//        Log.d("TAG",studentList.size()+"");
//        Log.d("TAG",studentList.get(0).getStu_name());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(charSequence.length()==0)
        {
            studentList.clear();
            adapter.notifyDataSetChanged();
            return;
        }
        search(editText.getText().toString().trim());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void search(final String string)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message=new Message();
                message.what=0x11;
                Bundle bundle=new Bundle();
                bundle.putSerializable("StudentList", (Serializable) getStudentList(string));
                message.setData(bundle);
                handler.sendMessage(message);
            }
        }).start();
    }

    public List<Student> getStudentList(String string)
    {
        String sql="stu_no like ?";
        @SuppressLint("Recycle") Cursor cursor=contentResolver.query(uriALL,null,sql,new String[]{"%"+string+"%"},null);
        List<Student> list=new LinkedList<>();
        Gson gson=new Gson();
        assert cursor != null;
        while (cursor.moveToNext())
        {
            String str=cursor.getString(cursor.getColumnIndex("stu_message"));
            Student student=gson.fromJson(str,Student.class);
            byte[] photo=cursor.getBlob(cursor.getColumnIndex("stu_image"));
            if (photo!=null)
            {
                Bitmap bitmap = BitmapFactory.decodeByteArray(photo, 0,photo.length, null);
                student.setBitmap(bitmap);
            }
            list.add(student);
        }
        return list;
    }
}
