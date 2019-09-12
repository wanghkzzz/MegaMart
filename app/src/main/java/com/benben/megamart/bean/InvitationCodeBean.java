package com.benben.megamart.bean;

import java.io.Serializable;

/**
 * Created by Android Studio.
 * User: feng
 * Date: 2019/6/15
 * Time: 9:35
 */
public class InvitationCodeBean implements Serializable {

    /**
     * invitation : {"invit_img":"megamart.brd-techi.com/static/uploads/qrcode/.png","invit_url":"","invit_code":null}
     */

    private InvitationBean invitation;

    public InvitationBean getInvitation() {
        return invitation;
    }

    public void setInvitation(InvitationBean invitation) {
        this.invitation = invitation;
    }

    public static class InvitationBean {
        /**
         * invit_img : megamart.brd-techi.com/static/uploads/qrcode/.png
         * invit_url :
         * invit_code : null
         */

        private String invit_img;
        private String invit_url;
        private String invit_code;
        private String bind_code;

        public String getBind_code() {
            return bind_code;
        }

        public void setBind_code(String bind_code) {
            this.bind_code = bind_code;
        }

        public String getInvit_img() {
            return invit_img;
        }

        public void setInvit_img(String invit_img) {
            this.invit_img = invit_img;
        }

        public String getInvit_url() {
            return invit_url;
        }

        public void setInvit_url(String invit_url) {
            this.invit_url = invit_url;
        }

        public String getInvit_code() {
            return invit_code == null ? "" : invit_code;
        }

        public void setInvit_code(String invit_code) {
            this.invit_code = invit_code;
        }
    }
}
