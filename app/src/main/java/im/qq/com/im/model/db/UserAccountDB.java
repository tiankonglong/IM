package im.qq.com.im.model.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import im.qq.com.im.model.dao.UserAccountTable;

/**
 * Created by asdf on 2017/12/11.
 */

public class UserAccountDB extends SQLiteOpenHelper{

    public UserAccountDB(Context context) {
        super(context, "account.db", null,1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserAccountTable.CREATE_TAB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
