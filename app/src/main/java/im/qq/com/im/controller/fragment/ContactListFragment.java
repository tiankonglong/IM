package im.qq.com.im.controller.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import im.qq.com.im.LoginActivity;
import im.qq.com.im.R;
import im.qq.com.im.controller.activity.AddContactActivity;
import im.qq.com.im.controller.activity.ChatActivity;
import im.qq.com.im.controller.activity.GroupListActivity;
import im.qq.com.im.controller.activity.InvitationActivity;
import im.qq.com.im.model.bean.UserInfo;
import im.qq.com.im.model.db.Model;
import im.qq.com.im.utils.Constant;
import im.qq.com.im.utils.SpUtils;

/**
 * Created by asdf on 2017/12/14.
 */

/*从环信服务器获取联系人信息：
        思路：进入ContactListFragment,进入setUpView方法，创建getContactFromHxServer().
                获取到所有联系人的id，检验，转换数据，保存好友的本地数据库，刷新页面。

        代码剖析：
                1.我们是要从环信服务器获取信息，所以必须开启一个线程
                2.获取所有好友的环信id
                3.校验是否为空
                4.转换数据，因为环信那边只能拿到id，我们这边数据存储是存整个用户对象
                5.把用户列表存储到本地数据库
                6.刷新页面，刷新之前先判断getActivity是否为空，防止页面切换的时候，报空指针*/

public class ContactListFragment extends EaseContactListFragment{

    private ImageView iv_contact_red;
    private LinearLayout ll_contact_invite;
    private LocalBroadcastManager mLBM;
    private String mHxid;
    private BroadcastReceiver ContactInviteChangeReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //更新红点
            iv_contact_red.setVisibility(View.VISIBLE);
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);
        }
    };

    private BroadcastReceiver ContactChangReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //刷新页面
            refreshContact();
        }
    };

    private BroadcastReceiver GroupChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //更新红点
            iv_contact_red.setVisibility(View.VISIBLE);
            SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, true);
        }
    };

    //从环信服务器获取所有的联系人信息
    private void getContactFromHxServer() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //获取到所有的好友的环信的id
                    List<String> hxids = EMClient.getInstance().contactManager().getAllContactsFromServer();

                    //校验
                    if (hxids != null && hxids.size() >= 0) {
                        List<UserInfo> contacts = new ArrayList<UserInfo>();

                        //转换
                        for (String hxid : hxids) {
                            UserInfo userInfo = new UserInfo(hxid);
                            contacts.add(userInfo);
                        }

                        //保存好友信息到本地数据库
                        Model.getInstance().getDbManager().getContactTableDao().savaContacts(contacts,true);
                        //判断getActivity是否为空，防止页面切换的时候，报空指针
                        if (getActivity() == null) {
                            return;
                        }

                        //刷新页面
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshContact();
                            }
                        });
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //刷新页面
    private void refreshContact() {

        //获取数据
        List<UserInfo> contacts = Model.getInstance().getDbManager().getContactTableDao().getContacts();

        //校验
        if (contacts != null && contacts.size() >= 0) {

            //设置数据，环信已经给我们做了刷新方法
            Map<String, EaseUser> contactsMap = new HashMap<>();

            //转换
            for (UserInfo contact : contacts) {
                EaseUser easeUser = new EaseUser(contact.getHxid());

                contactsMap.put(contact.getHxid(), easeUser);
            }

            setContactsMap(contactsMap);

            //刷新页面
            refresh();
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        //获取环信id
        int position = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
        EaseUser easeUser = (EaseUser) listView.getItemAtPosition(position);
        mHxid = easeUser.getUsername();

        //添加布局
        getActivity().getMenuInflater().inflate(R.menu.delete, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        
        if (item.getItemId() == R.id.contact_delett) {
            //执行删除选中的联系人操作
            deleteContact();
            
            return true;
        }
        return super.onContextItemSelected(item);
    }

    //执行删除选中的联系人操作
    private void deleteContact() {

        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {

                try {
                    //从环信服务器中删除联系人
                    EMClient.getInstance().contactManager().deleteContact(mHxid);

                    //本地数据库的更新
                    Model.getInstance().getDbManager().getContactTableDao().deleteContactByHxId(mHxid);

                    if (getActivity() == null) {
                        return;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // toast提示
                            Toast.makeText(getActivity(), "删除" + mHxid + "成功", Toast.LENGTH_SHORT).show();

                            //刷新页面
                            refreshContact();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();

                    if (getActivity() == null) {
                        return;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //提示
                            Toast.makeText(getActivity(),"删除" + mHxid + "失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void setUpView() {
        super.setUpView();

        //注册广播
        mLBM = LocalBroadcastManager.getInstance(getActivity());
        mLBM.registerReceiver(ContactInviteChangeReceive, new IntentFilter(Constant.CONTACT_INVITE_CHANGED));
        mLBM.registerReceiver(ContactChangReceiver, new IntentFilter(Constant.CONTACT_CHANGED));
        mLBM.registerReceiver(GroupChangeReceiver, new IntentFilter(Constant.CONTACT_INVITE_CHANGED));

        titleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddContactActivity.class);
                startActivity(intent);
                //getActivity().finish();
            }
        });

        //邀请信息条目的点击事件
        ll_contact_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //红点的处理
                iv_contact_red.setVisibility(View.GONE);
                SpUtils.getInstance().save(SpUtils.IS_NEW_INVITE, false);

                //跳转到邀请信息列表页面
                Intent intent = new Intent(getActivity(), InvitationActivity.class);

                startActivity(intent);
            }
        });

        //初始化红点显示
        boolean isNewInvite = SpUtils.getInstance().getBoolean(SpUtils.IS_NEW_INVITE, false);
        iv_contact_red.setVisibility(isNewInvite? View.VISIBLE: View.GONE);

        //从环信服务器获取所有的联系人信息
        getContactFromHxServer();

        //绑定listview和contextmenu
        registerForContextMenu(listView);


    }




    @Override
    public void onDestroy() {
        super.onDestroy();

        mLBM.unregisterReceiver(ContactInviteChangeReceive);
        mLBM.unregisterReceiver(ContactChangReceiver);
        mLBM.unregisterReceiver(GroupChangeReceiver);
    }

    @Override
    protected void initView() {
        super.initView();

        //布局显示加号
        titleBar.setRightImageResource(R.drawable.em_add);
        titleBar.setTitle("通讯录");

        //添加头布局
        View headerView = View.inflate(getActivity(),R.layout.header_fragment_contact,null);
        listView.addHeaderView(headerView);

        //获取红点对象
        iv_contact_red = (ImageView) headerView.findViewById(R.id.iv_contact_red);

        //获取邀请信息条目的对象
        ll_contact_invite = (LinearLayout) headerView.findViewById(R.id.ll_contact_invite);

        //跳转到群组列表页面
        LinearLayout ll_contact_group = (LinearLayout) headerView.findViewById(R.id.ll_contact_group);
        ll_contact_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GroupListActivity.class);

                startActivity(intent);
            }
        });

        //设置listview条目的点击事件
        setContactListItemClickListener(new EaseContactListItemClickListener() {
            @Override
            public void onListItemClicked(EaseUser user) {

                if (user == null) {
                    return;
                }

                Intent intent = new Intent(getActivity(), ChatActivity.class);

                //传递参数
                intent.putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername());

                startActivity(intent);
            }
        });
    }
}
