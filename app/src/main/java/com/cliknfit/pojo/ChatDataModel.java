package com.cliknfit.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Prince on 3/23/2017.
 */

public class ChatDataModel {
 /*   *//**
     * status : 1
     * chat : [{"Id":"8","AdvocateId":"3","UserId":"1","SenderId":"3","Message":"Hi Hakim, this is prince","CreatedOn":"17 hours ago"}]
     * message : Chat ArrayList
     *//*

    private int status;
    private String message;
    private ArrayList<ChatBean> chat;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<ChatBean> getChat() {
        return chat;
    }

    public void setChat(ArrayList<ChatBean> chat) {
        this.chat = chat;
    }

    public static class ChatBean {
        *//**
         * Id : 8
         * AdvocateId : 3
         * UserId : 1
         * SenderId : 3
         * Message : Hi Hakim, this is prince
         * CreatedOn : 17 hours ago
         *//*

        private String Id;
        private String AdvocateId;
        private String UserId;
        private String SenderId;
        private String Message;
        private String CreatedOn;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getAdvocateId() {
            return AdvocateId;
        }

        public void setAdvocateId(String AdvocateId) {
            this.AdvocateId = AdvocateId;
        }

        public String getUserId() {
            return UserId;
        }

        public void setUserId(String UserId) {
            this.UserId = UserId;
        }

        public String getSenderId() {
            return SenderId;
        }

        public void setSenderId(String SenderId) {
            this.SenderId = SenderId;
        }

        public String getMessage() {
            return Message;
        }

        public void setMessage(String Message) {
            this.Message = Message;
        }

        public String getCreatedOn() {
            return CreatedOn;
        }

        public void setCreatedOn(String CreatedOn) {
            this.CreatedOn = CreatedOn;
        }
    }*/

    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("AdvocateId")
    @Expose
    private String advocateId;
    @SerializedName("UserId")
    @Expose
    private String userId;
    @SerializedName("SenderId")
    @Expose
    private String senderId;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("CreatedDate")
    @Expose
    private String CreatedDate;

    @SerializedName("CreatedOn")
    @Expose
    private String createdOn; //IsBlocked

    @SerializedName("IsBlocked")
    @Expose
    private int isblocked;


    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public int getIsblocked() {
        return isblocked;
    }

    public void setIsblocked(int isblocked) {
        this.isblocked = isblocked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdvocateId() {
        return advocateId;
    }

    public void setAdvocateId(String advocateId) {
        this.advocateId = advocateId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

}
