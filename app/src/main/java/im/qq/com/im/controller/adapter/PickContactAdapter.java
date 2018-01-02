package im.qq.com.im.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import im.qq.com.im.R;
import im.qq.com.im.controller.activity.PickContactActivity;


/**
 * Created by asdf on 2017/12/28.
 */

public class PickContactAdapter extends BaseAdapter {
    private Context mContext;
    private List<PickContactActivity.PickContactInfo> mPicks = new ArrayList<>();

    public PickContactAdapter (Context context, List<PickContactActivity.PickContactInfo> picks){
        mContext = context;

        if (picks != null && picks.size()>=0) {
            mPicks.clear();
            mPicks.addAll(picks);
        }
    }
    @Override
    public int getCount() {
        return mPicks == null ?  0 : mPicks.size();
    }

    @Override
    public Object getItem(int position) {
        return mPicks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //创建或获取viewholder
        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = View.inflate(mContext, R.layout.item_pickcontacts, null);

            viewHolder.cb = (CheckBox) convertView.findViewById(R.id.cb_pick);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_pick_name);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //获取当前item数据
        PickContactActivity.PickContactInfo pickContactInfo = mPicks.get(position);

        //显示数据
        viewHolder.tv_name.setText(pickContactInfo.getUser().getName());
        viewHolder.cb.setChecked(pickContactInfo.isChecked());
        return convertView;
    }

    //获取选择的联系人
    public List<String> getPickContacts() {

        List<String> picks = new ArrayList<>();

        for (PickContactActivity.PickContactInfo pick : mPicks){
            //判断是否选中
            if (pick.isChecked()) {
                picks.add(pick.getUser().getName());
            }
        }

        return picks;
    }

    public class ViewHolder {
        private CheckBox cb;
        private TextView tv_name;
    }
}
