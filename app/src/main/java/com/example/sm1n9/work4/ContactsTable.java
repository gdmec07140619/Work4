package com.example.sm1n9.work4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.Vector;

/**
 * Created by sm1n9 on 2015/11/2.
 */
public class ContactsTable {
    private final static String TABLENAME="contactsTable";
    private MyDB db;
    public User[] getAllUser;

    //构造方法
    public ContactsTable(Context context){
        db=new MyDB(context);
        if (!db.isTableExists(TABLENAME)){
            String createTableSql="CREATE TABLE IF NOT EXISTS"+
                    TABLENAME+"(id_DB integer primary key AUTOINCREMENT,"+
                    User.NAME+"VARCHAR,"+
                    User.MOBILE+"VARCHAR"+
                    User.DANWEI+"VARCHAR"+
                    User.QQ+"VARCHAR"+
                    User.ADDRESS+"VARCHAR)";
            db.createTable(createTableSql);
        }
    }
    //添加数据到联系人表
    public boolean addData(User user){
        ContentValues values=new ContentValues();
        values.put(User.NAME,user.getName());
        values.put(User.MOBILE,user.getMobile());
        values.put(User.DANWEI,user.getDanwei());
        values.put(User.QQ,user.getQq());
        values.put(User.ADDRESS,user.getAddress());
        return db.save(TABLENAME,values);
    }
    //获取联系人表全部记录
    public User[] getAllUser(){
        Vector<User> v=new Vector<User>();
        Cursor cursor=null;
        try {
            cursor=db.find("select * from"+TABLENAME,null);
            while(cursor.moveToNext()){
                User temp=new User();
                temp.setId_DB(cursor.getInt(cursor.getColumnIndex("id_DB")));
                temp.setAddress(cursor.getString(cursor.getColumnIndex(User.NAME)));
                temp.setQq(cursor.getString(cursor.getColumnIndex(User.QQ)));
                temp.setDanwei(cursor.getString(cursor.getColumnIndex(User.DANWEI)));
                temp.setName(cursor.getString(cursor.getColumnIndex(User.NAME)));
                temp.setMobile(cursor.getString(cursor.getColumnIndex(User.MOBILE)));
                v.add(temp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
            }
            db.closeConection();
        }
        if (v.size()>0){
            return  v.toArray(new User[]{});
        }else {
            User[] users=new User[1];
            User user=new User();
            user.setName("无结果");
            users[0]=user;
            return users;
        }
    }
    //根据联系人的ID来获取联系人
    public User getUserByID(int id){
        Cursor cursor=null;
        try {
            cursor=db.find("select * from"+TABLENAME+"where id_DB=?",new String[]{id+""});
            User temp=new User();
            cursor.moveToNext();
            temp.setId_DB(cursor.getInt(cursor.getColumnIndex("id_DB")));
            temp.setAddress(cursor.getString(cursor.getColumnIndex(User.NAME)));
            temp.setQq(cursor.getString(cursor.getColumnIndex(User.QQ)));
            temp.setDanwei(cursor.getString(cursor.getColumnIndex(User.DANWEI)));
            temp.setName(cursor.getString(cursor.getColumnIndex(User.NAME)));
            temp.setMobile(cursor.getString(cursor.getColumnIndex(User.MOBILE)));
            return temp;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
            }
            db.closeConection();
        }
        return null;
    }
    //更新联系人信息
    public boolean updateUser(User user){
        ContentValues values=new ContentValues();
        values.put(User.NAME,user.getName());
        values.put(User.MOBILE,user.getMobile());
        values.put(User.QQ,user.getQq());
        values.put(User.DANWEI,user.getDanwei());
        values.put(User.ADDRESS,user.getAddress());
        return db.update(TABLENAME,values,"id_db=?",new String[]{user.getId_DB()+""});

    }
    //根据特定条件查询联系人信息
    public User[] findUserByKey(String key){
        Vector<User> v=new Vector<User>();
        Cursor cursor=null;
        try {
            cursor=db.find(
                    "select * from"+TABLENAME+"where"
                    +User.NAME+"like'%"+key+"%'"+
                    "or"+User.MOBILE+"like '%"+key+"%'"+
                            "or"+User.QQ+"like '%"+key+"%'"
                    ,null);
            while (cursor.moveToNext()){
                User temp=new User();
                temp.setId_DB(cursor.getInt(cursor.getColumnIndex("id_DB")));
                temp.setName(cursor.getString(cursor.getColumnIndex(User.NAME)));
                temp.setMobile(cursor.getString(cursor.getColumnIndex(User.MOBILE)));
                temp.setDanwei(cursor.getString(cursor.getColumnIndex(User.DANWEI)));
                temp.setQq(cursor.getString(cursor.getColumnIndex(User.QQ)));
                temp.setQq(cursor.getString(cursor.getColumnIndex(User.ADDRESS)));
                v.add(temp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor !=null){
                cursor.close();
            }
            db.closeConection();
        }
        if (v.size()>0){
            return v.toArray(new User[] {});
        }else{
            User[] users=new User[1];
            User user=new User();
            user.setName("无结果");
            users[0]=user;
            return users;
        }
    }
    //删除联系人
    public boolean deleceByUser(User user){
        return  db.delete(TABLENAME,"id_DB=?",new String[]{user.getId_DB()+""});
    }


}
