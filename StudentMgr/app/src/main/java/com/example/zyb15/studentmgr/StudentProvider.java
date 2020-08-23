package com.example.zyb15.studentmgr;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by zyb15 on 2020/4/26.
 */

public class StudentProvider extends ContentProvider {

    //private SQLiteDatabase database;
    private StudentDBHelper helper;
    private static UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH);
    private static String AUTHORITY="zyb.student.provider";
    private static final int STUDENTS=1;
    private static final int STUDENT=2;

    static
    {
        matcher.addURI(AUTHORITY,"students",STUDENTS);
        matcher.addURI(AUTHORITY,"student/#",STUDENT);
    }

    @Override
    public boolean onCreate() {
        helper=new StudentDBHelper(getContext(),"Student",null,1);
        //database=helper.getReadableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database=helper.getReadableDatabase();
        switch (matcher.match(uri))
        {
            case STUDENTS:return database.query("student",projection,selection,selectionArgs,null,null,sortOrder);
            case STUDENT:
                long id= ContentUris.parseId(uri);
                String WhereClause="stu_no="+id;
                if(selection!=null&&selection.equals(""))
                    WhereClause+=" and "+selection;
                return database.query("student",projection,selection,selectionArgs,null,null,sortOrder);
            default:throw new IllegalArgumentException("未知Uri"+uri);
        }

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (matcher.match(uri))
        {
            case STUDENTS:return "vnd.android.cursor.dir/zyb.student";
            case STUDENT:return "vnd.android.cursor.item/zyb.student";
            default:throw new IllegalArgumentException("未知Uri"+uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
