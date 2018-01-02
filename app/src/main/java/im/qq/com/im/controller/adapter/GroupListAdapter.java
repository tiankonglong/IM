package im.qq.com.im.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;

import java.util.ArrayList;
import java.util.List;

import im.qq.com.im.R;

/**
 * Created by asdf on 2017/12/26.
 */

public class GroupListAdapter extends BaseAdapter {

    private Context mContext;
    private List<EMGroup> mGrouops  = new ArrayList<>();

    public GroupListAdapter(Context context) {
        mContext = context;
    }

    //刷新方法
    public void refresh(List<EMGroup> groups) {
        if (groups != null && groups.size() >= 0) {
            mGrouops.clear();

            mGrouops.addAll(groups);

            notifyDataSetChanged();
        }
    }
    @Override
    public int getCount() {
        return mGrouops == null ? 0 : mGrouops.size();
    }

    @Override
    public Object getItem(int position) {
        return mGrouops.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*在getView 方法中遵循我们的四步走
    1.创建或获取viewholder
    2.获取当前item数据
    3.显示数据
    4.返回数据*/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //创建或获取viewholder
        ViewHolder viewHolder = null;
        //首先判断视图是否为空
        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = View.inflate(mContext , R.layout.item_grouplist, null);

            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_grouplist_name);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //获取当前item数据
        EMGroup emGroup = mGrouops.get(position);

        //显示数据
        viewHolder.name.setText(emGroup.getGroupName());

        //返回数据
        return convertView;
    }
    //创建或获取viewholder
    private class ViewHolder{
        TextView name;//群组名
    }
}
