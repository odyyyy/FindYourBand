package com.example.findyourband.services;

public class RequestDataClass {
    private String from;
    private String to;

    private String status; // send, accept, deny
    private boolean type; // 0 - musician, 1 - band

    public RequestDataClass() {
    }

    public RequestDataClass(String from, String to, String status, boolean type) {
        this.from = from;
        this.to = to;
        this.status = status;
        this.type = type;
    }

    public RequestDataClass(String from, String to, boolean type) {
        this.from = from;
        this.to = to;
        this.type = type;
    }
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public void setTo(String to) {
        this.to = to;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }


}
