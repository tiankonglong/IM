package im.qq.com.im.model.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import im.qq.com.im.model.bean.GroupInfo;
import im.qq.com.im.model.bean.InvationInfo;
import im.qq.com.im.model.bean.UserInfo;
import im.qq.com.im.model.db.DBHelper;

/**
 * Created by asdf on 2017/12/15.
 */

public class InviteTableDao {
    private final DBHelper mHelper;

    public InviteTableDao(DBHelper helper) {
        mHelper = helper;
    }

    //获取所有邀请信息
    public List<InvationInfo> getInvitations() {
        //获取数据库链接
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //操作数据库
        String sql = "select * from "+InviteTable.TAB_NAME;
        Cursor cursor = db.rawQuery(sql,null);

        List<InvationInfo> invationInfos = new ArrayList<>();

        while (cursor.moveToNext()) {
            InvationInfo invationInfo = new InvationInfo();
            invationInfo.setReason(cursor.getString(cursor.getColumnIndex(InviteTable.COL_REASON)));
            invationInfo.setStatus(int2InviteStatus(cursor.getInt(cursor.getColumnIndex(InviteTable.COL_STATUS))));

            String groupId = cursor.getString(cursor.getColumnIndex(InviteTable.COL_GROUP_HXID));
            if (groupId == null) {//联系人的邀请信息
                UserInfo userInfo = new UserInfo();

                userInfo.setHxid(cursor.getString(cursor.getColumnIndex(InviteTable.COL_USER_HXID)));
                userInfo.setName(cursor.getString(cursor.getColumnIndex(InviteTable.COL_USER_NAME)));
                userInfo.setNick(cursor.getString(cursor.getColumnIndex(InviteTable.COL_USER_NAME)));

                invationInfo.setUser(userInfo);
            }else {//群组的邀请信息
                GroupInfo groupInfo = new GroupInfo();

                groupInfo.setGroupId(cursor.getString(cursor.getColumnIndex(InviteTable.COL_GROUP_HXID)));
                groupInfo.setGroupName(cursor.getString(cursor.getColumnIndex(InviteTable.COL_GROUP_NAME)));
                groupInfo.setInvatePerson(cursor.getString(cursor.getColumnIndex(InviteTable.COL_USER_HXID)));

                invationInfo.setGroup(groupInfo);
            }

            //添加本次循环的邀请信息到总的集合中
            invationInfos.add(invationInfo);
        }
        //关闭资源
        cursor.close();

        //返回数据
        return invationInfos;
    }

    //将int类型状态转换为邀请的状态
    private InvationInfo.InvitationStatus int2InviteStatus(int intStatus) {

        if (intStatus == InvationInfo.InvitationStatus.NEW_INVITE.ordinal()) {
            return InvationInfo.InvitationStatus.NEW_INVITE;
        }

        if (intStatus == InvationInfo.InvitationStatus.INVITE_ACCEPT.ordinal()) {
            return InvationInfo.InvitationStatus.INVITE_ACCEPT;
        }

        if (intStatus == InvationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER.ordinal()) {
            return InvationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER;
        }

        return null;
    }

    //添加用户到数据库
    public void addInvitation(InvationInfo invationInfo) {

        //获取数据库链接
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //操作数据库,保存信息
        ContentValues values = new ContentValues();
        values.put(InviteTable.COL_REASON,invationInfo.getReason());
        values.put(InviteTable.COL_STATUS,invationInfo.getStatus().ordinal());

        UserInfo user = invationInfo.getUser();
        if (user != null) {//联系人
            values.put(InviteTable.COL_USER_HXID,invationInfo.getUser().getHxid());
            values.put(InviteTable.COL_USER_NAME,invationInfo.getUser().getName());
        }else {//群组
            values.put(InviteTable.COL_GROUP_HXID,invationInfo.getGroup().getGroupId());
            values.put(InviteTable.COL_GROUP_NAME,invationInfo.getGroup().getGroupName());
            values.put(InviteTable.COL_USER_HXID,invationInfo.getGroup().getInvatePerson());
        }

        db.replace(InviteTable.TAB_NAME, null,values);
    }


    //删除邀请
    public void removeInvitation(String hxId) {

        if (hxId == null) {
            return;
        }

        SQLiteDatabase db = mHelper.getReadableDatabase();

        db.delete(InviteTable.TAB_NAME,InviteTable.COL_USER_HXID+"=?",new String[]{hxId});
    }

    //更新邀请状态
    public void updateInvitationStatus(InvationInfo.InvitationStatus invitationStatus,String hxId) {
        if (hxId == null) {
            return;
        }

        //获取数据库链接
        SQLiteDatabase db = mHelper.getReadableDatabase();

        //执行更新操作
        ContentValues values = new ContentValues();
        values.put(InviteTable.COL_STATUS, invitationStatus.ordinal());

        db.update(InviteTable.TAB_NAME, values, InviteTable.COL_USER_HXID + "=?", new String[]{hxId});
    }
}
