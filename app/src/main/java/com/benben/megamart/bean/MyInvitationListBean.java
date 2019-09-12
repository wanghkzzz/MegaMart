package com.benben.megamart.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Android Studio.
 * User: feng
 * Date: 2019/6/15
 * Time: 8:52
 */
public class MyInvitationListBean implements Serializable {

    private List<InvitationListBean> invitation_list;

    public List<InvitationListBean> getInvitation_list() {
        return invitation_list;
    }

    public void setInvitation_list(List<InvitationListBean> invitation_list) {
        this.invitation_list = invitation_list;
    }

    public static class InvitationListBean implements Serializable{
        /**
         * user_id : 2
         * user_name : 哎嘿哈
         * user_avatar :
         * invit_time : null
         * user_type : 1
         */

        private int user_id;
        private String user_name;
        private String user_avatar;
        private String invit_time;
        private String user_type;

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

        public String getUser_avatar() {
            return user_avatar;
        }

        public void setUser_avatar(String user_avatar) {
            this.user_avatar = user_avatar;
        }

        public String getInvit_time() {
            return invit_time;
        }

        public void setInvit_time(String invit_time) {
            this.invit_time = invit_time;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }
    }
}
