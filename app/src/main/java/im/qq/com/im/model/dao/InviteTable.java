package im.qq.com.im.model.dao;

/**
 * Created by asdf on 2017/12/15.
 */

public class InviteTable {
    public static final String TAB_NAME = "tab_invite";

    public static final String COL_USER_NAME = "name";
    public static final String COL_USER_HXID = "hxid";

    public static final String COL_GROUP_NAME = "group_name";
    public static final String COL_GROUP_HXID = "group_hxid";

    public static final String COL_REASON = "reason";
    public static final String COL_STATUS = "status";

    public static final String CREATE_TAB = "create table "
            + TAB_NAME + " ("
            + COL_USER_HXID + " text primary key,"
            + COL_USER_NAME + " text,"
            + COL_GROUP_HXID + " text,"
            + COL_GROUP_NAME + " text,"
            + COL_REASON + " text,"
            + COL_STATUS + " integer);";
}
