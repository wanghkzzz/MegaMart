package com.benben.megamart.bean;

/**
 * Created by Android Studio.
 * User: feng
 * Date: 2019/6/19
 * Time: 15:29
 */
public class UpdateVersonBean {

    /**
     * version_info : {"oldversion":"1.0.0","newversion":"1.0.1","packagesize":"10M","content":"测试升级内容","downloadurl":"http://megamart.brd-techi.com","enforce":1,"createtime":1560928259}
     */

    private VersionInfoBean version_info;

    public VersionInfoBean getVersion_info() {
        return version_info;
    }

    public void setVersion_info(VersionInfoBean version_info) {
        this.version_info = version_info;
    }

    public static class VersionInfoBean {
        /**
         * oldversion : 1.0.0
         * newversion : 1.0.1
         * packagesize : 10M
         * content : 测试升级内容
         * downloadurl : http://megamart.brd-techi.com
         * enforce : 1
         * createtime : 1560928259
         */

        private String oldversion;
        private String newversion;
        private String packagesize;
        private String content;
        private String downloadurl;
        private int enforce;
        private int createtime;

        public String getOldversion() {
            return oldversion;
        }

        public void setOldversion(String oldversion) {
            this.oldversion = oldversion;
        }

        public String getNewversion() {
            return newversion;
        }

        public void setNewversion(String newversion) {
            this.newversion = newversion;
        }

        public String getPackagesize() {
            return packagesize;
        }

        public void setPackagesize(String packagesize) {
            this.packagesize = packagesize;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDownloadurl() {
            return downloadurl;
        }

        public void setDownloadurl(String downloadurl) {
            this.downloadurl = downloadurl;
        }

        public int getEnforce() {
            return enforce;
        }

        public void setEnforce(int enforce) {
            this.enforce = enforce;
        }

        public int getCreatetime() {
            return createtime;
        }

        public void setCreatetime(int createtime) {
            this.createtime = createtime;
        }
    }
}
