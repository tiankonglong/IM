package im.qq.com.im.controller.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import im.qq.com.im.OnInviteListener;
import im.qq.com.im.R;
import im.qq.com.im.model.bean.InvationInfo;
import im.qq.com.im.model.bean.UserInfo;

/**
 * Created by asdf on 2017/12/20.
 */

public class InviteAdapter extends BaseAdapter {

    private Context mContext;
    private InvationInfo invationInfo;
    private List<InvationInfo> mInvitationInfos = new ArrayList<>();
    private OnInviteListener mOnInviteListener;

    public InviteAdapter(Context context, OnInviteListener onInviteListener) {
        mContext = context;
        mOnInviteListener = onInviteListener;
    }

    //刷新数据的方法
    public void refresh(List<InvationInfo> invationInfos) {
        if (invationInfos != null && invationInfos.size() >= 0) {
            mInvitationInfos.clear();
            mInvitationInfos.addAll(invationInfos);

            notifyDataSetChanged();
        }
    }
    @Override
    public int getCount() {
        return mInvitationInfos == null ? 0 : mInvitationInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mInvitationInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1.获取或创建viewHolder
        ViewHolder hodler=new ViewHolder();
        if (convertView == null) {
            hodler = new ViewHolder();

            convertView = View.inflate(mContext, R.layout.item_invite, null);

            hodler.name = (TextView) convertView.findViewById(R.id.tv_invite_name);
            hodler.reason = (TextView) convertView.findViewById(R.id.tv_invite_reason);

            hodler.accept = (Button) convertView.findViewById(R.id.bt_invite_accept);
            hodler.reject = (Button) convertView.findViewById(R.id.bt_invite_reject);

            convertView.setTag(hodler);
        }else {
            hodler = (ViewHolder) convertView.getTag();
        }

        //2 获取当前item数据
        invationInfo = mInvitationInfos.get(position);

        //3 显示当前item数据
        UserInfo user = invationInfo.getUser();
        if (user != null) {
            String a="jl";
            Log.e("a",a);
            //名称的展示
            hodler.name.setText(invationInfo.getUser().getName());

            hodler.accept.setVisibility(View.GONE);
            hodler.reject.setVisibility(View.GONE);
            hodler.reason.setText("添加好友");
            hodler.reason.setText("接受邀请");
            invationInfo.getStatus();
            //原因
            if (invationInfo.getStatus() == InvationInfo.InvitationStatus.NEW_INVITE) {//新的邀请
                if (invationInfo.getReason() == null) {
                    hodler.reason.setText("添加好友");
                }else {
                    hodler.reason.setText(invationInfo.getReason());
                }

                hodler.accept.setVisibility(View.VISIBLE);
                hodler.reject.setVisibility(View.VISIBLE);
            }else if (invationInfo.getStatus() == InvationInfo.InvitationStatus.INVITE_ACCEPT) {//接受邀请
                if (invationInfo.getReason() == null) {
                    hodler.reason.setText("接受邀请");
                }else  {
                    hodler.reason.setText(invationInfo.getReason());
                }
            }else if (invationInfo.getStatus() == InvationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER) {//邀请被接受
               if (invationInfo.getReason() == null) {
                   hodler.reason.setText("邀请被接受");
               }else {
                   hodler.reason.setText(invationInfo.getReason());
               }
            }

            //按钮的处理
            hodler.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnInviteListener.onAccept(invationInfo);
                }
            });

            //拒绝按钮的点击事件处理
            hodler.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnInviteListener.onReject(invationInfo);
                }
            });
        }

        return convertView;
    }

    private class ViewHolder {
        //名字
        private TextView name;
        //原因
        private TextView reason;
        //接受按钮
        private Button accept;
        //拒绝按钮
        private Button reject;
    }
}
