package im.qq.com.im;

import android.app.Application;
import android.content.Context;

import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;

import im.qq.com.im.model.db.Model;

public class IMApplication extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(false);
        options.setAutoAcceptGroupInvitation(false);

        EaseUI.getInstance().init(this,options);

        Model.getInstance().init(this);


        mContext = this;
    }

    public static Context getGlobalApplication() {
        return mContext;
    }
}
