package im.qq.com.im.model.bean;

/**
 * Created by asdf on 2017/12/15.
 */

public class InvationInfo {
    private UserInfo user;
    private GroupInfo group;
    private String reason;
    private InvitationStatus status;  //邀请状态

    public InvationInfo() {
    }

    public InvationInfo(UserInfo user, GroupInfo group, String reason, InvitationStatus status) {
        this.user = user;
        this.group = group;
        this.reason = reason;
        this.status = status;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public GroupInfo getGroup() {
        return group;
    }

    public void setGroup(GroupInfo group) {
        this.group = group;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public InvitationStatus getStatus() {
        return status;
    }

    public void setStatus(InvitationStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "InvationInfo{" +
                "user=" + user +
                ", group=" + group +
                ", reason='" + reason + '\'' +
                ", status=" + status +
                '}';
    }

    //泛型
    public enum InvitationStatus {
        NEW_INVITE, //新邀请
        INVITE_ACCEPT, //接受邀请
        INVITE_ACCEPT_BY_PEER, //邀请被接受
        NEW_GROUP_INVITE,
        GROUP_APPLICATION_ACCEPTER,
        GROUOP_APPLICATION_DECLINED,
        GROUP_INVITE_ACCEPTER,
        GROUP_INVITE_DECLINER,
    }
}
