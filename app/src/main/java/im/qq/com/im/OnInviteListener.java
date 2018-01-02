package im.qq.com.im;

import im.qq.com.im.model.bean.InvationInfo;

/**
 * Created by asdf on 2017/12/20.
 */

public interface OnInviteListener {
    //联系人接受按钮的点击事件
    void onAccept(InvationInfo invationInfo);

    //联系人拒绝按钮的点击事件
    void onReject(InvationInfo invationInfo);
}
