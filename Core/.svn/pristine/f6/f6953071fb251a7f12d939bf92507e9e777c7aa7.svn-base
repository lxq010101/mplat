package com.ustcinfo.mobile.platform.core.model;


import com.ustcinfo.mobile.platform.core.core.SystemCore;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by SunChao on 2017/5/16.
 */


public class UserInfo implements Serializable {

    private static UserInfo mInstance;

    private String userId;

    private String name;

    private String password;

    private String department;

    private String ticket;

    //地理定位省
    private String province;

    //地理定位市
    private String cityCode;

    //本地网编码  -1:表示无效
    private String areaCode;

    private String telephoneNumber;

    private UserInfo() {
    }

    public static UserInfo get() {
        if (mInstance == null)
            mInstance = new UserInfo();
        return mInstance;
    }

    public UserInfo create(String userId, String areaCode, String name, String password, String department, String ticket, String telephoneNumber) {
        this.userId = userId;
        this.areaCode = areaCode;
        this.name = name;
        this.password = password;
        this.department = department;
        this.ticket = ticket;
        this.telephoneNumber = telephoneNumber;
        return mInstance;
    }


    public static UserInfoSX createInstance(UserInfo u) {
        UserInfoSX userInfo = new UserInfoSX();
        userInfo.userId = u.userId;
        userInfo.areaCode = u.areaCode;
        userInfo.name = u.name;
        userInfo.password = u.password;
        userInfo.department = u.department;
        userInfo.ticket = u.ticket;
        userInfo.telephoneNumber = u.telephoneNumber;
        return userInfo;
    }

    public void destory() {
        this.userId = null;
        this.areaCode = null;
        this.name = null;
        this.password = null;
        this.department = null;
        this.ticket = null;
        this.telephoneNumber = null;
    }

    public void save() {
        SystemCore.get().clearUserCache();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        StringBuilder b = new StringBuilder();
        b.append("insert into user values( ")
                .append("'").append(userId).append("' ,")
                .append("'").append(areaCode).append("' ,")
                .append("'").append(password).append("' ,")
                .append("'").append(telephoneNumber).append("' ,")
                .append("'").append(ticket).append("' ,")
                .append("'").append(sdf.format(new Date())).append("')");
        SystemCore.get().getDatabase().execSQL(b.toString());
    }


    public String getUserIdCache() {
        List<Map<String, String>> list = SystemCore.get().getDatabase().find("select * from user");
        if (list.size() == 0)
            return null;
        return list.get(0).get("user_id");
    }


    public String getPasswordCache() {
        List<Map<String, String>> list = SystemCore.get().getDatabase().find("select * from user");
        if (list.size() == 0)
            return null;
        return list.get(0).get("password");
    }

    public String getTicketCache() {
        List<Map<String, String>> list = SystemCore.get().getDatabase().find("select * from user");
        if (list.size() == 0) {
            return null;
        }
        return list.get(0).get("ticket");
    }


    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }
}
