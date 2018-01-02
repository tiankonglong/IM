package im.qq.com.im.model.db;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import im.qq.com.im.model.bean.UserInfo;
import im.qq.com.im.model.dao.UserAccountDao;

/**
 * Created by asdf on 2017/12/8.
 */

public class Model {
    private Context mContext;
    private UserAccountDao userAccountDao;
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private DBManager dbManager;

    private static Model model = new Model();

    private Model() {}

    public static Model getInstance() {
        return model;
    }

    public void  init(Context context) {
        mContext = context;

        //创建用户账号数据库的操做类对象
        userAccountDao = new UserAccountDao(mContext);

        //开启全局监听
        EventListener eventListener = new EventListener(mContext);
    }

    public ExecutorService getGlobalThreadPool() {
        return executorService;
    }

    //获取用户账号数据库的操作类对象
    public UserAccountDao getUserAccountDao() {
        return userAccountDao;
    }

    public void loginSuccess(UserInfo account) {

        //校验
        if (account == null) {
            return;
        }

        if (dbManager != null) {
            dbManager.close();
        }

        //传上下文和名称
        dbManager = new DBManager(mContext, account.getName());
    }

    public DBManager getDbManager() {
        return dbManager;
    }
}
