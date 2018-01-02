package im.qq.com.im.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import im.qq.com.im.model.dao.ContacTable;
import im.qq.com.im.model.dao.InviteTable;
import im.qq.com.im.model.dao.UserAccountTable;

/**
 * Created by asdf on 2017/12/15.
 */

public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //建立联系人的表
        db.execSQL(ContacTable.CREATE_TAB);

        //创建邀请信息的表
        db.execSQL(InviteTable.CREATE_TAB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
