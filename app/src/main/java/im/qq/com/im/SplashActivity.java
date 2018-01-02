package im.qq.com.im;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.hyphenate.chat.EMClient;

import im.qq.com.im.model.bean.UserInfo;
import im.qq.com.im.model.db.Model;

/**
 * Created by asdf on 2017/12/8.
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler.sendMessageDelayed(Message.obtain(),2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    private Handler handler=new Handler() {
        public void handleMessage(Message msg){
            //如果当前activity已经退出，那么就不处理handler中的消息
            if (isFinishing()) {
                return;
            }

            //判断是否登陆过，进入主界面还是登陆界面
            toMainOrLogin();
        }
    };

    private void toMainOrLogin() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //因为还没点击事件所以先注释掉EMClient
                if(EMClient.getInstance().isLoggedInBefore()) {
                    //获取到当前登陆用户的信息
                    UserInfo account = Model.getInstance().getUserAccountDao().getAccountByHxId(EMClient.getInstance().getCurrentUser());
                   if (account == null){
                       Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                       startActivity(intent);
                   }else {
                       //登陆成功后的方法
                       Model.getInstance().loginSuccess(account);

                       Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                       startActivity(intent);
                   }

                }else {
                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        });
    }
}
