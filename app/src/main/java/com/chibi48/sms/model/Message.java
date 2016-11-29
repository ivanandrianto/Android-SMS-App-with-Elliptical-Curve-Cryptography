package com.chibi48.sms.model;

import java.util.Date;

/**
 * Created by ivan on 11/29/16.
 */

public class Message {

    private int id;
    private int threadId;
    private String address;
    private String person;
    private Date date;
    private Date dateSent;
    private int read;
    private int status;
    private String body;
    private int type;

    public Message(){}

    public Message(int id, int threadId, String address, String person, Date date,
                   Date dateSent, int type, String body) {
        super();
        this.id = id;
        this.threadId = threadId;
        this.address = address;
        this.person = person;
        this.date = date;
        this.dateSent = dateSent;
        this.type = type;
        this.body = body;
    }

    public void setId(int id) { this.id = id; }
    public int getId() { return id; }

    public void setThreadId(int sender) { this.threadId = threadId; }
    public int getThreadId() { return threadId; }

    public void setAddress(String address) { this.address = address; }
    public String getAddress() { return address; }

    public void setPerson(String person) { this.person = person; }
    public String getPerson() { return person; }

    public void setDate(Date date) { this.date = date; }
    public Date getDate() { return date; }

    public void setDateSent(Date dateSent) { this.dateSent = dateSent; }
    public Date getDateSent() { return dateSent; }

    public void setBody(String body) { this.person = body; }
    public String getBody() { return body; }

    public void setType(int type) { this.type = type; }
    public int getType() { return type; }

    public Date getActualDate() {
        if (type == 1) {
            return dateSent;
        } else {
            return date;
        }
    }

    @Override
    public String toString() {
        return "Message [id=" + id + ", thread_id=" + threadId + ", address=" + address +
                ", date=" + date + ", dateSent=" + dateSent + ", type=" + type + ", body=" + body +
                "]";
    }

}
