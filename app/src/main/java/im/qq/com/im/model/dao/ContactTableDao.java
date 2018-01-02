package im.qq.com.im.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import im.qq.com.im.model.bean.UserInfo;
import im.qq.com.im.model.db.DBHelper;
import im.qq.com.im.model.db.UserAccountDB;

/**
 * Created by asdf on 2017/12/15.
 */

public class ContactTableDao {
    private DBHelper mHelper;

    public ContactTableDao(DBHelper helper) {
        mHelper = helper;
    }

    //获取所有联系人
    public List<UserInfo> getContacts() {
        //获取数据库链接
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //操作数据库
        String sql = "select * from "+ContacTable.TAB_NAME+" where "+ContacTable.COL_IS_CONTACT+"=1";


        List<UserInfo> users = new ArrayList<>();

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            UserInfo userInfo = new UserInfo();
            userInfo.setName(cursor.getString(cursor.getColumnIndex(ContacTable.COL_NAME)));
            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(ContacTable.COL_HXID)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(ContacTable.COL_NICK)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(ContacTable.COL_PHOTO)));

            users.add(userInfo);
        }
        //关闭资源
        cursor.close();

        return users;
    }

    //添加用户到数据库
    public void saveContact(UserInfo user, boolean isMyContact) {

        if (user == null) {
            return;
        }

        //获取数据库链接
        SQLiteDatabase db = mHelper.getWritableDatabase();

        //操作数据库,保存信息
        ContentValues values = new ContentValues();
        values.put(ContacTable.COL_HXID,user.getHxid());
        values.put(ContacTable.COL_NAME,user.getName());
        values.put(ContacTable.COL_NICK,user.getNick());
        values.put(ContacTable.COL_PHOTO,user.getPhoto());
        values.put(ContacTable.COL_IS_CONTACT,isMyContact ? 1 : 0);

        db.replace(ContacTable.TAB_NAME, null,values);
    }

    //保存联系人信息
    public void savaContacts(List<UserInfo> contacts, boolean isMyContact) {
        if (contacts == null || contacts.size() <= 0) {
            return;
        }

        //获取数据库链接
        SQLiteDatabase db = mHelper.getWritableDatabase();

        for (UserInfo contact : contacts) {
            saveContact(contact, isMyContact);
        }
    }



    // 根据环信 id 获取用户所有信息
    public UserInfo getContactByHx(String hxId){

        if (hxId == null) {
            return null;
        }

        // 获取数据库链接
        SQLiteDatabase db = mHelper.getReadableDatabase();

        // 操作数据库,执行sql语句将new String[]{hxId}赋值到=？；
        String sql = "select * from " + ContacTable.TAB_NAME + " where "
                + ContacTable.COL_HXID + "=?";
        Cursor cursor = db.rawQuery(sql, new String[]{hxId});

        UserInfo userInfo = null;
        if (cursor.moveToNext())
        {
            userInfo = new UserInfo();

            userInfo.setName(cursor.getString(cursor.getColumnIndex(ContacTable .COL_NAME)));
            userInfo.setHxid(cursor.getString(cursor.getColumnIndex(ContacTable .COL_HXID)));
            userInfo.setNick(cursor.getString(cursor.getColumnIndex(ContacTable .COL_NICK)));
            userInfo.setPhoto(cursor.getString(cursor.getColumnIndex(ContacTable.COL_PHOTO)));
        }
        // 关闭资源
        cursor.close();
        // 返回数据
        return userInfo;
    }

    //通过环信id获取用户联系人信息
    public List<UserInfo> getContactsByhx(List<String> hxIds) {

        if (hxIds == null || hxIds.size() <= 0) {
            return null;
        }

        List<UserInfo> contacts = new ArrayList<>();

        //遍历hxIds，来查找
        for (String hxid : hxIds) {
            UserInfo contact = getContactByHx(hxid);

            contacts.add(contact);
        }

        return contacts;
    }

    public void deleteContactByHxId(String hxId) {

        if (hxId == null) {
            return;
        }

        SQLiteDatabase db = mHelper.getReadableDatabase();

        db.delete(ContacTable.TAB_NAME,ContacTable.COL_HXID+"=?",new String[]{hxId});
    }

}
