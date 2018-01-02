package im.qq.com.im.model.db;

import android.content.Context;

import im.qq.com.im.model.dao.ContactTableDao;
import im.qq.com.im.model.dao.InviteTable;
import im.qq.com.im.model.dao.InviteTableDao;

/**
 * Created by asdf on 2017/12/18.
 */

public class DBManager {

    private final DBHelper dbHelper;
    private final ContactTableDao contactTableDao;
    private final InviteTableDao inviteTableDao;


    public DBManager(Context context, String name) {
        //创建数据库
        dbHelper = new DBHelper(context, name);

        //创建数据库中两张表的操作类
        contactTableDao = new ContactTableDao(dbHelper);
        inviteTableDao = new InviteTableDao(dbHelper);
    }

    //获取联系人表的操作类对象
    public ContactTableDao getContactTableDao() {
        return contactTableDao;
    }

    //获取邀请信息表的操作类对象
    public InviteTableDao getInviteTableDao() {
        return inviteTableDao;
    }

    //关闭数据库的方法
    public void close() {
        dbHelper.close();
    }
}
