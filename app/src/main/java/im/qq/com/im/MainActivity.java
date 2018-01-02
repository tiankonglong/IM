package im.qq.com.im;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import im.qq.com.im.controller.fragment.ChatFragment;
import im.qq.com.im.controller.fragment.ContactListFragment;
import im.qq.com.im.controller.fragment.SettingFragment;

/**
 * Created by asdf on 2017/12/8.
 */

public class MainActivity extends FragmentActivity {
    private RadioGroup rg_main;
    private ChatFragment chatFragment;
    private ContactListFragment contactListFragment;
    private SettingFragment settingFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im);

        initView();
        initData();
        initListener();
    }

    private void initView() {
        rg_main = (RadioGroup) findViewById(R.id.rg_main);
    }

    private void initData() {
        chatFragment = new ChatFragment();
        contactListFragment = new ContactListFragment();
        settingFragment = new SettingFragment();
    }

    private void initListener() {
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                Fragment fragment = null;
                switch (checkedId) {
                    case R.id.rb_main_chat:
                        fragment = chatFragment;
                        break;

                    case R.id.rb_main_contact:
                        fragment = contactListFragment;
                        break;

                    case R.id.rb_main_setting:
                        fragment = settingFragment;
                        break;
                }
                switchFragment(fragment);
            }
        });
        rg_main.check(R.id.rb_main_chat);
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl_main,fragment).commit();
    }
}
