package im.qq.com.im.model.bean;

/**
 * Created by asdf on 2017/12/15.
 */

public class GroupInfo {
    private String groupName;
    private String groupId;
    private String invatePerson;

    public GroupInfo() {
    }

    public GroupInfo(String groupName, String groupId, String invatePerson) {
        this.groupName = groupName;
        this.groupId = groupId;
        this.invatePerson = invatePerson;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getInvatePerson() {
        return invatePerson;
    }

    public void setInvatePerson(String invatePerson) {
        this.invatePerson = invatePerson;
    }


    @Override
    public String toString() {
        return "GroupInfo{" +
                "groupName='" + groupName + '\'' +
                ", groupId='" + groupId + '\'' +
                ", invatePerson='" + invatePerson + '\'' +
                '}';
    }

}
