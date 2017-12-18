package com.ustcinfo.mobile.platform.core.present;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ustcinfo.mobile.platform.ability.apicallback.ApiCallback;
import com.ustcinfo.mobile.platform.ability.apicallback.SubscriberCallBack;
import com.ustcinfo.mobile.platform.ability.presenter.common.BasePresenter;
import com.ustcinfo.mobile.platform.core.model.AnnouncementInfo;
import com.ustcinfo.mobile.platform.core.utils.Key64;
import com.ustcinfo.mobile.platform.core.view.AnnouncementView;

import java.util.List;
import java.util.Observable;
import java.util.function.Consumer;

import rx.functions.Action1;


/**
 * Created by xueqili on 2017/11/15.
 */

public class AnnouncementPresenter extends BasePresenter<AnnouncementView> {

    public AnnouncementPresenter(AnnouncementView announcementView) {
        attachView(announcementView);
    }

    //公告列表
    public void getAnnouncementInfos(final int pageNo, final int pageSize, String userId) {
        getView().showProgressBar();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageNo", pageNo);
        jsonObject.put("pageSize", pageSize);
        jsonObject.put("userId", userId);
        getView().showProgressBar();

        addSubscription(apiStores.getAnnouncements(Key64.encrypt(jsonObject.toJSONString())),
                new SubscriberCallBack<>(new ApiCallback<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject result) {

                        if (result.getIntValue("code") == 0) {

                            int count = result.getJSONObject("data").getJSONObject("annoList").getIntValue("count");
                            if (count > 0) {
                                List<AnnouncementInfo> list = JSONArray.parseArray(result.getJSONObject("data").getJSONObject("annoList").getJSONArray("list").toJSONString(), AnnouncementInfo.class);
                                int pageTotal = (count + pageSize - 1) / pageSize;
                                if (list.size() != 0) {
                                    getView().getAnnouncementInfos(pageTotal, list);
                                } else {
                                    if (pageNo == 1) {
                                        getView().empty();
                                    } else {
                                        getView().error("没有更多数据");
                                    }
                                }
                            } else {
                                getView().empty();
                            }
                        } else {
                            getView().error(result.getString("desc"));
                        }
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        getView().error(msg);

                    }

                    @Override
                    public void netError() {
                        getView().netError();
                    }

                    @Override
                    public void onCompleted() {
                        getView().closeProgressBar();
                    }
                }));

    }


    public void getDtailDrug(String content) {
        getView().showProgressBar();
        String codePrefixOne = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">" +
                "<html>" +
                "<head>" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=\">" +
                "<style type=\"text/css\">";

        String codePrefixTwo = "</style>" + "</head>" + "<body></body>" + "</html>";

        String codeSubfix = "</body></html>";
        String webData = codePrefixOne + "body{word-wrap:break-word;font-family:Arial}img{max-width:100%;height:auto}" + codePrefixTwo + content + codeSubfix;
        getView().getDetailContent(webData,null);
    }

    public void getDtailDrugById(String id) {
        getView().showProgressBar();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        getView().showProgressBar();

        addSubscription(apiStores.getAnnouncementById(Key64.encrypt(jsonObject.toJSONString())),
                new SubscriberCallBack<>(new ApiCallback<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject result) {

                        if (result.getIntValue("code") == 0) {
                            AnnouncementInfo announcementInfo = JSONObject.parseObject(result.getJSONObject("data").getJSONObject("annoEntity").toJSONString(),AnnouncementInfo.class);
                            String codePrefixOne = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">" +
                                    "<html>" +
                                    "<head>" +
                                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=\">" +
                                    "<style type=\"text/css\">";

                            String codePrefixTwo = "</style>" + "</head>" + "<body></body>" + "</html>";

                            String codeSubfix = "</body></html>";
                            String webData = codePrefixOne + "body{word-wrap:break-word;font-family:Arial}img{max-width:100%;height:auto}" + codePrefixTwo + announcementInfo.getContent() + codeSubfix;
                            getView().getDetailContent(webData,announcementInfo.getTitle());

                        } else {
                            getView().error(result.getString("desc"));
                        }
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        getView().error(msg);

                    }

                    @Override
                    public void netError() {
                        getView().netError();
                    }

                    @Override
                    public void onCompleted() {
                        getView().closeProgressBar();
                    }
                }));

    }

    //公告图片
    public void getAnnouncementImg(String id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        addSubscription(apiStores.getAnnouncementImg(Key64.encrypt(jsonObject.toJSONString())),
                new SubscriberCallBack<>(new ApiCallback<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject result) {

                        if (result.getIntValue("code") == 0) {

                            int count = result.getJSONObject("data").getJSONObject("annoList").getIntValue("count");

                        } else {
                            getView().error(result.getString("desc"));
                        }
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        getView().error(msg);

                    }

                    @Override
                    public void netError() {
                        getView().netError();
                    }

                    @Override
                    public void onCompleted() {
                    }
                }));

    }


}
