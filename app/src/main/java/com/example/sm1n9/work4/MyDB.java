package com.example.sm1n9.work4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sm1n9 on 2015/11/2.
 */
public class MyDB extends SQLiteOpenHelper {
    private static String DB_NAME = "MY_DB.db";//数据库名称
    private static int DB_VERSION = 2;//版本号
    private SQLiteDatabase db;  //数据库操作对象
    //构造方法
    public MyDB(Context context){
        super(context,DB_NAME,null,DB_VERSION);
        db=getWritableDatabase();
    }
    //连接数据库

    public SQLiteDatabase openConnection() {
        if (!db.isOpen()){
            //读写方式获取SQLiteDatabase
            db=getWritableDatabase();
        }
        return db;
    }
    //关闭数据库连接
    public void closeConection(){
        try {
            if (db!=null&&db.isOpen())
                db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //创建数据表
    public boolean createTable(String createTableSql){
       try {
           openConnection();
           db.execSQL(createTableSql);
       }catch (SQLException e){
           e.printStackTrace();
           return false;
       }finally {
           closeConection();
       }
        return true;
    }
    //保存数据
    public boolean save(String tableName,ContentValues values){
        try {
            openConnection();
            db.insert(tableName, null, values);
        }catch (SQLException e){
            e.printStackTrace();
            return false;

        } finally {
            closeConection();
        }
        return true;
    }
    //更新数据
    public boolean update(String table,ContentValues values,String whereClause,String whereArg[]){
    try {
        openConnection();
        db.update(table, values, whereClause, whereArg);
    }catch (SQLException e){
        e.printStackTrace();
        return false;
    }finally {
        closeConection();
    }
        return true;
    }
    //删除数据
    public boolean  delete(String table,String deletesql,String obj[]){
        try {
            openConnection();
            db.delete(table, deletesql, obj);
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }finally {
            closeConection();
        }
        return true;

    }
    //查找数据
    public Cursor find(String findsql,String obj[]){
        try {
            openConnection();
            Cursor cursor=db.rawQuery(findsql,obj);
            return cursor;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
    //查找数据表是否存在
    public boolean isTableExists(String tableName){
        try {
            openConnection();
            String str="select count(*)xcount from"+tableName;
            db.rawQuery(str,null).close();
        }catch (Exception e){
            return false;
        }
        return true;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlLiteDatabase, int i, int i1) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    /*public MyDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }*/


}
