package im.qq.com.im.model.db;

import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.v4.content.LocalBroadcastManager;

import com.hyphenate.EMContactListener;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMucSharedFile;
import com.hyphenate.easeui.domain.EaseEmojicon;

import java.util.List;

import im.qq.com.im.model.bean.GroupInfo;
import im.qq.com.im.model.bean.InvationInfo;
import im.qq.com.im.model.bean.UserInfo;
import im.qq.com.im.utils.Constant;
import im.qq.com.im.utils.SpUtils;

/**
 * Created by asdf on 2017/12/19.
 */

public class EventListener {
    private Context mContext;
    private final LocalBroadcastManager mLBM;

    public EventListener(Context context) {
        mContext = context;

        //创建一个发送广播的管理者对象
        mLBM = LocalBroadcastManager.getInstance(mContext);

        //注册一个联系人变化的监听
        EMClient.getInstance().contactManager().setContactListener(emContactListener);

        //注册一个群信息变化的监听
        EMClient.getInstance().groupManager().addGroupChangeListener(emGroupChangeListener);
    }

    private final EMContactListener emContactListener = new EMContactListener() {
        //联系人增加后执行的方法
        @Override
        public void onContactAdded(String hxid) {
            //数据更新
            Model.getInstance().getDbManager().getContactTableDao().saveContact(new UserInfo(hxid), true);

            //发送联系人变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
        }

        @Override
        public void onContactDeleted(String hxid) {
            Model.getInstance().getDbManager().getContactTableDao().deleteContactByHxId(hxid);
            Model.getInstance().getDbManager().getInviteTableDao().removeInvitation(hxid);

            //发送联系人变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
        }

        @Override
        public void onContactInvited(String hxid, String reason) {
            //数据更新
            InvationInfo invationInfo = new InvationInfo();
            invationInfo.setUser(new UserInfo(hxid));
            invationInfo.setReason(reason);
            invationInfo.setStatus(InvationInfo.InvitationStatus.NEW_INVITE);//新邀请

            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);

            //红点的处理，标记这是新邀请
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);

            //发送邀请信息变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }

        @Override
        public void onFriendRequestAccepted(String hxid) {
            //数据库更新
            InvationInfo invationInfo = new InvationInfo();
            invationInfo.setUser(new UserInfo(hxid));
            invationInfo.setStatus(InvationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER);//邀请呗同意

            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);

            //红点的处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);

            //发送邀请信息变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }

        @Override
        public void onFriendRequestDeclined(String s) {
            //红点的处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);

            //发送邀请信息变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }
    };

    private final EMGroupChangeListener emGroupChangeListener = new EMGroupChangeListener() {
        //收到群邀请
        @Override
        public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
            //数据更新
            InvationInfo invationInfo = new InvationInfo();
            invationInfo.setReason(reason);
            invationInfo.setGroup(new GroupInfo(groupName, groupId, inviter));
            invationInfo.setStatus(InvationInfo.InvitationStatus.NEW_GROUP_INVITE);
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);

            //红点处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);

            //发送广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        @Override
        public void onRequestToJoinReceived(String s, String s1, String s2, String s3) {

        }

        //收到 群申请被接受
        @Override
        public void onRequestToJoinAccepted(String groupId, String groupName, String accepter) {

            //更新数据
            InvationInfo invationInfo = new InvationInfo();
            invationInfo.setGroup(new GroupInfo(groupName, groupId, accepter));
            invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_APPLICATION_ACCEPTER);

            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);

            //红点处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);

            //发送广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }


        //拒绝
        @Override
        public void onRequestToJoinDeclined(String groupId, String groupName, String decliner, String reason) {
            //数据更新
            InvationInfo invationInfo = new InvationInfo();
            invationInfo.setReason(reason);
            invationInfo.setGroup(new GroupInfo(groupName, groupId, decliner));
            invationInfo.setStatus(InvationInfo.InvitationStatus.GROUOP_APPLICATION_DECLINED);
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);

            //红点处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);

            //发送广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        @Override
        public void onInvitationAccepted(String groupId, String inviter, String reason) {
            //数据更新
            InvationInfo invationInfo = new InvationInfo();
            invationInfo.setReason(reason);
            invationInfo.setGroup(new GroupInfo(groupId, groupId, inviter));
            invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_INVITE_ACCEPTER);
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);

            //红点处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);

            //发送广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        //群邀请被拒绝
        @Override
        public void onInvitationDeclined(String groupId, String inviter, String reason) {
            //数据更新
            InvationInfo invationInfo = new InvationInfo();
            invationInfo.setReason(reason);
            invationInfo.setGroup(new GroupInfo(groupId, groupId, inviter));
            invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_INVITE_DECLINER);
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);

            //红点处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);

            //发送广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        //群成员被删除
        @Override
        public void onUserRemoved(String s, String s1) {

        }

        @Override
        public void onGroupDestroyed(String s, String s1) {

        }

        @Override
        public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {
            //数据更新
            InvationInfo invationInfo = new InvationInfo();
            invationInfo.setReason(inviteMessage);
            invationInfo.setGroup(new GroupInfo(groupId, groupId, inviter));
            invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_INVITE_ACCEPTER);
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);

            //红点处理
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);

            //发送广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        @Override
        public void onMuteListAdded(String s, List<String> list, long l) {

        }

        @Override
        public void onMuteListRemoved(String s, List<String> list) {

        }

        @Override
        public void onAdminAdded(String s, String s1) {

        }

        @Override
        public void onAdminRemoved(String s, String s1) {

        }

        @Override
        public void onOwnerChanged(String s, String s1, String s2) {

        }

        @Override
        public void onMemberJoined(String s, String s1) {

        }

        @Override
        public void onMemberExited(String s, String s1) {

        }

        @Override
        public void onAnnouncementChanged(String s, String s1) {

        }

        @Override
        public void onSharedFileAdded(String s, EMMucSharedFile emMucSharedFile) {

        }

        @Override
        public void onSharedFileDeleted(String s, String s1) {

        }
    };
}
