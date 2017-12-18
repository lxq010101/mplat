package com.ustcinfo.mobile.platform.core.view;

import com.ustcinfo.mobile.platform.ability.view.common.BaseView;
import com.ustcinfo.mobile.platform.core.model.AnnouncementInfo;

import java.util.List;

/**
 * Created by xueqili on 2017/11/15.
 */

public interface AnnouncementView extends BaseView{

    void getAnnouncementInfos(int pageTotal,List<AnnouncementInfo> list);

    void getDetailContent(String content,String title);

    void getAnnoucementImg(String img);
}
