package im.qq.com.im.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.exceptions.HyphenateException;

import im.qq.com.im.R;
import im.qq.com.im.model.db.Model;

/**
 * Created by asdf on 2017/12/28.
 */

public class NewGroupActivity extends Activity {

    private EditText et_newgroup_name, et_namegroup_desc;
    private CheckBox cb_newgroup_publlic, cb_newgroup_invite;
    private Button bt_newgroup_create;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buidle_group);
        
        initView();
        
        initData();
        
        initListener();
    }

    //初始化监听
    private void initListener() {
        //创建按钮的点击事件处理
        bt_newgroup_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到选择联系人页面
                Intent intent = new Intent(NewGroupActivity.this, PickContactActivity.class);

                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //成功获取到联系人
        if (resultCode == RESULT_OK) {
            //创建群
            createGroup(data.getStringArrayExtra("members"));
        }
    }

    private void createGroup(final String[] memberses) {
        // 群名称
        final String groupName = et_newgroup_name.getText().toString();
        // 群描述
        final String groupDesc = et_namegroup_desc.getText().toString();

        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            String k="kdk";
            @Override
            public void run() {
                // 去环信服务器创建群
                // 参数一：群名称；参数二：群描述；参数三：群成员；参数四：原因；参数五：参数设置
                EMGroupOptions options = new EMGroupOptions();

                options.maxUsers = 200;

                EMGroupManager.EMGroupStyle groupStyle = null;

                if (cb_newgroup_publlic.isChecked()) {
                    if (cb_newgroup_invite.isChecked()) {
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
                    }else {
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval;
                    }
                }else {
                    if (cb_newgroup_invite.isChecked()) {
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
                    }else {
                        groupStyle = EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
                    }
                }

                options.style = groupStyle;

                try {
                    EMClient.getInstance().groupManager().createGroup(groupName, groupDesc, memberses, "申请加入群", options);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewGroupActivity.this, "创建群成功", Toast.LENGTH_SHORT).show();
                            // 结束当前页面
                            finish();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewGroupActivity.this, "创建群失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    private void initData() {
    }

    //初始化View
    private void initView() {
        et_namegroup_desc = (EditText) findViewById(R.id.et_newgroup_desc);
        et_newgroup_name = (EditText) findViewById(R.id.et_newgroup_name);
        cb_newgroup_invite = (CheckBox) findViewById(R.id.cb_newgroup_invite);
        cb_newgroup_publlic = (CheckBox) findViewById(R.id.cb_newgroup_public);
        bt_newgroup_create = (Button) findViewById(R.id.bt_newgroup_create);
    }
}
