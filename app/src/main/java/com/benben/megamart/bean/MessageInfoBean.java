package com.benben.megamart.bean;

/**
 * Created by: wanghk 2019-06-15.
 * Describe:
 */
public class MessageInfoBean {

    private String message;
    private String image;
    private String time;
    private String description;

    public MessageInfoBean(String message, String image, String time, String description) {
        this.message = message;
        this.image = image;
        this.time = time;
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
