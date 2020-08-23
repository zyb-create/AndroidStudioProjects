package com.example.zyb15.studentmgr;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ActivityGetSearch extends Activity {

    private SearchView searchView;
    private ListView listView;
    public static List<Student> studentList=new ArrayList<>();
    public static StudentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_search);
        getActionBar().hide();

///////////////////////////////////////////////////////////////
//        listView=(ListView)findViewById(R.id.search_listView);
//        adapter=new StudentAdapter(ActivityGetSearch.this,R.layout.stu_item,studentList);
//        listView.setAdapter(adapter);
//////////////////////////////////////////////////////
        searchView=(SearchView)findViewById(R.id.search_view);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                if(studentList.isEmpty()||studentList==null)
                    ActivityMain.setToast(R.mipmap.cry,"没有找到",ActivityGetSearch.this);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {   //输入框动态变化事件
                updateList(newText);
                return true;
            }
        });
///////////////////////////////////////////////////////////////

        ((TextView)findViewById(R.id.search_textview)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent=new Intent(ActivityGetSearch.this,ActivityMain.class);
                startActivity(intent);*/
                ActivityGetSearch.this.finish();
            }
        });
        ////////////////////////////////////////////////////////
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View v =  getLayoutInflater().inflate(R.layout.dialog_one,null);  /////////若自定义某界面，需先获得其View
                View v1 =  getLayoutInflater().inflate(R.layout.activity_student,null);  /////////若自定义某界面，需先获得其View
                ActivityMain.showStudent(v,v1,ActivityGetSearch.this,position);
            }
        });

        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {        //this.registerForContextMenu(listView);
                menu.add(0, 0, 0, "修改");
                menu.add(0, 1, 0, "删除");
            }
        });
//        ///////////////////////////////////////////////////////
    }

    @Override////////////菜单被点击
    public boolean onContextItemSelected(MenuItem item) {
//        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//
//        switch (item.getItemId())
//        {
//            case 1:
//                ActivityMain.deleteStu(getPosition(ActivityMain.list,studentList,menuInfo.position),ActivityGetSearch.this);
//                //synchronized (ActivityGetSearch.this){studentList.remove(menuInfo.position);};
//                //adapter.notifyDataSetChanged();
//                break;
//            case 0: updateStu(menuInfo.position,ActivityGetSearch.this);break;
//            default:return false;
//        }
        return true;
    }

    public static int getPosition(List<Student>list1,List<Student> list2,int position)   //获得对象在链表的位置
    {
        if(position>=list2.size())
            return -1;
        for(int i=0;i<list1.size();i++)
            if(list1.get(i).equals(list2.get(position)))
                return i;
        return -1;
    }

    public void updateStu(int position,Context context)
    {
//        Intent intent=new Intent(context,ActivityStudent.class);
//        Student student=studentList.get(position);
//        ActivityMain.list.remove(getPosition(ActivityMain.list,studentList,position));
//        ActivityMain.adapter.notifyDataSetChanged();
//        //adapter.notifyDataSetChanged(); //更新listView
//        Bundle bundle=new Bundle();
//        bundle.putSerializable("stu",student);
//        intent.putExtras(bundle);
//        startActivity(intent);
    }

    public void updateList(String newText)   //动态更新ListView
    {
//        studentList.clear();
//        if(newText==null||newText.equals(""))
//            return;
//        for(Student stu:ActivityMain.list)
//            if(isMatch(stu,newText))
//                studentList.add(stu);
//        adapter.notifyDataSetChanged();

    }
    public boolean isMatch(Student student,String s)  //字符串匹配
    {
        if(student.getStu_name().contains(s)||student.getStu_dep().contains(s)||student.getStu_zy().contains(s))
            return true;
        return false;
    }

}
