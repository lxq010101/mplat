package com.ustcinfo.mobile.platform.core.model;

import android.os.Handler;
import android.text.TextUtils;


import com.ustcinfo.mobile.platform.core.appstore.AppType;
import com.ustcinfo.mobile.platform.core.core.SystemCore;

import java.util.Map;

/**
 * Created by SunChao on 2017/5/16.
 */

public class AppInfo implements  Comparable<AppInfo>{

	
    public String id ;

    public String name ;

    public String version ;

    public String packageName ;

    /**
     * 0.android / 1.ios / 2.web app / 3.url
     * */
    public int type ;


    /**
     * 该应用所关联的父应用ID，一般在一个应用存在多个入口时，此字段才有意义
     * -1 为父应用
     * */
    public String parentId ;

    public String startUrl ;

    public String releaseArea;

    public String summay ;

    public String updateContent ;

    public String detail ;

    /**
     *是否显示桌面  0：不显示  1：显示
     * */
    public boolean isDesktopDisplay ;

    /**
     *是否角标 0：不显示  1：显示
     * */
    public int isShowCornerMark ;

    /**
     *角标请求地址
     * */
    public String cornerMarkUrl ;

    public long size ;

    /**
     *    是否预先自动下载
     * */
    public boolean autoDownload ;

    public String releaseTime ;

    public int order ;

    /**
     *  应用使用类型：  0：工具; 1：应用 ;2 :其它
     * */
    public int useType ;

    public boolean isForceUpdate  ;

    private int forceUpdate ;
    
    public AppType.StatusType status ;
    
    public AppType.StatusType getStatus() {
		return status;
	}

	public void setStatus(AppType.StatusType status) {
		this.status = status;
	}

    public AppInfo() {

    }

	public AppInfo(String id, String name, String version, String packageName, int type, String startUrl,
                   String releaseArea, boolean isDesktopDisplay, String summay, String updateContent, String detail,
                   int isShowCornerMark, String size, boolean autoDownload, String releaseTime, int order ,String cornerMarkUrl ,
                   int useType, String parentId ,int forceUpdate) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.packageName = packageName;
        this.type = type;
        this.startUrl = startUrl;
        this.releaseArea = releaseArea;
        this.isDesktopDisplay = isDesktopDisplay;
        this.summay = summay;
        this.updateContent = updateContent;
        this.detail = detail;
        this.isShowCornerMark = isShowCornerMark;
        try {
            this.size = Long.valueOf(size);
		} catch (Exception e) {
			this.size = 0 ;
		}
        this.autoDownload = autoDownload;
        this.releaseTime = releaseTime;
        this.order = order;
        //先去查用户是否有自定义排序，若没有，则使用系统下发排序
        int number = getSequenceCache(id) ;
        this.order = number == -1 ? order :number ;
        this.cornerMarkUrl = cornerMarkUrl ;
        this.useType = useType ;
        this.parentId = parentId ;
        this.forceUpdate = forceUpdate ;
        this.isForceUpdate = forceUpdate == 1 ? true :false ;
    }
	

	//插入缓存
    public void save(){

                StringBuilder b = new StringBuilder() ;
                b.append("replace  into app_list values( ")
                        .append("'").append(id).append("' ,")
                        .append("'").append(name).append("' ,")
                        .append("'").append(version).append("' ,")
                        .append("'").append(packageName).append("' ,")
                        .append("'").append(type).append("' ,")
                        .append("'").append(startUrl).append("' ,")
                        .append("'").append(releaseArea).append("' ,")
                        .append("'").append(isDesktopDisplay ?1:0).append("' ,")
                        .append("'").append(useType).append("' ,")
                        .append("'").append(summay).append("' ,")
                        .append("'").append(updateContent).append("' ,")
                        .append("'").append(detail).append("' ,")
                        .append("'").append(isShowCornerMark).append("' ,")
                        .append("'").append(cornerMarkUrl).append("' ,")
                        .append("'").append(size).append("' ,")
                        .append("'").append(autoDownload ? 1:0 ).append("' ,")
                        .append("'").append(order).append("' ,")
                        .append("'").append(parentId).append("' ,")
                        .append("'").append(forceUpdate).append("' ,")
                        .append("'").append(releaseTime) .append("')") ;
                SystemCore.get().getDatabase().execSQL(b.toString());

                StringBuilder bSequence = new StringBuilder() ;
                bSequence.append("replace  into app_sequence values( ")
                        .append("'").append(id).append("' ,")
                        .append("'").append(packageName).append("' ,")
                        .append("'").append(order) .append("')") ;
                SystemCore.get().getDatabase().execSQL(bSequence.toString());
            }




    public void delete(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                StringBuilder b = new StringBuilder() ;
                b.append("delete from app_sequence where package= '").append(packageName).append("'") ;
                SystemCore.get().getDatabase().execSQL(b.toString());

                StringBuilder bb = new StringBuilder() ;
                b.append("delete from app_list where package= '").append(packageName).append("'") ;
                SystemCore.get().getDatabase().execSQL(bb.toString());
            }
        }) ;

    }

    private int getSequenceCache(String id) {
        Map<String, String> map = SystemCore.get().getDatabase().findOne("select * from app_sequence where app_id=?", new String[]{id});
        if(map.size() == 0)
            return -1 ;
        return Integer.valueOf(map.get("number")) ;
    }

    //更新应用排列顺序
    public void syncOrder(final int squence){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                StringBuilder b = new StringBuilder() ;
                b.append("update app_sequence set number = '").append(squence).append("'")
                        .append("where app_id = '").append(id).append("'") ;
                SystemCore.get().getDatabase().execSQL(b.toString());
                order = squence ;
            }
        }) ;
    }

    @Override
    public int compareTo(AppInfo o) {
    	if(this.order < 0){
            return 1 ;
    	}
    	
        if(this.order > o.order){
            return 1 ;
        }
        if(this.order < o.order || TextUtils.equals("-1" ,id)){
            return -1 ;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", packageName='" + packageName + '\'' +
                ", type=" + type +
                ", startUrl='" + startUrl + '\'' +
                ", desktopDisplay=" + isDesktopDisplay +
                ", order=" + order +
                '}';
    }
}
