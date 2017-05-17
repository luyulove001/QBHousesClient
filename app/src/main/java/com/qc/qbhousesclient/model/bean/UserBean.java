package com.qc.qbhousesclient.model.bean;

import java.io.Serializable;

/**
 * Created by cly on 2017/5/16.
 */

public class UserBean implements Serializable{
    public String nickname;
    public String phone;
    public String headpic;
    public String incometype;//分红模式，固定模式
    public String modelname;
    public String shopid;
    public String role;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getIncometype() {
        return incometype;
    }

    public void setIncometype(String incometype) {
        this.incometype = incometype;
    }

    public String getModelname() {
        return modelname;
    }

    public void setModelname(String modelname) {
        this.modelname = modelname;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserBean{" + "nickname='" + nickname + '\'' + ", phone='" + phone + '\'' + ", headpic='" + headpic + '\'' + ", incometype='" + incometype + '\'' + ", modelname='" + modelname + '\'' + ", shopid='" + shopid + '\'' + ", role='" + role + '\'' + '}';
    }
}
