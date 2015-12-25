package com.example.sm1n9.work4;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateContactsActivity extends Activity {
    private EditText nameET,mobileET,qqET,danweiET,addressET;
    private User user;
    private ContactsTable ct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        nameET = (EditText) findViewById(R.id.name);
        mobileET = (EditText) findViewById(R.id.mobile);
        qqET = (EditText) findViewById(R.id.qq);
        danweiET = (EditText) findViewById(R.id.danwei);
        addressET= (EditText) findViewById(R.id.address);
        Bundle localBundle = getIntent().getExtras();
        int id=localBundle.getInt("user_ID");
        ContactsTable ct=new ContactsTable(this);
        user = ct.getUserByID(id);
        nameET.setText(user.getName());
        mobileET.setText(user.getMobile());
        qqET.setText(user.getQq());
        danweiET.setText(user.getDanwei());
        addressET.setText(user.getAddress());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       menu.add(Menu.NONE,1,Menu.NONE,"保存");
       menu.add(Menu.NONE,2,Menu.NONE,"返回");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()){
           case 1:
               if (!nameET.getText().toString().equals("")){
                   user.setName(nameET.getText().toString());
                   user.setMobile(mobileET.getText().toString());
                   user.setDanwei(danweiET.getText().toString());
                   user.setQq(qqET.getText().toString());
                   user.setAddress(addressET.getText().toString());
                   if (ct.updateUser(user)){
                       Toast.makeText(UpdateContactsActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                   }else {
                       Toast.makeText(UpdateContactsActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                   }
               }else {
                   Toast.makeText(UpdateContactsActivity.this,"数据不能为空",Toast.LENGTH_SHORT).show();
               }
               break;
           case 2:
               finish();
               break;
           default:
               break;
       }
        return super.onOptionsItemSelected(item);
    }
}
