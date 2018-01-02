package im.qq.com.im.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import im.qq.com.im.R;
import im.qq.com.im.controller.adapter.PickContactAdapter;
import im.qq.com.im.model.bean.UserInfo;
import im.qq.com.im.model.db.Model;

/**
 * Created by asdf on 2017/12/28.
 */

public class PickContactActivity extends Activity {

    private TextView tv_pick_save;
    private ListView lv_pick;
    private List<PickContactInfo> mPicks;
    private PickContactAdapter pickContactAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pick_contact);
        
        initView();
        
        initData();
        
        initListener();
    }

    private void initListener() {
        //listview条目点击事件
        lv_pick.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //checkbox的切换
                CheckBox cb_pick = (CheckBox) view.findViewById(R.id.cb_pick);
                cb_pick.setChecked(!cb_pick.isChecked());

                //修改数据
                PickContactInfo pickContactInfo = mPicks.get(position);
                pickContactInfo.setIsChecked(cb_pick.isChecked());

                //刷新页面
                pickContactAdapter.notifyDataSetChanged();
            }
        });

        //保存按钮的点击事件
        tv_pick_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取到已经选择的联系人
                List<String> names = pickContactAdapter.getPickContacts();

                //给启动页面返回数据
                Intent intent = new Intent();

                intent.putExtra("members", names.toArray(new String[0]));

                //设置返回的结果码
                setResult(RESULT_OK, intent);

                //结束当前界面
                finish();
            }
        });
    }

    private void initData() {
        //从本地数据库获取所有联系人信息
        List<UserInfo> contacts = Model.getInstance().getDbManager().getContactTableDao().getContacts();

        mPicks = new ArrayList<>();

        if (contacts != null && contacts.size() >= 0) {
            //转换
            for (UserInfo contact : contacts) {
                PickContactInfo pickContactInfo = new PickContactInfo(contact, false);
                mPicks.add(pickContactInfo);
            }
        }

        //初始化listview
        pickContactAdapter = new PickContactAdapter(this, mPicks);
        lv_pick.setAdapter(pickContactAdapter);
    }

    private void initView() {
        tv_pick_save = (TextView) findViewById(R.id.tv_pick_save);
        lv_pick = (ListView) findViewById(R.id.lv_pick);
    }

    //选择联系人的bean类
    public class PickContactInfo {
        private UserInfo user;
        private boolean isChecked;

        public UserInfo getUser() {
            return user;
        }

        public void setUser(UserInfo user) {
            this.user = user;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setIsChecked(boolean isChecked) {
            this.isChecked = isChecked;
        }

        public PickContactInfo(UserInfo user, boolean isChecked) {
            this.isChecked = isChecked;
            this.user = user;
        }

        public PickContactInfo() {}
    }
}
