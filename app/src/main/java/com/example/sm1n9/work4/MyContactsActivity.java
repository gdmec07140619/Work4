package com.example.sm1n9.work4;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyContactsActivity extends Activity {
    private ListView lv;//结果列表
    private BaseAdapter lvAdapter;//ListView列表适配器
    private User users[];
    private int selectItem=0;//当前选择
    private ContactsTable ct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        lv= (ListView) findViewById(R.id.listView);
        loadContacts();
    }
    //加载联系人列表
    private void loadContacts() {
        ContactsTable ct = new ContactsTable(this);
        users = ct.getAllUser();
        lvAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return users.length;
            }

            @Override
            public Object getItem(int i) {
                return users[i];
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null) {
                    TextView tv = new TextView(MyContactsActivity.this);
                    tv.setTextSize(22);
                    view = tv;
                }
                String mobile = users[i].getMobile() == null ? "" : users[i].getMobile();
                TextView tv = (TextView) view;
                tv.setText(users[i].getName() + "---" + mobile);
                if (i == selectItem) {
                    view.setBackgroundColor(Color.YELLOW);
                } else {
                    view.setBackgroundColor(0);
                }
                return view;
            }
        };
        lv.setAdapter(lvAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectItem = 1;
                lvAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,1,"添加");
        menu.add(0,2,2,"编辑");
        menu.add(0,3,3,"查看信息");
        menu.add(0,4,4,"删除");
        menu.add(0,5,5,"查询");
        menu.add(0, 6, 6, "导入手机电话簿");
        menu.add(0, 7, 7, "退出");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 1://添加
                Intent intent=new Intent(MyContactsActivity.this,AddcontactsActivity.class);
                startActivity(intent);
                break;
            case 2:
                if (users[selectItem].getId_DB()>0){
                    intent=new Intent(MyContactsActivity.this,UpdateContactsActivity.class);
                    intent.putExtra("user_ID",users[selectItem].getId_DB());
                    startActivity(intent);
                }else {
                    Toast.makeText(this, "无结果记录，无法操作", Toast.LENGTH_SHORT).show();

                }
                break;
            case 3:
                if (users[selectItem].getId_DB()>0){
                    intent=new Intent(MyContactsActivity.this,ContactsMessageActivity.class);
                    intent.putExtra("user_ID",users[selectItem].getId_DB());
                    startActivity(intent);
                }else{
                    Toast.makeText(this, "无结果记录，无法操作", Toast.LENGTH_SHORT).show();
                }
                break;
            case 4:
                if (users[selectItem].getId_DB()>0){
                    delete();
                }else {
                    Toast.makeText(this, "无结果记录，无法操作", Toast.LENGTH_SHORT).show();
                }
            case 5:
                new FindDialog(MyContactsActivity.this).show();
                break;
            case 6:
                if (users[selectItem].getId_DB()>0){
                    importPhone(users[selectItem].getName(),users[selectItem].getMobile());
                    Toast.makeText(this,"已经成功导入"+users[selectItem].getName()+"到手机电话簿",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(this, "无结果记录，无法操作", Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);

    }

    private void delete() {
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle("系统信息");
        alert.setMessage("是否要删除联系人?");
        alert.setPositiveButton("是",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ct.deleceByUser(users[selectItem])) {
                            users = ct.getAllUser;
                            lvAdapter.notifyDataSetChanged();
                            selectItem = 0;
                            Toast.makeText(MyContactsActivity.this, "删除成功!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MyContactsActivity.this, "删除失败!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        alert.setNegativeButton("否",
                new DialogInterface.OnClickListener() {
                        public void onClick (DialogInterface dialog,int whichButton){
                        }

                });
        alert.show();
    }
    public void importPhone(String name,String phone){
        Uri phoneURL = ContactsContract.Data.CONTENT_URI;
        ContentValues values = new ContentValues();
        Uri rawContactUri = this.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI,values);
        long rawContactId = ContentUris.parseId(rawContactUri);
        values.clear();
        values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Contacts.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,name);
        this.getContentResolver().insert(phoneURL,values);
        values.clear();
        values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Contacts.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE,ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        this.getContentResolver().insert(phoneURL,values);

    }
    @Override
    protected void onResume() {
        super.onResume();
        ContactsTable ct=new ContactsTable(this);//重新加载数据
        users=ct.getAllUser();
        lvAdapter.notifyDataSetChanged();//刷新列表
    }

}
