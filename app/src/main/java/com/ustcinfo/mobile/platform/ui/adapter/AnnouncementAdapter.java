package com.ustcinfo.mobile.platform.ui.adapter;

import android.content.Context;

import com.ustcinfo.mobile.platform.R;
import com.ustcinfo.mobile.platform.ability.common.BaseAdapter;
import com.ustcinfo.mobile.platform.ability.common.BaseViewHolder;
import com.ustcinfo.mobile.platform.core.model.AnnouncementInfo;

import java.util.List;

/**
 * Created by xueqili on 2017/11/15.
 */

public class AnnouncementAdapter extends BaseAdapter<AnnouncementInfo, BaseViewHolder> {

    public AnnouncementAdapter(Context context, int layoutResId, List<AnnouncementInfo> datas) {
        super(context, layoutResId, datas);
    }

    @Override
    protected void convert(BaseViewHolder holder, AnnouncementInfo item) {
        holder.setText(R.id.tv_title,item.getTitle());
        holder.setText(R.id.tv_summary,item.getSummary());
        holder.setText(R.id.tv_time,"   发布日期:"+item.getPublishDate());
    }
}
