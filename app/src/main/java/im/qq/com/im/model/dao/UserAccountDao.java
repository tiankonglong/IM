package im.qq.com.im.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.baidu.platform.comapi.map.N;

import im.qq.com.im.model.bean.UserInfo;
import im.qq.com.im.model.db.UserAccountDB;

/**
 * Created by asdf on 2017/12/11.
 */

public class UserAccountDao {
    private final UserAccountDB mHelper;

    public UserAccountDao(Context context) {
        mHelper = new UserAccountDB(context);
    }

    //添加用户到数据库
    public void addAccount(UserInfo user) {
        //获取数据库链接
        SQLiteDatabase db = mHelper.getReadableDatabase();
        //操作数据库
        ContentValues values = new ContentValues();
        values.put(UserAccountTable.COL_HXID,user.getHxid());
        values.put(UserAccountTable.COL_NAME,user.getName());
        values.put(UserAccountTable.COL_NICK,user.getNick());
        values.put(UserAccountTable.COL_PHOTO,user.getPhoto());

        db.replace(UserAccountTable.TAB_NAME, null,values);
    }

    //获取用户
    public UserInfo getAccount(String name) {
        //获取数据库链接
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //操作数据库
        String sql = "select * from "+UserAccountTable.TAB_NAME+" where "+UserAccountTable.COL_NAME+" =?";
        Cursor cursor = db.rawQuery(sql,new String[]{name});
        UserInfo account = null;
        if (cursor.moveToNext()) {
            account = new UserInfo();
            account.setName(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_NAME)));
            account.setHxid(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_HXID)));
            account.setNick(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_NICK)));
            account.setPhoto(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_PHOTO)));
        }

        // 关闭资源
        cursor.close();
        // 返回数据
        return account;
    }

    // 根据环信 id 获取所有用户信息
    public UserInfo getAccountByHxId(String hxId){
        // 获取数据库链接
        SQLiteDatabase db = mHelper.getReadableDatabase();
        // 操作数据库
        String sql = "select * from " + UserAccountTable.TAB_NAME + " where "
                + UserAccountTable.COL_HXID + "=?";
        Cursor cursor = db.rawQuery(sql, new String[]{hxId});
        UserInfo account = null;
        if (cursor.moveToNext())
        {
            account = new UserInfo();

            account.setName(cursor.getString(cursor.getColumnIndex(UserAccountTable .COL_NAME)));
            account.setHxid(cursor.getString(cursor.getColumnIndex(UserAccountTable .COL_HXID)));
            account.setNick(cursor.getString(cursor.getColumnIndex(UserAccountTable .COL_NICK)));
            account.setPhoto(cursor.getString(cursor.getColumnIndex(UserAccountTable.COL_PHOTO)));
        }
        // 关闭资源
        cursor.close();
        // 返回数据
        return account;
    }

}
