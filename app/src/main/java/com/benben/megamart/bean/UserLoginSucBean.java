package com.benben.megamart.bean;

/**
 * Created by: wanghk 2019-06-11.
 * Describe:登录结果bean
 */
public class UserLoginSucBean {


    /**
     * user_id : 10
     * user_name : 18211173994
     * user_thumb :
     * user_token : 30d36e68-a8f7-4c7e-8b1b-5e1e94d7cbc6
     */

    private int user_id;
    private String user_name;
    private String user_thumb;
    private String user_token;
    private String is_new;
    private String huanxin_uuid;
    private String huanxin_name;
    private String huanxin_password;

    public String getIs_new() {
        return is_new;
    }

    public void setIs_new(String is_new) {
        this.is_new = is_new;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_thumb() {
        return user_thumb;
    }

    public void setUser_thumb(String user_thumb) {
        this.user_thumb = user_thumb;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    public String getHuanxin_uuid() {
        return huanxin_uuid;
    }

    public void setHuanxin_uuid(String huanxin_uuid) {
        this.huanxin_uuid = huanxin_uuid;
    }

    public String getHuanxin_name() {
        return huanxin_name;
    }

    public void setHuanxin_name(String huanxin_name) {
        this.huanxin_name = huanxin_name;
    }

    public String getHuanxin_password() {
        return huanxin_password;
    }

    public void setHuanxin_password(String huanxin_password) {
        this.huanxin_password = huanxin_password;
    }
}
